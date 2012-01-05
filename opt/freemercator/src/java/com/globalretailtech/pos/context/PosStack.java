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

import org.apache.log4j.Logger;

import java.util.Stack;



/**
 * Manages event stacks for a context, pending and processed.
 * The pending stack is for events yet to be executed, processed
 * are events that have been processed. In error conditions where
 * the user wants to correct a dialog, the clear key will restore
 * the processed to pending.
 *
 * @author  Quentin Olson
 */
public class PosStack {

    static Logger logger = Logger.getLogger(PosStack.class);

    private Stack pending;
    private Stack processed;

    /**
     * Simple constructor creates a pending and processed stack.
     */
    public PosStack() {

        pending = new Stack();
        processed = new Stack();
    }

    /**
     * Number of elements in the pending stack.
     */
    protected int pendingSize() {
        return pending.size();
    }

    /**
     * Number of elements in the processed.
     */
    protected int processedSize() {
        return processed.size();
    }

    /**
     * Clear the pending and push an object on.
     */
    protected void set(Object o) {

        pending.clear();
        pending.push(o);
    }

    /**
     * Insert an object at a location in the stack.
     */
    protected void pendingInsertElementAt(Object o, int pos) {

        pending.insertElementAt(o, pos);
    }

    /**
     * Look at the top of the stack.
     */
    protected Object peek() {
        return (Object) pending.peek();
    }

    /**
     * Clear both stacks.
     */
    protected void clear() {

        pending.removeAllElements();
        processed.removeAllElements();
    }

    /**
     * Pop the pending, push it on the processed and return it.
     */
    protected Object popPending() {

        // 		Object o;

        // 		if (pending.size () > 1) {
        // 			o =  pending.pop ();
        // 			processed.push (o);
        // 		}
        // 		else {
        // 			o = pending.peek ();
        // 		}

        if (pending.size() == 0)
            return null;
        return pending.pop();
    }

    /**
     * Push an object on the pending stack.
     */
    protected void pushPending(Object o) {
        pending.push(o);
    }

    /**
     * Restore all of the processed to the pending.
     */
    protected void restore() {

        while (processed.size() > 1) {
            Object o = processed.pop();
            pending.push(o);
        }
    }

    /**
     * Clear the pending stack.
     */
    public void clearPending() {
        while (pending.size() > 1) {
            Object o = pending.pop();
        }
    }

    /**
     * Clear the processed stack.
     */
    public void clearProcessed() {
        while (processed.size() > 0) {
            Object o = processed.pop();
        }
    }

    /**
     * Useful method to dump the contents of both stacks to the
     * debug Log.
     */
    public void dump() {

		logger.info("dump pending   ");
        if (this instanceof PosStateStack) {

            for (int i = 0; i < pending.size(); i++) {
                Integer state = (Integer) pending.elementAt(i);
                logger.info("dump pending   " + state.intValue());
            }
            for (int i = 0; i < processed.size(); i++) {
                Integer state = (Integer) processed.elementAt(i);
                logger.info("dump processed " + state.intValue());
            }

        } else {

            for (int i = 0; i < pending.size(); i++) {
                Object o = (Object) pending.elementAt(i);
                logger.info("dump pending   " + o.getClass());
            }
            for (int i = 0; i < processed.size(); i++) {
                Object o = (Object) processed.elementAt(i);
                logger.info("dump processed " + o.getClass());
            }

        }
    }
}


