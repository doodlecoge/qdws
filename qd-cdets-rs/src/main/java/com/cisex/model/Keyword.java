package com.cisex.model;

import javax.persistence.*;

/**
 * User: huaiwang
 * Date: 13-9-29
 * Time: 下午1:18
 */

@Entity
@Table(name = "defects_keywords")
public class Keyword {
    @EmbeddedId
    private KeywordPK id;

    public KeywordPK getId() {
        return id;
    }

    public void setId(KeywordPK id) {
        this.id = id;
    }
}
