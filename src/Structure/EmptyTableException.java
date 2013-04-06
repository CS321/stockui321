package Structure;

/**
 *
 * @author treybriggs
 */
public class EmptyTableException extends RuntimeException {
    /**
     *
     */
    public EmptyTableException() {
        super();
    }
    /**
     *
     * @param s
     */
    public EmptyTableException(String s) {
        super(s);
    }
}
