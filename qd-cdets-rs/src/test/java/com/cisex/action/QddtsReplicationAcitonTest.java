package com.cisex.action;

import com.hch.utils.net.HttpEngine;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: huaiwang
 * Date: 13-11-13
 * Time: 下午3:39
 */
public class QddtsReplicationAcitonTest {
    public static void main(String[] args) throws IOException, URISyntaxException {
        String url = "http://10.224.138.205/qd-cdets-rs";
//        String url = "http://localhost:8080/cdets";
        String query = "( Version~T26,t27,T28,T29 ) minus Version:PSO minus Version~WebEx11,WAPI or Product:db or Product:mmp or Product:app-mc or Product:web-com or Product:telephony or Product:client-com or Product:eureka or Product:app-webacd or Product:app-framework or Product:app-sac and Severity:1,2,3,4,5 and Project:CSC.csg and Found:customer-use";

        List<NameValuePair> lst = new ArrayList<NameValuePair>();
        lst.add(new BasicNameValuePair("query", query));
        HttpEngine eng = new HttpEngine();
        String html = eng.post(url + "/act/service/qddts", lst).getHtml();


        System.out.println(html);


        System.out.println("Severity:1,2,3,4,5 and Project:CSC.csg".hashCode());
    }
}
