/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TextUI;

import Stocks.Market;

/**
 *
 * @author treybriggs
 */
public class TickerInputValidator implements InputValidator {

    @Override
    public boolean validate(Object input) {
        Market m = Market.getInstance();
        if (m.exists((String) input)) {
            return true;
        }
        return false;
    }
}
