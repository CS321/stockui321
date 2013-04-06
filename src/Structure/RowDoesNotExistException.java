package Structure;

/**
 *
 * @author treybriggs
 */
public class RowDoesNotExistException extends RuntimeException {
    /**
     *
     */
    public RowDoesNotExistException() {
        super();
    }
    /**
     *
     * @param s
     */
    public RowDoesNotExistException(String s) {
        super(s);
    }
}
