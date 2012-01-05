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

import com.globalretailtech.data.*;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.operators.*;

/**
 * Alternate currency event. This object is typically tied to a
 * key with the database alternate currency ID passed to the engage
 * method. It prompts for the amount of alt currency tendered, prints
 * the alt currency amount, the local currency amount and the amount
 * of change in local currency if the amount is enough to complete
 * the sale. This dialog works a little differently than others, since
 * this class is tied to a key and it looks up the currency when the
 * key is pressed we want to start off the dialog with the current
 * object.
 *
 * @author  Quentin Olson
 */
public class EjAltCurrTender extends EjLine {

    /** Prompt for amount. */
    public final static int ENTER_ALT_CURR_AMOUNT = 1;
    /** get amount. */
    public final static int GET_ALT_CURR_AMOUNT = 2;
    /** Complete the transaction. */
    public final static int TENDER_FINAL = 3;

    private TransTender transtender;
    private double oonvertamount;
    private double splitamount;
    private String prompttext;
    private String converttext;

    /** Prompt text for displays. */
    public String promptText() {
        return prompttext;
    }

    /** Text for receipt. */
    public String convertText() {
        return converttext;
    }

    /** Holds split tender amount */
    public double splitAmount() {
        return splitamount;
    }

    /** The database tender record. */
    public TransTender transTender() {
        return transtender;
    }

    /** Currency language */
    public String language() {
        return transTender().localeLanguage();
    }

    /** Currency language variant */
    public String variant() {
        return transTender().localeVariant();
    }

    public double convertAmount() {
        return oonvertamount;
    }

    /** Set prompt text. */
    public void setPromptText(String value) {
        prompttext = value;
    }

    /** Set convert text. */
    public void setConvertText(String value) {
        converttext = value;
    }

    /** Set the currency record. */
    public void setSplitAmount(double value) {
        splitamount = value;
    }

    /** Set the transaction record */
    public void setTransTender(TransTender value) {
        transtender = value;
    }

    /** Simple constructor, set the line type. */
    public EjAltCurrTender() {
        setLineType(EjLine.ALT_CURRENCY_TENDER);
    }

    /** Constructor, set the line type and context. */
    public EjAltCurrTender(PosContext c) {
        setLineType(EjLine.ALT_CURRENCY_TENDER);
        setContext(c);
    }

    /** Constructor, set the line type and trans record,
     * used in reprints.
     */
    public EjAltCurrTender(TransTender t) {
        setLineType(EjLine.ALT_CURRENCY_TENDER);
        setTransTender(t);
    }

    /** Clone method. */
    public Object clone() {

        EjAltCurrTender tmp = new EjAltCurrTender();

        tmp.setLineType(lineType());
        // 		tmp.setEventType (eventType ());
        tmp.setContext(context());
        // 		tmp.setValidEvents (validEvents ());
        tmp.setSplitAmount(splitAmount());
        tmp.setTransTender(transTender());

        return tmp;
    }

    /**
     * Get the currency record from the database from value (value is
     * configured in the key database). If the currency is found
     * start the alt currency dialog, which prompts for the amount
     * tenderd, and completes the transaction if the tendered amount
     * covers the sale, else split tender.
     */
    public void engage(int value) {

        PosMath math = context().posMath();

        switch (state()) {

            case ENTER_ALT_CURR_AMOUNT: // prompts for amount tendered

                setTransTender(new TransTender());  // create a tender record

                transTender().setLocaleLanguage(context().altCurrency().currencyCode().language());
                transTender().setLocaleVariant(context().altCurrency().currencyCode().variant());
                transTender().setTransID(context().transID());
                transTender().setSeqNo(context().currEj().currLineNo());
                transTender().setTenderType(TransTender.ALT_CURRENCY);
                transTender().setTenderDesc(context().posParameters().getString("AltCurrencyDescription"));
                transTender().setDataCapture(context().altCurrency().locale() + "," + context().altCurrency().conversionRate());
                oonvertamount = math.mult(context().currEj().ejTotal(), context().altCurrency().conversionRate());
                transTender().setTenderAmount(math.mult(context().currEj().ejTotal(), context().altCurrency().conversionRate()));

                context().clearInput();
                setPromptText(context().posParameters().getString("AltCurrTenderPrompt"));
                setConvertText(context().posParameters().getString("ConvertedAltCurrency"));
                context().operPrompt().update(this);
                context().receipt().update(this);
                states().pushState(GET_ALT_CURR_AMOUNT);

                break;

            case GET_ALT_CURR_AMOUNT:  // Pick up the amount from the input

                popState();
                setSplitAmount(context().inputDouble());

                if (amountIsValid()) {
                    popState();
                } else {
                    return;
                }

                context().clearInput();
                double amount = splitAmount();
                double localAmount = math.div(amount, context().altCurrency().conversionRate());

                transTender().setTenderAmount(localAmount);
                transTender().setChange(math.sub(localAmount, context().currEj().ejTotal()));
                transTender().setChangeDesc(context().posParameters().getString("Change"));

                // save the transaction

                context().receipt().update(this);
                context().currEj().ejAdd(this);
                context().eventStack().nextEvent();
                break;
        }


    }

    /**
     * Validate alternate currency amount.
     */
    private boolean amountIsValid() {
        return true;
    }


    // Abstract implementations, PosEvent

    /** Validate transistions state. */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "EjAltCurrTender";

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

    /** Save the transaction record */
    public boolean save() {

        if (!context().trainingMode()) {
            transTender().save();
        }
        return true;
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

    /** The line nubmer */
    public int lineNo() {
        return transTender().seqNo();
    }

    /** The data record */
    public DBRecord dataRecord() {
        return transTender();
    }

	/**
	 * Update the alt currncy totals record,
	 * note: the currency ID is used to tag
	 * the record with a type. That means if
	 * the ID changes, then the totals will no
	 * longer be valid, unless all records are
	 * replaced first.
	 */
	public void updateTotals() {
		updateTotals (1);
	}	

	/**
	 * Rollbacks total updates 
	 **/
	public void rollbackTotals() {
		updateTotals (-11);
	}	

	private void updateTotals(int sign) {

        if (context().trainingMode())
            return;

        Total.addToTotal(context().siteID(),
                context().posNo(),
                Total.ALT_CURRENCY_BASE + context().altCurrency().currencyCodeID(),
                sign * transTender().tenderAmount());

        if (transTender().change() != 0.0) {
            Total.addToTotal(context().siteID(),
                    context().posNo(),
                    Total.CASH_IN_DRAWER,
					sign * context().posMath().mult(transTender().change(), -1));
        }
    }
}


