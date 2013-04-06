package Stocks;

/**
 *
 * @author treybriggs
 */
public class Trader {

    private Trader() {
        // buzz off
    }

    /**
     *
     * @param account
     * @param tickerSymbol
     * @param numShares
     * @param offer
     * @throws InvalidTickerException
     */
    public static void buy(TraderAccount account, String tickerSymbol, int numShares, double offer) throws InvalidTickerException {
        account.buy(tickerSymbol, numShares, offer);
    }

    /**
     *
     * @param account
     * @param tickerSymbol
     * @param numShares
     * @param offer
     */
    public static void sell(TraderAccount account, String tickerSymbol, int numShares, double offer) {
        account.sell(tickerSymbol, numShares, offer);
    }
}
