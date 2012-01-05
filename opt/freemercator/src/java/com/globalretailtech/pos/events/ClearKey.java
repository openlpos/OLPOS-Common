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


import com.globalretailtech.pos.ej.*;
import com.globalretailtech.pos.events.PosEvent;
import org.apache.log4j.Logger;

/**
 * ClearKey implements dialog error recovery. If
 * a dialog is in progress it restores the processed
 * stack, otherwise it just clears the input buffer and
 * re-posts the current prompt.
 *
 * @author  Quentin Olson
 */
public class ClearKey extends PosEvent {

    static Logger logger = Logger.getLogger(ClearKey.class);

    private String prompttext;

    /**
     * Get the prompt text for this object, used for display
     */
    public String promptText() {
        return prompttext;
    }

    /**
     * Simple constructor, required for dynamic load.
     */
    public ClearKey() {
    }

    /**
     * Customized for event type on top of stack
     * - PosError, pop the next event and update dialogs.
     * - If there is input line data, (i.e. user is in the
     *   middle inputing a number, remove the last number.
     * - else, clear input, pops the stack and update dialogs.
     */
    public void engage(int value) {

		context().eventStack().dump();

        PosEvent event = context().eventStack().event();

        // Clear the error event, update dialogs and return

        if (event instanceof PosError) {
            context().clearInput();
            context().eventStack().clearPending();
            context().eventStack().clearProcessed();
            context().eventStack().nextEvent();
            return;
        } else if (event instanceof UnLock) {
            return;
        }

        // If this is a finish trans then the user wants to
        // cancel the current tender, clean up the operator
        // reciept, remove the tender ej and continue.

        else if (event instanceof FinishTransaction) {

            context().receipt().clear((EjLine) context().currEj().currLine());

            context().clearInput();
            context().currEj().removeCurrLine();
            context().eventStack().clearPending();
            context().eventStack().nextEvent();
            return;
        } 
        
		// If this is a start trans then prev. transaction was already
		// failed or completed and flow was paused to allow user to
		// see the reason of failure. Just engage next event to start
		// new transaction.

		else if (event instanceof StartTransaction) {
			context().eventStack().nextEvent();
			return;
			
		}else if (event instanceof EjBank) {

			context().clearInput();
			context().eventStack().clearPending();
			context().eventStack().setEvent(new RegisterOpen(context()));
			context().eventStack().nextEvent();
			return;
		}else if (event instanceof EjLine) {

			context().clearInput();
			context().currEj().removeCurrLine();
			context().eventStack().clearPending();
			context().eventStack().nextEvent();
			return;
        }

        if (context().input() > 0) {
            context().clearInput();
            PosEvent e = context().eventStack().event();
            try {
                e.engage(0);
            } catch (PosException pose) {
                logger.warn("Error in clear, engage, " + pose.toString(), pose);
            }
        } else {
            context().clearInput();
            PosEvent e = context().eventStack().popEvent();
            e = context().eventStack().event();
            try {
                e.engage(0);
            } catch (PosException pose) {
                logger.warn("Error in clear, engage, " + pose.toString(), pose);
            }
        }
        return;
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

    private static String eventname = "ClearKey";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


