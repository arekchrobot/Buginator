package pl.ark.chr.buginator.client.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Arek on 2017-04-17.
 */
public class HttpSenderImpl implements HttpSender {

    private static final Logger logger = LoggerFactory.getLogger(HttpSenderImpl.class);

    protected final static String DEFAULT_ENDPOINT_SUFFIX = "/ext/notify/";
//    protected final static String DEFAULT_ENDPOINT_PREFIX = "http://api.buginator.com";
    protected final static String DEFAULT_ENDPOINT_PREFIX = "https://localhost:8443";
    protected final static int DEFAULT_TIMEOUT = 5000;

    protected String endpoint = DEFAULT_ENDPOINT_PREFIX + DEFAULT_ENDPOINT_SUFFIX;
    protected int timeout = DEFAULT_TIMEOUT;

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
                } catch(IOException e) {
                    //Not needed, just in case not to break result of response
                }
            }

            int status = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();

            if(status != 200) {
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
}
