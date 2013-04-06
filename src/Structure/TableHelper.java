/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Structure;

import java.util.List;

/**
 *
 * @author treybriggs
 */
public class TableHelper {

    private TableHelper() {
        // buzz off
    }

    /**
     *
     * @param src
     * @param dest
     */
    public static void copy(Table src, Table dest) {
        try {
            dest.seedTable(src.copyTable());
        } catch (Exception e) {
        }
    }

    /**
     *
     * @param src
     * @param dest
     * @param rowNum
     */
    public static void copyRow(Table src, Table dest, int rowNum) {
        dest.addRow(src.getRow(rowNum));
    }

    /**
     *
     * @param src
     * @param dest
     * @param colNum
     */
    public static void copyColumn(Table src, Table dest, int colNum) {
        dest.addColumn(src.getColumn(colNum));
    }

    /**
     *
     * @param t
     * @param row
     * @param col
     * @return
     */
    public static Object cut(Table t, int row, int col) {
        Object r = t.getEntry(row, col);
        t.deleteEntry(row, col);
        return r;
    }

    /**
     *
     * @param t
     * @param row
     * @return
     */
    public static List<Object> cutRow(Table t, int row) {
        List<Object> r = t.getRow(row);
        t.deleteRow(row);
        return r;
    }

    /**
     *
     * @param t
     * @param col
     * @return
     */
    public static List<Object> cutColumn(Table t, int col) {
        List<Object> r = t.getColumn(col);
        t.deleteColumn(col);
        return r;
    }

    /**
     *
     * @param t
     * @param col
     * @throws NotComparableException
     */
    public static void sort(Table t, int col) throws NotComparableException {
        t.sort(col);
    }

    /**
     *
     * @param t
     * @param searchFor
     * @param col
     * @return
     */
    public static int search(Table t, Comparable searchFor, int col) {
        return t.search(searchFor, col);
    }
}
