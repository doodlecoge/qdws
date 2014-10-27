package com.cisex.service;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/1/13
 * Time: 9:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReplicationJobWrapper {
    private String project;
    private String product;
    private boolean force;

    public ReplicationJobWrapper(String project, String product, boolean force) {
        this.project = project;
        this.product = product;
        this.force = force;
    }

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

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    @Override
    public int hashCode() {
        return (project.trim() + product.trim() + force).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }
}
