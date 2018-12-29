package pl.ark.chr.buginator.client.sender;

/**
 * Created by Arek on 2017-04-17.
 */
public interface HttpSender extends Sender {

    void setEndpoint(String endpoint);

    void setTimeout(int timeout);

    void setTrustAllSsl(boolean trustAllSsl);
}
