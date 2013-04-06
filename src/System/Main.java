package System;

import Stocks.InvalidTickerException;
import Stocks.Market;
import Stocks.TraderAccount;
import TextUI.StockUI;
import Users.Accounts;
import Users.BadUserOrPasswordException;
import Users.UsernameTakenException;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author treybriggs
 */
public class Main {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) {
        try {
            LogHandler.setup();
        } catch (IOException ex) {
        }

        Accounts a;
        try {
            a = Database.retrieveAccounts();
        } catch (DatabaseReadException e) {
            a = new Accounts();
        }

        StockUI userInterface = new StockUI(a);
        userInterface.initialize();
        
        try {
            Database.saveAccounts(a);
        } catch (DatabaseWriteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
