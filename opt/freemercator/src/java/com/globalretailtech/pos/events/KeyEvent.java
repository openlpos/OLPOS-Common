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


import com.globalretailtech.data.PosKey;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.devices.*;
import org.apache.log4j.Logger;

/**
 * Ties a key to an PosEvent. Used to provide some abstract
 * functionality between GUI menu keys and hardware keys. Holds
 * the key enabler that abstracts enabling/disabling of keys.
 *
 *
 * @author  Quentin Olson
 * @see MenuButton
 * @see KeyEnable
 */
public class KeyEvent {

    static Logger logger = Logger.getLogger(KeyEvent.class);

    private PosKey config;
    private PosContext context;
    private PosEvent function;
    private KeyEnable enabler;

    /** The database key definition. */
    public PosKey config() {
        return config;
    }

    /** PosContext for this key. */
    public PosContext context() {
        return context;
    }

    /** The event for this function. */
    public PosEvent function() {
        return function;
    }

    /** Enabler function for this key. */
    public KeyEnable enabler() {
        return enabler;
    }

    public void setEnabler(KeyEnable value) {
        enabler = value;
    }

    /** Simple constructor for dynamic load */
    public KeyEvent() {
    }

    /**
     * Constructor loads the PosEvent class specified in the database
     * and sets up the context.
     */
    public KeyEvent(com.globalretailtech.data.PosKey c, PosContext context) {

        config = c;
        enabler = null;

        if (config.keyClass() != null) {

            try {
//				function = (PosEvent) javaos.javax.system.services.Loader.getInstance(config.keyClass());
				function = (PosEvent) Class.forName(config.keyClass()).newInstance();
            } catch (Exception e) {
                logger.warn("Can't load class : " + config.keyClass(), e);
                return;
            }
            if (function != null)
                function.setContext(context);
        }
    }
}


