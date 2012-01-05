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

import java.util.Vector;

import com.globalretailtech.util.*;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.ej.*;
import com.globalretailtech.data.Transaction;
import com.globalretailtech.pos.events.PosEvent;

/**
 * Initial state for most transactions, also used by FirstItem to
 * get the prompt text.
 *
 * @author  Quentin Olson
 * @see FirstItem
 */
public class StartTransaction extends PosEvent {

    private String promptText;

    /** The register open prompt from the context. */
    public String promptText() {
        return promptText;
    }

    /** Simple constructor */
    public StartTransaction() {
    }

    /** Constructor sets the context*/
    public StartTransaction(PosContext context) {
        setContext(context);
        promptText = context().posParameters().getString("StartTransaction");
    }

    public StartTransaction(PosContext context, String prompt) {
        setContext(context);
        promptText = prompt;
    }

    /**
     * Just update the dialogs and displays. For
     * displays like reciepts this clears.
     */
    public void engage(int value) {

		// check not printed transactions
		boolean pendingFound = findPendingPrinting();

        // clear the input buffer and ej
        context().enableKeys();

		if ( ! pendingFound ){
			context().clearInput();
			context().clearGuis();
			context().setQuantity(0);
			context().setSaleMod(null);
			context().setCustomer(null);
			context().setSign(1.0);
			context().setCurrEj(new Ej(context()));
			context().setLastItem(null);
			context().setLastItemIndex(0);

			// create a new transaction, EjHeader and put it in the ej
			context().setCurrTrans(new EjHeader(getTrans(context().transType())));
			context().currEj().ejAdd(context().currTrans());
		}

        // Execute the next event

        context().eventStack().nextEvent();

    }

    /**
	 * Searches transactions with state 'printing',
	 * if found, try to print them.
	 */
	private boolean findPendingPrinting() {
		String fetchSpec = Transaction.getByPosAndState(context().posNo(), Transaction.PRINTING);
		Vector tmp = (Application.dbConnection().fetch(new Transaction(), fetchSpec));
		
		boolean found = false;
		if (tmp.size() > 0) {  // were the transactions found?

			found = true;
			
			Transaction trans = (Transaction) tmp.elementAt(0);
			
			context().setCurrEj(Ej.getEj(trans, context()));

			if (context().currEj().size() > 0) {  // finally make sure there are records

				context().eventStack().setEvent(new FirstItem(context()));

				EjLine line;
				for (int j = 0; j < context().currEj().size(); j++) {

					line = (EjLine) context().currEj().elementAt(j);
					line.setContext(context());
					context().receipt().update(line);
				}
			}
			
			// push FinishTransaction event
			context().eventStack().clearPending();
			context().eventStack().clearProcessed();
			context().eventStack().pushEvent( new RegisterOpen() );  
			context().eventStack().pushEvent( this );  // StartTransaction
			context().eventStack().pushEvent( new FinishTransaction(context()));
			context().eventStack().pushEvent( new Pause(context()));
			context().operPrompt().update(context().posParameters().getString("PrintingTranFound"));
		}
		return found;
	}
	
	private void printPendingError() {

		context().clearInput();
		context().eventStack().clearPending();
		context().eventStack().clearProcessed();
		context().eventStack().pushEvent(new PosError(context(), PosError.RECALL_FALLED));
		context().operPrompt().update((PosError) context().eventStack().event());
	}

	/** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "StartTransaction";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }

    /**
     * Create a new trnsaction.
     * First try to get the most recent for this POS
     * if that fails create new one initialized to "1" and
     * save it. Note: most of this work is hidden in a
     * stored database procedure for security.
     */
    public Transaction getTrans(int type) {

        Transaction transaction = new Transaction();

        // initialize known stuff

        transaction.setSiteID(context().siteID());
        transaction.setPosNo(context().posNo());
        transaction.setEmpNo(context().user());
        transaction.setConfigNo(context().config().configNo());
        transaction.setDrawerNo(context().drawerNo());
        transaction.setState(Transaction.INCOMPLETE);
        transaction.setTransType(type);
        transaction.setLocaleLanguage(Application.localeLanguage());
        transaction.setLocaleVariant(Application.localeVariant());
//        if (!context().trainingMode())  // create as usual, it just will not be saved and therefore incremented
        transaction.setTransID(transaction.startTrans(context().posID()));
        transaction.setTransNo(transaction.transID());

        return transaction;
    }
}