package Stocks;

/**
 *
 * @author treybriggs
 */
public class NotEnoughCashException extends RuntimeException {
    /**
     *
     */
    public NotEnoughCashException() {
        super();
    }
    /**
     *
     * @param s
     */
    public NotEnoughCashException(String s) {
        super(s);
    }
}
