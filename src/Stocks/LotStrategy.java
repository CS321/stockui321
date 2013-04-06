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
 * LotView StructuredTable Format.
 * 
 * Column 0) String Ticker.
 * Column 1) Date buyDate.
 * Column 2) int numShares.
 * Column 3) double costBasis.
 * Column 4) double unrealizedGains.
 * Column 5) double realizedGains.
 * Column 6) intsharesSold.
 * Column 7) double percentGains
 * 
 * @author treybriggs
 */
public class LotStrategy implements Strategy, Serializable {

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

            } else if ((TraderAccount.transactionType) srcRow.get(0) == TraderAccount.transactionType.SELL) {
                String tickerSymbol = (String) srcRow.get(1);
                int numShares = (Integer) srcRow.get(3);
                while (numShares > 0) {
                    int index = t.search(tickerSymbol, 0);
                    int sharesInLot = (Integer) t.getEntry(index, 2);
                    if (numShares < sharesInLot) {
                        double sellPrice = (Double) srcRow.get(4);
                        double buyPrice = (Double) t.getEntry(index, 3);
                        double realizedGains = (Double) t.getEntry(index, 5);
                        realizedGains += (numShares * sellPrice) - (numShares * buyPrice);
                        int sharesSold = (Integer) t.getEntry(index, 6);
                        sharesSold += numShares;

                        t.setEntry(index, 2, sharesInLot - numShares);
                        t.setEntry(index, 5, realizedGains);
                        t.setEntry(index, 6, sharesSold);

                        numShares = 0;
                    } else {
                        numShares -= sharesInLot;
                        t.deleteRow(index);
                    }
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
