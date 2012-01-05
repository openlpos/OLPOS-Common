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

package com.globalretailtech.pos.events;


import jpos.FiscalPrinterConst;
import jpos.JposConst;
import jpos.JposException;

import com.globalretailtech.pos.context.*;


/**
 * PosEvent for handling errors. Typically another
 * PosEvent will encounter an error condition, create
 * this object with the event type and push it on the
 * stack. The clear key clears it. This mainly provides
 * user feedback. Catalog all POS errors here
 *
 * @author  Quentin Olson
 */
public class PosError extends PosEvent {

    /** Base event */
    public static final int ERROR_EVENT = 1000;
    /** Bad input, or invalid state transition. */
    public static final int INVALID_INPUT = 1001;
    /** Insufficient funds for current operation. */
    public static final int INSUFFICIENT_FUNDS = 1004;
    /** Manager required for this operation */
    public static final int MGR_REQUIRED = 1005;
    /**  */
    public static final int LOGON_FAILED = 1006;
    /**  */
    public static final int BAD_USER = 1007;
    /**  */
    public static final int BAD_PASSWORD = 1008;
    /**  */
    public static final int INVALID_DRAWER_NO = 1009;
    /**  */
    public static final int INVALID_CUSTOMER_NO = 1010;
    /**  */
    public static final int INVALID_CHECK_NO = 1011;
    /**  */
    public static final int RECALL_FALLED = 1012;
    /**  */
    public static final int OVER_MEDIA_LIMIT = 1013;
    /**  */
    public static final int UNDER_MEDIA_LIMIT = 1014;
    /**  */
    public static final int INVALID_CC = 1015;
    /**  */
    public static final int LOCKED = 1016;
	/**  */
	public static final int CASH_EXCEEDED = 1017;
	/**  attached device exception */
	public static final int JPOS_EXCEPTION = 1018;
	/**  item not found */
	public static final int ITEM_NOT_FOUND = 1019;
	/** */
	public static final int POS_EXCEPTION = 1020;
	/** */
	public static final int ITEM_LOCKED = 1021;

    private int errorcode;
    private String prompttext;
    private String descr;

    /** Error code */
    public int errorCode() {
        return errorcode;
    }

    /** User feedback prompt. */
    public String promptText() {
        return prompttext + 
        	((descr != null && descr.length()>0)?"["+descr+"]":"");
    }

    /** Simple constructor */
    public PosError() {
    }

	/**
	 * Constructor with JPosException. Depends on
	 * provided exception's description. 
	 * Represents attached device error.
	 */
	public PosError(PosContext context, JposException e) {
		setContext(context);
		errorcode = JPOS_EXCEPTION;
		// recognize some error codes
		prompttext = decodeJposException(e);
		if (prompttext == null )
			prompttext = e.getMessage();
		else
			prompttext = prompttext+":"+e.getMessage();	
	}

	/**
	 * Constructor with PosException. Depends on
	 * provided exception's description. 
	 * Represents attached device error.
	 */
	public PosError(PosContext context, PosException e) {
		setContext(context);
		errorcode = POS_EXCEPTION;
		// recognize some error codes
		prompttext = e.getMessage();
	}

	/**
	 * Constructor with error code. Depending on
	 * code (switch) set up promptText ().
	 */
	public PosError(PosContext context, int code) {

        setContext(context);
        errorcode = code;

        switch (errorcode) {
            case INVALID_INPUT:
                prompttext = context().posParameters().getString("InvalidInput");
                break;
            case INSUFFICIENT_FUNDS:
                prompttext = context().posParameters().getString("InsufficientFunds");
                break;
            case MGR_REQUIRED:
                prompttext = context().posParameters().getString("MgrIntervention");
                break;
            case LOGON_FAILED:
                prompttext = context().posParameters().getString("");
                break;
            case BAD_USER:
                prompttext = context().posParameters().getString("BadName");
                break;
            case BAD_PASSWORD:
                prompttext = context().posParameters().getString("BadPass");
                break;
            case INVALID_DRAWER_NO:
            case INVALID_CUSTOMER_NO:
            case INVALID_CHECK_NO:
            case RECALL_FALLED:
                prompttext = context().posParameters().getString("");
                break;
            case CASH_EXCEEDED:
                prompttext = context().posParameters().getString("CashLimitExceeded");
                break;
            case OVER_MEDIA_LIMIT:
            case UNDER_MEDIA_LIMIT:
            case INVALID_CC:
            case LOCKED:
            	break;
			case ITEM_NOT_FOUND:
				prompttext = context().posParameters().getString("ItemNotFound");
				break;
			case ITEM_LOCKED:
				prompttext = context().posParameters().getString("ItemLocked");
				break;
            default:
                prompttext = " -- unknown error --";
                break;
        }
    }

    /**
     * Do nothing, but enable keys
     */
    public void engage(int value) {
    	context().enableKeys();
        context().eventStack().nextEvent();
    }

    /** Always return true. */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear impementation for clear, do nothing. */
    public void clear() {
    }

    /** Always return true. */
    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "PosError";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
	/**
	 * Recognize some JposException codes and replace it with 
	 * friendly message
	 * @return Either friendly message or null
	 */
	private String decodeJposException(JposException e) {
		int code = e.getErrorCode();
		int extCode = e.getErrorCodeExtended();
		if ( (code == JposConst.JPOS_E_EXTENDED && extCode == FiscalPrinterConst.JPOS_EFPTR_JRN_EMPTY) ||
			 (code == JposConst.JPOS_E_EXTENDED && extCode == FiscalPrinterConst.JPOS_EFPTR_REC_EMPTY)
			)
			return context().posParameters().getString("ErrorNoPaper");
		else if (code == JposConst.JPOS_E_EXTENDED && extCode == FiscalPrinterConst.JPOS_EFPTR_WRONG_STATE)	
			return context().posParameters().getString("ErrorFPState");
			
		return null;
	}
	/**
	 * @param string
	 */
	public void setDescr(String string) {
		descr = string;
	}

}


