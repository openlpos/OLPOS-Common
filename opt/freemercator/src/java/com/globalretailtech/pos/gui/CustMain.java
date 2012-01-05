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
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;


import com.globalretailtech.util.Format;
import com.globalretailtech.util.Application;
import com.globalretailtech.util.ShareProperties;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.*;
import com.globalretailtech.pos.ej.*;
import com.globalretailtech.pos.operators.MarkdownByPercent;
import com.globalretailtech.data.Customer;
import com.globalretailtech.data.Promotion;

import org.apache.log4j.Logger;

/**
 * Customer display dialog. Lookups on customer
 * number and phone number. It's really like a big
 * menu button with the extra customer lookup
 * functionality.
 *
 * @author  Quentin Olson
 * @see
 */
public class CustMain extends PosNumberDialog implements PosGui {

    static Logger logger = Logger.getLogger(CustMain.class);
    
    static Map instances = new HashMap();

    // Main graphical elements

    private JTabbedPane tabPane;
    private JPanel main;
    private JPanel detail;
    private JPanel history;

    // fonts

    private Font tabFont;
    private Font font;
    private Font titleFont;
    private Font searchFont;

    private CustMainField custno;
    private CustMainField custname;
    private CustMainField custaddr;
    private CustMainField custcity;
    private CustMainField custstate;
    private CustMainField custzip;
    private CustMainField custphone;
    private CustMainField custfax;
    private CustMainField custemail;
	private CustMainField custdiscount;
    private JTextField focusField;
    private JPanel container;
    private String inputtext;

	/** 
	 * Returns instace of CustMain object if it was created,
	 * NULL otherwise
	 **/
	public static CustMain getInstance(PosContext context){
		return (CustMain)instances.get(context);
	}
    /**
     * Constructor reads the properties, sets up the fields
     * and adds the text components.
     */
    public CustMain() {

        super();

        ShareProperties p = new ShareProperties(this.getClass().getName());

        if (p.Found()) {

            tabFont = new Font(p.getProperty("TabFont", "Helvetica"), Font.ITALIC, new Integer(p.getProperty("TabFontSize", "24")).intValue());
            font = new Font(p.getProperty("Font", "Courier"), Font.PLAIN, Integer.valueOf(p.getProperty("FontSize", "10")).intValue());
            titleFont = new Font(p.getProperty("TitleFont", "Courier"), Font.PLAIN, Integer.valueOf(p.getProperty("TitleFontSize", "10")).intValue());
            searchFont = new Font(p.getProperty("SearchFont", "Courier"), Font.ITALIC, Integer.valueOf(p.getProperty("SearchFontSize", "10")).intValue());
        } else {
        }
    }


    public void init(PosContext context) {

		instances.put (context, this);

        // Create graphical elements

        setContext(context);
        tabPane = new JTabbedPane();
        tabPane.setFont(tabFont);
        main = new JPanel();

        // add the store, transaction and customer info panels to the header panel, using a gridbag

        GridBagLayout layout = new GridBagLayout();
        main.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 1.0;

        // fill out the customer info

        main.add(setConstraints(custno = new CustMainField(context.posParameters().getString("CustNoTag"), 12, true, this, font, titleFont, searchFont),
                layout,
                constraints,
                0, 1, 1, 1));

        main.add(setConstraints(custname = new CustMainField(context.posParameters().getString("CustNameTag"), 20, false, this, font, titleFont, searchFont),
                layout,
                constraints,
                1, 1, 1, 4));

        main.add(setConstraints(custaddr = new CustMainField(context.posParameters().getString("CustAddrTag"), 16, false, this, font, titleFont, searchFont),
                layout,
                constraints,
                0, 2, 1, 4));
		main.add(setConstraints(custdiscount = new CustMainField(context.posParameters().getString("CustDiscountTag"), 4, false, this, font, titleFont, searchFont),
				layout,
				constraints,
				4, 2, 1, 1));

        main.add(setConstraints(custcity = new CustMainField(context.posParameters().getString("CustCityTag"), 20, false, this, font, titleFont, searchFont),
                layout,
                constraints,
                0, 3, 1, 3));

        main.add(setConstraints(custstate = new CustMainField(context.posParameters().getString("CustStateTag"), 4, false, this, font, titleFont, searchFont),
                layout,
                constraints,
                3, 3, 1, 1));

		main.add(setConstraints(custzip = new CustMainField(context.posParameters().getString("CustZipTag"), 4, false, this, font, titleFont, searchFont),
				layout,
				constraints,
				4, 3, 1, 1));


        main.add(setConstraints(custphone = new CustMainField(context.posParameters().getString("CustPhoneTag"), 10, true, this, font, titleFont, searchFont),
                layout,
                constraints,
                0, 4, 1, 1));
        main.add(setConstraints(custfax = new CustMainField(context.posParameters().getString("CustFaxTag"), 10, false, this, font, titleFont, searchFont),
                layout,
                constraints,
                1, 4, 1, 1));

        main.add(setConstraints(custemail = new CustMainField(context.posParameters().getString("CustEmailTag"), 20, false, this, font, titleFont, searchFont),
                layout,
                constraints,
                2, 4, 1, 3));
        detail = new JPanel();
        history = new JPanel();

        tabPane.addTab("Customer", main);
        tabPane.addTab("Edit", detail);
        tabPane.addTab("Purchase History", history);

        // Add this to the list of context guis so it can
        // be homed and cleared.

        context().guis().add(this);
    }

    public void clear() {

        custno.setText("");
        custname.setText("");
        custaddr.setText("");
        custcity.setText("");
        custstate.setText("");
        custzip.setText("");
        custphone.setText("");
        custfax.setText("");
        custemail.setText("");
		custdiscount.setText("");
    }

    /**
     *
     */
    public void update(PosEvent event) {
    }

    public void update(EjHeader header) {
    }

    public void home() {
    }

    public void open() {
    }

    public void close() {
    }

    public JComponent getGui() {
        return tabPane;
    }

    public void keyEnable() {
    }

    public void keyDisable() {
    }

    public boolean isOpen() {
        return true;
    }  // or we wouldn't be herexm

    public void setText(String text) {
    }

    public void setText(String value, int row, int col) {
    }

    public int getWidth() {
        return 0;
    }

    public void toggleFocus() {
        if (custno.hasFocus())
            custphone.requestFocus();
        else
            custno.requestFocus();
    }

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

    private class CustMainField extends JTextField implements FocusListener {

        private PosEvent parent;

        public CustMainField(String title, int len, boolean searchable, PosEvent p, Font font, Font titleFont, Font searchFont) {

            super(len);

            parent = p;
            setOpaque(true);
            setBackground(new Color(0xFFF8C6));
            Border base = BorderFactory.createEtchedBorder();

            setFont(font);
            if (searchable) {
                setEditable(true);
            } else {
                setEditable(false);
            }

            TitledBorder border = BorderFactory.createTitledBorder(base,
                    title,
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    searchable ? searchFont : titleFont,
                    Color.black);
            setBorder(border);
            setForeground(Color.black);
            addFocusListener(this);
        }

        public void focusGained(FocusEvent e) {

            logger.debug("Focus gained in custmain");

            focusField = this;
            clear();

            if (context().eventStack().event() instanceof CustMain) {
                return;
            } else {
                states().pushState(0);  // dummy state
                context().eventStack().pushEvent(parent);
            }
        }

        public void focusLost(FocusEvent e) {
        }
    }

    // *** PosEvent abstract implementations ***

    /**
     *
     */
    public void engage(int value) {

        clear();
        focusField = null;
        states().popState();

        if (context().customer() != null) {
			updateCustomerInfo();
            return;
        }
//		String custNo = context().posFields().toCustNo(inputtext);
		// we don't have to make user to type "00004" if customer no is "4"
		String custNo = inputtext;

		String fetchSpec = Customer.getByCustomerNo(inputtext);
        Vector v = Application.dbConnection().fetch(new Customer(), fetchSpec);

        if (v.size() > 0) {
        	Customer customer = (Customer) v.elementAt(0);
            context().setCustomer(customer);
            int discount = customer.discount();
            if (discount > 0){
            	Promotion markdownPromotion = createMarkdownPromotion (discount);
				context().setSaleMod(markdownPromotion);
				context().applySaleMod();
            }
            context().operPrompt().update(customer.customerName());
            context().eventStack().pushEvent(new Pause (context(),1));
        } else {
			context().operPrompt().update(context().posParameters().getString("CustomerNotFound"));
            return;
        }
		context().updateCustomer(custNo);
		updateCustomerInfo();

        focusField = null;  // keep this from being updated as numbers are entered
        context().eventStack().nextEvent();
    }

    /**
	 * 
	 */
	private void updateCustomerInfo() {
		custno.setText(context().posFields().toCustNo(context().customer().customerNo()));
		custname.setText(context().customer().customerName());
		custaddr.setText(context().customer().addr1());
		custcity.setText(context().customer().addr2());
		custstate.setText(context().customer().addr3());
		custzip.setText(context().customer().addr4());
		custphone.setText(context().posFields().toPhoneNo(context().customer().phone()));
		custfax.setText(context().posFields().toPhoneNo(context().customer().fax()));
		custemail.setText(context().customer().email());
		custdiscount.setText(context().customer().discount()+"%");
	}
	/**
	 * @param discount
	 */
	private Promotion createMarkdownPromotion(int discount) {
		Promotion promo = new Promotion();
		promo.setPromotionVal1(discount);
		promo.setPromotionString(discount+"%");
		promo.setPromotionClass(MarkdownByPercent.class.getName());
		return promo;		
	}
	
	/**  */
	public void setInputText(String text) {
		setInputText (text,true);
	}
	public void setInputText(String text, boolean cutLastDigit) {

		// hack? last digit stays in the input line anyway
		if (cutLastDigit && text != null && text.length() > 0)
			text = text.substring(0,text.length()-1);
        inputtext = text;
        if ( focusField != null )
        	focusField.setText(Format.print(inputtext, " ", focusField.getColumns(), Format.RIGHT));
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    private static String eventname = "CustMain";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }

    /** Implemntation of type for PosNumberDialog */
    public int type() {
        return PosNumberDialog.CLEAR;
    }

    /** Implemntation of promptText for PosNumberDialog */
    public String promptText() {
        return "";
    }
}

