package Stocks;

/**
 *
 * @author treybriggs
 */
public class NotEnoughSharesException extends RuntimeException {
    /**
     *
     */
    public NotEnoughSharesException() {
        super();
    }
    /**
     *
     * @param s
     */
    public NotEnoughSharesException(String s) {
        super(s);
    }
}
