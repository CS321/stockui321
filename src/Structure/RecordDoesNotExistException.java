package Structure;

/**
 *
 * @author treybriggs
 */
public class RecordDoesNotExistException extends RuntimeException {
    /**
     *
     */
    public RecordDoesNotExistException() {
        super();
    }
    /**
     *
     * @param s
     */
    public RecordDoesNotExistException(String s) {
        super(s);
    }
}
