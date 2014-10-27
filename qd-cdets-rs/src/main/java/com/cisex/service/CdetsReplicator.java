package com.cisex.service;

import com.cisex.GeneralException;
import com.cisex.Global;
import com.cisex.crawler.CdetsCrawler;
import com.cisex.model.*;
import com.cisex.util.DbUtils;
import com.cisex.util.HibernateUtil;
import com.cisex.util.Mapping;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: hch
 * Date: 13-6-30
 * Time: 下午9:46
 * To change this template use File | Settings | File Templates.
 */
public class CdetsReplicator {
    private static final Logger log = LoggerFactory.getLogger(CdetsReplicator.class);

    public static int FieldNum = DefectField.values().length + TimeField.values().length;

    public static void replicate(String defectId) throws IOException, URISyntaxException {
        log.debug(">>> " + defectId);
        InputStream is = CdetsCrawler.getDefectById(defectId);
        DateCount dc = update(is);
        log.debug("count: " + dc.count);
        log.debug("<<< " + defectId);
    }

    public static void replicate(String project, String product) throws IOException, URISyntaxException {
        replicate(project, product, null, null);
    }


    public static void replicate(String project, String product, Date startDate, Date endDate) throws IOException, URISyntaxException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

        log.debug(">>> replicate [" + project + "," + product + "]");
        log.debug("=== quering from " + startDate + " to " + endDate);


        long st = System.currentTimeMillis();

        int expectCount = CdetsCrawler.getCount(project, product, startDate, endDate);

        if (expectCount == 0) {
            log.debug("=== 0 defect");
            updateProductUpdateTime(project, product);
            return;
        }

        int actualCount = 0;

        Date date = startDate;
        while (true) {
            InputStream is = CdetsCrawler.getDefects(project, product, date, endDate);

            DateCount dc = update(is);

            log.debug("=== replicated defects: " + dc.count);
            log.debug("=== end date: " + dc.date);

            if (dc.count == 0) {
                log.warn("=== no defect found: " + project + ", " + product);
            }

            actualCount += dc.count;

            if (dc.count < Global.CDETS_RESULT_LIMIT) break;

            date = dc.date;
        }

        long et = System.currentTimeMillis();
        log.debug("=== replicate time for [" + project + "," + product + "]: " + (et - st));

        if (expectCount != actualCount)
            log.error("=== expect " + expectCount + ", actual " + actualCount);
        else
            updateProductUpdateTime(project, product);

        log.debug("<<< replicate [" + project + "," + product + "]");
    }


    public static void updateProject(String project, String product, boolean force) throws IOException, URISyntaxException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

        if (!force && !DbUtils.needUpdate(project, product)) return;

        log.debug(">>> update [" + project + ", " + product + "]");
        long st = System.currentTimeMillis();

        Date lastUpdatedTime = DbUtils.getLatestLastUpdatedTime(project, product);
        log.debug("=== last updated time " + lastUpdatedTime);

        int expectCount = CdetsCrawler.getNewOrUpdatedDefectsCount(project, product, lastUpdatedTime);

        if (expectCount == 0) {
            log.debug("=== 0 defect");
            updateProductUpdateTime(project, product);
            return;
        }

        InputStream is = CdetsCrawler.getNewOrUpdatedDefects(project, product, lastUpdatedTime);
        DateCount dc = update(is);

        log.debug("=== updated defects: " + dc.count);

        if (dc.count == 0) log.warn("(update) no defect found: " + project + ", " + product);
        if (dc.count >= Global.CDETS_RESULT_LIMIT) log.error("update result exceeds limit: " + dc.count);


        long et = System.currentTimeMillis();
        log.debug("=== update time for [" + project + "," + product + "]: " + (et - st));

        if (expectCount != dc.count) log.error("=== expect " + expectCount + ", actual " + dc.count);
        else updateProductUpdateTime(project, product);
        log.debug("<<< update [" + project + ", " + product + "]");
    }

//    public static void replicateAuditTrial(String project, String product) throws IOException, SAXException {
//        List<String> defectIds = DbUtils.getDefectIds(project, product);
//
//        int sz = defectIds.size();
//        int sliceSize = QdCdetsProperties.getInt("dump.max.size");
//        int batches = (sz + sliceSize - 1) / sliceSize;
//
//        for (int i = 0; i < batches; i++) {
//            int from = i * sliceSize;
//            int to = from + sliceSize;
//            to = to > sz ? sz : to;
//
//            List<String> slice = defectIds.subList(from, to);
//            log.debug("dumping audits: " + StringUtils.join(slice, ","));
//
//            replicateAuditTrial(slice);
//        }
//    }
//
//    public static void replicateAuditTrial(List<String> defectIds) throws IOException, SAXException {
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
//
//        long st = System.currentTimeMillis();
//        InputStream is = CdetsDumper.dump(defectIds);
//        long et = System.currentTimeMillis();
//
//        log.debug("dumping time: " + (et - st) + "ms");
//
//
//        CdetsDumpExtractor extractor = new CdetsDumpExtractor();
//        st = System.currentTimeMillis();
//        Map<String, List<DefectAuditTrial>> auditTrials = extractor.extract(is);
//        Map<String, Date> lastUpdatedTimes = extractor.getLastUpdatedTimes();
//        et = System.currentTimeMillis();
//
//        log.debug("extracting time: " + (et - st) + "ms");
//        is.close();
//
//
//        st = System.currentTimeMillis();
//        Session session = null;
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//            session.beginTransaction();
//
//
//            for (String defectId : auditTrials.keySet()) {
//                log.debug(defectId);
//
//                // from xml
//                List<DefectAuditTrial> xmlAudits = auditTrials.get(defectId);
//
//                // from db
//                List dbAudits = session.createCriteria(DefectAuditTrial.class)
//                        .add(Restrictions.eq("defectId", defectId))
//                        .addOrder(Order.asc("date"))
//                        .addOrder(Order.asc("id")).list();
//
//                Collections.reverse(xmlAudits); // oldest first
//
//                int xmlSize = xmlAudits.size();
//                int dbSize = dbAudits.size();
//                boolean eq = true;
//
//                if (xmlSize == dbSize) {
//                    for (int i = 0; i < xmlSize; i++) {
//                        if (!xmlAudits.get(i).equals(dbAudits.get(i))) {
//                            eq = false;
//                            break;
//                        }
//                    }
//
//                    log.debug("eq: " + eq);
//
//                    if (eq) continue;
//                }
//
//                if (!eq) log.warn("neq");
//
//                if (xmlSize < dbSize) {
//                    eq = false;
//                    log.warn("fetched audits: " + xmlSize + ", db audits size: " + dbSize);
//                }
//
//
//                if (!eq) {
//                    // clear all
//                    for (Object defect : dbAudits) {
//                        session.delete(defect);
//                    }
//
//                    for (DefectAuditTrial audit : xmlAudits) {
//                        session.save(audit);
//                    }
//                } else {
//                    for (int i = dbSize; i < xmlSize; i++) {
//                        session.save(xmlAudits.get(i));
//                    }
//                }
//
//
//                // update time
//                Date date = lastUpdatedTimes.get(defectId);
//                Defect defect = (Defect) session.createCriteria(Defect.class)
//                        .add(Restrictions.idEq(defectId)).uniqueResult();
//                defect.setAuditUpdatedTime(date);
//
//                session.saveOrUpdate(defect);
//            }
//
//            session.getTransaction().commit();
//        } catch (HibernateException e) {
//            if (session != null) session.getTransaction().rollback();
//            log.error("", e);
//        } finally {
//            if (session != null) session.close();
//        }
//        et = System.currentTimeMillis();
//        log.debug("db updating time: " + (et - st) + "ms");
//    }


    /*
    public static void replicateAuditTrial(String project, String product) throws IOException, DateParseException {
        List<String> defectIds = DbUtils.getDefectIds(project, product);

        int sz = defectIds.size();
        int sliceSize = 10;
        int batches = (sz + sliceSize - 1) / sliceSize;

        for (int i = 0; i < batches; i++) {
            int from = i * sliceSize;
            int to = from + sliceSize;
            to = to > sz ? sz : to;

            List<String> slice = defectIds.subList(from, to);
            replicateAuditTrial(slice);
        }
    }

    public static void replicateAuditTrial(List<String> defectIds) throws IOException, DateParseException {
        InputStream is = CdetsCrawler.getDefectAuditTrials(defectIds);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        br.readLine(); // skip history line

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for (String defectId : defectIds) {
                log.debug("dumping audit trial for " + defectId);

                List<DefectAuditTrial> auditTrials = readAuditTrial(br);
                for (DefectAuditTrial auditTrial : auditTrials) {
                    auditTrial.setDefectId(defectId);
                    session.save(auditTrial);
                }
            }

            String line = br.readLine();

            if (line == null) session.getTransaction().commit();
            else {
                log.error("expect eof, actual " + line);
                session.getTransaction().rollback();
            }
        } catch (HibernateException e) {
            if (session != null) session.getTransaction().rollback();
        } finally {
            if (session != null) session.close();
        }

        br.close();
    }

    private static Pattern HistoryPattern = Pattern.compile("^(\\d{2}/\\d{2}/\\d{4}) ([^\t]*\t)([^\t]*\t)([^\t]*\t)([^\t]*\t)([^\t]*)$");
    private static String[] HistoryDatePatterns = new String[]{"MM/dd/yyyy"};
    private static Set<String> AcceptFields = new HashSet<String>();

    static {
        AcceptFields.add("Status");
        AcceptFields.add("Version");
        AcceptFields.add("To-be-fixed");
        AcceptFields.add("Severity-desc");
    }

    private static List<DefectAuditTrial> readAuditTrial(BufferedReader br) throws IOException, DateParseException {
        br.readLine(); // column line
        br.readLine(); // dash lien
        String line = null;
        line = br.readLine();

        if (!line.trim().equals(""))
            throw new GeneralException("expect empty line, actual [" + line + "]");

        List<DefectAuditTrial> auditTrials = new ArrayList<DefectAuditTrial>();

        while ((line = br.readLine()) != null) {
            if (line.startsWith("***")) break;

            Matcher matcher = HistoryPattern.matcher(line);

            if (!matcher.find()) {
                throw new GeneralException("unrecognizable line [" + line + "]");
            }

            if (!AcceptFields.contains(matcher.group(3).trim())) continue;


            DefectAuditTrial dh = new DefectAuditTrial();

            dh.setDate(DateUtils.parseDate(matcher.group(1).trim(), HistoryDatePatterns));
            dh.setEmployeeId(matcher.group(2).trim());
            dh.setField(matcher.group(3).trim());
            dh.setOldValue(matcher.group(4).trim());
            dh.setNewValue(matcher.group(5).trim());
            dh.setOperation(matcher.group(6).trim());

            auditTrials.add(dh);
        }

        return auditTrials;
    }

    */

    public static List<String> getProducts(String project) throws IOException, URISyntaxException {
        InputStream is = CdetsCrawler.getProducts(project);

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;

        List<String> lst = new ArrayList<String>();

        while ((line = br.readLine()) != null) {
            lst.add(line.trim());
        }

        return lst;
    }


    public static DateCount update(InputStream is) throws IOException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        Date date = null;
        String line = null;
        String defectString = "";
        int count = 0;

        while ((line = br.readLine()) != null) {
            defectString += line;
            if (!line.endsWith(Global.FIELD_SPLITTER)) continue;

            Defect defect = saveDefect(defectString);

            defectString = "";

            if (defect != null) date = defect.getSTime();

            count++;
        }

        if (count == 0) log.debug(defectString);

        return new DateCount(date, count);
    }


    private static void updateProductUpdateTime(String project, String product) {
        Product prod = new Product();

        ProductPK id = new ProductPK();
        id.setProduct(product);
        id.setProject(project);

        prod.setId(id);
        prod.setLastUpdateTime(Calendar.getInstance().getTime());

        try {
            DbUtils.saveOrUpdate(prod);
        } catch (Exception e) {
            String fmt = "set last update time failed, project: %s, product: %s";
            String msg = String.format(fmt, project, product);
            log.error(msg, e);
        }
    }


    public static Defect saveDefect(String line) {
        try {
            Defect defect = lineToDefect(line);

            if (defect == null) {
                log.warn("defect null: [" + line + "]");
            } else {
                updateVersions(DefectVersion.class, defect.getId(),
                        getVersions(defect.getVersions())
                );
                updateVersions(DefectToBeFixedVersion.class, defect.getId(),
                        getToBeFixedVersions(defect.getToBeFixedVersions())
                );
                updateKeywords(defect.getId(), defect.getKeywords());

                defect.setStale(0);
                DbUtils.saveOrUpdate(defect);
            }

            return defect;
        } catch (Exception e) {
            String msg = "save defect error: [" + line + "]";
            log.error(msg, e);
        }

        return null;
    }

    private static Set<String> getVersions(Set<DefectVersion> dvs) {
        Set<String> set = new HashSet<String>();
        for (DefectVersion dv : dvs) {
            set.add(dv.getId().getVersion());
        }
        return set;
    }

    private static Set<String> getToBeFixedVersions(Set<DefectToBeFixedVersion> dvs) {
        Set<String> set = new HashSet<String>();
        for (DefectToBeFixedVersion dv : dvs) {
            set.add(dv.getId().getVersion());
        }
        return set;
    }

    public static void updateKeywords(String id, Set<Keyword> keywords) {
        if (keywords == null || keywords.size() == 0) return;

        Set<String> keywordStrings = new HashSet<String>();

        for (Keyword keyword : keywords) {
            keywordStrings.add(keyword.getId().getKeyword());
        }


        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            List<String> oldKeywords = (List<String>) session.createCriteria(Keyword.class)
                    .add(Restrictions.eq("id.defectId", id))
                    .setProjection(Projections.property("id.keyword"))
                    .list();

            oldKeywords.removeAll(keywordStrings);

            // remove old keywords
            if (oldKeywords.size() > 0) {
                List list = session.createCriteria(Keyword.class)
                        .add(Restrictions.eq("id.defectId", id))
                        .add(Restrictions.in("id.keyword", oldKeywords))
                        .list();

                for (Object o : list) {
                    session.delete(o);
                }
            }

            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
                session.close();
            }
            throw new GeneralException(e);
        }
    }

    public static void updateVersions(Class klass, String id, Set<String> versions) {
        if (versions == null || versions.size() == 0) return;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            List<String> oldVersions = (List<String>) session.createCriteria(klass)
                    .add(Restrictions.eq("id.id", id))
                    .setProjection(Projections.property("id.version"))
                    .list();

            oldVersions.removeAll(versions);
            if (oldVersions.size() > 0) {
                List list = session.createCriteria(klass)
                        .add(Restrictions.eq("id.id", id))
                        .add(Restrictions.in("id.version", oldVersions))
                        .list();

                for (Object o : list) {
                    session.delete(o);
                }
            }

            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
                session.close();
            }
            throw new GeneralException(e);
        }

        session.close();
    }


    private static Defect lineToDefect(String line) throws IllegalAccessException, DateParseException {
        String[] fieldValues = line.split(Global.FIELD_SPLITTER);
        if (fieldValues.length != FieldNum) {
            log.error("field length: " + fieldValues.length);
            return null;
        }

        Defect defect = new Defect();

        for (int i = 0; i < FieldNum; i++) {
            String fieldValue = fieldValues[i].trim();
            if ("".equals(fieldValue) || fieldValue == "") continue;

            String columnName = Mapping.ColumnNames.get(i);

            if (columnName.equalsIgnoreCase("version")) {
                defect.setVersions(fieldValue);
                continue;
            }

            if (columnName.equalsIgnoreCase("to_be_fixed")) {
                defect.setToBeFixedVersions(fieldValue);
                continue;
            }

            if (columnName.equals(DefectField.KEYWORD.dbColName)) {
                defect.setKeywords(fieldValue);
                continue;
            }

            Field field = Mapping.GetColumnField(columnName);

            Object obj = fieldValue;
            if (field.getType() == Date.class) {
                obj = stringToDate(fieldValue);
            }

            field.setAccessible(true);
            field.set(defect, obj);
        }

        return defect;
    }

    private static Date stringToDate(String dateStr) throws DateParseException {

        return DateUtils.parseDate(dateStr, new String[]{"MM/dd/yyyy HH:mm:ss"});
    }


    private static class DateCount {
        public Date date;
        public int count;

        public DateCount(Date date, int count) {
            this.date = date;
            this.count = count;
        }
    }
}
