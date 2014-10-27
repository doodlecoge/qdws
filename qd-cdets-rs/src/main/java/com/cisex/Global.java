package com.cisex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 6/25/13
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Global {
    public static final Logger LOG = LoggerFactory.getLogger(Global.class);

    public static final String CDETS_SEARCH_URL;
    public static final String CDETS_URL;
    public static final int UPDATE_GAP_TIME;
    public static final int JOB_GAP_TIME;
    public static final int CDETS_RESULT_LIMIT = 10000;
    public static final String FIELD_SPLITTER = "\u5206\u9694\u7B26";


    public static final String[] DatePatterns = new String[] {
            "MM/dd/yyyy",
            "MM/dd/yyyy HH:mm:ss",
            "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss"
    };

    public static final Map<String, String> SysProps = new HashMap<String, String>();

    static {
        InputStream is = Global.class.getResourceAsStream("/system.properties");
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (Exception e) {
            LOG.error("LOAD system.properties FAILED", e);
        }

        CDETS_URL            = props.getProperty("cdets.url", "http://cdetsweb-prd.cisex.com/apps/cdets");
        CDETS_SEARCH_URL     = props.getProperty("cdets.search.url", "http://cdetsweb-prd.cisex.com/findsimple/findcr_simple");

        int updateGapTime    = Integer.parseInt(props.getProperty("update.gap.time", "60")) * 60 * 1000;
        int jobGapTime       = Integer.parseInt(props.getProperty("job.gap.time", "10")) * 1000;

        int minUpdateGapTime = 1800 * 1000;
        int minJobGapTime    = 1 * 1000;

        UPDATE_GAP_TIME      = updateGapTime > minUpdateGapTime ? updateGapTime : minUpdateGapTime;
        JOB_GAP_TIME         = jobGapTime > minJobGapTime ? jobGapTime : minJobGapTime;
    }
}
