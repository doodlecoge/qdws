package com.cisex.service;

import com.hch.utils.net.HttpEngine;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * User: huaiwang
 * Date: 13-9-13
 * Time: 下午2:59
 */
public class QddtsCacherTest {
    public static void main(String[] args) throws IOException, URISyntaxException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {

//        List<String> query = QddtsTrendQuery.query("", "121114", "130113");
//
//        for (String integer : query) {
//            System.out.println(integer);
//        }


        HttpEngine.setConnectionTimeout(500000);
        HttpEngine.setReadTimeout(500000);
        String query = "Severity:1,2,3,4,5 and Project:CSC.csg";
        QddtsCacher.cache(query, true);
    }
}
