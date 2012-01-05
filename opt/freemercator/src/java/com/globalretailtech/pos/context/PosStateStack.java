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

/**
 * Extension of PosStack to manage states used in PosDialogs. Built
 * to just handle integers (ints).
 *
 * @author  Quentin Olson
 */
public class PosStateStack extends PosStack {

    public PosStateStack() {
        super();
    }

    /**
     * Return the current event type.
     */
    public int state() {

        int value = 0;

        if (pendingSize() > 0) {
            Integer state = (Integer) super.peek();
            value = state.intValue();
        }

        return value;
    }

    /**
     * Remove the top of the pending stack and return it. Invoke
     * the push operation to move it to the pending stack. If there
     * is only one event on the stack leave it and just peek.
     */
    public int popState() {

        int value = 0;
        if (pendingSize() > 0) {
            Integer tmp = (Integer) super.popPending();
            value = tmp.intValue();
        }
        return value;
    }

    /**
     * Add an object to the pending stack.
     */
    public void pushState(int value) {
        pushPending(new Integer(value));
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
    public void setState(Integer event) {

        super.clear();
        pushPending(event);
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

}


