package com.cisex.service;


import com.cisex.job.QddtsWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/2/13
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class CdetsAppListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(CdetsAppListener.class);
    private ReplicationWorker worker = ReplicationWorker.getInstance();
    private QddtsWorker qddtsWorker = QddtsWorker.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        worker.start();
        qddtsWorker.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        worker.shutDown();
        qddtsWorker.shutDown();
    }
}
