package Structure;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @param <T>
 * @author Trey Briggs (A25000839)
 */
public class Table<T extends Comparable<? super T>> implements Serializable, Iterable<List<T>> {

    /**
     *
     */
    protected List<List<T>> theTable;

    /**
     * Parameterized constructor for Table class. Creates a new empty table
     * ready for your bidding.
     */
    public Table() {
        theTable = new ArrayList<List<T>>();
    }

    /**
     * Parameterized constructor for Table class. Input a 2d ArrayList of
     * objects and I'll build a Table object for you.
     *
     * The input Object must be non-ragged.
     *
     * @param inputTable 2D array list of objects.
     */
    public Table(List<List<T>> inputTable) {
        theTable = new ArrayList<List<T>>();
        theTable = copyTable(inputTable);
    }

    /**
     * Empty the Table.
     */
    public void clear() {
        theTable.clear();
    }

    /**
     * Is the Table empty?
     *
     * @return
     */
    public boolean isEmpty() {
        return theTable.isEmpty();
    }

    /**
     * Empty and recreate table with given records.
     *
     * @param inputTable
     * @throws InputProblemException
     */
    public void seedTable(List<List<T>> inputTable) throws InputProblemException {
        theTable.clear();
        theTable = copyTable(inputTable);
    }

    /**
     * Adds a row to the bottom of the table. Must be the same size as other
     * rows.
     *
     * @param row A list containing each object in the new row.
     */
    public void addRow(List<T> row) {
        ArrayList<T> tmpRow = new ArrayList();

        for (T item : row) {
            tmpRow.add(item);
        }

        if (!theTable.isEmpty()) {
            if (tmpRow.size() != theTable.get(0).size()) {
                throw new WrongSizeException("Table has " + theTable.get(0).size()
                        + " columns, input row had " + tmpRow.size() + "columns");
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
    public void addColumn(List<T> column) {
        ArrayList<T> tmpCol = new ArrayList();

        for (T item : column) {
            tmpCol.add(item);
        }

        if (theTable.isEmpty()) {
            for (int i = 0; i < tmpCol.size(); i++) {
                theTable.add(new ArrayList<T>(0));
            }
        } else {
            if (tmpCol.size() != theTable.size()) {
                throw new WrongSizeException("Table has " + theTable.size()
                        + " rows, input column had " + tmpCol.size() + "rows");
            }
        }

        for (int i = 0; i < tmpCol.size(); i++) {
            theTable.get(i).add(tmpCol.get(i));
        }
    }

    /**
     * Deletes the row at the specified index. The row must exist.
     *
     * @param rowIndex The index of the row you wish to delete, starts at 0;
     */
    public void deleteRow(int rowIndex) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        try {
            theTable.remove(rowIndex);
        } catch (NullPointerException e) {
            throw new RowDoesNotExistException();
        }
    }

    /**
     * Deletes the column at the specified index. The column must exist.
     *
     * @param columnIndex The index of the column you wish to delete, starts at
     * 0;
     */
    public void deleteColumn(int columnIndex) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for column
        try {
            theTable.get(0).get(columnIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new ColumnDoesNotExistException();
        }

        for (int i = 0; i < theTable.size(); i++) {
            theTable.get(i).remove(columnIndex);
        }
    }

    /**
     * Deletes the record at the specified index. The entry must exist.
     *
     * @param row The row index of the item to be added, starts at 0.
     * @param col The column index of the item to be added, starts at 0.
     */
    public void deleteEntry(int row, int col) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for entry
        try {
            theTable.get(row).set(col, null);
        } catch (IndexOutOfBoundsException e) {
            throw new RecordDoesNotExistException();
        }

    }

    /**
     * Adds an object to the specified record.
     *
     * @param row The row index of the item to be added, starts at 0.
     * @param col The column index of the item to be added, starts at 0.
     * @param item The item itself.
     */
    public void setEntry(int row, int col, Comparable item) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        try {
            theTable.get(row).set(col, (T) item);
        } catch (NullPointerException e) {
            throw new RecordDoesNotExistException();
        } catch (IndexOutOfBoundsException e) {
            throw new RecordDoesNotExistException();
        }
    }

    /**
     * Sums a column of Numbers. 1) If the column contains only non-atomic
     * Number objects, returns the sum.
     *
     * 2) If the column contains any other Object besides non-atomic Number
     * Objects, the column is printed delimited by spaces.
     *
     * <b>NOTE THAT THE RETURN TYPE WILL BE THAT OF THE WIDEST NUMBER TYPE IN
     * THE COLUMN (case 1), OR A STRING (case 2).</b>
     *
     * @param colNum Column index you wish to sum, starts at 0.
     * @return
     */
    public Object sumColumn(int colNum) {

        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for column
        try {
            theTable.get(0).get(colNum);
        } catch (IndexOutOfBoundsException e) {
            throw new ColumnDoesNotExistException();
        }

        // THERE HAS TO BE A BETTER WAY
        // WHY CAN'T JAVA.LANG.NUMBER JUST ADD?!

        Number sum = 0;
        int type = 0; // 0-byte, 1-short, 2-int, 3-long, 4-float, 5-double

        // Find "widest" type in column, or short circuit method on non-"primitive".
        for (int i = 0; i < theTable.size(); i++) {
            if (theTable.get(i).get(colNum) instanceof Byte) {
                // Nothing to do
            } else if (theTable.get(i).get(colNum) instanceof Short) {
                if (type < 1) {
                    type = 1;
                }
            } else if (theTable.get(i).get(colNum) instanceof Integer) {
                if (type < 2) {
                    type = 2;
                }
            } else if (theTable.get(i).get(colNum) instanceof Long) {
                if (type < 3) {
                    type = 3;
                }
            } else if (theTable.get(i).get(colNum) instanceof Float) {
                if (type < 4) {
                    type = 4;
                }
            } else if (theTable.get(i).get(colNum) instanceof Double) {
                if (type < 5) {
                    type = 5;
                }
            } else {
                String retStr = new String();
                for (int n = 0; n < theTable.size(); n++) {
                    retStr = retStr + String.format("%s ", theTable.get(n).get(colNum) == null
                            ? "null" : theTable.get(n).get(colNum));
                }

                return retStr;
            }
        }

        // Find sum of numbers, cast to widest type.
        for (int i = 0; i < theTable.size(); i++) {
            switch (type) {
                case 0:
                    sum = sum.byteValue() + ((Number) theTable.get(i).get(colNum)).byteValue();
                    break;
                case 1:
                    sum = sum.shortValue() + ((Number) theTable.get(i).get(colNum)).shortValue();
                    break;
                case 2:
                    sum = sum.intValue() + ((Number) theTable.get(i).get(colNum)).intValue();
                    break;
                case 3:
                    sum = sum.longValue() + ((Number) theTable.get(i).get(colNum)).longValue();
                    break;
                case 4:
                    sum = sum.floatValue() + ((Number) theTable.get(i).get(colNum)).floatValue();
                    break;
                case 5:
                    sum = sum.doubleValue() + ((Number) theTable.get(i).get(colNum)).doubleValue();
                    break;
            }
        }
        return sum;
    }

    /**
     * Possible degrees of rotation. Only these 3 make sense in our case.
     */
    public static enum Degree { // "public static" is implied, but it helps me to specify it.

        /**
         *
         */
        NINETY,
        /**
         *
         */
        ONEEIGHTY,
        /**
         *
         */
        TWOSEVENTY;
    };

    /**
     * Rotates the table by the specified degree. This one does the actual work.
     *
     * @param degree How many times to rotate the table.
     * @return The rotated table.
     */
    private List<List<T>> rotate(Degree degree) {
        List<List<T>> returnTable = new ArrayList<List<T>>();
        List<List<T>> tmpTable = this.copyTable();

        // Use the ordinal of our enum to determine how many 90 degree rotates we need.
        for (int d = degree.ordinal(); d >= 0; d--) {
            returnTable.clear(); // make sure this is empty

            // Create table of nulls with correct (rotated) dimensions.
            // It seems like we shouldn't need to do this but I can't 
            // figure out a way around it.
            for (int i = 0; i < tmpTable.get(0).size(); i++) {
                returnTable.add(new ArrayList<T>());
                for (int j = 0; j < tmpTable.size(); j++) {
                    returnTable.get(i).add(null);
                }
            }

            // Copy first row to last column, etc..
            for (int i = 0; i < tmpTable.size(); i++) {
                for (int j = 0; j < tmpTable.get(0).size(); j++) {
                    returnTable.get(j).set(tmpTable.size() - 1 - i, tmpTable.get(i).get(j));
                }
            }

            // Copy rotated table to tmpTable as the starting point for the next loop
            tmpTable = copyTable(returnTable);
        }

        return returnTable;
    }

    /**
     * Prints a representation of the table that has been rotated by the
     * specified degree.
     *
     * @param d
     */
    public void printRotate(Degree d) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        System.out.print(formatString(rotate(d)));
    }

    /**
     * Rotates the table by the specified degree.
     *
     * @param d
     */
    public void tableRotate(Degree d) {

        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        theTable = rotate(d);
    }

    /**
     * Returns a copy of the tables structure+data
     *
     * @return A copy of the table.
     */
    public List<List<T>> copyTable() {

        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        return copyTable(theTable);
    }

    /**
     * Returns a copy of the tables structure+data
     *
     * @param copyThis The table to copy.
     * @return A copy of the input table.
     */
    protected List<List<T>> copyTable(List<List<T>> copyThis) {
        List<List<T>> tmpTable = new ArrayList<List<T>>();
        for (int i = 0; i < copyThis.size(); i++) {
            tmpTable.add(new ArrayList<T>());
            for (int j = 0; j < copyThis.get(0).size(); j++) {
                tmpTable.get(i).add(copyThis.get(i).get(j));
            }
        }
        return tmpTable;
    }

    /**
     * Returns a formatted, multi-line string containing a pretty representation
     * of the table.
     *
     * @param table The table to generate a string representation of.
     * @return The formatted string representation.
     */
    private String formatString(List<List<T>> table) {

        // Empty Table?
        if (table.isEmpty()) {
            throw new EmptyTableException();
        }

        String retStr = new String();

        // Find how many columns we have,
        // for now it's non-ragged so just pick a row and check.
        int maxColumns = table.get(0).size();

        // Find the maximum length for each column
        int[] colLength = new int[maxColumns];
        for (List<T> row : table) {
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i) != null && row.get(i).toString().length() > colLength[i]) {
                    colLength[i] = row.get(i).toString().length();
                } else {
                    colLength[i] = 4;
                }
            }
        }

        // Build a format string
        String[] format = new String[maxColumns];
        for (int i = 0; i < maxColumns; i++) {
            format[i] = "%1$" + colLength[i] + "s"
                    + (i + 1 == maxColumns ? "\n" : " ");
        }

        // Build the string representation and return it!
        for (int i = 0; i < table.size(); i++) {
            for (int j = 0; j < table.get(i).size(); j++) {
                retStr = retStr + String.format(format[j], table.get(i).get(j) == null
                        ? "null" : table.get(i).get(j));
            }
        }
        return retStr;
    }

    /**
     * Prints a formatted representation of the table to STDOUT.
     */
    public void printTable() {
        System.out.print(this);
    }

    /**
     * Returns a formatted string representation of the table.
     *
     * @return A formatted string representation of the table.
     */
    @Override
    public String toString() {
        return formatString(theTable);
    }

    /**
     * Prints a space-delimited string of each item in the specified row. The
     * row must exist.
     *
     * @param rowNum The row index to print, starts at 0.
     */
    public void printRow(int rowNum) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for row
        try {
            theTable.get(rowNum);
        } catch (IndexOutOfBoundsException e) {
            throw new RowDoesNotExistException();
        }

        for (T item : theTable.get(rowNum)) {
            System.out.printf("%s ", item.toString());
        }
        System.out.print("\n");
    }

    /**
     * Returns an ArrayList containing the records in the specified row. The row
     * must exist.
     *
     * @param rowNum The row index to print, starts at 0.
     * @return
     */
    public List<T> getRow(int rowNum) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for row
        try {
            return theTable.get(rowNum);
        } catch (IndexOutOfBoundsException e) {
            throw new RowDoesNotExistException();
        }

    }

    /**
     * Prints a space-delimited string of each item in the specified column. The
     * column must exist.
     *
     * @param colNum The column index to print, starts at 0.
     */
    public void printColumn(int colNum) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for column
        try {
            theTable.get(0).get(colNum);
        } catch (IndexOutOfBoundsException e) {
            throw new ColumnDoesNotExistException();
        }

        for (int i = 0; i < theTable.size(); i++) {
            System.out.printf("%s ", theTable.get(i).get(colNum).toString());
        }
        System.out.print("\n");
    }

    /**
     * @return The number of rows in the table.
     */
    public int numRows() {
        // Empty Table?
        if (theTable.isEmpty()) {
            return 0;
        }

        return theTable.size();
    }

    /**
     * @return The number of columns in the table.
     */
    public int numColumns() {
        // Empty Table?
        if (theTable.isEmpty() || theTable.get(0).isEmpty()) {
            return 0;
        }

        return theTable.get(0).size();
    }

    /**
     * Returns an ArrayList containing the records in the specified column.
     *
     * The column must exist.
     *
     * @param colNum The column index to print, starts at 0.
     * @return
     */
    public List<T> getColumn(int colNum) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for column
        try {
            theTable.get(0).get(colNum);
        } catch (IndexOutOfBoundsException e) {
            throw new ColumnDoesNotExistException();
        }

        List<T> r = new ArrayList<T>();
        for (int i = 0; i < theTable.size(); i++) {
            r.add(theTable.get(i).get(colNum));
        }
        return r;
    }

    /**
     * Returns an ArrayList containing the records in the specified column.
     *
     * The column must exist.
     *
     * @param table The source table.
     * @param colNum The column index to print, starts at 0.
     * @return
     */
    public List<T> getColumn(List<List<T>> table, int colNum) {
        // Empty Table?
        if (table.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for column
        try {
            table.get(0).get(colNum);
        } catch (IndexOutOfBoundsException e) {
            throw new ColumnDoesNotExistException();
        }

        List<T> r = new ArrayList<T>();
        for (int i = 0; i < table.size(); i++) {
            r.add(table.get(i).get(colNum));
        }
        return r;
    }

    /**
     * Prints the item at the specified coordinates.
     *
     * @param row The row index of the object, starts at 0.
     * @param col The column index of the object, starts at 0.
     */
    public void printEntry(int row, int col) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for entry
        try {
            theTable.get(row).get(col);
        } catch (IndexOutOfBoundsException e) {
            throw new RecordDoesNotExistException();
        }

        System.out.printf("%s\n", getEntry(row, col));
    }

    /**
     * Returns the item at the specified coordinates.
     *
     * @param row The row index of the object, starts at 0.
     * @param col The column index of the object, starts at 0.
     * @return
     */
    public T getEntry(int row, int col) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for entry
        try {
            theTable.get(row).get(col);
        } catch (IndexOutOfBoundsException e) {
            throw new RecordDoesNotExistException();
        }


        return theTable.get(row).get(col);
    }

    /**
     * Is this column comparable? The column must exist.
     *
     * @param col
     * @return true if it makes sense to compare values in this column,
     * otherwise false.
     */
    public boolean isColumnComparable(int col) {

        for (int i = 0; i < theTable.size() - 1; i++) {
            try {
                theTable.get(i).get(col).compareTo(theTable.get(i + 1).get(col));
            } catch (ClassCastException e) {
                return false;
            }
        }

        return true;
    }

    /**
     * Column Comparator Class
     *
     * @param <T>
     */
    protected static class ColumnComparator<T extends Comparable<? super T>> implements Comparator {

        int col;

        /**
         *
         * @param col
         */
        public ColumnComparator(int col) {
            this.col = col;
        }

        /**
         *
         * @param row1
         * @param row2
         * @return
         */
        @Override
        public int compare(Object row1, Object row2) {
            if (!(row1 instanceof ArrayList) || !(row2 instanceof ArrayList)) {
                throw new ClassCastException();
            }
            return ((ArrayList<T>) row1).get(col).compareTo(((ArrayList<T>) row2).get(col));
        }
    }

    protected static class ReverseColumnComparator<T extends Comparable<? super T>> implements Comparator {

        int col;

        /**
         *
         * @param col
         */
        public ReverseColumnComparator(int col) {
            this.col = col;
        }

        /**
         *
         * @param row1
         * @param row2
         * @return
         */
        @Override
        public int compare(Object row1, Object row2) {
            if (!(row1 instanceof ArrayList) || !(row2 instanceof ArrayList)) {
                throw new ClassCastException();
            }
            return ((ArrayList<T>) row2).get(col).compareTo(((ArrayList<T>) row1).get(col));
        }
    }

    /**
     * Sort Table by the specified Column.
     *
     * The Column must exist and it must make sense to sort it's entries.
     *
     * @param col which column to sort by?
     * @throws NotComparableException
     */
    public void sort(int col) throws NotComparableException {

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


        // Can we sort?
        if (!isColumnComparable(col)) {
            throw new NotComparableException();
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
    public void sort(int col, boolean ascending) throws NotComparableException {

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


        // Can we sort?
        if (!isColumnComparable(col)) {
            throw new NotComparableException();
        }
        
        Comparator compare;
        if (ascending) {
            compare = new ColumnComparator(col);
        } else {
            compare = new ReverseColumnComparator(col);

        }
        Collections.sort(theTable, compare);
    }

    /**
     * Search for a Record in a Column.
     *
     * @param searchFor Object to search for.
     * @param col Column to search in.
     * @return Index of first matching record, or -1 if no matches.
     */
    public int search(Comparable searchFor, int col) {

        // Empty Table?
        if (theTable.isEmpty()) {
            return -1;
        }

        // check for column
        try {
            theTable.get(0).get(col);
        } catch (IndexOutOfBoundsException e) {
            throw new ColumnDoesNotExistException();
        }

        for (int i = 0; i < theTable.size(); i++) {

            if (theTable.get(i).get(col).equals(searchFor)) {
                return i;
            }

        }

        return -1;
    }

    /**
     * Iterate through table from left-to-right, horizontal, top-to-bottom.
     */
    private static class IterateLTRHorizontal<T extends Comparable<? super T>> implements Iterator {

        List<List<T>> t;
        int x, y;

        public IterateLTRHorizontal(List<List<T>> t1) {
            t = t1;
            x = y = 0;
        }

        @Override
        public boolean hasNext() {
            try {
                t.get(x).get(y);
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
            return true;
        }

        @Override
        public T next() throws RecordDoesNotExistException {
            T ret;

            try {
                ret = t.get(x).get(y);
            } catch (IndexOutOfBoundsException e) {
                throw new RecordDoesNotExistException("This is the end. Maybe you should use hasNext()?");
            }

            if (t.get(x).size() <= y + 1) {
                x++;
                y = 0;
            } else {
                y++;
            }
            return ret;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * Iterate through table from left-to-right, horizontal, top-to-bottom.
     *
     * @return an iterator object built from the static class of the same name.
     */
    public Iterator IterateLTRHorizontal() {
        return new IterateLTRHorizontal(this.theTable);
    }

    /**
     * Iterate through table from left-to-right, vertical, top-to-bottom.
     */
    private static class IterateLTRVertical<T extends Comparable<? super T>> implements Iterator {

        List<List<T>> t;
        int x, y;

        public IterateLTRVertical(List<List<T>> t1) {
            t = t1;
            x = y = 0;
        }

        @Override
        public boolean hasNext() throws RecordDoesNotExistException {
            try {
                t.get(x).get(y);
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
            return true;
        }

        @Override
        public T next() throws RecordDoesNotExistException {
            T ret;

            try {
                ret = t.get(x).get(y);
            } catch (IndexOutOfBoundsException e) {
                throw new RecordDoesNotExistException("This is the end. Maybe you should use hasNext()?");
            }

            if (t.size() <= x + 1) {
                y++;
                x = 0;
            } else {
                x++;
            }
            return ret;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * Iterate through table from left-to-right, vertical, top-to-bottom.
     *
     * @return an iterator object built from the static class of the same name.
     */
    public Iterator IterateLTRVertical() {
        return new IterateLTRVertical(this.theTable);
    }

    /**
     * Iterate through a single column top-to-bottom.
     */
    private static class IterateColumn<T extends Comparable<? super T>> implements Iterator {

        List<List<T>> t;
        int colNum;
        int x;

        public IterateColumn(List<List<T>> t1, int colNum) {
            t = t1;
            x = 0;
            this.colNum = colNum;
        }

        @Override
        public boolean hasNext() throws RecordDoesNotExistException {
            try {
                t.get(x).get(colNum);
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
            return true;
        }

        @Override
        public T next() throws RecordDoesNotExistException {
            T ret;

            try {
                ret = t.get(x).get(colNum);
            } catch (IndexOutOfBoundsException e) {
                throw new RecordDoesNotExistException("This is the end. Maybe you should use hasNext()?");
            }

            x++;
            return ret;
        }

        /**
         * Removes the ENTIRE ROW
         */
        @Override
        public void remove() {
            t.remove(x - 1);
        }
    }

    /**
     * Iterate through table from top-to-bottom on a given column.
     *
     * @param colNum Which column to iterate?
     * @return an iterator object built from the static class of the same name.
     */
    public Iterator IterateColumn(int colNum) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for column
        try {
            theTable.get(0).get(colNum);
        } catch (IndexOutOfBoundsException e) {
            throw new ColumnDoesNotExistException();
        }

        return new IterateColumn(this.theTable, colNum);
    }

    /**
     * Iterate through a single row top-to-bottom.
     */
    private static class IterateRow<T extends Comparable<? super T>> implements Iterator {

        List<List<T>> t;
        int rowNum;
        int x;

        public IterateRow(List<List<T>> t1, int rowNum) {
            t = t1;
            x = 0;
            this.rowNum = rowNum;
        }

        @Override
        public boolean hasNext() throws RecordDoesNotExistException {
            try {
                t.get(rowNum).get(x);
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
            return true;
        }

        @Override
        public T next() throws RecordDoesNotExistException {
            T ret;

            try {
                ret = t.get(rowNum).get(x);
            } catch (IndexOutOfBoundsException e) {
                throw new RecordDoesNotExistException("This is the end. Maybe you should use hasNext()?");
            }

            x++;
            return ret;
        }

        /**
         * Removes the ENTIRE COLUMN
         */
        @Override
        public void remove() {
            t.get(rowNum).remove(x - 1);
        }
    }

    /**
     * Iterate through table from top-to-bottom on a given row.
     *
     * @param rowNum Which row to iterate?
     * @return an iterator object built from the static class of the same name.
     */
    public Iterator IterateRow(int rowNum) {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        // check for row
        try {
            theTable.get(rowNum).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new ColumnDoesNotExistException();
        }

        return new IterateRow(this.theTable, rowNum);
    }

    /**
     * Iterate through a table top-to-bottom returning every row.
     */
    private static class IterateRows<T extends Comparable<? super T>> implements Iterator {

        List<List<T>> t;
        int x;

        public IterateRows(List<List<T>> t1) {
            t = t1;
            x = 0;
        }

        @Override
        public boolean hasNext() {
            try {
                t.get(x);
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
            return true;
        }

        @Override
        public List<T> next() throws RowDoesNotExistException {
            List<T> ret;

            try {
                ret = t.get(x);
            } catch (IndexOutOfBoundsException e) {
                throw new RowDoesNotExistException("This is the end. Maybe you should use hasNext()?");
            }
            x++;
            return ret;
        }

        /**
         * Removes the ENTIRE ROW
         */
        @Override
        public void remove() {
            t.remove(x - 1);
        }
    }

    /**
     * Iterate through a table top-to-bottom returning every row.
     *
     * @return an iterator object built from the static class of the same name.
     */
    public Iterator IterateRow() {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        return new IterateRows(this.theTable);
    }

    /**
     * Iterate through a table left-to-right returning every column.
     */
    private static class IterateColumns<T extends Comparable<? super T>> implements Iterator {

        List<List<T>> t;
        int x;

        public IterateColumns(List<List<T>> t1) {
            t = t1;
            x = 0;
        }

        @Override
        public boolean hasNext() {
            try {
                t.get(0).get(x);
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
            return true;
        }

        @Override
        public List<T> next() throws ColumnDoesNotExistException {
            List<T> ret = null;

            try {
                for (int i = 0; i < t.size(); i++) {
                    ret.add(t.get(i).get(x));
                }
            } catch (IndexOutOfBoundsException e) {
                throw new ColumnDoesNotExistException("This is the end. Maybe you should use hasNext()?");
            }
            x++;
            return ret;

        }

        /**
         * Removes the ENTIRE COLUMN
         */
        @Override
        public void remove() {

            for (int i = 0; i < t.size(); i++) {
                t.get(i).remove(x - 1);
            }
        }
    }

    /**
     * Iterate through a table left-to-right returning every column
     *
     * @return an iterator object built from the static class of the same name.
     */
    public Iterator IterateColumn() {
        // Empty Table?
        if (theTable.isEmpty()) {
            throw new EmptyTableException();
        }

        return new IterateColumns(this.theTable);
    }

    private class DefaultIterator implements Iterator {

        int x = 0;

        @Override
        public boolean hasNext() {
            try {
                theTable.get(x);
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
            return true;
        }

        @Override
        public List<T> next() throws RowDoesNotExistException {
            List<T> ret;

            try {
                ret = theTable.get(x);
            } catch (IndexOutOfBoundsException e) {
                throw new RowDoesNotExistException("This is the end. Maybe you should use hasNext()?");
            }

            x++;
            return ret;
        }

        /**
         * Removes the ENTIRE ROW
         */
        @Override
        public void remove() {
            theTable.remove(x - 1);
        }
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new DefaultIterator();
    }
}
