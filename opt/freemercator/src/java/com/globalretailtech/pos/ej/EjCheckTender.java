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

package com.globalretailtech.pos.ej;

import java.util.Hashtable;

import com.globalretailtech.util.*;
import com.globalretailtech.data.*;
import com.globalretailtech.pos.events.*;
import com.globalretailtech.data.DBRecord;

/**
 * Check tender operation
 *
 * When started from a key press, it initiates a dialog
 * by pushing the pos configuration check dialog onto the
 * event stack. The user then executes the dialog to complete
 * any validation for the check. When validation is complete
 * the tender operation is completed via the TENDER_FINAL event.
 *
 * @author  Quentin Olson
 */
public class EjCheckTender extends EjLine implements Cloneable {

    /** Start dialog. */
    public final static int CHECK_TENDER = 0;
    /** Prompt for cash back amount. */
    public final static int ENTER_CASH_BACK_AMOUNT = 1;
    /** Prompt for check number. */
    public final static int ENTER_CHECK_NO = 2;
    /** Get cash back amount. */
    public final static int GET_CASH_BACK_AMOUNT = 3;
    /** Get the check number. */
    public final static int GET_CHECK_NO = 4;
    /** Complete the transaction. */
    public final static int TENDER_FINAL = 5;

    private double splitamount;
    private double cashback;
    private String checknumber;
    private TransTender transtender;
    private String prompttext;

    /** Prompt text for displays. */
    public String promptText() {
        return prompttext;
    }

    /** Split tender amount. */
    public double splitAmount() {
        return splitamount;
    }

    /** Cash back amount. */
    public double cashBack() {
        return cashback;
    }

    /** Check number. */
    public String checkNumber() {
        return checknumber;
    }

    /** Tender record. */
    public TransTender transTender() {
        return transtender;
    }

    /** Set prompt text. */
    public void setPromptText(String value) {
        prompttext = value;
    }

    /** Set split amount. */
    public void setSplitAmount(double value) {
        splitamount = value;
    }

    /** Set cash back amount. */
    public void setCashBack(double value) {
        cashback = value;
    }

    /** Set check number */
    public void setCheckNumber(String value) {
        checknumber = value;
    }

    /** Set transaction record. */
    public void setTransTender(TransTender value) {
        transtender = value;
    }

    /** Constructor sets line type and initializes
     * transition list.
     */
    public EjCheckTender() {
        setLineType(EjLine.CHECK_TENDER);
        initTransition();
    }

    /** Constructor sets line type, transaction
     * record and initializes transition list.
     */
    public EjCheckTender(TransTender t) {
        setLineType(EjLine.CHECK_TENDER);
        setTransTender(t);
        initTransition();
    }

    /** Clone method */
    public Object clone() {

        EjCheckTender tmp = new EjCheckTender();

        tmp.setLineType(lineType());
        tmp.setContext(context());
        tmp.setSplitAmount(splitAmount());
        tmp.setCashBack(cashBack());
        tmp.setCheckNumber(checkNumber());
        tmp.setTransTender(transTender());

        return tmp;
    }

    /**
     * Gather input and create check tender record.
     */
    public void engage(int value) {

        switch (state()) {

            case ENTER_CASH_BACK_AMOUNT: // prompts for cash back

                context().clearInput();
                setPromptText(context().posParameters().getString("PromptCashBack"));
//                numbertype = EjLine.AMOUNT;
                context().operPrompt().update(this);
                states().pushState(GET_CASH_BACK_AMOUNT);

                break;

            case ENTER_CHECK_NO:          // prompts for check number

                context().clearInput();
                setPromptText(context().posParameters().getString("PromptCheckNo"));
//                numbertype = EjLine.NUMBER;
                context().operPrompt().update(this);
                states().pushState(GET_CHECK_NO);

                break;

            case GET_CASH_BACK_AMOUNT:  // get the amount and save it

                popState();
                setCashBack(context().inputDouble());
                if (cashBackIsValid()) {
                    popState();
                } else {
                }
                context().clearInput();
                context().eventStack().nextEvent();

                break;

            case GET_CHECK_NO:

                popState();
                setCheckNumber(new String(context().inputLine()));
                if (checkNumberIsValid()) {
                    popState();
                } else {
                }
                context().clearInput();
                context().eventStack().nextEvent();

                break;

            case TENDER_FINAL:  // this completes the transaction


                setTransTender(new TransTender());

                // logic for cash back, assume amount () is either 0 or the amount, add it regardless

                if (splitAmount() > 0.0) {
                    transTender().setTenderAmount(splitAmount() + cashBack());
                } else {
                    transTender().setTenderAmount(context().posMath().add(cashBack(), context().currEj().ejTotal()));  // amount was requested cash back
                }


                transTender().setTransID(context().transID());
                transTender().setTenderDesc(context().posParameters().getString("CheckTender"));
                transTender().setChange(cashBack());
                transTender().setChangeDesc(context().posParameters().getString("CashBack"));
                transTender().setTenderType(TransTender.CHECK);
                transTender().setDataCapture(checkNumber());
                transTender().setLocaleLanguage(Application.localeLanguage());
                transTender().setLocaleVariant(Application.localeVariant());

                context().currEj().ejAdd(this);
                context().receipt().update(this);
                context().operPrompt().update(this);

                // wait till after display updates to pop the state
                // because some of the display code depends on the current state

                popState();
                context().eventStack().nextEvent();

                break;

            default:
                break;

        }
    }

    /**
     * Validate cash back value.
     */
    private boolean cashBackIsValid() {
        return true;
    }

    /**
     * Validate check number Format.
     */
    private boolean checkNumberIsValid() {
        return true;
    }

    /**
     * For future use.
     */
    private boolean validateCheckNo(String check_no) {
        return true;
    }

    // Abstract implementations, PosEvent

    // transistion setup
    private Hashtable transistions;

    private void initTransition() {
        transistions = new Hashtable();
        transistions.put(FirstItem.eventName(), new Boolean(true));
    }

    /** Validate transistions state. */
    public boolean validTransition(String event) {
        if (transistions.get(event) != null)
            return true;
        else
            return false;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "EjCheckTender";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }

    // Abstract implementations, EjLine

    /** Quantitiy for this line. */
    public double quantity() {
        return 0;
    }

    /** Amount for this line. */
    public double amount() {
        return transtender.tenderAmount();
    }

    /** Extended amount (quantity * amount). */
    public double extAmount() {
        return amount();
    }

    /** Taxable amount. */
    public double taxAmount() {
        return 0;
    }

    /** Chage for this transaction. */
    public double change() {
        return transtender.change();
    }

    /** Save the trans record */
    public boolean save() {

        if (!context().trainingMode()) {
            transtender.save();
        }
        return true;
    }

    /** Display prompt. */
    public String prompt() {
        return promptText();
    }

    /** Display customer. */
    public String cust() {
        return desc();
    }

    /** Display description. */
    public String desc() {
        return transTender().tenderDesc();
    }

    /** The line nubmer */
    public int lineNo() {
        return transtender.seqNo();
    }

    /** The data record */
    public DBRecord dataRecord() {
        return transtender;
    }

	/**
	 * Update the check total and subtract cash back
	 * from cash total.
	 */
	public void updateTotals() {
		updateTotals (1);
	}
	/**
	 * Rollbacks totals update
	 */
	public void rollbackTotals(){
		updateTotals (-1);
	}
    
    private void updateTotals(int sign) {

        if (context().trainingMode())
            return;

        Total.addToTotal(context().siteID(),
                context().posNo(),
                Total.CHECK_IN_DRAWER,
                sign * amount());

        if (transtender.change() > 0) {  // subtract cash back from drawer
            Total.addToTotal(context().siteID(),
                    context().posNo(),
                    sign * Total.CASH_IN_DRAWER, (context().posMath().mult(transtender.change(), -1.0)));
        }

    }
}


