package com.cisex.service;

import com.cisex.Global;
import com.cisex.QdCdetsProperties;
import com.cisex.model.Product;
import com.cisex.util.DbUtils;
import com.hch.utils.time.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/1/13
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ReplicationWorker extends Thread {
    private static final Logger log = LoggerFactory.getLogger(ReplicationWorker.class);

    private static final List<ReplicationJobWrapper> Jobs = new ArrayList<ReplicationJobWrapper>();

    public static boolean stop = false;
    public static boolean pause = false;
    public static ReplicationJobWrapper CurJob;

    private static ReplicationWorker ins = new ReplicationWorker();

    private ReplicationWorker() {
    }

    public static ReplicationWorker getInstance() {
        return ins;
    }

    public synchronized void addJob(String project, String product, boolean force) {
        ReplicationJobWrapper newJob = new ReplicationJobWrapper(project, product, force);
        for (ReplicationJobWrapper job : Jobs) {
            if (newJob.equals(job)) return;
        }

        Jobs.add(newJob);
    }

    public List<ReplicationJobWrapper> getJobs() {
        return Jobs;
    }

    private synchronized ReplicationJobWrapper getJob() {
        return Jobs.remove(0);
    }

    public void shutDown() {
        stop = true;
        this.stop();
    }

    @Override
    public void run() {
        long cdets_defect_updated_time = System.currentTimeMillis();
        long last_health_check_time = System.currentTimeMillis();

        while (!stop) {
            long now = System.currentTimeMillis();

            if (!pause) {
                try {
                    if (Jobs.size() > 0) {
                        runRegularJob();
                    }

                    // update job & health check
                    else {

                        long gap = TimeUtils.timeGap(QdCdetsProperties.getString("update.try.gap.time"));
                        if (now - cdets_defect_updated_time > gap) {
                            DatabaseUpdateJob.execute();

                            cdets_defect_updated_time = now;
                        }
                    }

                    if (now - last_health_check_time >
                            TimeUtils.timeGap(
                                    QdCdetsProperties.getString("health.check.gap.time")
                            )) {
                        CdetsHealthCheck.healthCheck();
                    }


                } catch (Exception e) {
                    log.error("<-> ", e);
                }
            }

            _sleep(Global.JOB_GAP_TIME);
        }
    }

    private void runRegularJob() throws IOException, URISyntaxException {
        ReplicationJobWrapper job = getJob();
        CurJob = job;
        String project = job.getProject();
        String product = job.getProduct();
        boolean force = job.isForce();

        Product prod = DbUtils.getProduct(project, product);

        if (prod == null) CdetsReplicator.replicate(project, product);
        else CdetsReplicator.updateProject(project, product, force);
        CurJob = null;
    }

    public static void _sleep(long mills) {
        try {
            sleep(mills);
        } catch (InterruptedException e) {
            log.warn("sleep interrupted", e);
        }
    }
}
