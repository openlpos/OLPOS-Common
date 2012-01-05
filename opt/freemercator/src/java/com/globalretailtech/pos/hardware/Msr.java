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

import java.util.Vector;
import java.util.Hashtable;

import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.operators.*;
import org.apache.log4j.Logger;

/**
 * Msr implementation. Attaches to the jpos service
 * passed in the constructor. When input arrivives it
 * is added (appended) to the input buffer associated
 * with the current context.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class Msr extends BaseDevice {

    static Logger logger = Logger.getLogger(Msr.class);

    jpos.MSR control;
    ContextSet contextSet;

    Vector filters;

    public Msr(jpos.MSR c, String devicename, ContextSet set) {

        super(c, devicename);
        control = c;
        contextSet = set;

        c.addDataListener(new com.globalretailtech.pos.services.events.DataListenerAdapter() {

            public void dataOccurred(jpos.events.DataEvent e) {

                Vector tracks = new Vector();

                try {
                    tracks.addElement(new String(control.getTrack1Data()));
                    tracks.addElement(new String(control.getTrack2Data()));
                    tracks.addElement(new String(control.getTrack3Data()));
                } catch (jpos.JposException jpe) {
                    logger.warn(jpe.toString(), jpe);
                }
                process(tracks);

            }
        }
        );
    }

    public void process(Vector tracks) {

        PosContext context = contextSet.currentContext();

        if (context != null) {

            Hashtable results = new Hashtable();

            for (int i = 0; i < tracks.size(); i++) {

                String track = (String) tracks.elementAt(i);

                for (int j = 0; j < context.msrFilters().size(); j++) {

                    // Get the filter and apply it

                    Filter f = context.msrFilters().getFilter(j);

					// filter checks whether it should be applied,
					// extracts the data and perform all job
                    f.apply (context, track);
                    
                }
            }
        }
    }
}


