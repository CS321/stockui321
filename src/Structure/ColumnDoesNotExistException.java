package Structure;

/**
 *
 * @author treybriggs
 */
public class ColumnDoesNotExistException extends RuntimeException {
    /**
     *
     */
    public ColumnDoesNotExistException() {
        super();
    }
    /**
     *
     * @param s
     */
    public ColumnDoesNotExistException(String s) {
        super(s);
    }
}