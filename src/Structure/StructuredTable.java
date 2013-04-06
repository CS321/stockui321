package Structure;

import java.util.*;

/**
 * A Table which requires each Column to contain the same Type of data.
 * 
 * @param <S> 
 * @author Trey Briggs (A25000839)
 */
public class StructuredTable<S extends Comparable<? super S>> extends Table<S> {

    /**
     * Default constructor for Table class. Creates a new empty table ready for
     * your bidding.
     */
    public StructuredTable() {
        theTable = new ArrayList<List<S>>();
    }

    /**
     * Parameterized constructor for Table class. Input a 2d ArrayList of
     * objects and I'll build a Table object for you.
     *
     * The input Object must be non-ragged.
     *
     * @param inputTable 2D array list of objects.
     * @throws ColumnHasMixedTypesException  
     */
    public StructuredTable(List<List<S>> inputTable) throws ColumnHasMixedTypesException {

        // Structured Table?
        for (int i = 0; i < inputTable.get(0).size(); i++) {
            List<S> c = getColumn(inputTable, i);
            if (!isSameType(c.get(0), c)) {
                throw new ColumnHasMixedTypesException("A column in the input table contain mixed types");
            }
        }

        theTable = new ArrayList<List<S>>();
        theTable = copyTable(inputTable);
    }

    /**
     * Empty and recreate table with given records.
     *
     * @param inputTable
     * @throws ColumnHasMixedTypesException  
     */
    @Override
    public void seedTable(List<List<S>> inputTable) throws ColumnHasMixedTypesException {

        // Structured Table?
        for (int i = 0; i < inputTable.get(0).size(); i++) {
            List<S> c = getColumn(inputTable, i);
            if (!isSameType(c.get(0), c)) {
                throw new ColumnHasMixedTypesException("A column in the input table contain mixed types");
            }
        }

        theTable.clear();
        theTable = copyTable(inputTable);
        System.out.println();
    }

    /**
     * Adds a row to the bottom of the table. Must be the same size as other
     * rows.
     *
     * @param row A list containing each object in the new row.
     */
    @Override
    public void addRow(List<S> row) {
        ArrayList<S> tmpRow = new ArrayList();

        for (S item : row) {
            tmpRow.add(item);
        }

        if (!theTable.isEmpty()) {
            if (tmpRow.size() != theTable.get(0).size()) {
                throw new WrongSizeException("Table has " + theTable.get(0).size()
                        + " columns, input row had " + tmpRow.size() + "columns");
            }


            //Structured Table?
            for (int i = 0; i < tmpRow.size(); i++) {
                if (!isSameType(tmpRow.get(i), getColumn(i))) {
                    throw new IncorrectDataException("An entry in the input row is a different type than the column");
                }
            }
        }

        theTable.add(tmpRow);
    }

    /**
     * Adds a column to the end of the table. Must be the same size as other
     * columns.
     *
     * @param column A list containing each object in the new column.
     */
    @Override
    public void addColumn(List<S> column) {
        ArrayList<S> tmpCol = new ArrayList();

        for (S item : column) {
            tmpCol.add(item);
        }

        if (theTable.isEmpty()) {
            for (int i = 0; i < tmpCol.size(); i++) {
                theTable.add(new ArrayList<S>(0));
            }
        } else {
            if (tmpCol.size() != theTable.size()) {
                throw new WrongSizeException("Table has " + theTable.size()
                        + " rows, input column had " + tmpCol.size() + "rows");
            }


            //Structured Table?
            if (!isSameType(tmpCol.get(0), tmpCol)) {
                throw new IncorrectDataException("An entry in the input row is a different type than the column");
            }
        }

        for (int i = 0; i < tmpCol.size(); i++) {
            theTable.get(i).add(tmpCol.get(i));
        }
    }

    /**
     * Adds an object to the specified record.
     *
     * @param row The row index of the item to be added, starts at 0.
     * @param col The column index of the item to be added, starts at 0.
     * @param item The item itself.
     */
    @Override
    public void setEntry(int row, int col, Comparable item) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        //Structured Table?
        if (!isSameType(item, getColumn(col))) {
            throw new IncorrectDataException("The entry is a different type than the column");
        }

        try {
            theTable.get(row).set(col, (S)item);
        } catch (NullPointerException e) {
            throw new RecordDoesNotExistException();
        } catch (IndexOutOfBoundsException e) {
            throw new RecordDoesNotExistException();
        }
    }

    private boolean isSameType(Comparable item, List<S> column) {
        for (S colEntry : column) {
            if (!colEntry.getClass().equals(item.getClass())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Sort Table by the specified Column.
     *
     * The Column must exist and it must make sense to sort it's entries.
     *
     * @param col which column to sort by?
     * @throws NotComparableException
     */
    @Override
    public void sort(int col) {

        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for column
        try {
            theTable.get(0).get(col);
        } catch (IndexOutOfBoundsException e) {
            throw new ColumnDoesNotExistException();
        }

        ColumnComparator compare = new ColumnComparator(col);
        Collections.sort(theTable, compare);
    }

    /**
     * Sort Table by the specified Column.
     *
     * The Column must exist and it must make sense to sort it's entries.
     *
     * @param col which column to sort by?
     * @throws NotComparableException
     */
    @Override
    public void sort(int col, boolean ascending) {

        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for column
        try {
            theTable.get(0).get(col);
        } catch (IndexOutOfBoundsException e) {
            throw new ColumnDoesNotExistException();
        }
        
        Comparator compare;
        if (ascending) {
            compare = new ColumnComparator(col);
        } else {
            compare = new ReverseColumnComparator(col);

        }
        Collections.sort(theTable, compare);
    }
}
