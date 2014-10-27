package com.cisex.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/10/13
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class CdetsResultParser {
    public static List<String> getComponents(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;

        List<String> lst = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
            lst.add(line.trim());
        }

        return lst;
    }
}
