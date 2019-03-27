package pl.ark.chr.buginator.app.exceptions;

/**
 * Created by Arek on 2016-09-29.
 */
public class UsernameNotFoundException extends RuntimeException {

    public UsernameNotFoundException(String s) {
        super(s);
    }

    public UsernameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
