package Users;

import Stocks.TraderAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author treybriggs
 */
public class Accounts implements Serializable, Iterable<String> {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private List<TraderAccount> accounts;

    /**
     *
     */
    public Accounts() {
        accounts = new ArrayList<TraderAccount>();
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return accounts.isEmpty();
    }

    /**
     *
     * @param username
     * @param md5Password
     * @throws UsernameTakenException
     */
    public void addAccount(String username, String md5Password) throws UsernameTakenException {

        UserIterator checkUsername = (UserIterator) iterator();
        while (checkUsername.hasNext()) {
            if (checkUsername.next().equals(username)) {
                throw new UsernameTakenException();
            }
        }
        UserIterator findUser = (UserIterator) iterator();
        int userIndex = -1;
        accounts.add(new TraderAccount(username));

        while (findUser.hasNext()) {
            if (findUser.next().equals(username)) {
                userIndex = findUser.getIndex();
                break;
            }
        }

        if (userIndex == -1) {
            throw new UnknownUserException();
        }

        accounts.get(userIndex).setPassword(md5Password);
    }

    /**
     *
     * @param username
     * @param md5Password
     * @throws UsernameTakenException
     */
    public void addAccount(String username, String md5Password, double cashBalance) throws UsernameTakenException {

        UserIterator checkUsername = (UserIterator) iterator();
        while (checkUsername.hasNext()) {
            if (checkUsername.next().equals(username)) {
                throw new UsernameTakenException();
            }
        }
        UserIterator findUser = (UserIterator) iterator();
        int userIndex = -1;
        accounts.add(new TraderAccount(username));

        while (findUser.hasNext()) {
            if (findUser.next().equals(username)) {
                userIndex = findUser.getIndex();
                break;
            }
        }

        if (userIndex == -1) {
            throw new UnknownUserException();
        }

        accounts.get(userIndex).setPassword(md5Password);
        accounts.get(userIndex).depositCash(cashBalance);

    }

    /**
     *
     * @param username
     */
    public void removeAccount(String username) {
        UserIterator checkUsername = (UserIterator) iterator();
        while (checkUsername.hasNext()) {
            if (checkUsername.next().equals(username)) {
                checkUsername.remove();
            }
        }
    }

    /**
     *
     * @param username
     * @param md5Password
     * @return
     * @throws BadUserOrPasswordException
     */
    public TraderAccount login(String username, String md5Password) throws BadUserOrPasswordException {
        UserIterator findUser = (UserIterator) iterator();
        int userIndex = -1;
        while (findUser.hasNext()) {
            if (findUser.next().equals(username)) {
                userIndex = findUser.getIndex();

                if (accounts.get(userIndex).checkPassword(md5Password)) {
                    LOGGER.log(Level.INFO, "{0} has logged in.", username);
                    return accounts.get(userIndex);
                }
            }
        }
        throw new BadUserOrPasswordException();
    }

    /**
     * Check if a user exists.
     *
     * @param username
     * @return
     */
    public boolean userExists(String username) {
        for (TraderAccount a : accounts) {
            if (a.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private static class UserIterator implements Iterator {

        List<TraderAccount> accounts;
        int index = 0;

        public UserIterator(List<TraderAccount> accounts) {
            this.accounts = accounts;
        }

        @Override
        public boolean hasNext() {
            try {
                accounts.get(index);
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
            return true;
        }

        /**
         *
         * @return Username at current position.
         */
        @Override
        public String next() {
            String ret = accounts.get(index).getUsername();
            index++;
            return ret;
        }

        /**
         * Removes the *previous* account.
         */
        @Override
        public void remove() {
            accounts.remove(index - 1);
            index--;
        }

        /**
         * Get the previous index.
         *
         * @return
         */
        public int getIndex() {
            return index - 1;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Iterator iterator() {
        return new UserIterator(accounts);
    }
}
