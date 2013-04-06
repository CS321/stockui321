package Stocks;

import Structure.StructuredTable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author treybriggs
 */
public class TraderAccount implements Serializable {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Double cashBalance = 0.0;
    private String username = null;
    private String password = null;
    private Strategy view = null;
    // transactionType action | String ticker | Date date | int numShares | double costBasis
    private StructuredTable<?> transactions;

    /**
     *
     */
    public enum transactionType {
        BUY,
        SELL;
    }

    /**
     *
     */
    public TraderAccount() {
        transactions = new StructuredTable();
        view = new PortfolioStrategy();
    }

    /**
     *
     * @param username
     */
    public TraderAccount(String username) {
        this.username = username;
        transactions = new StructuredTable();
        view = new PortfolioStrategy();
    }

    /**
     * Deposit cash into a 0% money market.
     *
     * @param cash
     */
    public void depositCash(double cash) {
        cashBalance += cash;
    }

    /**
     * Buy a stock.
     *
     * @param tickerSymbol
     * @param numShares
     * @param offer
     * @throws InvalidTickerException
     */
    public void buy(String tickerSymbol, int numShares, double offer) throws InvalidTickerException {

        Market currentMarket = Market.getInstance();


        if (currentMarket.exists(tickerSymbol)) {
            double ask = currentMarket.getAsk(tickerSymbol);
            if (offer >= ask) {
                if (cashBalance >= offer * numShares) {
                    ArrayList row = new ArrayList();
                    row.add(transactionType.BUY);
                    row.add(tickerSymbol);
                    row.add(new Date());
                    row.add(numShares);
                    row.add(offer);

                    transactions.addRow(row);
                    cashBalance -= offer * numShares;

                    LOGGER.log(Level.INFO, "Transaction SUCCESS: {0} has bought {1} of {2} for ${3} per share.",
                            new Object[]{username, numShares, tickerSymbol, String.format("%.2f", offer)});
                } else {
                    throw new NotEnoughCashException();
                }
            } else {
                LOGGER.log(Level.INFO, "Transaction FAILED: {0} has attempted to buy {1} of {2} for ${3} per share, ask price is {4}.",
                        new Object[]{username, numShares, tickerSymbol, String.format("%.2f", offer), String.format("%.2f", ask)});
                throw new TransactionFailedException("Offered less than asking price");
            }
        } else {
            throw new InvalidTickerException();
        }
    }

    /**
     * Sell shares.
     *
     * @param tickerSymbol
     * @param numShares
     * @param offer
     */
    public void sell(String tickerSymbol, int numShares, double offer) {

        Market currentMarket = Market.getInstance();

        if (getPortfolio().search(tickerSymbol, 0) != -1) {
            Strategy tmpView = new PortfolioStrategy();
            StructuredTable tmpTable = tmpView.genTable(transactions);
            int totalShares = (Integer) tmpTable.getEntry(tmpTable.search(tickerSymbol, 0), 2);
            if (numShares <= totalShares) {
                double bid = currentMarket.getBid(tickerSymbol);
                if (offer <= bid) {
                    ArrayList row = new ArrayList();
                    row.add(transactionType.SELL);
                    row.add(tickerSymbol);
                    row.add(new Date());
                    row.add(numShares);
                    row.add(offer);
                    transactions.addRow(row);
                    cashBalance += numShares * offer;

                    LOGGER.log(Level.INFO, "Transaction SUCCESS: {0} has sold {1} of {2} for ${3} per share.",
                            new Object[]{username, numShares, tickerSymbol, String.format("%.2f", offer)});
                } else {
                    LOGGER.log(Level.INFO, "Transaction FAILED: {0} has attempted to sell {1} of {2} for ${3} per share, bid price is {4}.",
                            new Object[]{username, numShares, tickerSymbol, String.format("%.2f", offer), String.format("%.2f", bid)});
                    throw new TransactionFailedException("Asked for more than bid price");
                }
            } else {
                throw new NotEnoughSharesException("Can't sell more shares than owned");
            }
        } else {
            throw new NotEnoughSharesException("Security not owned, NO SHORTING!");
        }

    }

    /**
     * Set View Strategy.
     *
     * Portfolio or Lot View.
     *
     * @param s
     */
    public void setView(Strategy s) {
        view = s;
    }

    /**
     * Sets the username, it is your responsibility to ensure there are no
     * duplicates.
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password.
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return Cash Balance.
     *
     * @return
     */
    public double getCashBalance() {
        return cashBalance;
    }

    /**
     * Return the Username.
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Verify Password.
     *
     * @param password
     * @return
     */
    public boolean checkPassword(String md5Password) {
        if (password.equals(md5Password)) {
            return true;
        }
        return false;
    }

    /**
     * Return Lots view of holdings.
     *
     * @return
     */
    public StructuredTable getLots() {
        Strategy tmpView = new LotStrategy();
        return tmpView.genTable(transactions);
    }

    /**
     * Return Portfolio view of holdings.
     *
     * @return
     */
    public StructuredTable getPortfolio() {
        Strategy tmpView = new PortfolioStrategy();
        return tmpView.genTable(transactions);
    }

    /**
     * Return Portfolio view of holdings.
     *
     * @return
     */
    public StructuredTable getClosedPositions() {
        Strategy tmpView = new ClosedPositionView();
        return tmpView.genTable(transactions);
    }

    /**
     * Return Portfolio view of holdings.
     *
     * @return
     */
    public StructuredTable getTransactions() {
        Strategy tmpView = new TransactionStrategy();
        return tmpView.genTable(transactions);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        if (!transactions.isEmpty()) {
            return "Cash: $" + String.format("%.2f", cashBalance) + "\n" + view.genTable(transactions);
        } else {
            return "Cash: $" + String.format("%.2f", cashBalance) + "\n" + "No Holdings";
        }
    }
}
