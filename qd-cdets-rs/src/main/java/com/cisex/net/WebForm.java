package com.cisex.net;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: huaiwang
 * Date: 13-9-11
 * Time: 下午1:22
 */
public class WebForm {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    private String method;
    private String action;
    private Map<String, String> params = new HashMap<String, String>();


    public static List<WebForm> fromHtml(String html) {
        return fromHtml(html, "form");
    }


    public static List<WebForm> fromHtml(String html, String jsoupFormSelector) {
        Document doc = Jsoup.parse(html);
        Elements forms = doc.select(jsoupFormSelector);


        List<WebForm> webForms = new ArrayList<WebForm>();
        for (Element form : forms) {
            WebForm webForm = new WebForm();

            // action url
            webForm.action = form.attr("action").trim();


            // method
            String method = form.attr("method");

            if (METHOD_GET.equalsIgnoreCase(method.trim()))
                webForm.method = METHOD_POST;
            else
                webForm.method = METHOD_GET;


            // input fields values
            Elements inputs = form.select("input");

            for (Element input : inputs) {
                if (!input.hasAttr("name")) continue;
                String name = input.attr("name");
                String value = input.attr("value");
                webForm.addParam(name, value);
            }

            // select field values
            Elements selects = form.select("select");

            for (Element select : selects) {
                if (!select.hasAttr("name")) continue;
                String name = select.attr("name");
                String value = getSelectElementValue(select);
                webForm.addParam(name, value);
            }

            webForms.add(webForm);
        }

        return webForms;
    }


    private static String getSelectElementValue(Element select) {
        Elements options = select.select("option[selected]");
        if(options.size() > 0) {
            return options.get(0).attr("value");
        }

        options = select.select("option");

        if(options.size() > 0) {
            return options.get(0).attr("value");
        }

        return "";
    }


    public void addParam(String name, String value) {
        params.put(name, value);
    }

    public String getParam(String name) {
        return params.get(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("> ").append(method).append(": ").append(action).append("\n");

        for (String name : params.keySet()) {
            sb.append("> ").append(name).append(": ").append(params.get(name)).append("\n");
        }

        return sb.toString();
    }

    // ************************ accessor ************************

    public List<NameValuePair> getFormData() {
        List<NameValuePair> lst = new ArrayList<NameValuePair>();

        for (String name : params.keySet()) {
            lst.add(new BasicNameValuePair(name, params.get(name)));
        }

        return lst;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
