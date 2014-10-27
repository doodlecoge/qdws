package com.cisex;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-8-28
 * Time: 上午10:31
 * To change this template use File | Settings | File Templates.
 */
public class AuditTrialReader {
    public static void main(String[] args) throws IOException {

        FileInputStream fis = new FileInputStream("f:\\tmp\\his.txt");

        BufferedReader br = new BufferedReader(new FileReader("f:\\tmp\\his.txt"));

        Pattern ptn = Pattern.compile("^(.{10}) (.*\t)(.*\t)(.*\t)(.*\t)(.*)$");
        String line = null;
        int count = 0;
        while ((line = br.readLine()) != null) {
            Matcher matcher = ptn.matcher(line);
            if(matcher.find()) {
                System.out.println("--- " + count++);
                System.out.println(matcher.group(1));
                System.out.println(matcher.group(2));
                System.out.println(matcher.group(3));
                System.out.println(matcher.group(4));
                System.out.println(matcher.group(5));
                System.out.println(matcher.group(6));
            }
        }
    }

}
