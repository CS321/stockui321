package System;

import Users.Accounts;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

/**
 *
 * @author treybriggs
 */
public class Database {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     *
     * @param a
     * @throws DatabaseWriteException
     */
    public static void saveAccounts(Accounts a) throws DatabaseWriteException {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("_uAcc.db");
            ObjectOutputStream out =
                    new ObjectOutputStream(fileOut);
            out.writeObject(a);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            throw new DatabaseWriteException(e.toString());
        }
    }

    /**
     *
     * @return
     * @throws DatabaseReadException
     */
    public static Accounts retrieveAccounts() throws DatabaseReadException {
        Accounts a = null;
        try {
            FileInputStream fileIn =
                    new FileInputStream("_uAcc.db");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            a = (Accounts) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception e) {
            throw new DatabaseReadException();
        }
        return a;
    }
}
