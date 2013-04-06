package Stocks;

/**
 *
 * @author treybriggs
 */
public class InvalidTickerException extends Exception {
    /**
     *
     */
    public InvalidTickerException() {
        super();
    }
    /**
     *
     * @param s
     */
    public InvalidTickerException(String s) {
        super(s);
    }
}
