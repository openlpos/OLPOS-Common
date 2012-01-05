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

package com.globalretailtech.pos.apps;

import java.util.Vector;
import java.util.Date;

import com.globalretailtech.util.*;
import com.globalretailtech.data.*;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.*;
import com.globalretailtech.pos.hardware.*;
import org.apache.log4j.Logger;

/**
 * A gui pos Application. Creates a PosFrame and loads
 * it.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class HPOS extends Application implements ContextSet {

    static Logger logger = Logger.getLogger(HPOS.class);

    private PosContext context;
    private int configid;
    private int siteid;
    private int posid;
    private int siteno;
    private int posno;
    private int sleepInterval = 500;

    public HPOS() {
    }

    public void Run() {
        start(null);
    }

    public void start(String[] args) {

        // read properites

        ShareProperties p = new ShareProperties(this.getClass().getName());

		if (args != null && args.length == 5) {
            siteno = Integer.valueOf(args[0]).intValue();
            posno = Integer.valueOf(args[1]).intValue();
            configid = Integer.valueOf(args[2]).intValue();
            Application.setDebug(args[3].equals("y"));
            Application.setLogEvents(args[4].equals("y"));
        } else {
            siteno = new Integer(p.getProperty("Site", "1")).intValue();
            posno = new Integer(p.getProperty("POS", "1")).intValue();
            configid = new Integer(p.getProperty("Configuration", "1")).intValue();
            sleepInterval = new Integer(p.getProperty("SleepInterval", "1000")).intValue();
            Application.setDebug(p.getProperty("Debug", "n").equals("y"));
            Application.setLogEvents(p.getProperty("LogEvents", "n").equals("y"));
        }

        //initialize logging
        initLog4j();

        // create the database connection

        Application.setDbConnection(new DBContext());

        if (!dbConnection().init()) {
            logger.fatal("Database initialization failure");
            System.exit(0);
        }

        // see if command line params make sense

        if ((siteno < 0) || (posno < 0) || (configid < 0)) {
            logger.fatal("POS confituration not found in properties.");
            System.exit(0);
        }

        // find this pos in the database, create it if it
        // does not.

        checkPos(posno, siteno);
        if (posid < 0) {
            logger.fatal("SITE/Configuration not found in database.");
            System.exit(0);
        }

        // print startup info

        logger.info("Application POS started, Site ID: " + siteno + " POS ID: " + posno + " CONFIG ID: " + configid);
        PosEvent.eventLogger.info("Application POS started, Site ID: " + siteno + " POS ID: " + posno + " CONFIG ID: " + configid);


        context = new PosContext(siteid, posid, posno, configid);

        // attach hardware

        new Keyboard(new jpos.POSKeyboard(), "com.globalretailtech.pos.services.POSKeyboard", this);
        new Msr(new jpos.MSR(), "com.globalretailtech.pos.services.POSMsr", this);
        new Scanner(new jpos.Scanner(), "com.globalretailtech.pos.services.POSScanner", this);

        // complete initialization for this context.
        // clears devices, creates an initial transaction and starts logon sequence

        context.hardwarePosInit();
        context.setTrainingMode(true);  // to stop cash drawer dialog

    }

    public static void main(String[] args) {
        new HPOS().start(args);
    }

    public void checkPos(int posno, int siteno) {

        Site site;
        siteid = -1;
        posid = -1;

        String fetchSpec = Site.getBySiteNo(siteno);
        Vector v = Application.dbConnection().fetch(new Site(), fetchSpec);

        if (v.size() > 0) {
            site = (Site) v.elementAt(0);
            siteid = site.siteID();
        } else
            return;

        for (int i = 0; i < site.pos().size(); i++) {

            Pos pos = (Pos) site.pos().elementAt(i);

            if (pos.posNo() == posno) {
                posid = pos.posID();
                return;
            }
        }

        logger.info("Pos not found, creating pos: " + posno);

        Pos pos = new Pos();
        pos.setPosID(0);
        pos.setPosNo(posno);
        pos.setShortDesc(new String("Reg#" + Integer.toString(posno)));
        pos.setCreateDate(new Date());
        pos.setModifyDate(new Date());
        posid = pos.create(siteno);
    }

    public PosContext currentContext() {
        return context;
    }
}


