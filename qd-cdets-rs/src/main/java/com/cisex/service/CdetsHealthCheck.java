package com.cisex.service;

import com.cisex.crawler.CdetsCrawler;
import com.cisex.util.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

/**
 * User: huaiwang
 * Date: 13-11-13
 * Time: 上午9:38
 */
public class CdetsHealthCheck {
    private static final Logger log = LoggerFactory.getLogger(CdetsHealthCheck.class);

    public static void healthCheck() {
        List<Object> prods = DbUtils.getAllProductsOrderByDefectCount();
        for (Object prod : prods) {
            Object[] p = (Object[]) prod;
            log.error("" + p.length);
            String project = p[0].toString();
            String product = p[1].toString();


            try {
                CdetsHealthCheck.check(project, product);
            } catch (Exception e) {
                e.printStackTrace();
            }

            break; // check 1 at a time
        }
    }

    public static void check(String project, String product) throws IOException, URISyntaxException {
        check(project, product, false);
    }


    public static void check(String project, String product, boolean force) throws IOException, URISyntaxException {
        Date latestSubmitTime = DbUtils.getLatestSubmitTime(project, product);

        int countReal = CdetsCrawler.getCount(project, product, null, latestSubmitTime);
        int countInDb = DbUtils.getCount(project, product, null, latestSubmitTime);

        log.debug(project + ", " + product + ": " + countReal + ", " + countInDb);

        if (force || countReal != countInDb) {
            DbUtils.staleProduct(project, product);

            try {
                CdetsReplicator.replicate(project, product);
            } catch (Exception e) {
                log.error("==", e);
            }
        }

        DbUtils.updateHealthCheckTime(project, product);
    }

}
