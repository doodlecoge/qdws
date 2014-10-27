package com.cisex.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/1/13
 * Time: 8:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "products")
public class Product {
    @EmbeddedId
    private ProductPK id = new ProductPK();

    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    public ProductPK getId() {
        return id;
    }

    public void setId(ProductPK id) {
        this.id = id;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
