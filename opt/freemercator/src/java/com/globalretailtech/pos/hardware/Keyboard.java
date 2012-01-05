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

package com.globalretailtech.pos.hardware;


import com.globalretailtech.pos.events.*;
import com.globalretailtech.pos.context.*;

import org.apache.log4j.Logger;

/**
 * Keyboard implementation. Attaches to the jpos service
 * passed in the constructor and excutes the PosEvent tied
 * to the key using the key code from jpos.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class Keyboard extends BaseDevice {

    static Logger logger = Logger.getLogger(Keyboard.class);

    jpos.POSKeyboard control;
    ContextSet contextSet;

    public Keyboard(jpos.POSKeyboard c, String devicename, ContextSet set) {

        super(c, devicename);
        control = c;
        contextSet = set;

        c.addDataListener(new com.globalretailtech.pos.services.events.DataListenerAdapter() {

            public void dataOccurred(jpos.events.DataEvent e) {

                try {
                    process(control.getPOSKeyData());
                } catch (jpos.JposException jpe) {
                    logger.warn(jpe.toString(), jpe);
                }

            }
        }
        );
    }

    public void process(int value) {

        PosContext context = contextSet.currentContext();

        if (context != null) {

            // find the key in the key code hash and execute it.

            KeyEvent key = (KeyEvent) context.keysByCode().get(new Integer(value));
            
            if (key == null) {
            
                logger.warn("Key definition for " + value + " not found.");
                return;
            
            }

			// is button enabled?
			
        	if ( key.enabler() == null || key.enabler().isEnabled()){

				key.function().setContext(context);

				// is this a valid thing to be doing?

				if (key.function().isValidEvent()) {

					PosEventStack eventStack = context.eventStack();

					if (eventStack != null){

						eventStack.pushEvent (key.function());
						eventStack.nextEvent(key.config().keyVal(), true);

					}
				}else{
					context.eventStack().pushEvent(new PosError(context, PosError.INVALID_INPUT));
					context.operPrompt().update((PosError) context.eventStack().event());
				}
        	}else{
        		logger.debug ("key disabled");
        	}
        }
    }
}


