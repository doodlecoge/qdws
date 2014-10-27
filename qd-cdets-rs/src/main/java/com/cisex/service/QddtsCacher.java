package com.cisex.service;

import com.cisex.QdCdetsProperties;
import com.cisex.dao.QddtsDao;
import com.cisex.job.QddtsWorker;
import com.cisex.model.QddtsTrend;
import com.cisex.net.QddtsTrendQuery;
import com.cisex.util.DbUtils;
import com.hch.utils.time.TimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.cookie.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * User: huaiwang
 * Date: 13-9-13
 * Time: 上午9:28
 */
public class QddtsCacher {
    private static final Logger log = LoggerFactory.getLogger(QddtsCacher.class);
    private static final String QddtsDateFormat = "yyMMdd";


    public static boolean needUpdate(int hash) {
        QddtsTrend qddtsTrend = DbUtils.querySingleEntity(QddtsTrend.class, hash);
        if (qddtsTrend == null) return true;

        Date updatedOn = qddtsTrend.getUpdatedOn();
        if (updatedOn == null) return true;

        String gap = QdCdetsProperties.getString("qddts.query.update.time.gap");
        long gapMs = TimeUtils.timeGap(gap);
        long diff = Calendar.getInstance().getTimeInMillis() - updatedOn.getTime();
        if (diff < gapMs) return false;
        else return true;
    }

    public static void cache(String qddtsQuery, boolean force) {
        qddtsQuery = qddtsQuery.trim();
        int hash = qddtsQuery.hashCode();

        if (!force && !needUpdate(hash)) return;

        Calendar now = Calendar.getInstance();

        String date1 = DateUtils.formatDate(now.getTime(), QddtsDateFormat);
        now.add(Calendar.MONTH, -12); // get one year trend data
        String date0 = DateUtils.formatDate(now.getTime(), QddtsDateFormat);

        List<String> outTrendData = null;

        try {
            log.debug("querying qddts: " + qddtsQuery
                    + " from " + date0 + " to " + date1);
            outTrendData = QddtsTrendQuery.query3(qddtsQuery, date0, date1);
            log.debug("data: " + outTrendData.size());
        } catch (Exception e) {
            log.error("query qddts trend data failed.", e);
        }

        updateData(qddtsQuery, hash, outTrendData);
    }

    public static void updateData(String query, int hash, List<String> trendData) {
        QddtsTrend qddtsTrend = DbUtils.querySingleEntity(QddtsTrend.class, hash);

        if (qddtsTrend == null) {
            if (trendData == null) return;
            qddtsTrend = new QddtsTrend();
            qddtsTrend.setQueryHash(hash);
            qddtsTrend.setQuery(query);
        }

        Date now = new Date();

        if (trendData != null) {
            qddtsTrend.setTrendData("[[" + StringUtils.join(trendData, "],[") + "]]");
            qddtsTrend.setUpdatedOn(now);
        }

        qddtsTrend.setVisitedOn(now);
        DbUtils.saveOrUpdate(qddtsTrend);
    }

    public static void cache(String qddtsQuery) {
        cache(qddtsQuery, false);
    }


    public static void update() {
        List<QddtsTrend> trends = QddtsDao.getAllStaleQuery();

        for (QddtsTrend trend : trends) {
            try {
                cache(trend.getQuery());
            } catch (Exception e) {
                log.error("cache: [" + trend.getQuery() + "] fail", e);
            }
        }
    }

    public static void updateAll(boolean force) throws NoSuchPaddingException, InvalidKeySpecException, NoSuchAlgorithmException, IOException, BadPaddingException, URISyntaxException, InvalidKeyException, IllegalBlockSizeException {
        List<QddtsTrend> trends = QddtsDao.getAllTrends();

        for (QddtsTrend trend : trends) {
            if (force) {
                cache(trend.getQuery(), true);
            } else {
                cache(trend.getQuery());
            }
        }
    }

    public static void refreshAllAsync() {
        List<QddtsTrend> trends = QddtsDao.getAllTrends();
        for (QddtsTrend trend : trends) {
            QddtsWorker.getInstance().addQuery(trend.getQuery());
        }
    }
}
