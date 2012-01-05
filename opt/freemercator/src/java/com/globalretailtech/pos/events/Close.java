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

import java.util.Vector;
import java.util.Date;


import com.globalretailtech.util.Application;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.PosEvent;
import com.globalretailtech.data.PosSession;
import com.globalretailtech.data.PosTransUpload;
import org.apache.log4j.Logger;

/**
 * Closes the current transaction session, creating upload
 * records and logging the current operator off.
 *
 * @author  Quentin Olson
 */
public class Close extends PosEvent {

    static Logger logger = Logger.getLogger(Close.class);

    public boolean validTransition(String event) {
        return true;
    }

    /**
     */
    public void clear() {
    }

    public Close() {
    }

    public Close(PosContext context) {
        setContext(context);
    }

    public void engage(int value) {

        // find the most recent close record (pos_session, pos_trans_upload) create a
        // new record starting at the next transaction to the current.

        PosSession session = new PosSession();
        session.setSiteID(context().siteID());
        session.setPosNo(context().posNo());
        session.setSessionType(PosSession.TRANS_UPLOAD);
        session.setStatus(PosSession.COMPLETE);
        session.setStartDate(new Date());
        session.setEndDate(new Date());

        if (session.save()) {

            PosTransUpload uploadSession = new PosTransUpload();

            uploadSession.setPosSessionID(session.posSessionID());
            uploadSession.setTransEnd(context().currTrans().transHeader().transNo());

            String fetchSpec = PosSession.getLastByType(PosSession.TRANS_UPLOAD);
            Vector tmp = Application.dbConnection().fetch(new PosSession(), fetchSpec);

            if (tmp.size() == 0) {  // create a the first upload session, trans = 0 to current
                uploadSession.setTransStart(0);
            } else {
                PosSession oldSession = (PosSession) tmp.elementAt(0);
                PosTransUpload oldUpload = (PosTransUpload) oldSession.sessionRecords().elementAt(0);
                uploadSession.setTransStart(oldUpload.transEnd() + 1);
            }
        } else {
            logger.warn("Failed to create session record " +
                    session.siteID() +
                    session.posNo());
        }
        // 		context ().logOff ();  // should create a logoff transaction record.
    }

    private static String eventname = "Close";

    public String toString() {
        return eventname;
    }

    public static String eventName() {
        return eventname;
    }
}


