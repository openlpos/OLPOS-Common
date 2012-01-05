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

import com.globalretailtech.util.Application;

import com.globalretailtech.data.Employee;
import com.globalretailtech.data.Transaction;
import com.globalretailtech.pos.context.*;
import org.apache.log4j.Logger;

/**
 * Log on dialog. Allows entry of user ID and PIN, then
 * open amount and drawer assignment if set up in database.
 *
 * @author  Quentin Olson
 */
public class LogOn extends PosNumberDialog {

    static Logger logger = Logger.getLogger(LogOn.class);

    /** Prompt for user ID. */
    public static final int ENTER_USER = 0;
    /** Prompt for PIN. */
    public static final int ENTER_PASS = 1;
    /** Prompt for drawer number. */
    public static final int ENTER_DRAWER_NO = 2;
    /** Promte for opening amount. */
    public static final int ENTER_OPEN_AMOUNT = 3;
    /** Get the user ID from context input. */
    public static final int GET_USER = 4;
    /** Get the PIN from context input. */
    public static final int GET_PASS = 5;
    /** Get the open amount from context input. */
    public static final int GET_AMOUNT = 6;
    /** Get the drawer number from context input. */
    public static final int GET_DRAWER_NO = 7;
    /** Complete the logon */
    public static final int COMPLETE_LOGON = 8;
    /** Temp logon for manager override */
    public static final int TEMPORARY_LOGON = 9;
    /** Unlock the context */
    public static final int UNLOCK = 10;

    private int trys = 0;
    private Employee emp;
    private String user;
    private String pass;
    private int userno;
    private int passno;
    private int openamount;
    private int drawerno;
    private String prompttext;
    private boolean mask;

    /** Prompt text for user dialog. */
    public String promptText() {
        return prompttext;
    }

    /** Mask the displayed output */
    public boolean mask() {
        return mask;
    }

    public void setPromptText(String value) {
        prompttext = value;
    }

    /** Simple constructor for dynamic load. */
    public LogOn() {
    }

    /** Simple constructor with context. */
    public LogOn(PosContext context) {
        setContext(context);
    }

    /**
     * Implements the logon dialog given the current
     * event on the state stack. At completion sets
     * the transaction state and creates a Ej Bank record
     * if an open amount was entered. Drawer number is saved
     * in context and transaction header.
     */
    public void engage(int value) {

        mask = false;
        switch (state()) {

            case ENTER_USER:

                context().clearInput();
                setPromptText(context().posParameters().getString("EnterUser"));
                type = PosNumberDialog.CLEAR;
                context().operPrompt().update(this);
                states().pushState(GET_USER);

                break;

            case ENTER_PASS:

                mask = true;
                context().clearInput();
                setPromptText(context().posParameters().getString("EnterPass"));
                type = PosNumberDialog.MASK;
                context().operPrompt().update(this);
                states().pushState(GET_PASS);
                break;

            case ENTER_OPEN_AMOUNT:

                context().clearInput();
                setPromptText(context().posParameters().getString("PromptOpenAmount"));
                type = PosNumberDialog.CURRENCY;
                context().operPrompt().update(this);
                states().pushState(GET_AMOUNT);

                break;

            case ENTER_DRAWER_NO:

                context().clearInput();
                setPromptText(context().posParameters().getString("PromptDrawerNo"));
                type = PosNumberDialog.CLEAR;
                context().operPrompt().update(this);
                states().pushState(GET_DRAWER_NO);

                break;

            case GET_USER:

                popState();
                user = context().inputLine();
                try {
                    userno = Integer.valueOf(user).intValue();
                } catch (java.lang.NumberFormatException e) {
                    setPromptText(context().posParameters().getString("BadName"));
                    context().operPrompt().update(this);
                    return;
                }
                popState();
                context().clearInput();
                context().eventStack().nextEvent();
                break;

            case GET_PASS:

                popState();
                pass = context().inputLine();
                try {
                    passno = Integer.valueOf(pass).intValue();
                } catch (java.lang.NumberFormatException e) {
                    context().operPrompt().update(this);
                    return;
                }

                if (getUserValidation(context())) {

                    popState();
                    context().pushEmployee(emp);
                    context().clearInput();
                } else {
                    setPromptText(context().posParameters().getString("BadName"));
                    context().operPrompt().update(this);
                }
                context().eventStack().nextEvent();
                break;

            case GET_AMOUNT:

                // Minimal validation here!!!
                popState();
                if ((openamount = context().input()) < 0) {
                    context().eventStack().restore();
                    // 				pushUserEvent (new PosError (context (), PosError.INVALID_INPUT));
                } else {
                    popState();
                }
                context().clearInput();
                context().eventStack().nextEvent();
                break;

            case GET_DRAWER_NO:

                // What, no validation here!!!

                popState();
                context().setDrawerNo(drawerno = context().input());
                if (isDrawerIsValid()) {
                    popState();
                }
                context().clearInput();
                context().eventStack().nextEvent();

                break;

            case COMPLETE_LOGON:

                popState();

                Employee employee = context().employee();
                employee.logon();

                // set a default drawer number if not input

                if (drawerno == 0)
                    context().setDrawerNo(context().posParameters().getInt("DefaultDrawerNo"));
                else
                    context().setDrawerNo(drawerno);

                // set the transaction type to logon.

                context().setTransType(Transaction.LOGON);

                // create a EjBank record for the open amount.

                if (openamount > 0) {

                    // 				context ().setCurrTrans (new EjHeader (context ().getTrans (Transaction.BANK)));
                    // 				context ().currTrans ().setContext (context ());
                    // 				EjBank bank = new EjBank (context ());
                    // 				bank.setTransBank (new com.globalretailtech.data.TransBank ());
                    // 				bank.setEventType (EjBank.TENDER_FINAL);
                    // 				bank.setTransType (com.globalretailtech.data.TransBank.PAIDIN);
                    // 				bank.setTransAmount (openamount);
                    // 				context (). currEj ().ejAdd (bank);
                    // 				bank.engage (0);
                }

                // complete the logon transaction



                Transaction trans = (Transaction) context().currTrans().dataRecord();

                trans.updateStateAndDrawer(Transaction.COMPLETE, context().drawerNo());

                context().eventStack().nextEvent();

                break;

            case TEMPORARY_LOGON:

                popState();
                context().eventStack().nextEvent();
                break;

            case UNLOCK:

                popState();
                context().enableKeys();
                context().eventStack().nextEvent();
                break;

            default:
                logger.warn("Unknown event in " + getClass().toString());
                break;
        }
    }

    /**
     * Validate a cash drawer numer.
     */
    private boolean isDrawerIsValid() {
        return true;
    }

    /**
     * Look in the database for this user ID, if found
     * check the password. Returns true for succes. If it
     * fails create a PosError, push it on the stack
     * and return false.
     */
    private boolean getUserValidation(PosContext context) {

        if (trys++ > 2) {
            trys = 0;
            return false;
        }

        String fetchSpec = Employee.getByLogonNo(userno);
        Vector v = Application.dbConnection().fetch(new Employee(), fetchSpec);

        if (v.size() == 1) {

            emp = (Employee) v.elementAt(0);

            if (emp.logonPass() == passno)
                return true;
            else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int type;

    /** Implemntation of type for PosNumberDialog */
    public int type() {
        return type;
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    // 		if (display instanceof OperPrompt) {
    // 			((OperPrompt) display).update (this);
    // 		}
    // 	}
    /**
     * Clear key implementation for this class.
     * If the input line length is greater than 0
     * remove the last character by calling NumKey.clear ().
     */
    public void clear() {

        if (context().inputLine().length() > 0) {
            new NumKey().clear();
        }
    }

    private static String eventname = "LogOn";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


