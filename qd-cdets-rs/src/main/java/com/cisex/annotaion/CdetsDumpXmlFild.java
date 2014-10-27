package com.cisex.annotaion;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-8-29
 * Time: 下午5:29
 * To change this template use File | Settings | File Templates.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CdetsDumpXmlFild {
    public String value();
}
