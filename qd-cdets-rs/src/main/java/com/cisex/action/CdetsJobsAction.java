package com.cisex.action;

import com.cisex.service.ReplicationJobWrapper;
import com.cisex.service.ReplicationWorker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by huaiwang on 14-3-3.
 */
@Controller
@RequestMapping("/cdets/jobs")
public class CdetsJobsAction {
    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap map) {
        List<ReplicationJobWrapper> jobs = ReplicationWorker.getInstance().getJobs();
        map.put("jobs", jobs);
        map.put("curjob", ReplicationWorker.CurJob);
        return "cdets.jobs";
    }
}
