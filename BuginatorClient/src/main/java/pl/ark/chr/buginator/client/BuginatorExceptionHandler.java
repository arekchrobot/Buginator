package pl.ark.chr.buginator.client;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Created by Arek on 2017-04-17.
 */
class BuginatorExceptionHandler implements UncaughtExceptionHandler {
    private final UncaughtExceptionHandler originalHandler;
    private final BuginatorClient client;

    BuginatorExceptionHandler(UncaughtExceptionHandler originalHandler, BuginatorClient client) {
        this.originalHandler = originalHandler;
        this.client = client;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        client.sendNotification(exception);

        if(originalHandler != null) {
            originalHandler.uncaughtException(thread, exception);
        } else {
            System.err.printf("Exception in thread \"%s\" ", thread.getName());
            exception.printStackTrace(System.err);
        }
    }

    static void enableHandler(BuginatorClient client) {
        UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        BuginatorExceptionHandler buginatorHandler;

        if(!(defaultHandler instanceof BuginatorExceptionHandler)) {
            buginatorHandler = new BuginatorExceptionHandler(defaultHandler, client);

            Thread.setDefaultUncaughtExceptionHandler(buginatorHandler);
        }
    }

    static void disableHandler() {
        UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();

        if(currentHandler instanceof BuginatorExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(((BuginatorExceptionHandler) currentHandler).originalHandler);
        }
    }
}
