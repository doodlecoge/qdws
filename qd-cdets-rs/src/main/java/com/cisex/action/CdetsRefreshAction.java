package com.cisex.action;

import com.cisex.model.Defect;
import com.cisex.service.CdetsReplicationImpl;
import com.cisex.service.ReplicationWorker;
import com.cisex.util.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by huaiwang on 14-2-18.
 */

@Controller
@RequestMapping("/cdets/refresh")
public class CdetsRefreshAction {
    public static final Logger log = LoggerFactory.getLogger(CdetsRefreshAction.class);

    @RequestMapping(method = RequestMethod.GET)
    public String get() {
        return "form.cdets.refresh";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String post(@RequestParam("query") String query) {
        CdetsReplicationImpl rep = new CdetsReplicationImpl();
        List<Defect> lst = null;
        try {
            lst = DbUtils.getProductsFromCdetsQuery(rep.cdetsQueryToMysqlQuery(query));
            for (Defect defect : lst) {
                log.error("#-#" + defect.getProject() + ", " + defect.getProduct());
            }
        } catch (Exception e) {
            return "{error:true}";
        }

        for (Defect defect : lst) {
            ReplicationWorker.getInstance().addJob(
                    defect.getProject(),
                    defect.getProduct(),
                    true
            );
        }

        return "{error:false}";
    }
}
