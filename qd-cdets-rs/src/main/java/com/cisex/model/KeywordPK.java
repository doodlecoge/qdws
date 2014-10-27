package com.cisex.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * User: huaiwang
 * Date: 13-9-29
 * Time: 下午1:25
 */

@Embeddable
public class KeywordPK implements Serializable {
    @Column(name = "defect_id")
    private String defectId;

    @Column(name = "keyword")
    private String keyword;

    public String getDefectId() {
        return defectId;
    }

    public void setDefectId(String defectId) {
        this.defectId = defectId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
