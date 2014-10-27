package com.cisex.action;

import com.cisex.job.QddtsWorker;
import com.cisex.service.QddtsCacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * User: huaiwang
 * Date: 13-10-31
 * Time: 下午2:28
 */
@Controller
@RequestMapping("/qddts")
public class QddtsReplicationAction {
    private static final Logger log = LoggerFactory.getLogger(QddtsReplicationAction.class);

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "qddts.form";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public String cache(@RequestParam String query) {
        // todo: test only log
        log.error("query:" + query);

        try {
            QddtsCacher.cache(query, true);
            return "success";
        } catch (Exception e) {
            log.error("=== manual cache qddts failed.", e);
            return e.getMessage();
        }
    }

    @RequestMapping("/hc")
    public String hc(ModelMap map) {
        map.put("currentQuery", QddtsWorker.currentQuery);
        map.put("waitingQueries", QddtsWorker.getInstance().getQueries());

        return "qddts.hc";
    }

    @RequestMapping("/refresh_all_async")
    @ResponseBody
    public String refreshAll() {
        try {
            QddtsCacher.refreshAllAsync();
            return "success";
        } catch (Exception e) {
            return "err: " + e.getMessage();
        }
    }

    @RequestMapping("/qddts_jobs")
    @ResponseBody
    public String qddtsJobs() {
        String currentQuery = QddtsWorker.getInstance().currentQuery;
        List<String> queries = QddtsWorker.getInstance().getQueries();

        StringBuilder sb = new StringBuilder();
        sb.append(currentQuery).append("<hr/><hr/>");
        sb.append("count: " + queries.size() + "<hr/>");
        for (String query : queries) {
            sb.append(query).append("<hr/>");
        }
        return sb.toString();
    }
}

