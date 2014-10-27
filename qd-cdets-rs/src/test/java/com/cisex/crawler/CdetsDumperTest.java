package com.cisex.crawler;

import com.cisex.util.TimeProfiler;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-8-29
 * Time: 下午1:34
 * To change this template use File | Settings | File Templates.
 */
public class CdetsDumperTest {
    public static void main(String[] args) throws IOException {
        List<String> lst = new ArrayList<String>();
//        lst.add("CSCuh80437");
        String[] l = "CSCua17447,CSCua17452,CSCua27245,CSCua27274,CSCua27302,CSCua27306,CSCua27308,CSCua27309,CSCua27312,CSCua27318".split(",");
        for (int i = 0; i < l.length; i++) {
            lst.add(l[i]);
        }

        TimeProfiler tp = new TimeProfiler("dump time");
        tp.start();
        InputStream is = CdetsDumper.dump(lst);
        IOUtils.copy(is, new FileOutputStream("f:/tmp/dump.xml"));
        tp.stop();


    }
}
