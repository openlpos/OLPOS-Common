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

package com.globalretailtech.pos.context;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.globalretailtech.pos.events.*;
import com.globalretailtech.data.Dialog;
import com.globalretailtech.data.DialogEvent;
import org.apache.log4j.Logger;

/**
 * Implements a stack of POS events by extending PosStack. Manages
 * the current and queued event for a PosContext. This class included the more
 * complex operations for loading and executing events/dialogs
 * (nextEvent () and loadDialog ()).
 *
 * @author  Quentin Olson
 */
public class PosEventStack extends PosStack {

    static Logger logger = Logger.getLogger(PosEventStack.class);
	
	private Set listeners = new HashSet();
	
    /**
     * Simple constructor calls super ().
     */
    public PosEventStack() {
        super();
    }

    /**
     * Return the current event type.
     */
    public PosEvent event() {
        PosEvent event = (PosEvent) super.peek();
        return event;
    }

    /**
     * Pop the pending, push it on the processed and return it.
     */
    /**
     * Remove the top of the pending stack and return it. Invoke
     * the push operation to move it to the pending stack. If there
     * is only one event on the stack leave it and just peek.
     */
    // 	public PosEvent popEvent () { return (PosEvent) super.popPending (); }
    public PosEvent popEvent() {

        Object o;

        if (pendingSize() > 1) {
            o = popPending();
        } else {
            o = peek();
        }
        
        return (PosEvent) o;
    }

    /**
     * Add an object to the pending stack.
     */
    public void pushEvent(PosEvent value) {
		notifyBeforeEvent (value);
        pushPending(value);
    }

    /**
     * Add an object to the processed stack.
     */
    public void pushProcessed(Object value) {
        pushProcessed(value);
    }

    /**
     * Set the fist event in the pending stack
     * to the object.
     */
    public void setEvent(PosEvent event) {

        super.clear();
        pushPending(event);
    }

    /**
     * Set the fist event in the pending stack
     * to the object.
     */
    public void insertElementAt(PosEvent event, int pos) {
        pendingInsertElementAt(event, pos);
    }


    /**
     * The size of the pending stack.
     */
    public int pendingSize() {
        return super.pendingSize();
    }

    /**
     * The size of the processed stack.
     */
    public int processedSize() {
        return super.processedSize();
    }

    /**
     * Removes all events from the proccesed and puts them back
     * on the pending stack.
     */
    public void restore() {
        super.restore();
    }

    /**
     * Removes all events from the pending stack.
     */
    public void clearPending() {
        super.clearPending();
    }

    /**
     * Removes all events from the processed stack.
     */
    public void clearProcessed() {
        super.clearProcessed();
    }

	/**
	 * Pops and executes the next event on the stack.
	 */
	public void nextEvent() {
		nextEvent(0, false);
	}


	/**
	 * Pops and executes the next event on the stack,
	 * calls engage() with specified value, if forceLoadDialog is true,
	 * dialog will be loaded even there is no state
	 */
	public void nextEvent(int value, boolean forceLoadDialog) {
		
        PosEvent event = null;
        try {

            // if this is a dialog pop it's state stack and invoke
            // the current event

            if (event().isDialog()) {

                PosDialogEvent dialog = (PosDialogEvent) event();

                // if there are state events pop

                if (dialog.states().pendingSize() > 0) {
                    event = event();
                } else {
                    // if there are no state events in the dialog
                    // and load dialog is not forced
                    // pop the entire event, and engage the next one
                    if ( ! forceLoadDialog)
						popEvent();
					event = popEvent();
                }
            } else {
                event = popEvent();
            }

            event.engage(value);

			notifyAfterEvent (event);
			
        } catch (PosException pe) {
            logger.warn("Failed in nextEvent ()", pe);
        }
    }

    /**
     * Finds a dialog set in PosConfig and loads it.
     */
    public void loadDialog(String dialog_name, PosContext context) {

        // get the dialog from pos config

        Dialog dialog = (Dialog) context.config().dialogs().get(dialog_name);
        PosEvent event = null;

        if (dialog != null) {  // found!

            int insertPos = pendingSize() == 1 ? 1 : 0; // leave the first one on the stack

            for (int i = 0; i < dialog.dialogEvents().size(); i++) {  // load each event

                DialogEvent dialogEvent = (DialogEvent) dialog.dialogEvents().elementAt(i);

                if (dialogEvent.eventClass() != null) {

                    try {  // try to load the event class

//						event = (PosEvent) javaos.javax.system.services.Loader.getInstance(dialogEvent.eventClass());
						event = (PosEvent) Class.forName(dialogEvent.eventClass()).newInstance();

                        if (event.isDialog()) {
                            ((PosDialogEvent) event).pushState(dialogEvent.state());
                        }
                    } catch (java.lang.ClassNotFoundException e) {
                        logger.warn("Class not found : " + dialogEvent.eventClass());
                        return;
                    } catch (java.lang.Exception e) {
                        logger.warn("Class load error : " + e.toString() + "/" + dialogEvent.eventClass(), e);
                        return;
                    }

                    // push the event on the stack if found and is required or enabled.
                    if (dialogEvent.eventRequired() || dialogEvent.eventEnabled()) {

                        if (event != null && ((dialogEvent.eventRequired() || dialogEvent.eventEnabled()))) {

                            event.setContext(context);  // it was created using the default constructor, so set up the context

                            // This tells it what to do. If a non-zero event type was specified in the dialog
                            // definition, use it, else use that set by the default constructor.

                            pushEvent(event);
                        }
                    }
                } else {  // use the event on the top of the stack.

                    event = event();
                    if (event.isDialog() && ((dialogEvent.eventRequired() || dialogEvent.eventEnabled()))) {
                        ((PosDialogEvent) event).states().pushState(dialogEvent.state());
                    }
                }
            }
        } else {
            logger.warn("Dialog not found " + dialog_name);
        }
    }
/****************************************************************************/
/*                              Listener support                            */    
/****************************************************************************/    
	public void addPosEventListener(PosEventListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}
	private void notifyBeforeEvent(PosEvent event) {
		for (Iterator i = listeners.iterator(); i.hasNext();) {
			PosEventListener l = (PosEventListener) i.next();
			l.beforeEvent(event);
		}
	}
	private void notifyAfterEvent(PosEvent event) {
		for (Iterator i = listeners.iterator(); i.hasNext();) {
			PosEventListener l = (PosEventListener) i.next();
			l.afterEvent(event);
		}
	}

}


