package com.cisex.crawler;

import com.cisex.QdCdetsProperties;
import com.cisex.net.HttpEngine;
import com.cisex.util.RandomUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-8-29
 * Time: 下午1:28
 * To change this template use File | Settings | File Templates.
 */
public class CdetsDumper {
    public static InputStream dump(List<String> defectIds) throws IOException {
        List<NameValuePair> lst = new ArrayList<NameValuePair>();

        lst.add(new BasicNameValuePair("username", RandomUtils.randomUsername()));
        lst.add(new BasicNameValuePair("format", "xml"));
        lst.add(new BasicNameValuePair("identifier", StringUtils.join(defectIds, ",")));

        String url = QdCdetsProperties.getString("cdets.dump.url");
        HttpEngine eng = new HttpEngine();
        return eng.get(url, lst).getInputStream();
    }
}
