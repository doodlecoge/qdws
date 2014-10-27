package com.cisex.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/24/13
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */

@Embeddable
public class DefectVersionPK implements Serializable {

    @Column(name = "defect_id")
    public String id;

    @Column(name = "version")
    private String version;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    @Override
    public boolean equals(Object obj) {
        return obj.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return (id + version).hashCode();
    }

}
