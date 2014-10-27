package com.cisex.net;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 12-8-21
 * Time: 上午9:29
 * To change this template use File | Settings | File Templates.
 */

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HttpEngine {
    private static final int DefaultTimeout = 240000;
    private HttpClient client;
    private HttpResponse resp;

    // ***************************************************************
    // https
    X509TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    };

    X509HostnameVerifier verifier = new X509HostnameVerifier() {
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }

        public void verify(String arg0, String[] arg1, String[] arg2)
                throws SSLException {
        }

        public void verify(String arg0, X509Certificate arg1)
                throws SSLException {
        }

        public void verify(String arg0, SSLSocket arg1) throws IOException {
        }
    };

    // end
    // ***************************************************************

    {
        try {
            this.client = new DefaultHttpClient();

            this.client.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, DefaultTimeout);

            this.client.getParams().setParameter(
                    CoreConnectionPNames.SO_TIMEOUT, DefaultTimeout);

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, verifier);
            ClientConnectionManager ccm = this.client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));

        } catch (NoSuchAlgorithmException e) {
        } catch (KeyManagementException e) {
        }
    }

    public HttpEngine get(String url) throws IOException {
        return this.get(url, null);
    }

    public HttpEngine get(String url, List<NameValuePair> params) throws IOException {
        return this.get(url, params, new Header[]{});
    }

    public HttpEngine get(String url, List<NameValuePair> params, Header header) throws IOException {
        return this.get(url, params, new Header[]{header});
    }

    public HttpEngine get(String url, List<NameValuePair> params, Header[] headers) throws IOException {
        String getUrl = url;

        if (params != null && params.size() > 0)
            getUrl += "?" + URLEncodedUtils.format(params, "UTF-8");

        HttpGet get = new HttpGet(getUrl);

        if (headers != null && headers.length > 0)
            get.setHeaders(headers);

        return this.exec(get);
    }

    public HttpEngine post(String url, List<NameValuePair> postData)
            throws IOException {
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData,
                "UTF-8");

        HttpPost post = new HttpPost(url);
        post.setEntity(entity);

        return this.exec(post);
    }

    public HttpEngine post(String url, Map<String, String> postData)
            throws IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        Iterator<String> it = postData.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String val = postData.get(key);
            params.add(new BasicNameValuePair(key, val));
        }

        return this.post(url, params);
    }

    private HttpEngine exec(HttpUriRequest req) throws IOException {
        if (this.resp != null) this.consume();
        this.resp = this.client.execute(req);
        return this;
    }

    public void consume() {
        if (this.resp == null)
            return;

        try {
            this.resp.getEntity().getContent().close();
        } catch (Exception e) {
        }
    }

    public Header[] getAllHeaders() {
        if (this.resp != null)
            return this.resp.getAllHeaders();
        else
            return null;
    }

    public Header[] getHeaders(String key) {
        if (this.resp != null)
            return this.resp.getHeaders(key);
        else
            return null;
    }

    public int getStatusCode() {
        if (this.resp != null)
            return this.resp.getStatusLine().getStatusCode();
        else
            return -1;
    }

    public InputStream getInputStream() throws IllegalStateException, IOException {
        return this.resp.getEntity().getContent();
    }

    public String getHtml() throws IllegalStateException, IOException {
        InputStream is = this.resp.getEntity().getContent();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b = -1;

        while ((b = is.read()) != -1)
            baos.write(b);

        is.close();

        String enc = null;

        if (enc != null)
            return new String(baos.toByteArray(), enc);
        else
            return baos.toString();
    }

    public CookieStore getCookieStore() {
        return ((DefaultHttpClient) this.client).getCookieStore();
    }

    public void setCookie(CookieStore cs) {
        ((DefaultHttpClient) this.client).setCookieStore(cs);
    }
}

