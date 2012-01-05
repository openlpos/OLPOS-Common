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

import com.globalretailtech.util.*;
import com.globalretailtech.pos.events.*;

/**
 * Terminal lock thread, initiate a lock event if not
 * interrupted in the time out period. Timer is reset
 * by every key stroke, via reset ().
 *
 * @author  Quentin Olson
 */
public class LockThread extends Thread {

    private PosContext context;
    private int timeOut;

    /**
     * Constructor with a PosContext. Get the timeout
     * from a shared property file.
     */
    public LockThread(PosContext c) {

        context = c;

        ShareProperties p = new ShareProperties(this.getClass().getName());
        if (p.Found()) {
            timeOut = Integer.valueOf(p.getProperty("TimeOut", "300")).intValue();
        }
    }

    /**
     * Thread start, sleep until timeout, resite the interrupted flag
     * if interrupted. Note: reset () is called at a key press.
     */
    public void run() {

        while (true) {
            boolean interupted = false;
            try {
                Thread.sleep(timeOut * 1000);
            } catch (java.lang.InterruptedException e) {
                interupted = true;
            }

            if (!interupted && !context.locked())
                new Lock(context).engage(0);
        }
    }

    /**
     * Interrupt this thread, calls Thread.interupt ().
     */
    public void reset() {
        interrupt();
    }
}


