package com.cisex.crawler;

import com.cisex.annotaion.CdetsDumpXmlFild;
import com.cisex.model.AuditTrialField;
import com.cisex.model.DefectAuditTrial;
import com.cisex.model.Severity;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-8-29
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class CdetsDumpExtractor extends DefaultHandler {
    private static final Logger log = LoggerFactory.getLogger(CdetsDumpExtractor.class);

    private Stack<String> context = new Stack<String>();
    private String defectId;
    private DefectAuditTrial auditTrial;
    private List<DefectAuditTrial> auditTrialList;
    private Map<String, List<DefectAuditTrial>> auditTrialMap = new HashMap<String, List<DefectAuditTrial>>();
    private Map<String, Date> auditLastUpdatedTimeMap = new HashMap<String, Date>();
    private String currentNodeName;
    private boolean inAuditTrial;
    private Date lastUpdatedTime;

    private final static String AuditTrialListPath = "/SiebelMessage/ListOfcisexProductDefect/ProductDefect/ListOfAuditTrailItem2";
    private final static String AuditTrialPath = AuditTrialListPath + "/AuditTrailItem2";
    private final static String IdentifierPath = "/SiebelMessage/ListOfcisexProductDefect/ProductDefect/Identifier";

    private final static String AuditTrialListNodeName = "ListOfAuditTrailItem2";
    private final static String AuditTrialNodeName = "AuditTrailItem2";


    private static Set<String> AcceptFields = new HashSet<String>();

    static {
        AcceptFields.add("Status");
        AcceptFields.add("Version");
        AcceptFields.add("To-be-fixed");
        AcceptFields.add("Severity-desc");
    }

    private static final Map<String, Method> FieldMethodMapping;

    static {
        FieldMethodMapping = new HashMap<String, Method>();
        Method[] methods = DefectAuditTrial.class.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() != CdetsDumpXmlFild.class) {
                    continue;
                }

                String value = ((CdetsDumpXmlFild) annotation).value();
                FieldMethodMapping.put(value, method);

                log.debug("annotation: " + value);
                break;
            }
        }
    }


    public Map<String, List<DefectAuditTrial>> extract(InputStream is) throws IOException, SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(this);
        InputSource source = new InputSource(is);
        parser.parse(source);
        return auditTrialMap;
    }

    public Map<String, Date> getLastUpdatedTimes() {
        return auditLastUpdatedTimeMap;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentNodeName = localName;

        if (AuditTrialNodeName.equals(localName)) {
            inAuditTrial = true;
            auditTrial = new DefectAuditTrial();
            auditTrial.setDefectId(defectId);
        } else if (AuditTrialListNodeName.equals(localName)) {
            auditTrialList = new ArrayList<DefectAuditTrial>();
            auditTrialMap.put(defectId, auditTrialList);
        }


//        context.push(localName);
//        String path = "/" + StringUtils.join(context, "/");
//
//        if (path.equals(AuditTrialListPath)) {
//            auditTrialList = new ArrayList<DefectAuditTrial>();
//            auditTrialMap.put(defectId, auditTrialList);
//        } else if (path.equals(AuditTrialPath)) {
//            auditTrial = new DefectAuditTrial();
//            auditTrial.setDefectId(defectId);
//        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (AuditTrialNodeName.equals(localName)) {
            inAuditTrial = false;

            if (AcceptFields.contains(auditTrial.getField())) {
                if (auditTrial.getField().equals(AuditTrialField.SEVERITY.val)) {
                    String newValue = auditTrial.getNewValue();
                    String oldValue = auditTrial.getOldValue();

                    if (newValue != null) {
                        auditTrial.setNewValue(Severity.getValue(newValue));
                    }

                    if (oldValue != null) {
                        auditTrial.setOldValue(Severity.getValue(oldValue));
                    }
                }

                auditTrialList.add(auditTrial);
            }
        } else if (AuditTrialListNodeName.equals(localName)) {
            auditLastUpdatedTimeMap.put(defectId, lastUpdatedTime);
            lastUpdatedTime = null;
        }

//        String path = "/" + StringUtils.join(context, "/");
//
//        if (path.equals(AuditTrialPath) && AcceptFields.contains(auditTrial.getField())) {
//            auditTrialList.add(auditTrial);
//        }
//
//        context.pop();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String cont = new String(ch, start, length);

        try {
            process(cont.trim());
        } catch (Exception e) {
            log.error("process xml content error", e);
            log.error("--- " + defectId);
        }
    }

    private void process(String cont) throws DateParseException, InvocationTargetException, IllegalAccessException {
        if (currentNodeName.equals("Identifier")) {
            defectId = cont;
        } else if (inAuditTrial) {
            processAuditTrial(cont);
        }

//        String path = "/" + StringUtils.join(context, "/");
//
//        if (path.equals(IdentifierPath)) {
//            defectId = cont.trim();
//        } else if (path.startsWith(AuditTrialPath)) {
//            processAuditTrial(cont.trim());
//        }
    }

    private static final String[] DatePtn = new String[]{
            "MM/dd/yyyy HH:mm:ss"
    };

    private void processAuditTrial(String cont) throws DateParseException, InvocationTargetException, IllegalAccessException {

        if (!FieldMethodMapping.containsKey(currentNodeName)) return;

        Method method = FieldMethodMapping.get(currentNodeName);
        Object obj = cont;
        if (currentNodeName.equals("Date")) {
            Date date = DateUtils.parseDate(cont, DatePtn);
            obj = date;
            if (lastUpdatedTime == null) {
                lastUpdatedTime = date;
            } else {
                if (lastUpdatedTime.compareTo(date) < 0) lastUpdatedTime = date;
            }
        }

        method.invoke(auditTrial, obj);


//        String attr = context.peek();
//
//        if (!FieldMethodMapping.containsKey(attr)) return;
//
//        Method method = FieldMethodMapping.get(attr);
//        Object obj = cont;
//        if (attr.equals("Date")) obj = DateUtils.parseDate(cont, DatePtn);
//
//        method.invoke(auditTrial, obj);
    }
}
