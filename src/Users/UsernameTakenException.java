package Users;

import Stocks.*;

/**
 *
 * @author treybriggs
 */
public class UsernameTakenException extends Exception {
    /**
     *
     */
    public UsernameTakenException() {
        super();
    }
    /**
     *
     * @param s
     */
    public UsernameTakenException(String s) {
        super(s);
    }
}
