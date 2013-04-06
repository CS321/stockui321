package Users;

import Stocks.*;

/**
 *
 * @author treybriggs
 */
public class BadUserOrPasswordException extends Exception {
    /**
     *
     */
    public BadUserOrPasswordException() {
        super();
    }
    /**
     *
     * @param s
     */
    public BadUserOrPasswordException(String s) {
        super(s);
    }
}
