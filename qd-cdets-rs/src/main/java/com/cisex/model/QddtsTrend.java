package com.cisex.model;

import javax.persistence.*;
import java.util.Date;

/**
 * User: huaiwang
 * Date: 13-9-12
 * Time: 下午4:36
 */

@Entity
@Table(name = "qddts_trends")
public class QddtsTrend {
    @Id
    private int queryHash;

    private String query;

    private String trendData;

    private Date startDate;

    private Date endDate;

    private Date createdOn;

    private Date updatedOn;

    private Date visitedOn;

    public int getQueryHash() {
        return queryHash;
    }

    public void setQueryHash(int queryHash) {
        this.queryHash = queryHash;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Date getVisitedOn() {
        return visitedOn;
    }

    public void setVisitedOn(Date visitedOn) {
        this.visitedOn = visitedOn;
    }

    public String getTrendData() {
        return trendData;
    }

    public void setTrendData(String data) {
        this.trendData = data;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
