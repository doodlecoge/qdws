package com.cisex.service;


import com.cisex.ds.KeyValuePair;
import com.cisex.model.DailyCount;
import com.cisex.model.SeverityStatusCount;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/2/13
 * Time: 9:56 AM
 * To change this template use File | Settings | File Templates.
 */

//@WebService
public interface CdetsReplication {
    //    @WebMethod
    public List<DailyCount> getDailyDefects(String timeField, String query);

    //    @WebMethod
    public List<SeverityStatusCount> groupBySeverityAndStatus(String query);

//    public List<KeyValuePair<String, Integer>> groupByComponent(String query);

    public List<KeyValuePair<String, Integer>> groupBy(String field, String query);

    // get oldest updated time among all updated datetimes of products returned by this query
    public Date getUpdatedTime(String query);
}
