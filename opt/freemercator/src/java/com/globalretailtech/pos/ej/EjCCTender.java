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


import com.globalretailtech.util.Application;
import com.globalretailtech.data.DBRecord;
import com.globalretailtech.data.TransTender;
import com.globalretailtech.data.Total;
import com.globalretailtech.pos.operators.*;
import com.globalretailtech.pos.events.*;
import org.apache.log4j.Logger;

/**
 * Process a credit card tender event. Set up through
 * configured dialog. Can process cash back credit card
 * number and expiration. Can take input from MSR via filters
 * or operator input. Handles split tender amounts.
 *
 * @author  Quentin Olson
 * @see Filter
 */
public class EjCCTender extends EjLine {

    static Logger logger = Logger.getLogger(EjCCTender.class);

    /** Credit card tender base event */
    public static final int CC_TENDER = 0;
    /** Prompt for cash back */
    public static final int ENTER_CASH_BACK_AMOUNT = 1;
    /** Prompt for credit card number */
    public static final int ENTER_CC_NO = 2;
    /** Prompt for credit card expiration date */
    public static final int ENTER_CC_EXPR = 3;
    /** Get cash back amount from context */
    public static final int GET_CASH_BACK_AMOUNT = 4;
    /** Get credit card number from context */
    public static final int GET_CC_NO = 5;
    /** Get credit card expiration date from context */
    public static final int GET_CC_EXPR = 6;
    /** Finalize credit card transaction */
    public static final int TENDER_FINAL = 7;

    private double splitamount;
    private double cashback;
    private String ccnumber;
    private String exprdate;
    private String firstname;
    private String surname;
    private String mi;
    private String data;
    private TransTender transtender;
    private String prompttext;

    /** Split tender amount. */
    public double splitAmount() {
        return splitamount;
    }

    /** Cash back amount. */
    public double cashBack() {
        return cashback;
    }

    /** Credit card number. */
    public String ccNumber() {
        return ccnumber;
    }

    /** Credit card expiration date. */
    public String exprDate() {
        return exprdate;
    }

    /** Cardholder first name. */
    public String firstName() {
        return firstname;
    }

    /** Cardholder sur (last) name. */
    public String surName() {
        return surname;
    }

    /** Cardholder middle initial. */
    public String mi() {
        return mi;
    }

    /** Card data. */
    public String data() {
        return data;
    }

    /** Tender record. */
    public TransTender transTender() {
        return transtender;
    }

    /** Display prompt text */
    public String promptText() {
        return prompttext;
    }

    /** Set split amount. */
    public void setSplitAmount(double value) {
        splitamount = value;
    }

    /** Set cash back amount. */
    public void setCashBack(double value) {
        cashback = value;
    }

    /** Set CC number. */
    public void setCcNumber(String value) {
        ccnumber = value;
    }

    /** Set CC expiration date. */
    public void setExprDate(String value) {
        exprdate = value;
    }

    /** Set first name. */
    public void setFirstName(String value) {
        firstname = value;
    }

    /** Set sur name. */
    public void setSurName(String value) {
        surname = value;
    }

    /** Set middle initial. */
    public void setMi(String value) {
        mi = value;
    }

    /** Set data. */
    public void setData(String value) {
        data = value;
    }

    /** Set transaction record. */
    public void setTransTender(TransTender value) {
        transtender = value;
    }

    /** Set prompt text. */
    public void setPromptText(String value) {
        prompttext = value;
    }

    /** Simple constructor, call init (). */
    public EjCCTender() {
        init();
    }

    /** Constructor, call init () and sets trans record,
     * used in reprints.
     */
    public EjCCTender(TransTender t) {
        init();
        setTransTender(t);
    }

    /** Private initialization method. */
    private void init() {
        setLineType(EjLine.CC_TENDER);
        setSplitAmount(0.0);
        setCashBack(0.0);
        setCcNumber(null);
        setExprDate(null);
        initTransition();
    }

    /** Clone the object */
    public Object clone() {

        EjCCTender tmp = new EjCCTender();

        tmp.setLineType(lineType());
        // 		tmp.setEventType (eventType ());
        tmp.setContext(context());
        // 		tmp.setValidEvents (validEvents ());
        tmp.setSplitAmount(splitAmount());
        tmp.setCashBack(cashBack());
        tmp.setCcNumber(ccNumber());
        tmp.setExprDate(exprDate());
        tmp.setTransTender(transTender());

        return tmp;
    }

    /**
     * Gather input and create Credit Card tender record, TransTender.
     * MSR filters are applied along with check digit.
     */
    public void engage(int value) {

        Hashtable results = new Hashtable();

        switch (state()) {

            case ENTER_CASH_BACK_AMOUNT:  // prompts for cash back amount

                context().clearInput();
                setPromptText(context().posParameters().getString("PromptCashBack"));
                context().operPrompt().update(this);
                states().pushState(GET_CASH_BACK_AMOUNT);
                break;

            case ENTER_CC_NO:          // prompts for cc number

                context().clearInput();
                setPromptText(context().posParameters().getString("PromptCCNo"));
                context().operPrompt().update(this);
                pushUserEvent(GET_CC_NO);
                states().pushState(GET_CC_NO);
                break;

            case ENTER_CC_EXPR:        // prompt for cc expiration date

                context().clearInput();
                setPromptText(context().posParameters().getString("PromptCCExpr"));
                context().operPrompt().update(this);
                states().pushState(GET_CC_EXPR);
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

            case GET_CC_NO:  // get the CC number and save it

                setCcNumber(new String(context().inputLine()));

                for (int i = 0; i < context().msrFilters().size(); i++) {


                    Filter f = context().msrFilters().getFilter(i);

                    // If a filter matched deterimine what it is and
                    // extract the data
// TODO repair this block, it is broken because of new filter behavior. Igor.
//                    if (f.apply(ccNumber(), results) == Filter.CHECKDIGIT_FAILED) {
//
//                        logger.warn("CC check digit failed " + ccNumber());
//
//                        // if the card is invalid, get all the way out of credit card and post
//                        // an invalid credit card message, they'll have to pay some other way.
//
//                        context().clearInput();
//                        context().eventStack().clearPending();
//                        context().eventStack().pushEvent(new PosError(context(), PosError.INVALID_CC));
//                        context().operPrompt().update((PosError) context().eventStack().event());
//                        return;
//                    }

                }

                // see if anything matched

                if (results.get(Filter.ACCT_NO) == null) {

                    logger.warn("Invalid CC number " + ccNumber());

                    // same as above, except no fields were picked up.

                    context().clearInput();
                    context().eventStack().clearPending();
                    context().eventStack().pushEvent(new PosError(context(), PosError.INVALID_CC));
                    context().operPrompt().update((PosError) context().eventStack().event());
                    return;
                } else {
                    popState();
                }

                context().clearInput();
                context().eventStack().nextEvent();
                break;

            case GET_CC_EXPR:  // this completes the transaction

                setExprDate(new String(context().inputLine()));
                context().clearInput();
                if (ccExprIsValid()) {
                    popState();
                } else {
                }
                context().clearInput();
                context().eventStack().nextEvent();
                break;

            case TENDER_FINAL:  // this completes the transaction

                popState();
                setTransTender(new TransTender());

                transTender().setTenderDesc(context().posParameters().getString("CreditCard"));
                transTender().setDataCapture(ccNumber() + " " + exprDate());

                // logic for cash back, assume amount () is either 0 or the amount, add it regardless
                // if it's 0 charge the full amount

                if (splitAmount() > 0.0) {
                    transTender().setTenderAmount(splitAmount() + cashBack());
                } else {
                    // add cash back into amount
                    transTender().setTenderAmount(context().posMath().add(cashBack(), context().currEj().ejTotal()));
                }

                transTender().setChange(cashBack());
                transTender().setChangeDesc(context().posParameters().getString("CashBack"));
                transTender().setLocaleLanguage(Application.localeLanguage());
                transTender().setLocaleVariant(Application.localeVariant());

                context().currEj().ejAdd(this);

                if (context().currEj().ejBalance() > 0.0) {
                    transTender().setChangeDesc(context().posParameters().getString("BalanceDue"));
                    setPromptText(transTender().changeDesc());
                }

                context().operPrompt().update(this);
                context().receipt().update(this);
                context().eventStack().nextEvent();
                break;

            default:
                break;
        }
        return;
    }

    /**
     * Validate cash back value.
     */
    private boolean cashBackIsValid() {
        return true;
    }

    /**
     * Validate Credit Card expiration date.
     */
    private boolean ccExprIsValid() {
        return true;
    }

    // Abstract implementations, PosEvent

    private Hashtable transistions;

    private void initTransition() {
        transistions = new Hashtable();
        /** set up transistion evenst here */
        transistions.put(FirstItem.eventName(), new Boolean(true));
    }

    /** Detects valid transitions for this event */
    public boolean validTransition(String event) {
        if (transistions.get(event) != null)
            return true;
        else
            return false;
    }

    /** Clear (key press) behaviour for this class. */
    public void clear() {
    }

    private static String eventname = "EjCCTender";

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
        return transTender().tenderAmount();
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
        return transTender().change();
    }

    /** Display prompt. */
    public String prompt() {
        return desc();
    }

    /** Display customer. */
    public String cust() {
        return desc();
    }

    /** Display description. */
    public String desc() {
        return transTender().tenderDesc();
    }

    /** Save the trans record */
    public boolean save() {

        if (!context().trainingMode()) {
            transTender().save();
        }
        return true;
    }

    /** The line nubmer */
    public int lineNo() {
        return transTender().seqNo();
    }

    /** The data record */
    public DBRecord dataRecord() {
        return transTender();
    }

    /**
     * Upate the credit card total, use card type
     * as offset for database key, subtract the
     * cash back amount from cash totals.
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
                Total.CREDIT_CARD_BASE,
                sign * amount());

        if (transTender().change() > 0) {  // subtract cash back from drawer
            Total.addToTotal(context().siteID(),
                    context().posNo(),
                    sign * Total.CASH_IN_DRAWER, (context().posMath().mult(transTender().change(), -1.0)));
        }
    }
}


