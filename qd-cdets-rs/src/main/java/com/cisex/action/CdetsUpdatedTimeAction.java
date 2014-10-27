package com.cisex.action;

import com.cisex.service.CdetsReplicationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by huaiwang on 14-3-3.
 */
@Controller("/cdets/updated_time")
public class CdetsUpdatedTimeAction {
    private static final Logger log = LoggerFactory.getLogger(CdetsUpdatedTimeAction.class);

    @RequestMapping(method = RequestMethod.GET)
    public String update_time_form() {
        return "form.updated.time";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String get_update_time(@RequestParam String query) {
        CdetsReplicationImpl cdetsReplication = new CdetsReplicationImpl();
        Date updatedTime = null;
        try {
            updatedTime = cdetsReplication.getUpdatedTime(query);
        } catch (Exception e) {
            return "err";
        }

        if(updatedTime == null) return "no-defects";

        Calendar cal = Calendar.getInstance();
        log.error(query);
        log.error(cal.getTimeInMillis() + "," + updatedTime.getTime());
        long diff = cal.getTimeInMillis() - updatedTime.getTime();
        if (diff < 0) diff += 8 * 3600 * 1000;

        return "" + diff;
    }
}
