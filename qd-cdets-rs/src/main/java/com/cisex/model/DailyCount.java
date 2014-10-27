package com.cisex.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/2/13
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class DailyCount {
    private int count;
    private Date date;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
