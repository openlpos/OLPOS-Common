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

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.Date;


import com.globalretailtech.util.Application;
import com.globalretailtech.data.DBContext;
import com.globalretailtech.pos.gui.PosFrame;
import com.globalretailtech.data.Pos;
import com.globalretailtech.data.Site;
import com.globalretailtech.util.ShareProperties;
import com.globalretailtech.pos.events.PosEvent;
import com.globalretailtech.pos.context.ContextSet;
import com.globalretailtech.pos.context.PosContext;

import org.apache.log4j.Logger;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

/**
 * A gui pos Application. Creates a PosFrame and loads
 * it.
 *
 *
 * @author  Quentin Olson
 * @version $Id: GPOS.java,v 1.24 2003/07/16 16:47:56 isemenko Exp $
 */
public class GPOS extends Application implements PosApp {

    static Logger logger = Logger.getLogger(GPOS.class);

    private static PosFrame frame;

    private static PosFrame frame() {
        return frame;
    }

    private static ContextSet contextset;
    private int configno;
    private int siteid;
    private int posid;
    private int siteno;
    private int posno;

    // context set interface

    public ContextSet contextSet() {
        return contextset;
    }

    public void setContextSet(ContextSet value) {
        contextset = value;
    }

    public GPOS() {
    }

    public void Run() {
        start(null);
    }

    public void start(String[] args) {

        // read properites

        ShareProperties p = new ShareProperties(this.getClass().getName());

        //set defaults
        siteno = new Integer(p.getProperty("Site", "0")).intValue();
        posno = new Integer(p.getProperty("POS", "1")).intValue();
        configno = new Integer(p.getProperty("Configuration", "0")).intValue();
		int warmupContext = new Integer(p.getProperty("WarmupContext", "0")).intValue();
		
        //parse command-line arguments
        LongOpt[] longopts = new LongOpt[5];

        StringBuffer sb = new StringBuffer();
        longopts[0] = new LongOpt("site", LongOpt.OPTIONAL_ARGUMENT, null, 's');
        longopts[1] = new LongOpt("pos", LongOpt.OPTIONAL_ARGUMENT, null, 'p');
        longopts[2] = new LongOpt("config", LongOpt.OPTIONAL_ARGUMENT, null, 'c');
		longopts[3] = new LongOpt("edit", LongOpt.OPTIONAL_ARGUMENT, null, 'e');
		longopts[4] = new LongOpt("share", LongOpt.REQUIRED_ARGUMENT, null, 'r');

        Getopt g = new Getopt("GPOS", args, ":s:p:c:e:r", longopts);
        g.setOpterr(false); // We'll do our own error handling

        String arg;
        int c;
        while ((c = g.getopt()) != -1) {
            switch (c) {
                case 's':
                    arg = g.getOptarg();
//                    System.out.println("You picked option '" + (char) c +"' with argument " +((arg != null) ? arg : "null") + sb);
                    siteno = Integer.parseInt(arg);
                    break;
                case 'p':
                    arg = g.getOptarg();
//                    System.out.println("You picked option '" + (char) c +"' with argument " +((arg != null) ? arg : "null") + sb);
                    posno = Integer.parseInt(arg);
                    break;
                case 'c':
                    arg = g.getOptarg();
//                    System.out.println("You picked option '" + (char) c +"' with argument " +((arg != null) ? arg : "null") + sb);
                    configno = Integer.parseInt(arg);
                    break;
				case 'e':
					arg = g.getOptarg();
//					  System.out.println("You picked option '" + (char) c +"' with argument " +((arg != null) ? arg : "null") + sb);
					Application.setEditMode(true);
					break;
				case 'r':
					arg = g.getOptarg();
//					  System.out.println("You picked option '" + (char) c +"' with argument " +((arg != null) ? arg : "null") + sb);
					System.setProperty("SHARE",arg);
					break;
                case ':':
                    System.out.println("Oops! You need an argument for option " + (char) g.getOptopt());
                    break;
                case '?':
                    System.out.println("The option '" + (char) g.getOptopt() +"' is not valid");
                    break;
                default:
                    System.out.println("getopt() returned " + c + " but we couldn't handle it");
                    break;
            }
        }


        //initialize logging
		
        initLog4j();

		dumpEnvironment();

        // create the database connection

        Application.setDbConnection(new DBContext());

        if (!dbConnection().init()) {
            logger.fatal("Database initialization failure");
            System.exit(1);
        }

		// find default site if wasn't passed
		Site site = null;
		if (siteno == 0){
			logger.debug ("find default site");
			site = Site.getDefault();
			if ( site != null)
				siteno = site.siteNo();
		}else {
			String fetchSpec = Site.getBySiteNo(siteno);
			Vector tmp = Application.dbConnection().fetch(new Site(), fetchSpec);
			if (tmp.size() > 0) 
				site = (Site)tmp.elementAt (0);
		}
		
		// see if command line params make sense
        if ((siteno < 0) || (posno < 0) || (configno < 0)) {
            logger.fatal("Site, POS, or Config ID is invalid.");
            System.exit(1);
        }

        // find this pos in the database, create it if it
        // does not.

        checkPos(posno, siteno);
        if (posid < 0) {
            logger.fatal("SITE/Configuration not found in database.");
            System.exit(1);
        }

        // print startup info

        logger.info("Application POS started, Site No: " + siteno + ", Site ID:"+ site.siteID()+" POS No: " + posno + " CONFIG NO: " + configno);
        PosEvent.eventLogger.info("Application POS started, Site ID: " + siteno + " POS ID: " + posno + " CONFIG NO: " + configno);

        // create the app and start it

        frame = new PosFrame();
        frame.add(siteid, posid, posno, configno);
        // don't change this ! important to have "POS" title
        // in undecorated mode
        if ( ! frame.isUndecorated() )
			frame.setTitle(getTitle());
        frame.show();
        frame.tabPane().requestFocus();

        // set the static context set managed by the TabPane

        setContextSet(frame.tabPane());

        // start workers
        startDaemons();
        
		// turn on NumLock
logger.debug ("before NumLock");		
		Toolkit.getDefaultToolkit().setLockingKeyState( 
			KeyEvent.VK_NUM_LOCK, true );
logger.debug ("after NumLock");

        // warm up application
        if ( warmupContext > 0 ){ 
			logger.debug ("warmup started");
			try {
				if (site == null)
					site = Site.getDefault();
				new PosContext(site.siteID(), 1, 1, warmupContext); 
			} catch (Exception e) {
			}        
			logger.debug ("warmup finished");        
        }
    }

    /** Extends base title with values from site table*/
    protected String getTitle() {
        String title = super.getTitle();
        try {
            Site site = Site.getDefault();
            if (site != null){
                title += " - " + site.name() + ", " + site.addr1();
            }
        } catch (Exception e) {
            logger.error ("",e);
        }
        return title;
    }


    /**
     * main
     */
    public static void main(String[] args) {
        new GPOS().start(args);
    }

    /**
     * Find this pos in the dtabase, if it's there use it
     * if it is not create it as a new one. This allows
     * adding pos to the store on the fly.
     */
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

            if (pos != null && pos.posNo() == posno) {
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
}


