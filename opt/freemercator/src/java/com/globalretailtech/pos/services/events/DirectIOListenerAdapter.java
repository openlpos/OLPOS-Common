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

package com.globalretailtech.pos.services.events;

import org.apache.log4j.Logger;



/**
 *
 * @author  Quentin Olson
 * @see
 */
public class DirectIOListenerAdapter implements jpos.events.DirectIOListener {
    static Logger logger = Logger.getLogger(DirectIOListenerAdapter.class);

    public void directIOOccurred(jpos.events.DirectIOEvent e) {
        logger.debug("Data event : " + e.toString());
    }
}


