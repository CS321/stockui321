package Stocks;

/**
 *
 * @author treybriggs
 */
public class TransactionFailedException extends RuntimeException {
    /**
     *
     */
    public TransactionFailedException() {
        super();
    }
    /**
     *
     * @param s
     */
    public TransactionFailedException(String s) {
        super(s);
    }
}
