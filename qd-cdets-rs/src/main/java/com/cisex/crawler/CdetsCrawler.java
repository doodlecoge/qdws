package com.cisex.crawler;

import com.cisex.GeneralException;
import com.cisex.Global;
import com.cisex.QdCdetsProperties;
import com.cisex.model.DefectField;
import com.cisex.model.TimeField;
import com.cisex.util.RandomUtils;
import com.cisex.util.UrlUtils;
import com.hch.utils.net.HttpEngine;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 6/28/13
 * Time: 11:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class CdetsCrawler {
    private static final Logger log = LoggerFactory.getLogger(CdetsCrawler.class);

    public static final HttpEngine eng = new HttpEngine();

    private static final String[] _2escape = new String[]{" ", "=", ",", "%", "\\", "#", "/", ">", "<", ":", "[", "]"};
    private static final String[] _escape2 = new String[]{"%20", "%3D", "%2C", "%25", "%5C", "%23", "%2F", "%3E", "%3C", "%3A", "%5B", "%5D"};
    private static final String CdetsQueryDateFmt = "MM/dd/yyyy HH:mm:ss";


    public static int getCount(String project, String product) throws IOException, URISyntaxException {
        return getCount(project, product, null, null);
    }

    public static int getCount(String project, String product, Date startDate, Date endDate) throws IOException, URISyntaxException {
        String query = String.format("Project='%s' AND Product='%s'", project, product);

        if (startDate != null) {
            String startDateString = DateUtils.formatDate(startDate, CdetsQueryDateFmt);
            query += String.format(" AND [Submitted-on] >= '%s'", startDateString);
        }

        if (endDate != null) {
            String endDateString = DateUtils.formatDate(endDate, CdetsQueryDateFmt);
            query += String.format(" AND [Submitted-on] <= '%s'", endDateString);
        }

        query = StringUtils.replaceEach(query, _2escape, _escape2);

        return getCount(query);
    }

    public static int getCount(String project, String product, String component) throws IOException, URISyntaxException {
        String query = String.format(
                "Project='%s' AND Product='%s' AND Component='%s'",
                project, product, component
        );

        query = StringUtils.replaceEach(query, _2escape, _escape2);

        return getCount(query);
    }


    public static int getCount(String query) throws IOException, URISyntaxException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("username", RandomUtils.randomUsername()));
        params.add(new BasicNameValuePair("noprint", "1"));
        params.add(new BasicNameValuePair("count", "1"));
        params.add(new BasicNameValuePair("query", query));

        String url = Global.CDETS_SEARCH_URL + "?" + UrlUtils.joinParameters(params);
        String cnt = eng.get(url).getHtml();

        return Integer.valueOf(cnt.trim());
    }


    public static InputStream getDefects(String project, String product, Date startDate, Date endDate) throws IOException, URISyntaxException {
        String query = null;

        query = String.format("Project='%s' AND Product='%s'", project, product);

        if (startDate != null) {
            String startDateString = DateUtils.formatDate(startDate, CdetsQueryDateFmt);
            query += String.format(" AND [Submitted-on] >= '%s'", startDateString);
        }

        if (endDate != null) {
            String endDateString = DateUtils.formatDate(endDate, CdetsQueryDateFmt);
            query += String.format(" AND [Submitted-on] <= '%s'", endDateString);
        }

        query = StringUtils.replaceEach(query, _2escape, _escape2);

        String url = buildUrl() + "&query=" + query;

        log.debug(url);
        eng.get(url);

        int statusCode = eng.getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new GeneralException(
                    "get defects error, status code: " + statusCode + ", " + url
            );
        }

        return eng.getInputStream();
    }

    public static InputStream getDefectById(String defectId) throws IOException, URISyntaxException {
        String url = buildUrl() + "&identifier=" + defectId;

        log.debug(url);
        eng.get(url);

        int statusCode = eng.getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new GeneralException(
                    "get defect [" + defectId + "] error, status code: " + statusCode + ", " + url
            );
        }

        return eng.getInputStream();
    }

    private static String getNewOrUpdatedDefectsQueryString(String project, String product, Date date) {
        String query = null;

        if (date == null) {
            query = String.format(
                    "Project='%s' AND Product='%s'",
                    project, product
            );
        } else {
            String dateStr = DateUtils.formatDate(date, "MM/dd/yyyy HH:mm:ss");

            query = String.format(
                    "Project='%s' AND Product='%s' AND [Sys-Last-Updated]>='%s'",
                    project, product, dateStr
            );
        }

        return StringUtils.replaceEach(query, _2escape, _escape2);
    }

    public static int getNewOrUpdatedDefectsCount(String project, String product, Date date) throws IOException, URISyntaxException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String query = getNewOrUpdatedDefectsQueryString(project, product, date);

        params.add(new BasicNameValuePair("username", RandomUtils.randomUsername()));
        params.add(new BasicNameValuePair("noprint", "1"));
        params.add(new BasicNameValuePair("count", "1"));
        params.add(new BasicNameValuePair("query", query));

        String url = Global.CDETS_SEARCH_URL + "?" + UrlUtils.joinParameters(params);
        String cnt = eng.get(url).getHtml();

        return Integer.valueOf(cnt.trim());
    }

//    public static InputStream getNewOrUpdatedDefects(String project, String product) throws IOException {
//        Date date = DbUtils.getLatestLastUpdatedTime(project, product);
//
//        if (date == null)
//            return getDefects(project, product);
//        else
//            return getNewOrUpdatedDefects(project, product, date);
//    }

    public static InputStream getNewOrUpdatedDefects(String project, String product, Date date) throws IOException, URISyntaxException {
        String query = getNewOrUpdatedDefectsQueryString(project, product, date);

        String url = buildUrl() + "&query=" + query;

        log.debug("base time: " + date);
        log.debug(url);

        eng.get(url);

        int statusCode = eng.getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new GeneralException("get defect error, status code: " + statusCode);
        }

        return eng.getInputStream();
    }

    public static InputStream getProducts(String project) throws IOException, URISyntaxException {
        String url = Global.CDETS_URL + "?username=bjtd&project=" + project + "&metadata=Product";

        log.debug(url);
        eng.get(url);

        int statusCode = eng.getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new GeneralException("get defect error, status code: " + statusCode);
        }

        return eng.getInputStream();
    }

    private static String buildUrl() {
        String url = Global.CDETS_SEARCH_URL + "?noprint=1&order=Submitted-on&username=" + RandomUtils.randomUsername();

        String fields = buildFields();
        fields = StringUtils.replaceEach(fields, _2escape, _escape2);

        String delimiter = " " + Global.FIELD_SPLITTER;
        delimiter = StringUtils.replaceEach(delimiter, _2escape, _escape2);

        url += "&fieldDelimiter=" + delimiter + "&field=" + fields;

        return url;
    }

    public static String buildFields() {
        StringBuilder sb = new StringBuilder();

        for (DefectField field : DefectField.values()) {
            sb.append(field.cdetsFieldName);
            sb.append(",");
        }

        for (TimeField field : TimeField.values()) {
            sb.append(field.cdetsFieldName);
            sb.append(",");
        }

        int len = sb.length();
        sb.delete(len - 1, len);

        return sb.toString();
    }


    public static InputStream getDefectAuditTrials(List<String> defectIds) throws IOException, URISyntaxException {
        if (defectIds == null || defectIds.size() == 0) return null;

        List<NameValuePair> lst = new ArrayList<NameValuePair>();

        lst.add(new BasicNameValuePair("username", RandomUtils.randomUsername()));
        lst.add(new BasicNameValuePair("content", "history"));
        lst.add(new BasicNameValuePair("identifier", StringUtils.join(defectIds, ",")));


        String dumpUrl = QdCdetsProperties.getString("cdets.dump.url");

        return eng.get(dumpUrl, lst).getInputStream();
    }
}
