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
import com.globalretailtech.pos.context.*;
import org.apache.log4j.Logger;

/**
 * Log on dialog. Allows entry of user ID and PIN, then
 * open amount and drawer assignment if set up in database.
 *
 * @author  Quentin Olson
 */
public class UnLock extends PosNumberDialog {

    static Logger logger = Logger.getLogger(UnLock.class);

    /** Prompt for user ID. */
    public static final int ENTER_USER = 0;
    /** Prompt for PIN. */
    public static final int ENTER_PASS = 1;
    /** Get the user ID from context input. */
    public static final int GET_USER = 2;
    /** Get the PIN from context input. */
    public static final int GET_PASS = 3;
    /** Unlock the context */
    public static final int UNLOCK = 4;

    private int trys = 0;
    private Employee emp;
    private String user;
    private String pass;
    private int userno;
    private int passno;
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
    public UnLock() {
    }

    /** Simple constructor with context. */
    public UnLock(PosContext context) {
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

            case GET_USER:

                popState();

                // ID must match the current user
                if (context().input() != context().user()) {
                    context().clearInput();
                    context().eventStack().nextEvent();
                    return;
                }

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
                    context().eventStack().nextEvent();
                } else {
                    context().eventStack().clearPending();
                    context().eventStack().loadDialog("UnLock", context());
                    context().eventStack().nextEvent();
                }
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

    private static String eventname = "UnLock";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


