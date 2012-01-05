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


import com.globalretailtech.pos.context.*;
import org.apache.log4j.Logger;

/**
 * POS dialog event super class. This class is extended
 * by any POS event or EJ line that wishes to move the user
 * through a series of prompts. Has it's own stack of
 * states to manage this.
 *
 * @author  Quentin Olson
 */

public abstract class PosDialogEvent extends PosEvent {

    static Logger logger = Logger.getLogger(PosDialogEvent.class);

    private PosStateStack statestack;

    /** Current state on top of stack (peek) */
    public int state() {
        return statestack.state();
    }

    /** Pop the top and return it. */
    public int popState() {
        return statestack.popState();
    }

    /** Push a state on the stack */
    public void pushState(int value) {
        statestack.pushState(value);
    }

    /** Return the entire state stack. */
    public PosStateStack states() {
        return statestack;
    }

    /**
     * Used when loading events (loadDialog) to determine if
     * states need to be pushed also.
     */
    public boolean isDialog() {
        return true;
    }

    /**
     * Constructor calls superclass constructor and
     * creates a state stack.
     */
    public PosDialogEvent() {
        super();
        statestack = new PosStateStack();
    }

    /**
     * Constructor calls superclass constructor,
     * creates a state stack and pushes an initial event.
     */
    public PosDialogEvent(int state) {
        super();
        statestack = new PosStateStack();
        statestack.pushState(state);
    }

    /**
     * Finds the next event or dialog state and engages it.
     */
    public void nextDialogEvent() {

        PosEvent nextEvent = null;
        if (states().pendingSize() > 1) {
            states().popState();
            nextEvent = this;  // recursion!!
        } else {
            nextEvent = context().eventStack().popEvent();
        }

        try {
            nextEvent.engage(0);
        } catch (PosException e) {
            logger.warn("PosException in nextDialogEvent", e);
        }
    }

    /**
     * Pushes a new event on, note that it also pops the current
     */
    public void pushUserEvent(int id) {

        states().popState();
        states().pushState(id);
    }
}


