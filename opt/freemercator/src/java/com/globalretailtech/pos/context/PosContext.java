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

package com.globalretailtech.pos.context;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Enumeration;
import java.util.Locale;


import com.globalretailtech.util.*;
import com.globalretailtech.data.*;
import com.globalretailtech.pos.devices.*;
import com.globalretailtech.pos.gui.*;
import com.globalretailtech.pos.hardware.CashDrawer;
import com.globalretailtech.pos.hardware.FiscalPrinter;
import com.globalretailtech.pos.hardware.PosPrinter;
import com.globalretailtech.pos.operators.*;
import com.globalretailtech.pos.events.*;
import com.globalretailtech.pos.ej.*;
import org.apache.log4j.Logger;

/**
 * Holds an entire POS (sales) context.
 *
 * A pos context is initially created with a site, pos number anda config ID.
 * initConfig () is called from the constructor to load the PosConfig,
 * based on config ID, all PosConfig relationships are loaded at that
 * time also.
 *
 *
 * @author  Quentin Olson
 */
public class PosContext {

    static Logger logger = Logger.getLogger(PosContext.class);

    private int posno;
    private int siteid;
    private int posid;
    private int configno;
    private PosConfig config;
    private PosParameters posparameters;
    private Site site;
    private Hashtable keysbyid;
    private Hashtable keysbycode;
    private StringBuffer inputline;
    private EjHeader transheader;
    private double sign;
    private double quantity;
    private boolean trainingmode;
    private Ej currej;
    private int tabindex;
    private int transtype;
    private int user;
    private int recallposno;
    private int recalltransno;
    private Currency localcurrency;
    private Customer customer;
    private PosMath posmath;
    private PosFields posfields;
    private PosEventStack eventstack;
    private Stack employee;
    private EjItem lastitem;
    private int lastitemindex;
    private Locale locale;
    private Promotion salemod;
    private Filters msrfilters;
    private Filters scannerfilters;
    private PosTotal postotal;
    private boolean locked;
    private LockThread lockthread;
    private OperPrompt operprompt;
    private Receipt receipt;
    private Vector guis;
    private int drawerno;
    private Currency altcurrency;
    private Vector components;
    
    // hardware components
	private FiscalPrinter fiscalPrinter;
	private CashDrawer cashDrawer;
	private PosPrinter posPrinter;

    /**
     *
     */

    /** POS number. */
    public int posNo() {
        return posno;
    }

    /**  Site ID. */
    public int siteID() {
        return siteid;
    }

    /**  POS ID, (db index). */
    public int posID() {
        return posid;
    }

    /** POS configuration number. */
    public int configNo() {
        return configno;
    }

    /** Configuration object. */
    public PosConfig config() {
        return config;
    }

    /** Site object. */
    public Site site() {
        return site;
    }

    /** POS parameters object. */
    public PosParameters posParameters() {
        return posparameters;
    }

    /** Maps POS keys/menu-butons by key ID. */
    public Hashtable keysByID() {
        return keysbyid;
    }

	/** Maps POS keys/menu-butons by key code. */
	public Hashtable keysByCode() {
		return keysbycode;
	}


    /** Transaction header record. */
    public EjHeader currTrans() {
        return transheader;
    }   /** The current EJ header. */
    /** Sign of the transaction, (for return sales). */
    public double sign() {
        return sign;
    }  /** Monitary amount working area. */
    /** Quantity of current item. */
    public double quantity() {
        return quantity;
    }  /** Quantity for this context. */
    /** Training mode indicator. */
    public boolean trainingMode() {
        return trainingmode;
    }  /** Training mode flag. */

    /** Thre current electronic journal. */
    public Ej currEj() {
        return currej;
    }  /** The current electronic journal structure. */
    /** The index of the current tab. */
    public int tabIndex() {
        return tabindex;
    }  /** The tab index for this context (hidden?)*/
    /** The transaction type, sale, logon.... */
    public int transType() {
        return transtype;
    } /** Numerical user ID. */
    /** The user ID. */
    public int user() {
        return user;
    } /** Numerical user ID. */
    /** The POS number indicated in the recall request. */
    public int recallPosNo() {
        return recallposno;
    } /** recall tmp */
    /** The recall transaction number in the recall request.  */
    public int recallTransNo() {
        return recalltransno;
    } /** recall tmp */
    /** Local currency object. */
    public Currency localCurrency() {
        return localcurrency;
    }

    /** The current customer. */
    public Customer customer() {
        return customer;
    }

    /** The math object. */
    public PosMath posMath() {
        return posmath;
    }

    /** The POS fields object. */
    public PosFields posFields() {
        return posfields;
    }

    /** The last item in the EJ. */
    public EjItem lastItem() {
        return lastitem;
    }

    /** The last item index in the EJ. */
    public int lastItemIndex() {
        return lastitemindex;
    }

    /** The locale for this context. */
    public Locale locale() {
        return locale;
    }

    /** The sale level promotion object (employee sale). */
    public Promotion saleMod() {
        return salemod;
    }

    /** Mag stripe filters. */
    public Filters msrFilters() {
        return msrfilters;
    }

    /** Scanner filters. */
    public Filters scannerFilters() {
        return scannerfilters;
    }

    /** Is the context locked? (keys locked) */
    public boolean locked() {
        return locked;
    }

    /** The lock thread object. */
    public LockThread lockThread() {
        return lockthread;
    }

    /** The event stack for this context. */
    public PosEventStack eventStack() {
        return eventstack;
    }

    /** Operator prompt displays */
    public OperPrompt operPrompt() {
        return operprompt;
    }

    /** Receipt displays */
    public Receipt receipt() {
        return receipt;
    }

    /** Graphical guis */
    public Vector guis() {
        return guis;
    }

    /** Cashier drawer number */
    public int drawerNo() {
        return drawerno;
    }

    /** Alternate currency record */
    public Currency altCurrency() {
        return altcurrency;
    }

    public Vector components() {
        return components;
    }

	/** Fiscal Printer */
	public FiscalPrinter fiscalPrinter() {
		return fiscalPrinter;
	}

	/** Cash Drawer*/
	public CashDrawer cashDrawer() {
		return cashDrawer;
	}

	/** POS Printer */
	public PosPrinter posPrinter() {
		return posPrinter;
	}

    /**
     *
     */

    /**  */
    public void setPosNo(int value) {
        posno = value;
    }

    /**  */
    public void setSiteID(int value) {
        siteid = value;
    }

    /**  */
    public void setPosID(int value) {
        posid = value;
    }

    /**  */
    public void setConfigNo(int value) {
        configno = value;
    }

    /**  */
    public void setSite(Site value) {
        site = value;
    }

    /**  */
    public void setConfig(PosConfig value) {
        config = value;
    }

    /**  */
    public void setPosParameters(PosParameters value) {
        posparameters = value;
    }


    /**  */
    public void setCurrEj(Ej value) {
        currej = value;
    }

    /**  */
    public void setCurrTrans(EjHeader value) {
        transheader = value;
    }

    /**  */
    public void setSign(double value) {
        sign = value;
    }

    /**  */
	public void setQuantity(int value) {
		quantity = value;
	}

	public void setQuantity(double value) {
		quantity = value;
	}

    /**  */
    public void setTrainingMode(boolean value) {
        trainingmode = value;
    }

    /**  */
    public void setTabIndex(int value) {
        tabindex = value;
    }

    /**  */
    public void setTransType(int value) {
        transtype = value;
    }

    /**  */
    public void setUser(int value) {
        user = value;
    }

    /**  */
    public void setRecallPosNo(int value) {
        recallposno = value;
    }

    /**  */
    public void setRecallTransNo(int value) {
        recalltransno = value;
    }

    /**  */
    public void setLocalCurrency(Currency value) {
        localcurrency = value;
    }

    /**  */
    public void setCustomer(Customer value) {
        customer = value;
    }

    /**  */
    public void setLastItem(EjItem value) {
        lastitem = value;
    }

    /**  */
    public void setLastItemIndex(int value) {
        lastitemindex = value;
    }

    /**  */
    public void setSaleMod(Promotion value) {
        salemod = value;
    }

    /**
	 * Applies sale mod. to the existing items in current 
	 * transaction
	 */
	public void applySaleMod() {

		
		for (int ejIndex = 0; ejIndex < currEj().size(); ejIndex++) {

			EjLine line = (EjLine) currEj().elementAt(ejIndex);
			EjPromotion promo = new EjPromotion(this, saleMod());

			switch (line.lineType()) {

				case EjLine.ITEM :

					TransItem transItem = (TransItem) line.dataRecord();
					if (transItem.state() != TransItem.VOID) {
						promo.applyToItem((EjItem) line);
					}
					break;

				case EjLine.ITEM_LINK :
					TransItemLink itemLink =
						(TransItemLink) line.dataRecord();
						
					if (itemLink.amount() > 0) 
						promo.applyToItem((EjItem) line);
					break;

					//				  case EjLine.TAX :
					//					  TransTax itemTax = (TransTax) line.dataRecord();
					//					  TODO what to do with tax, apply promotion?
					//					  break;

				default :
					}
		}
		
		// update receipt
		receipt().clear();
		for (int i = 0; i < currEj().size(); i++) {
			EjLine line = (EjLine)currEj().elementAt(i);
			receipt().update(line);
		}
		
	}

	/**  */
    public void setMsrFilters(Filters value) {
        msrfilters = value;
    }

    /**  */
    public void setScannerFilters(Filters value) {
        scannerfilters = value;
    }

    /**  */
    public void setPosTotal(PosTotal value) {
        postotal = value;
    }

    /**  */
    public void setLocked(boolean value) {
        locked = value;
    }

    /**  */
    public void setLockThread(LockThread value) {
        lockthread = value;
    }

    /**  */
    public void setOperPrompt(OperPrompt value) {
        operprompt = value;
    }

    /**  */
    public void setReceipt(Receipt value) {
        receipt = value;
    }

    /**  */
    public void setDrawerNo(int value) {
        drawerno = value;
    }

    /** Set the currency record. */
    public void setAltCurrency(Currency value) {
        altcurrency = value;
    }

	public void setFiscalPrinter(FiscalPrinter value) {
		fiscalPrinter = value;
	}

	public void setPosPrinter(PosPrinter value) {
		posPrinter = value;
	}

	public void setCashDrawer(CashDrawer value) {
		cashDrawer = value;
	}

    /**
     * PosConext constructors.
     */

    /**
     * Default constructor.
     */
    public PosContext() {
    }

    /**
     * Constructor PosContext with the site ID, the POS number and the
     * PosConfig ID.
     */
    public PosContext(int sid, int pid, int p, int c) {

        setSiteID(sid);
        setPosID(pid);
        setPosNo(p);
        setConfigNo(c);

        inputline = new StringBuffer();
        posmath = new SimpleMath();
        posfields = new SimpleFields();
        eventstack = new PosEventStack();
        employee = new Stack();
        components = new Vector();

        if (initConfig()) {

            setCurrEj(new Ej(this));
        } else {

            logger.fatal("POS configuration not found");
            System.exit(0);
        }

        setLocked(false);
        if (posParameters().getBoolean("LockTerminal")) {
            setLockThread(new LockThread(this));
            lockThread().start();
        }

    }

    // *** Main initializtion, transaction start and finish methods ***

    /**
     * Configuration and pos parameter initialization. Note: these are
     * for all logon contexts.
     */
    private boolean initConfig() {

        // read the config.

        String fetchSpec = PosConfig.getByNo(configNo());
        Vector tmp = Application.dbConnection().fetch(new PosConfig(), fetchSpec);

        if (tmp.size() == 1) {

            setConfig((PosConfig) tmp.elementAt(0));

            // create two hashes used by keypad devices.
            // load them with KeyEvents created from PosKey
            // and this context.

            keysbyid = new Hashtable();
            keysbycode = new Hashtable();
            guis = new Vector();

            // Get the receipt and prompt devices

            setReceipt(new Receipt());
            setOperPrompt(new OperPrompt());
            setCashDrawer (new CashDrawer(new jpos.CashDrawer(), "CashDrawer"));
			setFiscalPrinter(new FiscalPrinter(new jpos.FiscalPrinter(), "FiscalPrinter",this));
			setPosPrinter ( new PosPrinter ( new jpos.POSPrinter(), "POSPrinter", this));

            for (int i = 0; i < config().containers().size(); i++) {

                MenuRoot container = (MenuRoot) config().containers().elementAt(i);

                for (int j = 0; j < container.subMenus().size(); j++) {

                    SubMenu submenu = (SubMenu) container.subMenus().elementAt(j);

					loadButtons(submenu.buttons());
                }
            }
        } else {
            logger.fatal("Pos configuration not found");
            return false;
        }

        fetchSpec = Site.getByID(siteID());
        tmp = Application.dbConnection().fetch(new Site(), fetchSpec);

        if (tmp.size() == 1) {
            setSite((Site) tmp.elementAt(0));
        } else {
            logger.fatal("Site record not found");
            return false;
        }

        // read the pos paramters

        setPosParameters(new PosParameters(configNo()));
        locale = Application.locale();

        setMsrFilters(new Filters(InputFilter.MSR));
        setScannerFilters(new Filters(InputFilter.EAN_UPC));

        return true;
    }

	private void loadButtons(Vector buttons) {
		if (buttons == null) return;
		
		for (int k = 0; k < buttons.size(); k++) {
		
		    PosKey key = (PosKey) buttons.elementAt(k);
		
		    if (key.deviceType() == PosKey.POS_KEY) {
				
		        KeyEvent keyEvent = new KeyEvent(key, this);
		        if (key.keyID() > 0) {
		            keysByID().put(new Integer(key.keyID()), keyEvent);
		        }
		        if (key.keyCode() > 0) {
		            keysByCode().put(new Integer(key.keyCode()), keyEvent);
		        }
		    }
		}
	}

    /**
     * Starts the pos operations after system startup (i.e. sets up the event loop.)
     * Should only be called once.
     */
    public void hardwarePosInit() {

        // Get the receipt and prompt devices

        setReceipt(new Receipt());
        setOperPrompt(new OperPrompt());

        // these classes will create and load these
        // so just get rid of them, they are the gui
        // displays, a little kludgey eh?

        receipt().setOperDisplay(null);
        operPrompt().setPrompt(null);

        // start the logon dialog

        eventStack().loadDialog("LogOn", this);
        eventStack().nextEvent();
    }

    /**
     * Starts the pos operations after system startup.
     * Should only be called once.
     */
    public void posInit() {

        // start the logon dialog

        eventStack().loadDialog("Open", this);
        eventStack().nextEvent();
    }

    /**
     * Home all of the guis
     */
    public void homeGuis() {

        for (int i = 0; i < guis().size(); i++) {
            PosGui gui = (PosGui) guis.elementAt(i);
            gui.home();
        }
    }

    /**
     * Clear all of the guis
     */
    public void clearGuis() {
        for (int i = 0; i < guis().size(); i++) {
            PosGui gui = (PosGui) guis.elementAt(i);
            gui.clear();
        }
    }

    /**
     * method to update the customer in the base transaction record.
     *
     */
    public void updateCustomer(String cust) {

        currEj().transHeader().updateCustomer(cust);
    }

    /** simple utility methods **/

    public int transID() {
        return currTrans().transID();
    }

    /** Short cut to the current transaction ID. */
    public int transNo() {
        return currTrans().transNo();
    }  /** Short cut to the current transaction number. */

    /**
     * Toggle the sign for the sale, (return sale...).
     */
    public void toggleSign() {
        sign = sign * -1.0;
    }

    /**
     * Toggle training mode status.
     */
    public void toggleTrainingMode() {
        trainingmode = !trainingmode;
    }

    /**
     * Converts the input buffer to an int.
     */
    public int input() {

        int val = 0;
        if (inputline.length() == 0)
            return 0;
        try {
            val = new Integer(inputline.toString()).intValue();
        } catch (NumberFormatException e) {
            logger.warn("Invalid number in input Format" + inputline.toString(), e);
            this.eventStack().pushEvent(new PosError(this, PosError.INVALID_INPUT));
            operPrompt().update(eventStack().event());
            receipt().update(eventStack().event());
            clearInput();
            val = -1;
        }
        return val;
    }

    /**
     * Converts the input buffer to a double.
     */
    public double inputDouble() {
        if (inputline.length() == 0)
            return 0;
        return new Double(inputline.toString()).doubleValue();
    }

    /**
     * Returns the input buffer as a String.
     */
    public String inputLine() {
//        if (inputline.length() == 0)
//            inputline.append("0");
        return inputline.toString();
    }

    /**
     * Erase the last character in the input buffer (clear ()).
     */
    public void eraseLast() {
        inputline.deleteCharAt(inputline.length() - 1);
    }

    /**
     * Clears the input buffer.
     */
    public void clearInput() {
        if (inputline.length() > 0)
            inputline.delete(0, inputline.length());
    }

    /**
     * Multiplies the input buffer by the value.
     */
    public void multByInput(int i) {
        int tmp;
        if (inputline.length() == 0)
            tmp = 0;
        else
            tmp = input();
        tmp *= i;
        if (inputline.length() > 0)
            inputline.delete(0, inputline.length());
        inputline.append(Integer.toString(tmp));
    }

	/**
	 * Add the value to the end of the input buffer as a
	 * string.
	 */
	public void addToInput(int i) {
		inputline.append(Integer.toString(i));
	}

	/**
	 * Add the value to the end of the input buffer as a
	 * string.
	 */
	public void addToInput(char c) {
		inputline.append(c);
	}

	/**
	 * Add the value to the end of the input buffer as a
	 * string.
	 */
	public void addToInput(String s) {
		inputline.append(s);
	}

    /**
     * Employee stack maintains the current employee. Typically the stack
     * is one deep, but when manager intervention is used, the manager
     * is pushed on the stack for one PosEvent, the popped.
     */
    public Employee employee() {
        if (employee.size() > 0) {
            return (Employee) employee.peek();
        } else {
            return null;
        }
    }

    /**
     * Pop the top employee, used for manager intervention.
     */
    public Employee popEmployee() {

        if (employee.size() > 1) {
            employee.pop();
            Employee e = (Employee) employee();
            setUser(e.logonNo());
            return e;
        } else {
            Employee e = (Employee) employee();
            return e;
        }
    }

    /**
     * Set/initialize the top employee on the stack.
     */
    public void pushEmployee(Employee value) {
        setUser(value.logonNo());
        employee.push(value);
    }

    /**
     * Enable all keys.
     */
    public void enableKeys() {

        for (Enumeration enum = keysByID().elements(); enum.hasMoreElements();) {
            KeyEvent k = (KeyEvent) enum.nextElement();
            if (k.enabler() != null)
                k.enabler().keyEnable();
        }
    }

    /**
     * Disable keys that should disable, note number, enter, clear are left alone
     * since they are requrired for logon.
     */
    public void disableKeys() {

        for (Enumeration enum = keysByID().elements(); enum.hasMoreElements();) {
            KeyEvent k = (KeyEvent) enum.nextElement();

            if (k.config().logoutDisable()) {
                if (k.enabler() != null)
                    k.enabler().keyDisable();
            }
        }
    }


    /**
     * Get the current user name.
     */
    public String userName() {

        if (employee() == null) {
            return "login";
        }
        return employee().firstName() + " " + employee().lastName();
    }

    /**
     *Returns the current logged on user.
     */
    public String toString() {
        if (employee() == null) {
            return "logon";
        }
        return (employee().firstName() + " " + employee().lastName());
    }

    public void close() {

        inputline = null;
        posmath = null;
        posfields = null;
        eventstack = null;
        employee = null;
        setCurrEj(null);
        setLockThread(null);

    }
}


