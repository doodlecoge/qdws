package com.cisex.action;

import com.cisex.service.ReplicationJobWrapper;
import com.cisex.service.ReplicationWorker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 8/14/13
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/hc")
public class HealthControlAction {
    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap map) {
        map.put("state", ReplicationWorker.pause);
        jobInfo(map);

        return "hc.form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String post(@RequestParam("state") boolean state, ModelMap map) {
        ReplicationWorker.pause = state;

        map.put("state", ReplicationWorker.pause);
        jobInfo(map);

        return "hc.form";
    }


    private void jobInfo(ModelMap map) {
        List<ReplicationJobWrapper> jobs = ReplicationWorker.getInstance().getJobs();

        String waitingJobs = "";
        for (ReplicationJobWrapper job : jobs) {
            waitingJobs += "[" + job.getProject() + ", " + job.getProduct() + "], ";
        }

        String curJob = "";
        if (ReplicationWorker.CurJob != null) {
            curJob += "[" + ReplicationWorker.CurJob.getProject() + ", "
                    + ReplicationWorker.CurJob.getProduct() + "]";
        } else {
            curJob += "no job";
        }

        map.put("waitingJobs", waitingJobs);
        map.put("currentJob", curJob);
    }
}
