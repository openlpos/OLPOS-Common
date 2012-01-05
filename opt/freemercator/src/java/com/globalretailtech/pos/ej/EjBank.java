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

import com.globalretailtech.util.Application;
import com.globalretailtech.data.DBRecord;
import com.globalretailtech.data.TransBank;
import com.globalretailtech.data.Total;
import com.globalretailtech.pos.context.*;

/**
 * Bank event, paid-in, paid-out, loan....
 * Bank invokes the dialog that starts this dialog
 * based on the value programmed under the key.
 *
 * @author  Quentin Olson
 * @see com.globalretailtech.pos.events.Bank
 */
public class EjBank extends EjLine {

    /** Enter paid-in amount */
    public final static int PAID_IN = 1;
    /** Enter paid out amount */
    public final static int PAID_OUT = 2;
    /** Enter loan amount */
    public final static int LOAN = 3;
    /** Enter pickup amount */
    public final static int PICKUP = 4;
    /** Prompt for amount. */
    public final static int GET_AMOUNT = 5;
    /** Complete the transaction. */
    public final static int BANK_FINAL = 6;

    private TransBank transbank;
    private int transtype;  // paid-in/paid-out
    private double transamount;
    private String desc = "Unkown payment type";
    private String prompttext;
    private double sign;

    /** Prompt text for displays. */
    public String promptText() {
        return prompttext;
    }

    /** The transaction record */
    public TransBank transBank() {
        return transbank;
    }

    /** Transaction type from TransBank */
    public int transType() {
        return transtype;
    }

    /** Transaction amount */
    public double transAmount() {
        return transamount;
    }

    /** Set prompt text. */
    public void setPromptText(String value) {
        prompttext = value;
    }

    /** Set transaction record. */
    public void setTransBank(TransBank value) {
        transbank = value;
    }

    /** Set transactin type */
    public void setTransType(int value) {
        transtype = value;
    }

    /** Set transaction amount. */
    public void setTransAmount(double value) {
        transamount = value;
    }


    /** Simple constructor, call init (). */
    public EjBank() {
        init();
    }

    /** Constructor, call init () and set context. */
    public EjBank(PosContext c) {
        init();
        setContext(c);
    }

    /** Constructor, call init () and sets trans record,
     * used in reprints.
     */
    public EjBank(TransBank t) {
        init();
        transbank = t;
    }

    /** Private initialization method. */
    private void init() {
        setLineType(BANK);
        setNumberType (AMOUNT );
        setTransAmount(0.0);
    }

    /** Clone method */
    public Object clone() {

        EjBank tmp = new EjBank();
        tmp.setTransType(transType());
        tmp.setTransBank(transBank());
        tmp.setTransAmount(transAmount());
        return tmp;
    }

    /**
     * Transaction type is passed as value, prompt for
     * amount, create and save TransBank recofd.
     */
    public void engage(int value) {

        switch (state()) {

            case PAID_IN:

                setPromptText(context().posParameters().getString("PaidInPromptAmount"));
                desc = context().posParameters().getString("PaidInAmount");
                sign = 1.0;
                context().operPrompt().update(this);
                setTransType(Total.PAID_IN);
                context().clearInput();
                states().pushState(GET_AMOUNT);
                break;

            case PAID_OUT:

                setPromptText(context().posParameters().getString("PaidOutPromptAmount"));
                desc = context().posParameters().getString("PaidOutAmount");
                sign = -1.0;
                context().operPrompt().update(this);
                setTransType(Total.PAID_OUT);
                context().clearInput();
                states().pushState(GET_AMOUNT);
                break;

            case LOAN:
                setPromptText(context().posParameters().getString("PromptAmount"));
                desc = context().posParameters().getString("LoanAmount");
                sign = -1.0;
                context().operPrompt().update(this);
                setTransType(Total.LOAN);
                context().clearInput();
                states().pushState(GET_AMOUNT);
                break;

            case PICKUP:
                setPromptText(context().posParameters().getString("PromptAmount"));
                desc = context().posParameters().getString("Pickup");
                sign = -1.0;
                context().operPrompt().update(this);
                setTransType(Total.PICK_UP);
                context().clearInput();
                states().pushState(GET_AMOUNT);
                break;


            case GET_AMOUNT: // prompts for amout tendered

                popState();
                setTransAmount(context().posMath().mult(context().inputDouble(), sign));

                if (amountIsValid()) {
                    popState();
                } else {
                }

                setTransBank(new TransBank());  // create a bank record

                transBank().setBankDesc(desc);
                transBank().setTransID(context().transID());
                transBank().setSeqNo(context().currEj().currLineNo());
                transBank().setPayType(transType());
                transBank().setDepositNo(0);
                transBank().setState(0);
                transBank().setDrawerNo(context().drawerNo());
                transBank().setLocaleLanguage(Application.localeLanguage());
                transBank().setLocaleVariant(Application.localeVariant());
                transBank().setAmount(transAmount());

				// remove all EjBank items before adding this one
				for (int i = 0; i < context().currEj().size(); i++) {
					Object o = context().currEj().elementAt(i);
					if ( o instanceof EjBank )
						context().currEj().remove(o);
				}
                context().currEj().ejAdd(this);
                context().receipt().update(this);
                context().operPrompt().update(this);
                context().eventStack().nextEvent();

                break;

            default:
                break;
        }
    }

    /**
     * Validate the validity of the input amount.
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

    private static String eventname = "EjBank";

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
        return 1;
    }

    /** Amount for this line. */
    public double amount() {
        return transBank().amount();
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
        return 0;
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
        return transBank().bankDesc();
    }

    /** Save the trans record */
    public boolean save() {

        if (!context().trainingMode()) {
            transBank().save();
        }
        return true;
    }

    /** The line nubmer */
    public int lineNo() {
        return transBank().seqNo();
    }

    /** The data record */
    public DBRecord dataRecord() {
        return transBank();
    }

	/** Upate the cash in drawer amount */
	public void updateTotals() {
		updateTotals (1);
	}	
	
	/** Rollback the cash in drawer amount */
	public void rollbackTotals() {
		updateTotals (-1);
	}
	
	/** Adds or subtracts totals*/
	private void updateTotals(int sign) {

		if (context().trainingMode())
			return;

		if (amount() != 0.0) {

			Total.addToTotal(context().siteID(),
					context().posNo(),
					transBank().payType(),
					sign * transBank().amount());

			switch (transBank().payType()) {
				case Total.PAID_IN:
				case Total.PAID_OUT:
				case Total.PICK_UP:
				case Total.LOAN:

					Total.addToTotal(context().siteID(),
							context().posNo(),
							Total.CASH_IN_DRAWER,
							sign * transBank().amount());
					break;
				default:
					break;
			}
		}
	}
}


