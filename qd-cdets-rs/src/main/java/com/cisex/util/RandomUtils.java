package com.cisex.util;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/1/13
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class RandomUtils {
    private static final String Numbers = "0123456789";
    private static final String Characters = "abcdefghijklmnopqrstuvwxyz";
    private static final String[] UserNames = "juzhang2,aggchen,hivanc,yolchen,juahao,lialin,taliu2,smao,sallyt,huaiwang,yanlwang,lisay,maryin,charzhao,vezhou,brzhu,eqiu,justchen,ivyh,fairyw,menye,sammyy,xiaopzho,mercuryw".split(",");


    public static String randomUsername() {
        int length = UserNames.length;
        Random rand = new Random();
        int idx = rand.nextInt(length);

        return UserNames[idx];
    }
}
