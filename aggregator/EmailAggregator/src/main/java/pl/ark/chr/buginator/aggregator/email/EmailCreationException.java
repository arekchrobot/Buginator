package pl.ark.chr.buginator.aggregator.email;

class EmailCreationException extends RuntimeException {

    EmailCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
