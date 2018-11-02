/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loadtest;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author NakkaNar
 */
class SSLUtil {

    protected static SSLContext getInsecureSSLContext()
            throws KeyManagementException, NoSuchAlgorithmException {
        final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        final java.security.cert.X509Certificate[] arg0, final String arg1)
                        throws CertificateException {
                    // do nothing and blindly accept the certificate
                }

                public void checkServerTrusted(
                        final java.security.cert.X509Certificate[] arg0, final String arg1)
                        throws CertificateException {
                    // do nothing and blindly accept the server
                }

            }
        };

        final SSLContext sslcontext = SSLContext.getInstance("SSL");
        sslcontext.init(null, trustAllCerts,
                new java.security.SecureRandom());
        return sslcontext;
    }
}
