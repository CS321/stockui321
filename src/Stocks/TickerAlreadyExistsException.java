/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Stocks;

/**
 *
 * @author treybriggs
 */
class TickerAlreadyExistsException extends RuntimeException {
    public TickerAlreadyExistsException() {
        super();
    }
    public TickerAlreadyExistsException(String s) {
        super(s);
    }
}