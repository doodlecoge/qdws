package com.cisex.job;

/**
 * User: huaiwang
 * Date: 13-11-4
 * Time: 下午5:20
 */

import com.cisex.dao.QddtsDao;
import com.cisex.model.QddtsTrend;
import com.cisex.service.QddtsCacher;
import com.hch.utils.time.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * use singleton such that there's only one thread running.
 * we do this to avoid burdening qddts too much.
 */
public final class QddtsWorker extends Thread {
    private static final Logger log = LoggerFactory.getLogger(QddtsWorker.class);
    private static final QddtsWorker instance = new QddtsWorker();


    private Set<Integer> hashes = new HashSet<Integer>();
    private List<String> queries = new ArrayList<String>();
    public static String currentQuery = null;


    private boolean stop = false;
    private boolean pause = false;


    private QddtsWorker() {

    }

    public static QddtsWorker getInstance() {
        return instance;
    }

    public List<String> getQueries() {
        return queries;
    }

    public synchronized void addQuery(String query) {
        query = query.trim();

        int hash = query.hashCode();

        if (hashes.contains(hash)) {
            return;
        }

        hashes.add(hash);
        queries.add(query);
    }

    private synchronized String getQuery() {
        if (queries.size() < 1) {
            return null;
        }

        String query = queries.remove(0);
        int hash = query.hashCode();
        hashes.remove(hash);

        return query;
    }

    public void shutDown() {
        this.stop = true;
        this.stop();
    }


    public void _run() {
        currentQuery = getQuery();

        if (currentQuery == null) return;

        log.debug(">> caching: " + currentQuery);
        try {
            log.error("=========1" + currentQuery);
            QddtsCacher.cache(currentQuery, true);
            log.error("=========2" + currentQuery);
        } catch (Exception e) {
            log.error("=========3" + currentQuery);
        }
        log.debug("<< caching: " + currentQuery);

        currentQuery = null;
    }

    @Override
    public void run() {
        long lastCheckStaleTime = 0;

        while (!stop) {
            if (!pause) {
                _run();

                long now = System.currentTimeMillis();

                if (now - lastCheckStaleTime > TimeUtils.HourMilliSeconds) {
                    List<QddtsTrend> allStaleQuery = QddtsDao.getAllStaleQuery();

                    for (QddtsTrend qddtsTrend : allStaleQuery) {
                        addQuery(qddtsTrend.getQuery());
                    }

                    lastCheckStaleTime = System.currentTimeMillis();
                }

                _sleep(TimeUtils.SecondMilliSeconds);
            } else _sleep(TimeUtils.MinuteMilliSeconds);
        }
    }


    private void _sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.warn("sleep interrupt", e);
        }
    }
}
