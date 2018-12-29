package pl.ark.chr.buginator.client.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Created by Arek on 2017-04-17.
 */
public class HttpSenderImpl implements HttpSender {

    private static final Logger logger = LoggerFactory.getLogger(HttpSenderImpl.class);

    protected final static String DEFAULT_ENDPOINT_SUFFIX = "/ext/notify/";
    //    protected final static String DEFAULT_ENDPOINT_PREFIX = "http://api.buginator.com";
    protected final static String DEFAULT_ENDPOINT_PREFIX = "http://127.0.0.1:8080";
    protected final static int DEFAULT_TIMEOUT = 5000;

    protected String endpoint = DEFAULT_ENDPOINT_PREFIX + DEFAULT_ENDPOINT_SUFFIX;
    protected int timeout = DEFAULT_TIMEOUT;
    protected boolean trustAllSsl = false;

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String jsonObject, String uniqueKey, String token) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(endpoint + uniqueKey + "/" + token);

            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(timeout);
            connection.addRequestProperty("Content-Type", "application/json");

            OutputStream outputStream = null;

            try {
                outputStream = connection.getOutputStream();
                outputStream.write(jsonObject.getBytes());
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    //Not needed, just in case not to break result of response
                }
            }

            int status = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();

            if (status != 200) {
                logger.warn("Error not send to Buginator. Response status: {}", status);
            } else if (responseMessage == null || responseMessage.equals("ERROR")) {
                logger.warn("Error not send to Buginator. Response message: {}", responseMessage);
            }

        } catch (IOException e) {
            logger.warn("Error not send to Buginator. Exception: {}", e);
        } finally {
            connection.disconnect();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void setTrustAllSsl(boolean trustAllSsl) {
        this.trustAllSsl = trustAllSsl;
        if(trustAllSsl) {
            disableSslVerification();
        }
    }
}
