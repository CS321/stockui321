package Users;

import Stocks.*;

/**
 *
 * @author treybriggs
 */
public class UnknownUserException extends RuntimeException {
    /**
     *
     */
    public UnknownUserException() {
        super();
    }
    /**
     *
     * @param s
     */
    public UnknownUserException(String s) {
        super(s);
    }
}
