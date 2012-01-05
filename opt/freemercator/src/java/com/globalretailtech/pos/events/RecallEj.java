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
import java.util.Hashtable;

import com.globalretailtech.util.*;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.ej.*;
import com.globalretailtech.data.Transaction;
import org.apache.log4j.Logger;

/**
 * Lookup up an saved EJ. Loads the recall EJ
 * dialog and starts it.
 *
 * @author  Quentin Olson
 */
public class RecallEj extends PosDialogEvent {

    static Logger logger = Logger.getLogger(RecallEj.class);

    /** Start recall event */
    public static final int RECALL = 0;
    /** Prompt for transaction number. */
    public static final int ENTER_TRANS_NO = 1;
    /** Prompt for POS number. */
    public static final int ENTER_POS_NO = 2;
    /** Get transaction number. */
    public static final int GET_TRANS_NO = 3;
    /** Get POS number. */
    public static final int GET_POS_NO = 4;
    /** Complete recall. */
    public static final int COMPLETE_RECALL = 5;

    private int transno;
    private int posno;

    private String prompttext;

    public String promptText() {
        return prompttext;
    }

    public void setPromptText(String value) {
        prompttext = value;
    }

    public int transNo() {
        return transno;
    }

    public int posNo() {
        return posno;
    }

    public void setTransNo(int value) {
        transno = value;
    }

    public void setPosNo(int value) {
        posno = value;
    }

    public RecallEj() {
        initTransition();
    }

    public RecallEj(PosContext context) {
        setContext(context);
        initTransition();
    }

    public Object clone() {

        RecallEj tmp = new RecallEj();
        tmp.setTransNo(transNo());
        tmp.setPosNo(posNo());
        return tmp;
    }

    /**
     * If invoked as an event assume this is a
     * recall
     */
    public void engage(int value) {

        switch (state()) {

            case RECALL:

                context().eventStack().loadDialog("RecallEj", context());
                context().eventStack().nextEvent();
                break;

            case ENTER_TRANS_NO:

                context().clearInput();
                setPromptText(context().posParameters().getString("PromptTransNo"));
                context().operPrompt().update(this);
                pushUserEvent(GET_TRANS_NO);

                break;

            case ENTER_POS_NO:

                context().clearInput();
                setPromptText(context().posParameters().getString("PromptPosNo"));
                context().operPrompt().update(this);
                pushUserEvent(GET_POS_NO);

                break;

            case GET_TRANS_NO:

                setTransNo(context().input());
                context().clearInput();
                nextDialogEvent();

                break;

            case GET_POS_NO:

                setPosNo(context().input());
                context().clearInput();
                nextDialogEvent();

                break;

            case COMPLETE_RECALL:

                context().clearInput();
                String fetchSpec = Transaction.getByPosAndTrans(posNo(), transNo());
                Vector tmp = (Application.dbConnection().fetch(new Transaction(), fetchSpec));

                if (tmp.size() == 1) {  // was the transaction found?

                    Transaction trans = (Transaction) tmp.elementAt(0);

                    if (trans.state() == Transaction.SUSPEND) {  // was this a suspended transaction?

                        context().setCurrEj(Ej.getEj(trans, context()));
						EjHeader header = (EjHeader)(context().currEj().elementAt (0));
						context().setCurrTrans(header);

                        if (context().currEj().size() > 0) {  // finally make sure there are records

                            context().eventStack().setEvent(new FirstItem(context()));

                            if (!trans.updateState(Transaction.IN_PROGRESS)) {
                                logger.warn("Failed to update transaction state in EjItem.");
                                return;
                            }

							context().receipt().update(new RegisterOpen(context()));  //update display

                            EjLine line;
                            for (int i = 0; i < context().currEj().size(); i++) {

                                line = (EjLine) context().currEj().elementAt(i);
                                line.setContext(context());
                                context().receipt().update(line);
                            }
                        }
                    } else {
                        recallError();
                        return;
                    }
                } else {
                    recallError();
                    return;
                }

                break;
        }
    }

    private void recallError() {

        context().clearInput();
        context().eventStack().clearPending();
        context().eventStack().clearProcessed();
        context().eventStack().pushEvent(new PosError(context(), PosError.RECALL_FALLED));
        context().operPrompt().update((PosError) context().eventStack().event());
    }

    /** Validate transistions state */
    private Hashtable transistions;

    private void initTransition() {
        transistions = new Hashtable();
        transistions.put(RegisterOpen.eventName(), new Boolean(true));
		transistions.put(PosError.eventName(), new Boolean(true));
		transistions.put(FirstItem.eventName(), new Boolean(true)); // igor 
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

    private static String eventname = "RecallEj";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


