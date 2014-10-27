package com.cisex.crawler;

import com.cisex.QdCdetsProperties;
import com.cisex.model.DefectAuditTrial;
import org.apache.http.impl.cookie.DateUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-8-29
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
public class CdetsDefectExtractorTest {
    public static void main(String[] args) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

        System.out.println("------" + DateUtils.parseDate("08/29/2013 14:43:45", new String[]{"MM/dd/yyyy HH:mm:ss"}));

        String dir = QdCdetsProperties.getString("test.tmp.dir");
        String filePath = dir + "/dumpcr.xml";
        File file = new File(filePath);
        InputStream is = null;


        List<String> lst = new ArrayList<String>();
        lst.add("CSCuh80437");
        lst.add("CSCtz40072");
        lst.add("CSCuh20890");
        String[] l = "CSCua17447,CSCua17452,CSCua27245,CSCua27274,CSCua27302,CSCua27306,CSCua27308,CSCua27309,CSCua27312,CSCua27318".split(",");
        for (int i = 0; i < l.length; i++) {
            lst.add(l[i]);
        }

//        if (!file.exists()) {
//            is = CdetsDumper.dump(lst);
//            IOUtils.copy(is, new FileOutputStream(file));
//            is.close();
//        }
//
//        is = new FileInputStream(file);

        is = CdetsDumper.dump(lst);

        CdetsDumpExtractor extractor = new CdetsDumpExtractor();
        Map<String, List<DefectAuditTrial>> auditTrialMap = extractor.extract(is);


        for (String did : auditTrialMap.keySet()) {
            List<DefectAuditTrial> defectAuditTrials = auditTrialMap.get(did);
            System.out.println("------------------" + did + "------------------");
            for (DefectAuditTrial defectAuditTrial : defectAuditTrials) {
                System.out.println(
                        defectAuditTrial.getDefectId() + ", "
                                + defectAuditTrial.getDate() + ", "
                                + defectAuditTrial.getEmployeeId() + ", "
                                + defectAuditTrial.getField() + ", "
                                + defectAuditTrial.getNewValue() + ", "
                                + defectAuditTrial.getOldValue() + ", "
                                + defectAuditTrial.getOperation()
                );
            }
        }
    }
}
