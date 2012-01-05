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

import java.util.Vector;
import java.util.Hashtable;

import com.globalretailtech.util.Application;

import com.globalretailtech.data.DBRecord;
import com.globalretailtech.data.Media;
import com.globalretailtech.data.TransTender;
import com.globalretailtech.data.Total;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.*;
import com.globalretailtech.pos.operators.*;

/**
 * Cash tender operation
 *
 * A cash tender is accepted either as an exact
 * amount, a rounded up figure or a tenderred figure.
 * Cash tenders do not go through a dialog as in checks
 * and credit card.
 *
 *
 * @author  Quentin Olson
 */
public class EjTender extends EjLine {

    private String prompttext;

    public String promptText() {
        return prompttext;
    }

    public void setPromptText(String value) {
        prompttext = value;
    }

    private TransTender transTender;

    public TransTender transTender() {
        return transTender;
    }

    public EjTender() {
        setLineType(EjLine.TENDER);
        initTransition();
    }

    public EjTender(PosContext c) {
        setLineType(EjLine.TENDER);
        setContext(c);
        initTransition();
    }

    public EjTender(TransTender t) {
        setLineType(EjLine.TENDER);
        transTender = t;
        initTransition();
    }

    public EjTender(TransTender t, Hashtable literals) {
        setLineType(EjLine.TENDER);
        transTender = t;
        initTransition();
    }

    public void engage(int value) {

        popState();

        PosMath math = context().posMath();

        // pick up the amounts and see if enough was tendered

        double tenderTotal = 0;
        double tenderAmount = 0;
        double change = 0;

        transTender = new TransTender();

        if (context().inputDouble() > 0) {  // taken from the input line
            tenderAmount = context().inputDouble() * 100; // don't push customer to press too many buttons
        } else if (value < 0) {  // exact change
            tenderAmount = context().currEj().ejTotal();
        } else if (value == 0) {  // round up, need to fix for local currency, qo???, number decimal places and lowest denomination

            tenderAmount = context().currEj().ejTotal();
        } else {  // key is programmed or entered value
            tenderAmount = value;
        }

        // compute the change

        change = math.sub(tenderAmount, context().currEj().ejTotal());

        // get the media record and check halo/lalo

        String fetchSpec = Media.getByType(getMediaType());
        Vector tmp = Application.dbConnection().fetch(new Media(), fetchSpec);

        if (tmp.size() > 0) {  // record found

            Media media = (Media) tmp.elementAt(0);

//     			if (!checkProfile (PosEvent.OVERRIDE_MEDIA_LIMIT)) {
//
//     				if (tenderAmount > media.halo ()) {
//     					context ().clearInput ();
//     					context ().startDialog ("ManagerRequired", this);
//     					return;
//     				}
//     				if (tenderAmount < media.lalo ()) {
//     					context ().clearInput ();
//     					context ().updateDialogs (this);
//     					context ().updateDisplays (this);
//     					return;
//     				}
//     			}
        }

        transTender = new TransTender();

        transTender().setTransID(context().transID());
        transTender().setSeqNo(context().currEj().currLineNo());
        transTender().setTenderAmount(tenderAmount);
        transTender().setChange(change);
        transTender().setTenderDesc(getTenderDesc());
        transTender().setChangeDesc(context().posParameters().getString("Change"));
        transTender().setLocaleLanguage(Application.localeLanguage());
        transTender().setLocaleVariant(Application.localeVariant());
        transTender().setTenderType(getTenderType());
        transTender().setDataCapture("");

        context().currEj().ejAdd(this);

        double ejBalance = context().currEj().ejBalance();
		if (ejBalance > 0.0) {
            transTender().setChangeDesc(context().posParameters().getString("BalanceDue"));
            setPromptText(transTender().changeDesc());
        }

		context().receipt().update(this);
		// open cash drawer
		context().cashDrawer().openDrawer();

		// check whether we can finish transaction
		PosEvent nextEvent = context().eventStack().event();
		if (ejBalance <= 0){ // was change >= 0
			context().eventStack().nextEvent();
		}else{
			// rollback finish operation
			context().eventStack().clearPending();
			context().eventStack().clearProcessed();
			context().clearInput();
			context().enableKeys();
		}

    }

	/**
	 * Methods to override for other types of simple tenders
	 *  
	 */

	public int getTenderType() {
		return TransTender.CASH;
	}

	public String getTenderDesc() {
		return context().posParameters().getString("CashTender");
	}

	protected int getMediaType() {
		return Media.CASH;
	}


    /**
     * Abstract implementations
     */

    /**  */
    public String prompt() {
        return desc();
    }

    /**  */
    public String cust() {
        return desc();
    }

    /**  */
    public String desc() {
        return transTender.tenderDesc();
    }

    /**
     * Abstract implementations for PosEvent
     */

    /** Validate transistions state */
    private Hashtable transistions;

    private void initTransition() {

        transistions = new Hashtable();
        transistions.put(FirstItem.eventName(), new Boolean(true));
    }

    public boolean validTransition(String event) {
        if (transistions.get(event) != null)
            return true;
        else
            return false;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "EjTender";

    public String toString() {
        return eventname;
    }

    public static String eventName() {
        return eventname;
    }

    public double amount() {
        return transTender.tenderAmount();
    }

    public double extAmount() {
        return amount();
    }

    public double taxAmount() {
        return 0;
    }

    public double change() {
        return transTender.change();
    }

    public double quantity() {
        return 0;
    }

    public boolean save() {

        if (!context().trainingMode()) {
            transTender.save();
        }
        return true;
    }

    public int lineNo() {
        return transTender.seqNo();
    }

    public DBRecord dataRecord() {
        return transTender;
    }
	
	public void updateTotals() {
		updateTotals (1);
	}

	public void rollbackTotals() {
		updateTotals (-1);
	}

    protected void updateTotals(int sign) {

        if (context().trainingMode())
            return;

        double result = sign * context().posMath().sub(amount(), change() != amount() ? change() : 0.0);

        if (result != 0.0) {
            Total.addToTotal(context().siteID(),
                    context().posNo(),
                    Total.CASH_IN_DRAWER,
                    result);
        }
    }
}


