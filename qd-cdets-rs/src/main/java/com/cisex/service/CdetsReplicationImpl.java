package com.cisex.service;

import com.cisex.GeneralException;
import com.cisex.Global;
import com.cisex.ds.KeyValuePair;
import com.cisex.model.*;
import com.cisex.util.DbUtils;
import com.cisex.util.Expression;
import com.cisex.util.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/2/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */

//@WebService(endpointInterface = "com.cisex.service.CdetsReplication")
public class CdetsReplicationImpl implements CdetsReplication {
    private static final Logger log = LoggerFactory.getLogger(CdetsReplicationImpl.class);

    private static final Map<String, String> fieldMapping = new HashMap<String, String>();

    static {
        for (TimeField timeField : TimeField.values()) {
            fieldMapping.put(timeField.cdetsFieldName, timeField.dbColName);
        }

        for (DefectField defectField : DefectField.values()) {
            fieldMapping.put(defectField.cdetsFieldName, defectField.dbColName);
        }
    }


    public List<DailyCount> getDailyDefects(String timeField, String query) {
        addJob(query);
        timeField = firstUpperCaseLetter(timeField) + "Time";

        log.debug("raw query: " + query);
        query = cdetsQueryToMysqlQuery(query);
        log.debug("processed query: " + query);

        String sql = "" +
                "   SELECT COUNT(dd) AS COUNT, dd AS DATE " +
                "   FROM " +
                "   ( " +
                "      SELECT DATE(%s) dd " +
                "      FROM cdets.defects " +
                "      WHERE stale = 0 AND (%s) AND %s IS NOT NULL" +
                "   ) AS t " +
                "   GROUP BY t.dd ORDER BY dd ASC";
        sql = String.format(sql, timeField, query, timeField);

        log.debug("sql: " + sql);

        Session session = null;
        List<DailyCount> list = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            list = session.createSQLQuery(sql)
                    .addScalar("count", StandardBasicTypes.INTEGER)
                    .addScalar("date", StandardBasicTypes.DATE)
                    .setResultTransformer(Transformers.aliasToBean(DailyCount.class))
                    .list();
        } catch (Exception e) {
            log.error("[getDailyDefects]", e);
        } finally {
            if (session != null) session.close();
        }

        return list;
    }


    private String firstUpperCaseLetter(String str) {
        if (str.length() == 0) return "";
        String F = (str.charAt(0) + "").toUpperCase();
        return F;
    }

    public List<SeverityStatusCount> groupBySeverityAndStatus(String query) {
        addJob(query);


        query = cdetsQueryToMysqlQuery(query);

        String sql = "" +
                " SELECT severity, status, COUNT(*) cnt " +
                " FROM cdets.defects" +
                " WHERE stale = 0 AND (%s) GROUP BY severity, status";

        sql = String.format(sql, query);


        Session session = null;
        List<SeverityStatusCount> list = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            list = session.createSQLQuery(sql)
                    .addScalar("severity", StandardBasicTypes.STRING)
                    .addScalar("status", StandardBasicTypes.STRING)
                    .addScalar("cnt", StandardBasicTypes.INTEGER)
                    .setResultTransformer(Transformers.aliasToBean(SeverityStatusCount.class))
                    .list();

        } catch (Exception e) {
            log.error("[groupBySeverityAndStatus]", e);
            log.error("the sql is: " + sql);
        } finally {
            if (session != null) session.close();
        }

        return list;
    }


    private void addJob(String query) {
        Set<String> projects = getComponent("Project", query);
        Set<String> products = getComponent("Product", query);


        for (String project : projects) {
            for (String product : products) {
                ProductPK id = new ProductPK();
                id.setProduct(product);
                id.setProject(project);

                Product prod = DbUtils.querySingleEntity(Product.class, id);
                if (prod == null) ReplicationWorker.getInstance().addJob(project, product, false);
            }
        }
    }

    private Set<String> getComponent(String component, String query) {
        Pattern pattern = Pattern.compile("\\[?" + component + "\\]?\\s*=\\s*'([^']*)'");
        Matcher matcher = pattern.matcher(query);

        Set<String> lst = new HashSet<String>();

        while (matcher.find()) lst.add(matcher.group(1));

        return lst;
    }

    private List<Expression> getExpressions(String query) {
        Pattern pattern = Pattern.compile("\\[?([\\w\\d\\-_]+)\\]?\\s*(<|=|>|<>|<=|>=|LIKE|NOT LIKE|IS NULL|IS NOT NULL)\\s*'([^']*)'");
        Matcher matcher = pattern.matcher(query);


        List<Expression> exps = new ArrayList<Expression>();

        while (matcher.find()) {
            Expression exp = new Expression();
            exp.setLeftOperand(matcher.group(1));
            exp.setOperator(matcher.group(2));
            exp.setRightOperand(matcher.group(3));

            exps.add(exp);
        }

        return exps;
    }

    private String processLikePhase(String query) {

        Pattern pattern = Pattern.compile("\\[?([\\w\\d\\-_]+)\\]?\\s*(LIKE|NOT LIKE)\\s*'([^']*)'", Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(query);

        String str = "";

        int e2 = 0;

        while (!matcher.hitEnd()) {
            if (matcher.find()) {
                int s = matcher.start(3);
                int e = matcher.end(3);

                if (s > e2) str += query.substring(e2, s);

                e2 = e;

                String sub = query.substring(s, e);
                str += sub.replace('*', '%');

            } else {
                str += query.substring(e2);
            }
        }

        return str;
    }


    private String fieldMapping(String query) {
        Pattern pattern = Pattern.compile("\\[?([\\w\\d\\-_]+)\\]?\\s*(<|=|>|<>|<=|>=|LIKE|NOT LIKE|IS NULL|IS NOT NULL)\\s*'([^']*)'", Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(query);

        String str = "";

        int e2 = 0;

        while (!matcher.hitEnd()) {
            if (matcher.find()) {
                int s = matcher.start(1);
                int e = matcher.end(1);

                if (s > e2) str += query.substring(e2, s);

                e2 = e;

                String sub = query.substring(s, e);
                String map = fieldMapping.get(sub);

                if (map != null) str += map;
                else str += sub;
            } else {
                str += query.substring(e2);
            }
        }

        return str;
    }

    public String cdetsQueryToMysqlQuery(String query) {
        Pattern pattern = Pattern.compile("\\[?([\\w\\d\\-_]+)\\]?\\s*(<>|<=|>=|<|=|>|NOT LIKE|LIKE|IS NOT|IS)\\s*'?([^' ]*)'?", Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(query);

        String str = "";

        int e2 = 0;
        //String fmt = "({1} = '{2}' OR {1} LIKE '{2},%' OR {1} LIKE '%,{2}' OR {1} LIKE '%,{2},%')";
        String fmt = "id in (select defect_id from {1} where {2})";


        while (!matcher.hitEnd()) {
            if (matcher.find()) {
                int s = matcher.start();
                int e = matcher.end();

                if (s > e2) str += query.substring(e2, s);

                e2 = e;

                String key = matcher.group(1);
                String opr = matcher.group(2);
                String val = matcher.group(3);

                String dbField = fieldMapping.get(key);

                if (opr.toLowerCase().indexOf("like") != -1) {
                    val = val.replace("*", "%");
                }

                if (key.equalsIgnoreCase(DefectField.VERSION.cdetsFieldName)) {
                    str += StringUtils.replaceEach(
                            fmt,
                            new String[]{"{1}", "{2}"},
                            new String[]{"defects_versions", "version " + opr + " '" + val + "'"}
                    );
                } else if (key.equalsIgnoreCase(DefectField.KEYWORD.cdetsFieldName)) {
                    str += StringUtils.replaceEach(
                            fmt,
                            new String[]{"{1}", "{2}"},
                            new String[]{"defects_keywords", "keyword " + opr + " '" + val + "'"}
                    );
                } else if (key.equalsIgnoreCase(DefectField.TO_BE_FIXED.cdetsFieldName)) {
                    str += StringUtils.replaceEach(
                            fmt,
                            new String[]{"{1}", "{2}"},
                            new String[]{"defects_to_be_fixed_versions", "version " + opr + " '" + val + "'"}
                    );
                } else if (key.endsWith("-on") || key.equalsIgnoreCase(TimeField.SYS_LAST_UPDATED.cdetsFieldName)) {
                    String newDatetime = datetimeReplace(val);
                    str += "DATE(" + dbField + ") " + opr + " '" + newDatetime + "' ";
                } else {
                    if (val.equalsIgnoreCase("null"))
                        str += dbField + " " + opr + " " + val + " ";
                    else
                        str += dbField + " " + opr + " '" + val + "' ";
                }

            } else {
                str += query.substring(e2);
            }
        }

        return str;
    }

    private String datetimeReplace(String strDatetime) {
        Date date = strToDate(strDatetime);

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            return sdf.format(date);
        }

        if (strDatetime.toLowerCase().indexOf("today") != -1) {
            Pattern p = Pattern.compile("today\\s*(\\+|-)\\s*(\\d+)(d|m|y)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(strDatetime);


            if (matcher.find()) {
                int sign = matcher.group(1).equalsIgnoreCase("+") ? 1 : -1;
                int val = Integer.parseInt(matcher.group(2));
                int field = matcher.group(3).equalsIgnoreCase("d") ? Calendar.DAY_OF_YEAR : matcher.group(3).equalsIgnoreCase("m") ? Calendar.MONTH : Calendar.YEAR;
                cal.add(field, sign * val);
            }

            return sdf.format(cal.getTime());
        }

        return strDatetime;
    }


    private String mapToDb(String strField) {
        for (TimeField field : TimeField.values()) {
            field.cdetsFieldName.equalsIgnoreCase(strField);
            return field.dbColName;
        }

        return null;
    }

    private String versionProcess(String query) {
        Pattern pattern = Pattern.compile("\\[?Version\\]?\\s*=\\s*'([^']*)'", Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(query);

        String str = "";

        int e2 = 0;
        String fmt = "{1}='{2}' AND {1} LIKE '{2},%' AND {1} LIKE '%,{2}' AND {1} LIKE '%,{2},%'";


        while (!matcher.hitEnd()) {
            if (matcher.find()) {
                int s = matcher.start();
                int e = matcher.end();

                if (s > e2) str += query.substring(e2, s);

                e2 = e;

                String ver = matcher.group(1);
                str += StringUtils.replaceEach(fmt, new String[]{"{1}", "{2}"}, new String[]{"Version", ver});

            } else {
                str += query.substring(e2);
            }
        }

        return str;
    }

    private String toMysqlDateFormat(String query) {
        Pattern pattern = Pattern.compile("\\[?([\\w\\d\\-_]+)\\]?\\s*(<|=|>|<>|<=|>=)\\s*'([^']*)'", Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(query);

        String str = "";

        int e2 = 0;

        while (!matcher.hitEnd()) {
            if (matcher.find()) {
                int s = matcher.start(3);
                int e = matcher.end(3);

                if (s > e2) str += query.substring(e2, s);

                e2 = e;

                String sub = query.substring(s, e);
                Date date = strToDate(sub);

                if (date != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    str += sdf.format(date);
                } else {
                    str += sub;
                }

            } else {
                str += query.substring(e2);
            }
        }

        return str;
    }


    private Date strToDate(String str) {
        try {
            return DateUtils.parseDate(str, Global.DatePatterns);
        } catch (ParseException e) {
            return null;
        }
    }


//    @Override
//    public List<KeyValuePair<String, Integer>> groupByComponent(String query) {
//
//        query = cdetsQueryToMysqlQuery(query);
//
//        String sql = "SELECT component `KEY`, COUNT(*) `VALUE` FROM cdets.defects WHERE %s GROUP BY component";
//        sql = String.format(sql, query);
//
//        Session session = null;
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//            return session.createSQLQuery(sql)
//                    .addScalar("key", StandardBasicTypes.STRING)
//                    .addScalar("value", StandardBasicTypes.INTEGER)
//                    .setResultTransformer(Transformers.aliasToBean(KeyValuePair.class))
//                    .list();
//        } catch (Exception e) {
//            throw new GeneralException(e);
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//    }

    @Override
    public List<KeyValuePair<String, Integer>> groupBy(String field, String query) {
        query = cdetsQueryToMysqlQuery(query);

        String sql = "" +
                " SELECT {field} `KEY`, COUNT(*) `VALUE`" +
                " FROM cdets.defects" +
                " WHERE stale = 0 AND ({query}) GROUP BY {field}";

        sql = StringUtils.replaceEach(
                sql,
                new String[]{"{field}", "{query}"},
                new String[]{field, query}
        );

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createSQLQuery(sql)
                    .addScalar("key", StandardBasicTypes.STRING)
                    .addScalar("value", StandardBasicTypes.INTEGER)
                    .setResultTransformer(Transformers.aliasToBean(KeyValuePair.class))
                    .list();
        } catch (Exception e) {
            throw new GeneralException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Date getUpdatedTime(String query) {
        query = cdetsQueryToMysqlQuery(query);

        String sql = "" +
                " SELECT min(last_update_time) from " +
                " (" +
                "   SELECT project,product" +
                "   FROM cdets.defects" +
                "   WHERE stale = 0 AND ({query}) GROUP BY project,product" +
                " ) b" +
                " left join cdets.products a" +
                " on a.project = b.project and a.product = b.product";

        sql = StringUtils.replaceEach(
                sql,
                new String[]{"{query}"},
                new String[]{query}
        );

        log.error(sql);

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Object o = session.createSQLQuery(sql).uniqueResult();
            if (o == null) return null;
            return org.apache.http.impl.cookie.DateUtils.parseDate(o.toString(), new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss"});
        } catch (Exception e) {
            throw new GeneralException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
