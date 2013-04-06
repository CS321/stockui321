/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Stocks;

import Structure.StructuredTable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * PortfolioView StructuredTable Format.
 *
 * Column 0) String Ticker. 
 * Column 1) Date buyDate. 
 * Column 2) int numShares.
 * Column 3) double costBasis. 
 * Column 4) double unrealizedGains. 
 * Column 5) double realizedGains. 
 * Column 6) int sharesSold. 
 * Column 7) double percentGains
 *
 * @author treybriggs
 */
public class PortfolioStrategy implements Strategy, Serializable {

    /**
     * Generates a table containing real-time prices and gains.
     *
     * @param <T>
     * @param transactions
     * @return
     */
    @Override
    public <T extends Comparable<? super T>> StructuredTable genTable(StructuredTable transactions) {
        StructuredTable<T> t = new StructuredTable();

        Iterator stepThrough = transactions.IterateRow();

        while (stepThrough.hasNext()) {
            List<T> srcRow = (ArrayList<T>) stepThrough.next();
            if ((TraderAccount.transactionType) srcRow.get(0) == TraderAccount.transactionType.BUY) {
                int index;

                // Buying, search for already opened position           
                if (t.isEmpty() || (index = t.search(srcRow.get(1), 0)) == -1) {

                    // No opened position, add it
                    List destRow = new ArrayList();
                    String ticker = (String) srcRow.get(1);
                    Date buyDate = (Date) srcRow.get(2);
                    int numShares = (Integer) srcRow.get(3);
                    double costBasis = (Double) srcRow.get(4);
                    double unrealizedGains = 0.00;
                    double realizedGains = 0.00;
                    int sharesSold = 0;
                    double percentGains = 0.00;

                    destRow.add(ticker);
                    destRow.add(buyDate);
                    destRow.add(numShares);
                    destRow.add(costBasis);
                    destRow.add(unrealizedGains);
                    destRow.add(realizedGains);
                    destRow.add(sharesSold);
                    destRow.add(percentGains);

                    t.addRow(destRow);

                } else {

                    // We have an opened position at `index`, update numShares and avgCost
                    int prevShares = (Integer) t.getEntry(index, 2);
                    int newShares = (Integer) srcRow.get(3);
                    int totalShares = (prevShares + newShares);
                    double prevCost = (Double) t.getEntry(index, 3);
                    double newCost = (Double) srcRow.get(4);
                    double avgCost = ((prevCost * prevShares) + (newCost * newShares)) / totalShares;

                    t.setEntry(index, 2, totalShares);
                    t.setEntry(index, 3, avgCost);

                }

            } else if ((TraderAccount.transactionType) srcRow.get(0) == TraderAccount.transactionType.SELL) {
                String tickerSymbol = (String) srcRow.get(1);
                int numShares = (Integer) srcRow.get(3);
                int index = t.search(tickerSymbol, 0);

                // Selling, Find out if we're closing the position
                if (t.getEntry(index, 2).equals(numShares)) {

                    // Closing Position, delete the row, it's not needed here
                    t.deleteRow(index);

                } else {

                    // Just selling a few shares, keep position open
                    // Update Realized gains and numShares
                    int heldShares = (Integer) t.getEntry(index, 2);
                    double sellPrice = (Double) srcRow.get(4);
                    double buyPrice = (Double) t.getEntry(index, 3);
                    double realizedGains = (Double) t.getEntry(index, 5);
                    realizedGains += (numShares * sellPrice) - (numShares * buyPrice);
                    int sharesSold = (Integer) t.getEntry(index, 6);
                    sharesSold += numShares;

                    t.setEntry(index, 2, heldShares - numShares);
                    t.setEntry(index, 5, realizedGains);
                    t.setEntry(index, 6, sharesSold);
                }
            }
        }

        Market m = Market.getInstance();

        for (int i = 0; i < t.numRows(); i++) {
            String tickerSymbol = (String) t.getEntry(i, 0);
            int numShares = (Integer) t.getEntry(i, 2);
            double lastTradePrice = m.getPrice(tickerSymbol);
            double buyPrice = (Double) t.getEntry(i, 3);
            double unrealizedGains = (Double) t.getEntry(i, 4);
            unrealizedGains += (numShares * lastTradePrice) - (numShares * buyPrice);
            double percentGains = ((lastTradePrice / buyPrice ) - 1) * 100;

            t.setEntry(i, 4, unrealizedGains);
            t.setEntry(i, 7, percentGains);

        }

        return t;
    }
}
