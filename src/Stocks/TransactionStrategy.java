/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Stocks;

import Structure.ColumnHasMixedTypesException;
import Structure.StructuredTable;
import Structure.TableHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author treybriggs
 */
public class TransactionStrategy implements Strategy, Serializable {

    /**
     * Generates a table containing completed transactions.
     *
     * @param transactions
     * @return
     */
    @Override
    public <T extends Comparable<? super T>> StructuredTable genTable(StructuredTable transactions) {
        StructuredTable<T> t = null;
        try {
            t = new StructuredTable(transactions.copyTable());
        } catch (ColumnHasMixedTypesException ex) {
            Logger.getLogger(TransactionStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return t;

    }
}
