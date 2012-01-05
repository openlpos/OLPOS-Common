/*
 * Copyright (C) 2001 Global Retail Technology, LLC
 * <http://www.globalretailtech.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.globalretailtech.pos.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.ImageIcon;

import com.globalretailtech.util.*;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.devices.*;
import org.apache.log4j.Logger;

/**
 * An invoice display. Contains a header field
 * for transaction information, #, pos #, employee
 * and date. Implements JTable to display transaction
 * detail that includes quantity, item (sku number),
 * a long description and price. (Need to figure out how
 * to merge this with OperDisplay.)
 *
 * @author  Quentin Olson
 * @see PosTicket
 */
public class OperDisplay4C extends JPanel implements PosTicket {

    static Logger logger = Logger.getLogger(OperDisplay4C.class);

    /** Line up directive */
    public static final int UP = 1;
    /** Line down directive */
    public static final int DOWN = 2;
    /** Top of reciept directive */
    public static final int TOP = 3;
    /** Bottom of receipt directive */
    public static final int BOTTOM = 4;

    // fonts
    private Font operReceiptFont;
    private Font headerFont;
    private Font headerTitleFont;
    private int operReceiptFontSize;
    private int headerFontSize;
    private int headerTitleFontSize;

    // table panel size

    private int height;
    private int width;

    // receipt table widths and height

    private int qtyWidth;
    private int itemWidth;
    private int descWidth;
    private int valueWidth;
    private int rowHeight;
    private int tableRows;

    // Visual elements

    private JScrollPane scrollpane;
    private JViewport viewport;
    private static JTable table;
    private int row;
    private int rowStep;

    private tableModel dataModel;

    private OperReceiptField trx;
    private OperReceiptField pos;
    private OperReceiptField emp;
    private OperReceiptField date;


    // Table column indexes

    private static int QTY = 0;
    private static int ITEM = 1;
    private static int DESC = 2;
    private static int VALUE = 3;

    private static int COLS = 4;


    // the literals from pos config

    private PosParameters posParameters;

    /**
     * Initialize with the current literals and paramaters.
     */
    public OperDisplay4C() {
        super();
    }

    public boolean isOpen() {
        return true;
    }

    /**
     * Read the properties file for Format information.
     * Create the panels and setup the layout. Add the
     * fields to the layout.
     */
    public void init(PosParameters params, int w, int h) {

        setLayout(new BorderLayout());
        width = w;
        height = h;
        posParameters = params;

        ShareProperties p = new ShareProperties(this.getClass().getName());

        if (p.Found()) {
            operReceiptFontSize = Integer.valueOf(p.getProperty("OperReceiptFontSize", "10")).intValue();
            operReceiptFont = new Font(p.getProperty("OperReceiptFont", "Courier"), Font.PLAIN, operReceiptFontSize);
            headerFontSize = Integer.valueOf(p.getProperty("HeaderFontSize", "10")).intValue();
            headerFont = new Font(p.getProperty("HeaderFont", "Courier"), Font.PLAIN, headerFontSize);
            headerTitleFontSize = Integer.valueOf(p.getProperty("HeaderTitleFontSize", "10")).intValue();
            headerTitleFont = new Font(p.getProperty("HeaderTitleFont", "Courier"), Font.PLAIN, headerTitleFontSize);

            rowHeight = Integer.valueOf(p.getProperty("RowHeight", "20")).intValue();
            qtyWidth = Integer.valueOf(p.getProperty("QtyWidth", "5")).intValue();
            itemWidth = Integer.valueOf(p.getProperty("ItemWidth", "12")).intValue();
            descWidth = Integer.valueOf(p.getProperty("DescWidth", "32")).intValue();
            valueWidth = Integer.valueOf(p.getProperty("ValueWidth", "5")).intValue();

            tableRows = Integer.valueOf(p.getProperty("TableRows", "200")).intValue();
        } else {
            // big trouble here...
            logger.fatal("Properties not found for OperDisplay4C");
            return;
        }

        JPanel headerpanel = new JPanel(new BorderLayout());
        headerpanel.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));

        // add the store, transaction info panels to the header panel, using a gridbag

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.weighty = 1.0;
        JPanel trxInfo = new JPanel(layout);   // transaction information panel
        trxInfo.setBackground(new Color(0xFFF8C6));

        // fill out the transaction info panel, set constraints x, y, height, width

        trxInfo.add(setConstraints(trx = new OperReceiptField(posParameters.getString("TrxTag"), 8),
                layout,
                c,
                0, 0, 1, 1));

        trxInfo.add(setConstraints(pos = new OperReceiptField(posParameters.getString("PosTag"), 8),
                layout,
                c,
                1, 0, 1, 1));

        trxInfo.add(setConstraints(emp = new OperReceiptField(posParameters.getString("EmpTag"), 8),
                layout,
                c,
                2, 0, 1, 1));

        trxInfo.add(setConstraints(date = new OperReceiptField(posParameters.getString("DateTag"), 8),
                layout,
                c,
                3, 0, 1, 1));

		String shareDir = System.getProperty ("SHARE");
        trxInfo.add(setConstraints(new NavButton(shareDir+"/images/top.gif", TOP),
                layout,
                c,
                0, 1, 1, 1));

        trxInfo.add(setConstraints(new NavButton(shareDir+"/images/up.gif", UP),
                layout,
                c,
                1, 1, 1, 1));

        trxInfo.add(setConstraints(new NavButton(shareDir+"/images/down.gif", DOWN),
                layout,
                c,
                2, 1, 1, 1));

        trxInfo.add(setConstraints(new NavButton(shareDir+"/images/bottom.gif", BOTTOM),
                layout,
                c,
                3, 1, 1, 1));

        // add the transaction area

        headerpanel.add(trxInfo);

        // Done with header stuff, set up the item sale section

        table = new JTable(dataModel = new tableModel(tableRows));
        table.setFont(operReceiptFont);
        table.setBackground(new Color(0xFFF8C6));
        table.setRowHeight(rowHeight);

        javax.swing.table.TableColumn col = null;
        table.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));

        col = table.getColumnModel().getColumn(QTY);
        col.setPreferredWidth(qtyWidth);
        col.setMaxWidth(qtyWidth);
        col.setMinWidth(qtyWidth);

        col = table.getColumnModel().getColumn(ITEM);
        col.setPreferredWidth(itemWidth);
        col.setMaxWidth(itemWidth);
        col.setMinWidth(itemWidth);

        col = table.getColumnModel().getColumn(DESC);
        col.setPreferredWidth(descWidth);
        col.setMaxWidth(descWidth);
        col.setMinWidth(descWidth);

        col = table.getColumnModel().getColumn(VALUE);
        col.setPreferredWidth(valueWidth);
        col.setMaxWidth(valueWidth);
        col.setMinWidth(valueWidth);

        scrollpane = new JScrollPane(table);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);  // we'll use our own buttons instead.

        viewport = scrollpane.getViewport();

        // set the size of the whole thing here.

        scrollpane.setPreferredSize(new Dimension(width, height));

        add(headerpanel, BorderLayout.NORTH);
        add(scrollpane, BorderLayout.SOUTH);
        row = rowStep = 0;
    }


    /**
     * PosTicket abstract implementations
     */

    /** Character width of quantity column. */
    public int getQtyWidth() {
        return 3;
    }

    /** Character width of item/sku column. */
    public int getItemWidth() {
        return 2 * (itemWidth / operReceiptFontSize) - 3;
    }

    /** Character width of description column. */
    public int getItemDescWidth() {
        return 2 * (descWidth / operReceiptFontSize) - 3;
    }

    /** Character width of price/amount column. */
    public int getAmountWidth() {
        return 2 * (valueWidth / operReceiptFontSize) - 3;
    }

    /** Number of columns in the table */
    public int getColumns() {
        return 4;
    }

    /** Return the graphical component */
    public Component getGui() {
        return this;
    }

    /** Set the transaction number. */
    public void setTrxNo(String value) {
        trx.setText(value);
    }

    /** Set the POS number */
    public void setPosNo(String value) {
        pos.setText(value);
    }

    /** Set the operator/user ID. */
    public void setOperator(String value) {
        emp.setText(value);
    }

    /** Set the date field. */
    public void setDate(String value) {
    }

    /** Set the quantity field. */
    public void setQty(String value) {
        dataModel.setValueAt(value, row, QTY);
    }

    /** Set the item/sku field. */
    public void setItem(String value) {
        dataModel.setValueAt(value, row, ITEM);
    }

    /** Set the description field. */
    public void setDesc(String value) {
        dataModel.setValueAt(value, row, DESC);
    }

    /**Set the amount/price field. */
    public void setAmount(String value) {
        dataModel.setValueAt(value, row, VALUE);
    }

    /**
     *Print an enpty line, cr/lf.
     */
    public void println() {
        row++;
        rowStep = row;
        makeRowVisible(table, row);
    }

    /**
     * Print a string and a cr/lf
     */
    public void println(String value) {
        setDesc(value);
        row++;
        rowStep = row;
        makeRowVisible(table, row);
    }

    /**
     * Print the header for this class
     */
    public void printHeader() {
    }

    /**
     * Print the trailer for this class.
     */
    public void printTrailer() {
    }

    /**
     * The clear opperation for this class,
     * clear each row in the table, set
     * current row to 0 and move to the
     * top of the table.
     */
    public void clear() {

        for (int i = 0; i < dataModel.getRowCount(); i++) {

            dataModel.setValueAt(new String(""), i, QTY);
            dataModel.setValueAt(new String(""), i, ITEM);
            dataModel.setValueAt(new String(""), i, DESC);
            dataModel.setValueAt(new String(""), i, VALUE);
        }
        row = rowStep = 0;
        makeRowVisible(table, row);
    }

    public void clearln() {
    }

    /**
     * The table implementation.
     */
    private class tableModel extends javax.swing.table.AbstractTableModel {

        private int rows;

        public tableModel(int r) {
            rows = r;
            rowData = new Object[rows][COLS];
        };

        private String[] colunmNames = {"Qty", "Item", "Desc.", "Price"};
        private Object[][] rowData;

        public String getColumnName(int col) {
            return colunmNames[col];
        }

        public int getRowCount() {
            return rows;
        }

        public int getColumnCount() {
            return COLS;
        }

        public Object getValueAt(int row, int column) {
            return rowData[row][column];
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public void setValueAt(Object value, int row, int column) {
            rowData[row][column] = value;
            fireTableCellUpdated(row, column);
        }
    }

    /**
     * Private method to set GridBagConstraints.
     */
    private Component setConstraints(Component c,
                                     GridBagLayout layout,
                                     GridBagConstraints constraints,
                                     int x,
                                     int y,
                                     int height,
                                     int width) {

        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridheight = height;
        constraints.gridwidth = width;
        layout.setConstraints(c, constraints);

        return c;
    }

    /**
     * Class to manage labels.
     */
    private class OperReceiptLabel extends JLabel {

        public OperReceiptLabel(String text) {
            super(text);
            setAlignmentX(RIGHT_ALIGNMENT);
            setFont(headerFont);
            setOpaque(true);
            setForeground(Color.black);
            setBackground(new Color(0xFFF8C6));
        }
    }

    /**
     * Class to manage named text fields.
     */
    private class OperReceiptField extends JTextField {

        public OperReceiptField(String title, int len) {
            super(len);
            setOpaque(true);
            setFont(headerFont);
            setBackground(new Color(0xFFF8C6));
            Border base = BorderFactory.createEtchedBorder();
            TitledBorder border = BorderFactory.createTitledBorder(base,
                    title,
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    headerTitleFont,
                    Color.black);

            setBorder(border);
            setForeground(Color.black);
        }
    }

    /**
     * Class for implementing navigation buttons.
     */
    private class NavButton extends JButton {

        private int directive;

        public int directive() {
            return directive;
        }

        public NavButton(String img, int value) {

            super(new ImageIcon(img));
            directive = value;
            addActionListener(new NavAction());
        }
    }

    /**
     * Class for implementing navigation actions.
     */
    class NavAction implements ActionListener {

        public NavAction() {
        }

        public void actionPerformed(ActionEvent e) {

            NavButton button = (NavButton) e.getSource();

            switch (button.directive()) {
                case UP:

                    if (rowStep > 0) {
                        rowStep--;
                        makeRowVisible(table, rowStep);
                    }

                    break;
                case DOWN:

                    if (rowStep < row) {
                        rowStep++;
                        makeRowVisible(table, rowStep);
                    }
                    break;
                case TOP:
                    makeRowVisible(table, 0);
                    break;
                case BOTTOM:
                    makeRowVisible(table, row);
                    break;
            }
        }
    }

    /**
     * Method to scroll to the specified row.
     */
    public static void makeRowVisible(JTable table, int visibleRow) {
        if (table.getColumnCount() == 0)
            return;

        if (visibleRow < 0 || visibleRow >= table.getRowCount()) {
            throw new IllegalArgumentException(String.valueOf(visibleRow));
        }

        Rectangle visible = table.getVisibleRect();
        Rectangle cell = table.getCellRect(visibleRow, 0, true);

        if (cell.y < visible.y) {
            visible.y = cell.y;
            table.scrollRectToVisible(visible);
        } else if (cell.y + cell.height > visible.y + visible.height) {
            visible.y = cell.y + cell.height - visible.height;
            table.scrollRectToVisible(visible);
        }
    }
}


