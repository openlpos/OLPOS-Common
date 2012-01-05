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
import java.util.Vector;

import javax.swing.ImageIcon;

import com.globalretailtech.util.*;
import com.globalretailtech.data.PosKey;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.*;
import com.globalretailtech.pos.devices.*;
import org.apache.log4j.Logger;

/**
 * An invoice display. Contains a header field
 * for transaction information, #, pos #, employee
 * and date. Implements JTable to display transaction
 * detail that includes quantity. a long description
 * and price.
 *
 * @author  Quentin Olson
 * @see
 */
public class OperDisplay extends JPanel implements PosTicket, PosGui, PosEventListener {

    private NavButton voidButton;

	private NavButton topButton;

	private NavButton bottomButton;

	private NavButton downButton;

	private NavButton upButton;

	static Logger logger = Logger.getLogger(OperDisplay.class);

    /** Line up directive */
    public static final int UP = 1;
    /** Line down directive */
    public static final int DOWN = 2;
    /** Top of reciept directive */
    public static final int TOP = 3;
	/** Bottom of receipt directive */
	public static final int BOTTOM = 4;
	/** Void current item directive */
 	public static final int VOID = 5;

	private Vector buttons;

    // fonts
    private Font operReceiptFont;
    private Font headerFont;
    private Font headerTitleFont;
    private int operReceiptFontSize;
    private int headerFontSize;
    private int headerTitleFontSize;

    // receipt table widths and height

    private int qtyWidth;
    private int itemWidth;
    private int descWidth;
    private int valueWidth;
    private int rowHeight;
    private int tableRows;
    private int trxWidth;
    private int posWidth;
    private int empWidth;
    private int dateWidth;
	private int totalWidth;

    // Visual elements

    private JScrollPane scrollpane;
    private JTable table;
    private int row;
    private int rowStep;

    private tableModel dataModel;

    private OperReceiptField trx;
    private OperReceiptField pos;
    private OperReceiptField emp;
	private OperReceiptField date;
	private OperReceiptField total;

    // Table column indexes

    private static int QTY = 0;
    private static int DESC = 1;
    private static int VALUE = 2;

    private static int COLS = 3;

    // the literals from pos config

    private PosContext context;
	boolean displayNavButtons;

    /**
     * Initialize with the current literals and paramaters.
     */
    public OperDisplay() {
        super();
    }

    /**
     * Read the properties file for Format information.
     * Create the panels and setup the layout. Add the
     * fields to the layout.
     */
    public void init(PosContext ctx) {

        setLayout(new BorderLayout());
        context = ctx;

		// add itself as listener for PosEvents
		context.eventStack().addPosEventListener(this);

        ShareProperties p = new ShareProperties(this.getClass().getName());

        if (p.Found()) {
            operReceiptFontSize = Integer.valueOf(p.getProperty("OperReceiptFontSize", "10")).intValue();
            operReceiptFont = new Font(p.getProperty("OperReceiptFont", "Courier"), Font.PLAIN, operReceiptFontSize);
            headerFontSize = Integer.valueOf(p.getProperty("HeaderFontSize", "10")).intValue();
            headerFont = new Font(p.getProperty("HeaderFont", "Courier"), Font.PLAIN, headerFontSize);
            headerTitleFontSize = Integer.valueOf(p.getProperty("HeaderTitleFontSize", "10")).intValue();
            headerTitleFont = new Font(p.getProperty("HeaderTitleFont", "Courier"), Font.PLAIN, headerTitleFontSize);

            rowHeight = Integer.valueOf(p.getProperty("RowHeight", "20")).intValue();
            qtyWidth = Integer.valueOf(p.getProperty("QtyWidth", "60")).intValue();
            descWidth = Integer.valueOf(p.getProperty("DescWidth", "32")).intValue();
            valueWidth = Integer.valueOf(p.getProperty("ValueWidth", "5")).intValue();

            trxWidth = Integer.valueOf(p.getProperty("TrxWidth", "10")).intValue();
            posWidth = Integer.valueOf(p.getProperty("PosWidth", "3")).intValue();
            empWidth = Integer.valueOf(p.getProperty("EmpWidth", "3")).intValue();
            dateWidth = Integer.valueOf(p.getProperty("DateWidth", "10")).intValue();
			totalWidth = Integer.valueOf(p.getProperty("TotalWidth", "6")).intValue();
            tableRows = Integer.valueOf(p.getProperty("TableRows", "200")).intValue();
            
			displayNavButtons = p.getProperty("DisplayNavButtons", "true").equals("true");
        } else {
            // big trouble here...
            logger.fatal("Properties not found for OperDisplay");
            return;
        }

		// buttons
		String fetchSpec = PosKey.getBySubMenuID(32);
		setButtons(Application.dbConnection().fetch(new PosKey(), fetchSpec));

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

        trxInfo.add(setConstraints(trx = new OperReceiptField(context.posParameters().getString("TrxTag"), trxWidth, headerFont.deriveFont(Font.PLAIN,headerFontSize-2)),
                layout,
                c,
                0, 0, 1, 1));

        trxInfo.add(setConstraints(pos = new OperReceiptField(context.posParameters().getString("PosTag"), posWidth, headerFont),
                layout,
                c,
                1, 0, 1, 1));

        trxInfo.add(setConstraints(emp = new OperReceiptField(context.posParameters().getString("EmpTag"), empWidth, headerFont),
                layout,
                c,
                2, 0, 1, 1));

		trxInfo.add(setConstraints(date = new OperReceiptField(context.posParameters().getString("DateTag"), dateWidth, headerFont.deriveFont(Font.PLAIN,headerFontSize-1)),
				layout,
				c,
				3, 0, 1, 1));

		trxInfo.add(setConstraints(total = new OperReceiptField(context.posParameters().getString("Total"), totalWidth, headerFont.deriveFont(Font.BOLD,headerFontSize+4)),
				layout,
				c,
				4, 0, 1, 1));


		String shareDir = System.getProperty ("SHARE");
		topButton = new NavButton(shareDir+"/images//top.gif", TOP);
		if (displayNavButtons)
	        trxInfo.add(setConstraints(topButton,
                layout,
                c,
                0, 1, 1, 1));
		upButton = new NavButton(shareDir+"/images//up.gif", UP);
		if (displayNavButtons)
	        trxInfo.add(setConstraints(upButton,
                layout,
                c,
                1, 1, 1, 1));

		downButton = new NavButton(shareDir+"/images//down.gif", DOWN);
		if (displayNavButtons)
	        trxInfo.add(setConstraints(downButton,
                layout,
                c,
                2, 1, 1, 1));
		bottomButton = new NavButton(shareDir+"/images/bottom.gif", BOTTOM);
		if (displayNavButtons)
			trxInfo.add(setConstraints( bottomButton,
				layout,
				c,
				3, 1, 1, 1));

		voidButton = new NavButton(shareDir+"/images/void.gif", VOID);
		if (displayNavButtons)
			trxInfo.add(setConstraints(voidButton,
				layout,
				c,
				4, 1, 1, 1));

        // add the transaction area

        headerpanel.add(trxInfo);

        // Done with header stuff, set up the item sale section

        table = new JTable(dataModel = new tableModel(tableRows));
        table.setFont(operReceiptFont);
        table.setBackground(new Color(0xFFF8C6));
        table.setRowHeight(rowHeight);
		table.addMouseListener( new TableMouseAdapter(this));
		
        javax.swing.table.TableColumn col = null;
        table.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));

        col = table.getColumnModel().getColumn(QTY);
        col.setPreferredWidth(qtyWidth);
        col.setMaxWidth(qtyWidth);
        col.setMinWidth(qtyWidth);

        col = table.getColumnModel().getColumn(DESC);
        col.setPreferredWidth(descWidth);
//        col.setMaxWidth(descWidth);
//        col.setMinWidth(descWidth);

        col = table.getColumnModel().getColumn(VALUE);
        col.setPreferredWidth(valueWidth);
        col.setMaxWidth(valueWidth);
        col.setMinWidth(valueWidth);

        scrollpane = new JScrollPane(table);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);  // we'll use our own buttons instead.

        add(headerpanel, BorderLayout.NORTH);
        add(scrollpane, BorderLayout.CENTER);
        row = rowStep = 0;
    }


    /**
     * PosTicket abstract implementations

     */
    public boolean isOpen() {
        return true;
    }

    /** Character width of quantity column. */
    public int getQtyWidth() {
        return 6;
    }

    /** Character width of item/sku column. */
    public int getItemWidth() {
        return 2 * (itemWidth / operReceiptFontSize) - 3;
    }

    /** Character width of description column. */
    public int getItemDescWidth() {
        return 0;
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
    public JComponent getGui() {
        return this;
    }

    /** Set the transaction number. */
    public void setTrxNo(String value) {
        trx.setText(Format.zeroFill(value, trxWidth, " "));
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
        date.setText(value);
    }

    /** Set the quantity field. */
    public void setQty(String value) {
        dataModel.setValueAt(value, row, QTY);
    }

    /** Set the item/sku field. */
    public void setItem(String value) {
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
        makeRowVisible(table, row);
        row++;
        rowStep = row-1;
		table.changeSelection(rowStep, 1,false,false);
    }

    /**
     * Print a string and a cr/lf
     */
    public void println(String value) {
        setDesc(value);
        makeRowVisible(table, row);
        row++;
        rowStep = (row-1);
		table.changeSelection(rowStep, 1,false,false);
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
            dataModel.setValueAt(new String(""), i, DESC);
            dataModel.setValueAt(new String(""), i, VALUE);
        }
        row = rowStep = 0;
        makeRowVisible(table, row);
		table.changeSelection(rowStep, 1,false,false);
    }

    public void home() {
    }

    public void open() {
    }

    public void close() {
    }

    public void clearln() {
        row--;
        dataModel.setValueAt(new String(""), row, QTY);
        dataModel.setValueAt(new String(""), row, DESC);
        dataModel.setValueAt(new String(""), row, VALUE);
        makeRowVisible(table, row);
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

        private String[] colunmNames = {"Qty", "Desc.", "Price"};
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

        public OperReceiptField(String title, int len, Font font) {
            super(len);
            setOpaque(true);
            setFont(font);
            setEditable(false);
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
                case OperDisplay.UP:

                    if (rowStep > 0) {
                        rowStep--;
                        makeRowVisible(table, rowStep);
                    }else
                    	rowStep = row-1;

                    break;
                case OperDisplay.DOWN:

                    if (rowStep < (row-1)) {
                        rowStep++;
                        makeRowVisible(table, rowStep);
                    }else
                    	rowStep = 0;
                    break;
                case OperDisplay.TOP:
                    makeRowVisible(table, 0);
					rowStep=0;
                    break;
				case OperDisplay.BOTTOM:
					if ( row > 0){
						makeRowVisible(table, row-1);
						rowStep=row-1;
					}
					break;
				case OperDisplay.VOID:
					setItem (rowStep);
					break;
            }
			table.changeSelection(rowStep, 1,false,false);
        }

		/**
		 * @param index item index to remove
		 */
		private void setItem(int index) {
			PosEvent event = new ItemVoidRequest();
			event.setContext(context);
			context.eventStack().pushEvent(event);
			context.eventStack().nextEvent();
		}
    }

    /**
     * Method to scroll to the specified row.
     */
    public void makeRowVisible(JTable table, int visibleRow) {
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
    /**
     * Changes 'current row' internal field (rowStep) to the row selected by mouse. 
     * @author isemenko
     */
	class TableMouseAdapter extends MouseAdapter{
		OperDisplay display;
		public TableMouseAdapter (OperDisplay display){
			this.display = display;
		}
		public void mouseReleased(MouseEvent e) {
			JTable table = (JTable)e.getComponent();
			if (table != null ){
				display.rowStep =table.getSelectedRow(); 
			}
		}
	}

	/****************************************************************************/
	/*                  PosEventListener methods                                */
	/****************************************************************************/
	/* 
	 * @see com.globalretailtech.pos.events.PosEventListener#beforeEvent(com.globalretailtech.pos.events.PosEvent)
	 */
	public void beforeEvent (PosEvent event) {
	}
	/* 
	 * @see com.globalretailtech.pos.events.PosEventListener#afterEvent(com.globalretailtech.pos.events.PosEvent)
	 */
	public void afterEvent(PosEvent event) {
		if ( event instanceof ItemVoidRequest){
			PosEvent voidEvent = new ItemVoid();
			voidEvent.setContext( event.context());
			try {
				voidEvent.engage (rowStep+1);
				// notify about changes
				context.eventStack().pushEvent(new Null(context));
				context.eventStack().nextEvent();
			} catch (PosException e) {
				logger.error("Can't perform voiding current item #"+rowStep, e);
			}
		}else if ( event instanceof NavKey){
			NavKey navKey = (NavKey)event; 
			logger.debug ("NavKey pressed: "+navKey.getCode());
			if ( navKey.getCode() == 801 ) //up
				upButton.doClick();
			else if ( navKey.getCode() == 802 ) //down
				downButton.doClick();
			else if ( navKey.getCode() == 803 ) //pg up
				topButton.doClick();
			else if ( navKey.getCode() == 804 ) //pg down
				bottomButton.doClick();
		}else{
			if ( event.context() != null && 
				event.context().currEj() != null){
					String totalStr = event.context().currEj().ejTotal()+"";
					total.setText( Format.toMoney(totalStr,Application.locale()));
				}
		}
	}
	/****************************************************************************/
	/* 
	 * @see com.globalretailtech.pos.devices.PosPrompt#buttons()
	 */
	public Vector buttons() {
		return buttons;
	}
	/**
	 */
	public void setButtons(Vector vector) {
		buttons = vector;
	}
}


