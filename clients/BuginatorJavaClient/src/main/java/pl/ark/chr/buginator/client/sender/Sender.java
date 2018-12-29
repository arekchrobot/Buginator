package pl.ark.chr.buginator.client.sender;

/**
 * Created by Arek on 2017-04-17.
 */
public interface Sender {

    void send(String jsonObject, String uniqueKey, String token);

    void close();
}
