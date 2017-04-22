package pl.ark.chr.buginator.client.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Arek on 2017-04-17.
 */
public class AsyncHttpSenderImpl implements HttpSender {

    private static final Logger logger = LoggerFactory.getLogger(AsyncHttpSenderImpl.class);
    private static final int SHUTDOWN_TIMEOUT = 5000;

    private HttpSender baseSender = new HttpSenderImpl();

    private ExecutorService executorService = new ThreadPoolExecutor(0, 1, SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    private boolean shuttingDown = false;

    public AsyncHttpSenderImpl() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                AsyncHttpSenderImpl.this.close();
            }
        });
    }

    @Override
    public void send(final String jsonObject, final String uniqueKey, final String token) {
        if(shuttingDown) {
            logger.info("No notification is send. Threads are shutting down");

            return;
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                baseSender.send(jsonObject, uniqueKey, token);
            }
        });
    }

    @Override
    public void close() {
        shuttingDown = true;

        executorService.shutdown();

        try {
            if(!executorService.awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS)) {
                logger.warn("Shutdown of sending threads took to long. Forcing shutdown");

                executorService.shutdownNow();
            }
        } catch(InterruptedException ex) {
            logger.warn("Shutdown of sending threads interrupted. Forcing shutdown");
            executorService.shutdownNow();
        }
    }

    @Override
    public void setEndpoint(String endpoint) {
        baseSender.setEndpoint(endpoint);
    }

    @Override
    public void setTimeout(int timeout) {
        baseSender.setTimeout(timeout);
    }
}
