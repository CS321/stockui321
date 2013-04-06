package Stocks;

import Structure.StructuredTable;
import Structure.TableHelper;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.util.Iterator;

/**
 *
 * @author treybriggs
 */
public class Market {

    // singleton design pattern
    private static Market singleton = null;
    // NYSE TICKERSYMBOL | LAST TRADE | BID | ASK
    StructuredTable<?> market;

    /**
     * Constructor.
     */
    private Market() {
        market = new StructuredTable();
    }

    /**
     * Singleton Helper
     *
     * @return Singleton Market
     */
    public static Market getInstance() {
        if (singleton == null) {
            singleton = new Market();
        }
        return singleton;
    }

    /**
     *
     * @param ticker
     * @throws InvalidTickerException
     */
    public void getRealTimeData(String ticker) throws InvalidTickerException {
        try {
            URL yahooFinance = new URL(
                    "http://finance.yahoo.com/d/quotes.csv?f=b3b2l1&e=.csv&"
                    + "s=" + ticker);
            URLConnection yc = yahooFinance.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
                String[] s_bidAsk = inputLine.split(",");
                double bid, ask, last;
                bid = Double.parseDouble(s_bidAsk[0]);
                ask = Double.parseDouble(s_bidAsk[1]);
                last = Double.parseDouble(s_bidAsk[2]);

                // Sometime yahoo doesn't have one or the other (after hours?)
                // Just lazily set it to the one we do have
                if (ask == 0.00) {
                    ask = bid;
                } else if (bid == 0.00) {
                    bid = ask;
                }

                if (!market.isEmpty()) {
                    int index = TableHelper.search(market, ticker, 0);
                    if (index != -1) {
                        updatePrice(ticker, last);
                        updateBid(ticker, bid);
                        updateAsk(ticker, ask);
                        return;
                    }
                }
                addTicker(ticker, last, bid, ask);
            } else {
                throw new InvalidTickerException();
            }
        } catch (Exception e) {
            throw new InvalidTickerException();
        }

    }

    /**
     * Add a ticker record.
     *
     * @param tickerSymbol
     * @throws InvalidTickerException
     */
    public void addTicker(String tickerSymbol) throws InvalidTickerException {
        getRealTimeData(tickerSymbol);
    }

    /**
     * Add a ticker record.
     *
     * @param tickerSymbol
     * @param bidPrice
     * @param askPrice
     */
    private void addTicker(String tickerSymbol, double price, double bidPrice, double askPrice) {
        if (market.isEmpty() || TableHelper.search(market, tickerSymbol, 0) == -1) {
            ArrayList row = new ArrayList();
            row.add(tickerSymbol);
            row.add(price);
            row.add(bidPrice);
            row.add(askPrice);
            market.addRow(row);
        }
    }

    /**
     * Delete a ticker record.
     *
     * @param tickerSymbol
     */
    public void deleteTicker(String tickerSymbol) {
        int index = TableHelper.search(market, tickerSymbol, 0);
        if (index != -1) {
            market.deleteRow(index);
        }
    }

    /**
     * Update a ticker record.
     *
     * @param tickerSymbol
     */
    private void updateTicker(String tickerSymbol) {
        try {
            getRealTimeData(tickerSymbol);
        } catch (InvalidTickerException e) {
        }
    }

    /**
     * Update Price for a Ticker.
     *
     * Last bid price
     *
     * @param tickerSymbol
     * @param price
     */
    public void updatePrice(String tickerSymbol, Double price) {
        int index = TableHelper.search(market, tickerSymbol, 0);
        if (index != -1) {
            market.setEntry(index, 1, price);
        }
    }

    /**
     * Update Bid Price for a Ticker.
     *
     * @param tickerSymbol
     * @param bidPrice
     */
    public void updateBid(String tickerSymbol, Double bidPrice) {
        int index = TableHelper.search(market, tickerSymbol, 0);
        if (index != -1) {
            market.setEntry(index, 2, bidPrice);
        }
    }

    /**
     * Update Ask Price for a Ticker.
     *
     * @param tickerSymbol
     * @param askPrice
     */
    public void updateAsk(String tickerSymbol, Double askPrice) {
        int index = TableHelper.search(market, tickerSymbol, 0);
        if (index != -1) {
            market.setEntry(index, 3, askPrice);
        }
    }

    /**
     * Returns the price for a Ticker.
     *
     * @param tickerSymbol
     * @return Last trade price, or -1 if ticker does not exist.
     */
    public double getPrice(String tickerSymbol) {
        int index = TableHelper.search(market, tickerSymbol, 0);
        if (index != -1) {
            return ((Double) market.getEntry(index, 1)).doubleValue();
        }
        try {
            getRealTimeData(tickerSymbol);
        } catch (InvalidTickerException e) {
            return -1;
        }
        index = TableHelper.search(market, tickerSymbol, 0);
        return ((Double) market.getEntry(index, 1)).doubleValue();
    }

    /**
     * Returns the Bid price for a Ticker.
     *
     * @param tickerSymbol
     * @return Bid Price, or -1 if ticker does not exist.
     */
    public double getBid(String tickerSymbol) {
        int index = TableHelper.search(market, tickerSymbol, 0);
        if (index != -1) {
            return ((Double) market.getEntry(index, 2)).doubleValue();
        }
        try {
            getRealTimeData(tickerSymbol);
        } catch (InvalidTickerException e) {
            return -1;
        }
        index = TableHelper.search(market, tickerSymbol, 0);
        return ((Double) market.getEntry(index, 2)).doubleValue();
    }

    /**
     * Returns the Ask price for a Ticker.
     *
     * @param tickerSymbol
     * @return Asking Price, or -1 if ticker does not exist.
     */
    public double getAsk(String tickerSymbol) {
        int index = TableHelper.search(market, tickerSymbol, 0);
        if (index != -1) {
            return ((Double) market.getEntry(index, 3)).doubleValue();
        }
        try {
            getRealTimeData(tickerSymbol);
        } catch (InvalidTickerException e) {
            return -1;
        }
        index = TableHelper.search(market, tickerSymbol, 0);
        return ((Double) market.getEntry(index, 3)).doubleValue();
    }

    /**
     * Update all ticker records.
     */
    public void updateMarket() {
        if (market.isEmpty()) {
            return;
        }
        
        Iterator m = market.IterateColumn(0);
        while (m.hasNext()) {
            try {
                getRealTimeData((String) m.next());
            } catch (InvalidTickerException e) {
            }
        }
    }

    /**
     * Does the ticker symbol exist?
     *
     * If we don't already have it in our Market, look it up.
     *
     * @param tickerSymbol
     * @return
     */
    public boolean exists(String tickerSymbol) {

        if (market.search(tickerSymbol, 0) != -1) {
            return true;
        }

        try {
            getRealTimeData(tickerSymbol);
        } catch (InvalidTickerException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public Iterator iterator() {
        market.sort(0);
        return market.IterateColumn(0);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return market.toString();
    }
}
