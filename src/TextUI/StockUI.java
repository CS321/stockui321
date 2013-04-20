/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TextUI;

import Stocks.InvalidTickerException;
import Stocks.Market;
import Stocks.NotEnoughCashException;
import Stocks.NotEnoughSharesException;
import Stocks.TraderAccount;
import Stocks.TransactionFailedException;
import Structure.EmptyTableException;
import Structure.NotComparableException;
import Structure.StructuredTable;
import Structure.Table;
import System.Database;
import System.DatabaseReadException;
import Users.Accounts;
import Users.BadUserOrPasswordException;
import Users.MD5;
import Users.UsernameTakenException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author treybriggs
 */
public class StockUI {

    Market m = Market.getInstance();
    Accounts a;

    public StockUI(Accounts a) {
        this.a = a;
    }

    public void initialize() {
        boolean i_sit = true;
        while (i_sit) {
            if (a.isEmpty()) {
                System.out.println("There are no trader accounts.");
                Table<String> options = new Table<String>();
                options.addRow(Arrays.asList("1", "Create a User"));
                options.addRow(Arrays.asList("e", "exit"));
                String selected = Prompt.menuPrompt(options);
                switch (selected.charAt(0)) {
                    case '1':
                        createUser();
                        break;
                    case 'e':
                        break;
                }
            } /*else {
                Table<String> options = new Table<String>();
                options.addRow(Arrays.asList("1", "Login"));
                options.addRow(Arrays.asList("2", "Create a User"));
                options.addRow(Arrays.asList("e", "exit"));
                String selected = Prompt.menuPrompt(options);
                switch (selected.charAt(0)) {
                    case '1':
                        login();
                        break;
                    case '2':
                        createUser();
                        break;
                    case 'e':
                        i_sit = false;
                        break;
                }*/
            //}
        }
    }

    public void createUser() {
        System.out.println("\n");

        String username;
        String password;

        username = Prompt.stringPrompt("Please choose a Username:", new UsernameInputValidator());

        while (a.userExists(username)) {
            username = Prompt.stringPrompt("\nUser Exists.\nPlease choose a Username:", new UsernameInputValidator());
        }

        password = Prompt.stringPrompt("Please choose a Password", new PasswordInputValidator());

        try {
            a.addAccount(username, MD5.getMD5(password), 100000.00);
        } catch (UsernameTakenException ex) {
        }

        System.out.println("\n");

        System.out.println("User creation successful.\n");

    }

    public void login(String user, String pass) {

        System.out.println("\n");

        String username = user;
        String password = pass;
        TraderAccount u = null;

        try {
            u = a.login(username, MD5.getMD5(password));
        } catch (BadUserOrPasswordException ex) {
        }

        while (u == null) {
            System.out.println("Bad Username or Password, try again.");
            username = Prompt.stringPrompt("What is your Username?", new UsernameInputValidator());
            password = Prompt.stringPrompt("What is your Password?", new PasswordInputValidator());
            try {
                u = a.login(username, MD5.getMD5(password));
            } catch (BadUserOrPasswordException ex) {
            }
        }
        mainMenu(u);
    }

    private void mainMenu(TraderAccount u) {
        boolean mm_sit = true;
        while (mm_sit) {
            System.out.println("\n");

            System.out.println("Welcome " + u.getUsername() + "\n");

            Table<String> options = new Table<String>();
            options.addRow(Arrays.asList("1", "Go to the Market"));
            options.addRow(Arrays.asList("2", "See my Portfolio"));
            options.addRow(Arrays.asList("3", "See my Lot Information"));
            options.addRow(Arrays.asList("4", "See my Closed Positions"));
            options.addRow(Arrays.asList("5", "See my Transaction Log"));
            options.addRow(Arrays.asList("e", "Logout"));

            String selected = Prompt.menuPrompt(options);
            switch (selected.charAt(0)) {
                case '1':
                    market(u);
                    break;
                case '2':
                    portfolio(u);
                    break;
                case '3':
                    lots(u);
                    break;
                case '4':
                    closedPositions(u);
                    break;
                case '5':
                    transactions(u);
                    break;
                case 'e':
                    mm_sit = false;
                    break;
            }
        }
    }

    private void market(TraderAccount u) {
        boolean m_sit = true;
        while (m_sit) {
            System.out.println("\n");

            System.out.println("Welcome " + u.getUsername() + "\n");

            Table<String> options = new Table<String>();
            options.addRow(Arrays.asList("1", "Look up Stock"));
            options.addRow(Arrays.asList("2", "Buy a Stock"));
            options.addRow(Arrays.asList("3", "Sell a Stock"));
            options.addRow(Arrays.asList("e", "Return to Main Menu"));

            String selected = Prompt.menuPrompt(options);
            switch (selected.charAt(0)) {
                case '1':
                    marketLookUp(u);
                    break;
                case '2':
                    marketBuy(u);
                    break;
                case '3':
                    marketSell(u);
                    break;
                case 'e':
                    m_sit = false;
                    break;
            }
        }
    }

    private void marketLookUp(TraderAccount u) {

        System.out.println("\n");

        String ticker;

        ticker = Prompt.stringPrompt("Ticker Symbol:", new TickerInputValidator());
        System.out.println("\nTICKER: " + ticker + "\nLAST SELL: " + m.getPrice(ticker)
                + "\nASKING:" + m.getAsk(ticker) + "\nBIDDING: " + m.getBid(ticker));

        Table<String> meh = new Table<String>();
        meh.addRow(Arrays.asList("c", "Continue"));
        Prompt.menuPrompt(meh);

    }

    private void marketBuy(TraderAccount u) {
        System.out.println("\n");
        m.updateMarket();

        String ticker;
        int numShares;
        double bid;

        ticker = Prompt.stringPrompt("Ticker Symbol:", new TickerInputValidator());
        System.out.println("\nTICKER: " + ticker + "\nLAST SELL: " + m.getPrice(ticker)
                + "\nASKING:" + m.getAsk(ticker) + "\nBIDDING: " + m.getBid(ticker));

        numShares = Prompt.integerPrompt("How many shares would you like?", new BuySharesInputValidator());
        bid = Prompt.doublePrompt("What's your bid?", new MoneyInputValidator());

        try {
            u.buy(ticker, numShares, bid);
        } catch (InvalidTickerException e) {
            System.out.println("Ticker does not exist.");
        } catch (TransactionFailedException e) {
            System.out.println("Can't buy for less than Asking Price.");
        } catch (NotEnoughCashException e) {
            System.out.println("You don't have enough money for that.");
        }

        Table<String> meh = new Table<String>();
        meh.addRow(Arrays.asList("c", "Continue"));
        Prompt.menuPrompt(meh);

    }

    private void marketSell(TraderAccount u) {
        System.out.println("\n");
        m.updateMarket();

        String ticker;
        int numShares;
        double ask;

        ticker = Prompt.stringPrompt("Ticker Symbol:", new TickerInputValidator());
        System.out.println("\nTICKER: " + ticker + "\nLAST SELL: " + m.getPrice(ticker)
                + "\nASKING:" + m.getAsk(ticker) + "\nBIDDING: " + m.getBid(ticker));

        numShares = Prompt.integerPrompt("How many shares would you like to sell?", new BuySharesInputValidator());
        ask = Prompt.doublePrompt("How much would you like to sell each share for?", new MoneyInputValidator());

        try {
            u.sell(ticker, numShares, ask);
        } catch (TransactionFailedException e) {
            System.out.println("Can't sell for more than Bidding Price.");
        } catch (NotEnoughSharesException e) {
            System.out.println("You don't own that many shares.");
        }

        Table<String> meh = new Table<String>();
        meh.addRow(Arrays.asList("c", "Continue"));
        Prompt.menuPrompt(meh);

    }

    private void portfolio(TraderAccount u) {
        boolean p_sit = true;
        while (p_sit) {

            System.out.println("\n");
            m.updateMarket();

            StructuredTable p = null;

            try {
                p = u.getPortfolio();
            } catch (EmptyTableException e) {
            }
            double marketBalance = 0;

            if (p != null && !p.isEmpty()) {
                Iterator i = p.IterateRow();
                while (i.hasNext()) {
                    List l = (List) i.next();
                    marketBalance += (Integer) l.get(2) * m.getPrice((String) l.get(0));
                }
            }

            System.out.println(u.getUsername());
            System.out.println("Cash Balance: " + String.format("%.2f", u.getCashBalance()));
            System.out.println("Market Balance: " + String.format("%.2f", marketBalance));
            System.out.println("\n");

            System.out.println("Select an option below to view your portfolio:");
            Table<String> options = new Table<String>();
            options.addRow(Arrays.asList("1", "Sort by Ticker Name"));
            options.addRow(Arrays.asList("2", "Sort by Number Owned"));
            options.addRow(Arrays.asList("3", "Sort by Price Paid"));
            options.addRow(Arrays.asList("4", "Sort by Date"));
            options.addRow(Arrays.asList("5", "Sort by Dollars Gained"));
            options.addRow(Arrays.asList("6", "Sort by Percentage Gained"));

            options.addRow(Arrays.asList("e", "Return to Main Menu"));

            String selected = Prompt.menuPrompt(options);
            Table<String> ascdesc = new Table<String>();
            ascdesc.addRow(Arrays.asList("a", "Ascending"));
            ascdesc.addRow(Arrays.asList("d", "Descending"));
            int col = -1;
            boolean asc;

            switch (selected.charAt(0)) {
                case '1':
                    col = 0;
                    break;
                case '2':
                    col = 2;
                    break;
                case '3':
                    col = 3;
                    break;
                case '4':
                    col = 1;
                    break;
                case '5':
                    col = 4;
                    break;
                case '6':
                    col = 7;
                    break;
                case 'e':
                    p_sit = false;
                    continue;
            }

            asc = Prompt.menuPrompt(ascdesc).equals("a");
            portfolioPrint(p, col, asc);
        }
    }

    private void portfolioPrint(StructuredTable p, int col, boolean ascending) {
        if (p != null && !p.isEmpty()) {
            p.sort(col, ascending);
            Iterator i = p.iterator();

            // Find the maximum length of a string in each column
            int[] lengths = {6, 4, 12, 10, 16, 14, 0, 13};
            int n = 0;
            while (i.hasNext()) {
                int j = 0;
                for (Object elm : (List) i.next()) {
                    lengths[j] = Math.max(elm.toString().length(), lengths[j]);
                    j++;
                }
                n++;
            }
            // "TICKER" "DATE" "SHARES OWNED" "COST BASIS" "UNREALIZED GAINS" "REALIZED GAINS" "PERCENT GAINS"

            // Generate a format string
            String format = " %" + lengths[0] + "s "
                    + " %" + lengths[1] + "s "
                    + " %" + lengths[2] + "d "
                    + " %" + lengths[3] + ".2f "
                    + " %" + lengths[4] + ".2f "
                    + " %" + lengths[5] + ".2f "
                    + " %" + lengths[7] + ".2f\n";

            String titleFormat = " %" + lengths[0] + "s "
                    + " %" + lengths[1] + "s "
                    + " %" + lengths[2] + "s "
                    + " %" + lengths[3] + "s "
                    + " %" + lengths[4] + "s "
                    + " %" + lengths[5] + "s "
                    + " %" + lengths[7] + "s \n";

            i = p.iterator();

            System.out.printf(titleFormat, "TICKER", "DATE", "SHARES OWNED", "COST BASIS", "UNREALIZED GAINS", "REALIZED GAINS", "PERCENT GAINS");

            while (i.hasNext()) {
                List l = (List) i.next();
                System.out.printf(format, l.get(0), l.get(1), l.get(2), l.get(3), l.get(4), l.get(5), l.get(7));
            }

            //System.out.println(p);
        } else {
            System.out.println("You aren't holding any stocks.");
        }

        Table<String> meh = new Table<String>();
        meh.addRow(Arrays.asList("c", "Continue"));
        Prompt.menuPrompt(meh);

    }

    private void lots(TraderAccount u) {
        boolean l_sit = true;
        while (l_sit) {

            System.out.println("\n");
            m.updateMarket();

            StructuredTable p = null;

            try {
                p = u.getLots();
            } catch (EmptyTableException e) {
            }
            double marketBalance = 0;

            if (p != null && !p.isEmpty()) {
                Iterator i = p.IterateRow();
                while (i.hasNext()) {
                    List l = (List) i.next();
                    marketBalance += (Integer) l.get(2) * m.getPrice((String) l.get(0));
                }
            }

            System.out.println(u.getUsername());
            System.out.println("Cash Balance: " + String.format("%.2f", u.getCashBalance()));
            System.out.println("Market Balance: " + String.format("%.2f", marketBalance));
            System.out.println("\n");

            System.out.println("Select an option below to view your lots:");
            Table<String> options = new Table<String>();
            options.addRow(Arrays.asList("1", "Sort by Date"));
            options.addRow(Arrays.asList("2", "Sort by Ticker Name"));
            options.addRow(Arrays.asList("3", "Sort by Number Owned"));
            options.addRow(Arrays.asList("4", "Sort by Price Paid"));
            options.addRow(Arrays.asList("5", "Sort by Dollars Gained"));
            options.addRow(Arrays.asList("6", "Sort by Percentage Gained"));


            options.addRow(Arrays.asList("e", "Return to Main Menu"));

            String selected = Prompt.menuPrompt(options);
            Table<String> ascdesc = new Table<String>();
            ascdesc.addRow(Arrays.asList("a", "Ascending"));
            ascdesc.addRow(Arrays.asList("d", "Descending"));
            int col = -1;
            boolean asc;

            switch (selected.charAt(0)) {
                case '1':
                    col = 1;
                    break;
                case '2':
                    col = 0;
                    break;
                case '3':
                    col = 2;
                    break;
                case '4':
                    col = 3;
                    break;
                case '5':
                    col = 4;
                    break;
                case '6':
                    col = 7;
                    break;
                case 'e':
                    l_sit = false;
                    continue;
            }

            asc = Prompt.menuPrompt(ascdesc).equals("a");
            lotPrint(p, col, asc);
        }
    }

    private void lotPrint(StructuredTable p, int col, boolean ascending) {
        if (p != null && !p.isEmpty()) {
            p.sort(col, ascending);
            Iterator i = p.iterator();

            // Find the maximum length of a string in each column
            int[] lengths = {6, 4, 12, 10, 16, 14, 0, 13};
            int n = 0;
            while (i.hasNext()) {
                int j = 0;
                for (Object elm : (List) i.next()) {
                    lengths[j] = Math.max(elm.toString().length(), lengths[j]);
                    j++;
                }
                n++;
            }
            // "TICKER" "DATE" "SHARES OWNED" "COST BASIS" "UNREALIZED GAINS" "REALIZED GAINS" "PERCENT GAINS"

            // Generate a format string
            String format = " %" + lengths[0] + "s "
                    + " %" + lengths[1] + "s "
                    + " %" + lengths[2] + "d "
                    + " %" + lengths[3] + ".2f "
                    + " %" + lengths[4] + ".2f "
                    + " %" + lengths[5] + ".2f "
                    + " %" + lengths[7] + ".2f\n";

            String titleFormat = " %" + lengths[0] + "s "
                    + " %" + lengths[1] + "s "
                    + " %" + lengths[2] + "s "
                    + " %" + lengths[3] + "s "
                    + " %" + lengths[4] + "s "
                    + " %" + lengths[5] + "s "
                    + " %" + lengths[7] + "s \n";

            i = p.iterator();

            System.out.printf(titleFormat, "TICKER", "DATE", "SHARES OWNED", "COST BASIS", "UNREALIZED GAINS", "REALIZED GAINS", "PERCENT GAINS");

            while (i.hasNext()) {
                List l = (List) i.next();
                System.out.printf(format, l.get(0), l.get(1), l.get(2), l.get(3), l.get(4), l.get(5), l.get(7));
            }

            //System.out.println(p);
        } else {
            System.out.println("You aren't holding any stocks.");
        }

        Table<String> meh = new Table<String>();
        meh.addRow(Arrays.asList("c", "Continue"));
        Prompt.menuPrompt(meh);

    }

    private void closedPositions(TraderAccount u) {
        boolean c_sit = true;
        while (c_sit) {

            System.out.println("\n");

            StructuredTable p = null;

            try {
                p = u.getClosedPositions();
            } catch (EmptyTableException e) {
            }

            System.out.println(u.getUsername());

            System.out.println("Select an option below to view your closed positions:");
            Table<String> options = new Table<String>();
            options.addRow(Arrays.asList("1", "Sort by Buy Date"));
            options.addRow(Arrays.asList("2", "Sort by Closed Date"));
            options.addRow(Arrays.asList("3", "Sort by Ticker Name"));
            options.addRow(Arrays.asList("4", "Sort by Number of Shares"));
            options.addRow(Arrays.asList("5", "Sort by Price Paid"));
            options.addRow(Arrays.asList("6", "Sort by Dollars Gained"));


            options.addRow(Arrays.asList("e", "Return to Main Menu"));

            String selected = Prompt.menuPrompt(options);
            Table<String> ascdesc = new Table<String>();
            ascdesc.addRow(Arrays.asList("a", "Ascending"));
            ascdesc.addRow(Arrays.asList("d", "Descending"));
            int col = -1;
            boolean asc;

            switch (selected.charAt(0)) {
                case '1':
                    col = 1;
                    break;
                case '2':
                    col = 2;
                    break;
                case '3':
                    col = 0;
                    break;
                case '4':
                    col = 3;
                    break;
                case '5':
                    col = 4;
                    break;
                case '6':
                    col = 5;
                    break;
                case 'e':
                    c_sit = false;
                    continue;
            }

            asc = Prompt.menuPrompt(ascdesc).equals("a");
            closedPrint(p, col, asc);
        }
    }

    private void closedPrint(StructuredTable p, int col, boolean ascending) {
        if (p != null && !p.isEmpty()) {
            p.sort(col, ascending);
            Iterator i = p.iterator();

            // Find the maximum length of a string in each column
            int[] lengths = {6, 8, 11, 16, 10, 5};
            int n = 0;
            while (i.hasNext()) {
                int j = 0;
                for (Object elm : (List) i.next()) {
                    lengths[j] = Math.max(elm.toString().length(), lengths[j]);
                    j++;
                }
                n++;
            }
            // "TICKER" "BUY DATE" "CLOSED DATE" "NUMBER OF SHARES" "COST BASIS" "GAINS"

            // Generate a format string
            String format = " %" + lengths[0] + "s "
                    + " %" + lengths[1] + "s "
                    + " %" + lengths[2] + "s "
                    + " %" + lengths[3] + "d "
                    + " %" + lengths[4] + ".2f "
                    + " %" + lengths[5] + ".2f\n";

            String titleFormat = " %" + lengths[0] + "s "
                    + " %" + lengths[1] + "s "
                    + " %" + lengths[2] + "s "
                    + " %" + lengths[3] + "s "
                    + " %" + lengths[4] + "s "
                    + " %" + lengths[5] + "s\n";

            i = p.iterator();

            System.out.printf(titleFormat, "TICKER", "BUY DATE", "CLOSED DATE", "NUMBER OF SHARES", "COST BASIS", "GAINS");

            while (i.hasNext()) {
                List l = (List) i.next();
                System.out.printf(format, l.get(0), l.get(1), l.get(2), l.get(3), l.get(4), l.get(5));
            }

            //System.out.println(p);
        } else {
            System.out.println("You haven't closed any positions.");
        }

        Table<String> meh = new Table<String>();
        meh.addRow(Arrays.asList("c", "Continue"));
        Prompt.menuPrompt(meh);

    }

    private void transactions(TraderAccount u) {
        boolean t_sit = true;
        while (t_sit) {

            System.out.println("\n");

            StructuredTable p = null;

            try {
                p = u.getTransactions();
            } catch (EmptyTableException e) {
            }

            System.out.println(u.getUsername());

            System.out.println("Select an option below to view your transactions:");
            Table<String> options = new Table<String>();
            options.addRow(Arrays.asList("1", "Sort by Ticker Name"));
            options.addRow(Arrays.asList("2", "Sort by Buy Date"));
            options.addRow(Arrays.asList("3", "Sort by Number of Shares"));
            options.addRow(Arrays.asList("4", "Sort by Price Paid"));

            options.addRow(Arrays.asList("e", "Return to Main Menu"));

            String selected = Prompt.menuPrompt(options);
            Table<String> ascdesc = new Table<String>();
            ascdesc.addRow(Arrays.asList("a", "Ascending"));
            ascdesc.addRow(Arrays.asList("d", "Descending"));
            int col = -1;
            boolean asc;

            switch (selected.charAt(0)) {
                case '1':
                    col = 1;
                    break;
                case '2':
                    col = 2;
                    break;
                case '3':
                    col = 3;
                    break;
                case '4':
                    col = 4;
                    break;
                case 'e':
                    t_sit = false;
                    continue;
            }

            asc = Prompt.menuPrompt(ascdesc).equals("a");
            transactionPrint(p, col, asc);
        }
    }

    private void transactionPrint(StructuredTable p, int col, boolean ascending) {
        if (p != null && !p.isEmpty()) {
            p.sort(col, ascending);
            Iterator i = p.iterator();

            // Find the maximum length of a string in each column
            int[] lengths = {4, 6, 4, 16, 10};
            int n = 0;
            while (i.hasNext()) {
                int j = 0;
                for (Object elm : (List) i.next()) {
                    lengths[j] = Math.max(elm.toString().length(), lengths[j]);
                    j++;
                }
                n++;
            }
            // "TYPE" "TICKER" "BUY DATE" "NUMBER OF SHARES" "COST BASIS"

            // Generate a format string
            String format = " %" + lengths[0] + "s "
                    + " %" + lengths[1] + "s "
                    + " %" + lengths[2] + "s "
                    + " %" + lengths[3] + "d "
                    + " %" + lengths[4] + ".2f\n";

            String titleFormat = " %" + lengths[0] + "s "
                    + " %" + lengths[1] + "s "
                    + " %" + lengths[2] + "s "
                    + " %" + lengths[3] + "s "
                    + " %" + lengths[4] + "s\n";

            i = p.iterator();

            System.out.printf(titleFormat, "TYPE", "TICKER", "DATE", "NUMBER OF SHARES", "COST BASIS");

            while (i.hasNext()) {
                List l = (List) i.next();
                System.out.printf(format, l.get(0), l.get(1), l.get(2), l.get(3), l.get(4));
            }

            //System.out.println(p);
        } else {
            System.out.println("You don't have any transactions.");
        }

        Table<String> meh = new Table<String>();
        meh.addRow(Arrays.asList("c", "Continue"));
        Prompt.menuPrompt(meh);

    }
}
