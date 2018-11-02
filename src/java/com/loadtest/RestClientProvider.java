/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loadtest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

/**
 *
 * @author NakkaNar
 */
public class RestClientProvider {

    private String url;
    private String postBody;
    private String responseBody;
    private HashMap<String, String> responseHeaders;
    private HashMap<String, String> requestHeaders;
    private String responseCode;
    private String payLoad = "";
    private WebResource webResource;

    public void fireGetRequest() {

        try {
            url = url.replace(" ", "%20");
            Client client = Client.create(this.hostIgnoringClient());
            webResource = client
                    .resource(url);

            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);
            this.setResponseCode(response.getStatus() + "");
            this.setResponseBody(response.getEntity(String.class));
            this.setResponseHeaders(response.getHeaders());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Jenkins server Down!");
        }
    }

    public ClientConfig hostIgnoringClient() throws KeyManagementException, NoSuchAlgorithmException {
        final ClientConfig config = new DefaultClientConfig();
        config.getProperties()
                .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                        new HTTPSProperties(
                                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER, SSLUtil.getInsecureSSLContext()));
        return config;
    }

    public void addHeader(String key, String value) {
        if (webResource != null) {
            System.out.println("Added Header");
            webResource.header(key, value);
        }
    }

    public void firePostRequest() {

        try {

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            ClientConfig config = new DefaultClientConfig();
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            }, context));

            url = url.replace(" ", "%20");
            System.out.println("RestClient : " + url);
            Client client = Client.create(config);
            WebResource webResource = client
                    .resource(url);
            ClientResponse response = webResource.type("application/json")
                    .post(ClientResponse.class, payLoad);

            this.setResponseCode(response.getStatus() + "");
            this.setResponseBody(response.getEntity(String.class));
            this.setResponseHeaders(response.getHeaders());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void firePostRequest(String headername, String value) {

        try {

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            ClientConfig config = new DefaultClientConfig();
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            }, context));

            url = url.replace(" ", "%20");
            System.out.println("RestClient : " + url);
            Client client = Client.create(config);
            WebResource webResource = client
                    .resource(url);

            ClientResponse response = webResource.type("application/json").header(headername, value)
                    .post(ClientResponse.class, payLoad);

            this.setResponseCode(response.getStatus() + "");
            this.setResponseBody(response.getEntity(String.class));
            this.setResponseHeaders(response.getHeaders());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public HashMap<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(MultivaluedMap<String, String> m) {
        this.responseHeaders = new HashMap<String, String>();
        if (m == null) {

        } else {
            for (Entry<String, List<String>> entry : m.entrySet()) {
                StringBuilder sb = new StringBuilder();
                for (String s : entry.getValue()) {
                    if (sb.length() > 0) {
                        sb.append(',');
                    }
                    sb.append(s);
                }
                responseHeaders.put(entry.getKey(), sb.toString());
            }
        }

    }

    public HashMap<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(HashMap<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    public String firePostRequest(String userAuthentication, String serverURL, String postBody) {
        String response = "";
        HttpsURLConnection conn = null;
        try {
            System.out.print("Status...");
            URL url = new URL(serverURL);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", userAuthentication);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            // SSL setting
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{
                new javax.net.ssl.X509TrustManager() {

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }
                }
            }, null);
            conn.setSSLSocketFactory(context.getSocketFactory());
            String param = postBody;
            //System.out.println(param);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(param);
            wr.flush();
            wr.close();
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;

            //System.out.println("Confluence Page Response After Update : \n");
            while ((line = reader.readLine()) != null) {
                System.out.printf("%s\n", line);
                responseBody = responseBody + line;
            }
            reader.close();

        } catch (Exception e) {

            System.exit(0);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return responseBody;
    }

    public void firePostRequestJSON() {
        try {

            Client client = Client.create();
            WebResource webResource = client
                    .resource(url);
            ClientResponse response = webResource.type("application/json")
                    .post(ClientResponse.class, "");
            this.setResponseCode(response.getStatus() + "");
            this.setResponseBody(response.getEntity(String.class));
            this.setResponseHeaders(response.getHeaders());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String firePostHTTP(String authHeader) {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", authHeader);
            connection.setRequestProperty("max-html-redirects", "0");
            OutputStream os = connection.getOutputStream();
            os.write(this.payLoad.getBytes());
            os.close();
            return connection.getResponseCode() + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "500";
    }
}
