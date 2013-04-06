/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Stocks;

import Structure.StructuredTable;

/**
 *
 * @author treybriggs
 */
public interface Strategy {
    /**
     * Generates a table containing real-time prices and gains.
     * @param <T> 
     * @param transactions 
     * @return 
     */
    <T extends Comparable<? super T>> StructuredTable genTable(StructuredTable transactions);
}
