package com.cisex.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 6/17/13
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */

@Embeddable
public class ProductPK implements Serializable {
    @Column(name = "project")
    private String project;

    @Column(name = "product")
    private String product;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        return (project + product).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }
}
