package com.cisex.action;

import com.cisex.ds.KeyValuePair;
import com.cisex.job.QddtsWorker;
import com.cisex.model.DailyCount;
import com.cisex.model.QddtsTrend;
import com.cisex.model.SeverityStatusCount;
import com.cisex.service.CdetsReplication;
import com.cisex.service.CdetsReplicationImpl;
import com.cisex.util.DbUtils;
import org.apache.http.impl.cookie.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 8/13/13
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/service")
public class CdetsServiceAction {
    private final static Logger log = LoggerFactory.getLogger(CdetsServiceAction.class);

    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping(value = "/daily_defects", method = RequestMethod.POST)
    @ResponseBody
    public String getDailyDefects(
            @RequestParam("time") String time,
            @RequestParam("query") String query) {


        CdetsReplication cdets = new CdetsReplicationImpl();
        List<DailyCount> defects = cdets.getDailyDefects(time, query);

        StringBuilder sb = new StringBuilder();
        int len = defects.size();

        for (int i = 0; i < len; i++) {
            if (i != 0) sb.append(";");
            sb.append(dailyCountToString(defects.get(i)));
        }

        return sb.toString();
    }

    private String dailyCountToString(DailyCount dc) {
        return df.format(dc.getDate()) + "," + dc.getCount();
    }


    @RequestMapping(value = "/ssc", method = RequestMethod.POST)
    @ResponseBody
    public String groupBySeverityAndStatus(@RequestParam("query") String query) {
        CdetsReplication cdets = new CdetsReplicationImpl();
        List<SeverityStatusCount> sscs = cdets.groupBySeverityAndStatus(query);

        if (sscs == null) return "{}";

        int len = sscs.size();
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        for (int i = 0; i < len; i++) {
            SeverityStatusCount ssc = sscs.get(i);
            if (i != 0) sb.append(",");
            sb.append(severityStatusCountToString(ssc));
        }
        sb.append("}");

        return sb.toString();
    }

    private String severityStatusCountToString(SeverityStatusCount ssc) {
        return "\"" + ssc.getSeverity() + "," + ssc.getStatus() + "\":" + ssc.getCnt();
    }


    @RequestMapping(value = "/c2m", method = RequestMethod.POST)
    @ResponseBody
    public String cdetsQueryToMysqlQuery(String query) {
        log.debug("cdets query: " + query);
        CdetsReplicationImpl cdets = new CdetsReplicationImpl();
        String mysqlQuery = cdets.cdetsQueryToMysqlQuery(query);
        log.debug("mysql query: " + mysqlQuery);
        return mysqlQuery;
    }


    @RequestMapping(value = "/acc", method = RequestMethod.POST)
    @ResponseBody
    public String accumulate(String project, String product, String severities, String versions, String toBeFixed) {
//        CdetsDefectAccumulator accumulator = new CdetsDefectAccumulator(project, product);
//
//
//        Set<String> targetVersions = new HashSet<String>();
//        if (versions != null && !"".equals(versions.trim()))
//            Collections.addAll(targetVersions, versions.split(","));
//
//        Set<String> targetToBeFixedVersions = new HashSet<String>();
//        if (toBeFixed != null && !"".equals(toBeFixed.trim()))
//            Collections.addAll(targetToBeFixedVersions, toBeFixed.split(","));
//
//        Set<String> targetSeverities = new HashSet<String>();
//        if (severities != null && !"".equals(severities.trim()))
//            Collections.addAll(targetSeverities, severities.split(","));
//
//        int[] openedBugs = null;
//        Date minDate = null;
//
//        try {
//            accumulator.accumulate(targetVersions, targetToBeFixedVersions, targetSeverities);
//            openedBugs = accumulator.getOpenedDefects();
//            minDate = accumulator.getMinDate();
//        } catch (DateParseException e) {
//            log.error("", e);
//            return "{error: true}";
//        }
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("{error: false,data:\"");
//
//        int len = openedBugs.length;
//        long ts = minDate.getTime();
//        boolean fst = true;
//
//        int total = 0;
//        for (int i = 0; i < len; i++) {
//            total += openedBugs[i];
//            if (fst && total == 0) continue;
//
//            if (!fst) sb.append(";");
//            else fst = false;
//
//            sb.append("" + (ts + i * Consts.MillisecondsInOneDay) + "," + total);
//        }
//
//        sb.append("\"}");
//        return sb.toString();

        return "{error:true}";
    }


    @ResponseBody
    @RequestMapping(value = "/qddts")
    public String qddtsTrend(String query) {
        query = query.trim();
        int id = query.hashCode();

        QddtsTrend qddtsTrend = DbUtils.querySingleEntity(QddtsTrend.class, id);

        if (qddtsTrend == null) {
            QddtsWorker.getInstance().addQuery(query);
            return "{error:true,hit:false,query:\"" + query + "\"}";
        } else {

            long nowMills = Calendar.getInstance().getTimeInMillis();
            long updatedOnMills = qddtsTrend.getUpdatedOn().getTime();
            long diffMills = nowMills - updatedOnMills;

            if(diffMills < 0) diffMills += 8 * 3600 * 1000;

            return "{error:false,query:\"" + query + "\",data:\""
                    + qddtsTrend.getTrendData() + "\""
                    + ", updatedOn:\""
                    + DateUtils.formatDate(qddtsTrend.getUpdatedOn(), "yyyy-MM-dd HH:mm:ss")
                    + "\", timeLapse: \"" + (diffMills) + "\"}";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/qddts_ref")
    public String qddtsTrendRefresh(String query) {

        QddtsWorker.getInstance().addQuery(query);
        return "{error:false}";

    }


//    @Deprecated
//    @ResponseBody
//    @RequestMapping(value = "/bycomp")
//    public String groupByComponent(String query) {
//        CdetsReplicationImpl cdets = new CdetsReplicationImpl();
//        List<KeyValuePair<String, Integer>> data = null;
//        try {
//            data = cdets.groupByComponent(query);
//        } catch (Exception e) {
//            log.error("", e);
//        }
//
//        if (data == null || data.size() == 0) {
//            return "{error:true}";
//        } else {
//            StringBuilder sb = new StringBuilder();
//            sb.append("{error:false,data:{");
//
//            int len = data.size();
//
//            for (int i = 0; i < len; i++) {
//                if (i != 0) sb.append(",");
//                KeyValuePair<String, Integer> d = data.get(i);
//                sb.append("\"").append(d.getKey().replace("\"", "\\\"")).append("\":");
//                sb.append(d.getValue());
//            }
//
//            sb.append("}}");
//            return sb.toString();
//        }
//    }


    @ResponseBody
    @RequestMapping(value = "groupby")
    public String groupBy(String field, String query) {
        CdetsReplicationImpl cdets = new CdetsReplicationImpl();
        List<KeyValuePair<String, Integer>> data = null;

        try {
            data = cdets.groupBy(field, query);
        } catch (Exception e) {
            log.error("", e);
        }

        if (data == null || data.size() == 0) {
            return "{\"error\":true}";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"error\":false,\"data\":{");

            int len = data.size();
            boolean first = true;

            for (int i = 0; i < len; i++) {
                KeyValuePair<String, Integer> d = data.get(i);

                if (d.getKey() == null) continue;

                if (!first) {
                    sb.append(",");
                } else {
                    first = false;
                }

                sb.append("\"").append(d.getKey().replace("\"", "\\\"")).append("\":");
                sb.append(d.getValue());
            }

            sb.append("}}");
            return sb.toString();
        }
    }
}
