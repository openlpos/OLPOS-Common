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

package com.globalretailtech.pos.gui;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;


import com.globalretailtech.util.ShareProperties;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.hardware.*;

/**
 * The tabs section of the pos Application.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class PosTabs extends JTabbedPane implements ContextSet, PosGui {
	
	org.apache.log4j.Logger logger =
		org.apache.log4j.Logger.getLogger(PosTabs.class);

    private static int nTabs;
    private Vector contextlist;
    private String tabFont;
    private int tabFontSize;
    private int tabFontStyle;
    protected String defaultTabLabel;

    private Vector tabs;
    private int siteID;
    private int posID;
    private int posNo;
    private int configNo;
    private LogonPanel t;

	// we must keep control up
	private static Scanner scanner = null;
	    
    public PosTabs() {

        super();
        nTabs = 1;
        
        contextlist = new Vector();
        tabs = new Vector();

        ShareProperties prop = new ShareProperties(this.getClass().getName());
        if (prop.Found()) {

            tabFont = prop.getProperty("TabFont", "Helvetica");
            tabFontSize = new Integer(prop.getProperty("TabFontSize", "24")).intValue();
            tabFontStyle = new Integer(prop.getProperty("TabFontStyle", Font.ITALIC+"")).intValue();
            defaultTabLabel = prop.getProperty("DefaultTabLabel", "Logon");
        } else {
            tabFont = new String("Helvetica");
            tabFontSize = 24;
            tabFontStyle = Font.ITALIC;
            defaultTabLabel = new String("Logon");
        }

        setFont(new Font(tabFont, tabFontStyle, tabFontSize));
    }

    /**
     * Do a logon
     */
    public void logon(int sid, int pid, int pno, int cno) {

        siteID = sid;
        posID = pid;
        posNo = pno;
        configNo = cno;

        t = new LogonPanel(sid, pid, pno, cno, this);
        addTab(defaultTabLabel, t);
    }

    public void sizes() {
        t.sizes();
    }

    /**
     * Add a context/login.
     * Note: devices are added here? that means a device context per login?
     */
    public void addLogon(int index, PosContext context) {

		Component parent = SwingUtilities.getRoot((Component)this);
		if (parent != null && parent.isShowing())
		    parent.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // remove the old component
		
//		remove(getComponentAt(index));
		context.disableKeys();
		PosMenu newMenu = new PosMenu(context);
		addTab(context.toString(), newMenu);

		context.disableKeys();
		setSelectedComponent(newMenu);

        // The tab is a pos display
        contextlist.addElement(context);

        // create non-display devices

        new Keyboard(new jpos.POSKeyboard(), "POSKeyboard", this);
//        new Msr(new jpos.MSR(), "POSMsr", this);
        scanner = new Scanner(new jpos.Scanner(), "POSScanner", this);

        // set an index in the context for this tab

        context.setTabIndex(nTabs++);
		logger.debug ("added tab with index "+nTabs);
		
        // complete initialization for this context.
        // clears devices, creates an initial transaction and starts logon sequence

        context.posInit();
        context.guis().addElement(this);

        // create a new logon tab

//        addTab(defaultTabLabel, new LogonPanel(siteID, posID, posNo, configNo, this));
//
//        setSelectedComponent(newMenu);

		if (parent != null && parent.isShowing())
			parent.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

	/**
	 * Gives focus to the tab with specified context 
	 */
	public void focusContext(PosContext context) {
		logger.debug ("set focus for tab: "+context.tabIndex());
		this.setSelectedIndex(context.tabIndex());
		requestKeyboardFocus();
	}
	
    /**
     * Returns the pos context within the
     * curent tab. Used by shared devices to determine
     * the correct context to apply.
     */
    public PosContext currentContext() {
    	
    	// if getSelectedIndex() == null means Login tab is active
    	// Login tab has no assotiate context
    	
    	if ( contextlist.size() > 0 && getSelectedIndex() > 0 ){
			return ((PosContext) contextlist.elementAt(getSelectedIndex()-1)); // login tab has no context
    	}
    	return null;
    }

    public void init(PosContext c) {
    }

    public void home() {
    }

    public void clear() {
    }

    public void open() {
    }

    public void close() {

        PosContext context = (PosContext) contextlist.elementAt(getSelectedIndex()-1);
        context.setTabIndex(-1);
        contextlist.removeElementAt(getSelectedIndex()-1);
        removeTabAt(getSelectedIndex());
        context.close();
		nTabs--;
		
		LogonPanel logonPanel = (LogonPanel)getComponentAt(0); // logon
		if ( logonPanel != null)
			logonPanel.logout();

		requestKeyboardFocus();
    }

	private void requestKeyboardFocus() {
		Component c = getSelectedComponent();
		if ( c!= null ) 
			c.requestFocus();
	}

    public JComponent getGui() {
        return this;
    }

}


