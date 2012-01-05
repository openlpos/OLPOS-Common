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

package com.globalretailtech.pos.services;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import org.apache.log4j.Logger;


/**
 * Super class for JFC JPOS services.
 * Opens property file based on service name, from
 * there it figures out the size and layout for the
 * service. Creates a device properties pane used
 * to show the configued properties.
 *
 * @author  Quentin Olson
 * @see
 */
public class BaseServiceAdapter extends JPanel implements jpos.services.BaseService, jpos.JposConst {
	Logger logger = Logger.getLogger(BaseServiceAdapter.class.getName());

    // Event management

    static private int EventCount;
    static private Vector event_list;

    // Jpos Properties

    private boolean AutoDisable;
    private int CapPowerReporting;
    private String CheckHealthText;
    private boolean Claimed;
    private int DataCount;
    private boolean DataEventEnabled;
    private boolean DeviceEnabled;
    private boolean FreezeEvents;
    private int OutputID;
    private int PowerNotify;
    private int PowerState;
    private int State;
    private String DeviceControlDescription;
    private int DeviceControlVersion = 1002000;  //default
    private String DeviceServiceDescription;
    private int DeviceServiceVersion = 1002000;  //default
    private String PhysicalDeviceDescription;
    private String PhysicalDeviceName;

    /** X position in grid */
    public int x;
    /** Y position in grid */
    public int y;
    /** Height position in grid */
    public int h;
    /** Width position in grid */
    public int w;

    private jpos.services.EventCallbacks callbacks;

    /** the device panel (properties) for this device */
    public JPanel devPanel() {
        return dev;
    }

    private JPanel dev;
    private com.globalretailtech.util.ShareProperties prop;

    /** property list */
    public Properties propList() {
        return prop;
    }

    private static String name;

    /**
     * Constructor creates the device panel based on name.
     */
    public BaseServiceAdapter(String device_name) {

        super();
        name = device_name;
        event_list = new Vector();

        prop = new com.globalretailtech.util.ShareProperties(device_name);
        setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), prop.getProperty("Description", "")));

        dev = new JPanel();
        add(dev);
        dev.setVisible(true);
        setVisible(true);
    }

    /**
     * Initializes the properties for this device then
     * adds the device info as a tab.
     */
    public void create(JTabbedPane tp) {
        JPanel p = new JPanel();
        initProperties(p);
        try {
            tp.addTab(getDeviceServiceDescription(), p);
        } catch (jpos.JposException e) {
            System.out.println(e);
        }
    }

    /**
     * Read the properties and create the
     * layout.
     */
    void initProperties(JPanel p) {

        try {
            setDeviceServiceDescription(prop.getProperty("Description", ""));
            x = Integer.valueOf(prop.getProperty("x", "")).intValue();
            y = Integer.valueOf(prop.getProperty("y", "")).intValue();
            h = Integer.valueOf(prop.getProperty("h", "")).intValue();
            w = Integer.valueOf(prop.getProperty("w", "")).intValue();
        } catch (jpos.JposException e) {
            System.out.println(e);
        }

        p.setLayout(new BorderLayout());
        JPanel scrollPane = new JPanel(new BorderLayout());
        JPanel labelPane = new JPanel();
        labelPane.setLayout(new GridLayout(0, 1, 3, 3));
        JPanel fieldPane = new JPanel();
        fieldPane.setLayout(new GridLayout(0, 1, 3, 3));

        for (Enumeration e = prop.propertyNames(); e.hasMoreElements();) {
            String tmp = (String) e.nextElement();
            JLabel l = new JLabel(tmp);
            labelPane.add(l);
            JTextField t = new JTextField(prop.getProperty(tmp, ""), 30);
            fieldPane.add(t);
        }

        scrollPane.add(labelPane, BorderLayout.WEST);
        scrollPane.add(fieldPane, BorderLayout.EAST);
        JScrollPane scroller = new JScrollPane();
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setViewportView(scrollPane);
        p.add(scroller, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        JButton save = new JButton("Save Changes");
        JButton revert = new JButton("Revert");
        buttonPane.add(revert);
        buttonPane.add(save);
        p.add(buttonPane, BorderLayout.SOUTH);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /** See JPOS */
    public void fireEvent(jpos.events.DataEvent ev) {
    	if ( callbacks != null )
        	callbacks.fireDataEvent(ev);
        else
        	logger.warn ("No callbacks bound to Service!");	
    }

    /** See JPOS */
    public void fireEvent(jpos.events.DirectIOEvent ev) {
        callbacks.fireDirectIOEvent(ev);
    }

    // Jpos Implementation

    /** See JPOS */
    public String getCheckHealthText() throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        return CheckHealthText;
    }

    /** See JPOS */
    public void setCheckHealthText(String s) throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        CheckHealthText = s;
    }

    /** See JPOS */
    public boolean getClaimed() throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        return Claimed;
    }

    /** See JPOS */
    public void setClaimed(boolean b) throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        Claimed = b;
    }

    /** See JPOS */
    public boolean getDeviceEnabled() throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        return DeviceEnabled;
    }

    /** See JPOS */
    public void setDeviceEnabled(boolean b) throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        DeviceEnabled = b;
    }

    /** See JPOS */
    public String getDeviceServiceDescription() throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        return DeviceServiceDescription;
    }

    /** See JPOS */
    public void setDeviceServiceDescription(String s) throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        DeviceServiceDescription = s;
    }

    /** See JPOS */
    public int getDeviceServiceVersion() throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        return DeviceServiceVersion;
    }

    /** See JPOS */
    public void setDeviceServiceVersion(int v) throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        DeviceServiceVersion = v;
    }

    /** See JPOS */
    public boolean getFreezeEvents() throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        return FreezeEvents;
    }

    /** See JPOS */
    public void setFreezeEvents(boolean freezeEvents) throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        FreezeEvents = freezeEvents;
    }

    /** See JPOS */
    public String getPhysicalDeviceDescription() throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        return PhysicalDeviceDescription;
    }

    /** See JPOS */
    public void setPhysicalDeviceDescription(String s) throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        PhysicalDeviceDescription = s;
    }

    /** See JPOS */
    public String getPhysicalDeviceName() throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        return PhysicalDeviceName;
    }

    /** See JPOS */
    public void setPhysicalDeviceName(String s) throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        PhysicalDeviceName = s;
    }

    /** See JPOS */
    public int getState() throws jpos.JposException {
        if (State == JPOS_S_ERROR) {   // oops
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        return State;
    }

    /** See JPOS */
    public void setState(int s) throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
        State = s;
    }

    // Methods supported by all device services

    /** See JPOS */
    public void claim(int timeout) throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
    }

    /** See JPOS */
    public void close() throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
    }

    /** See JPOS */
    public void checkHealth(int level) throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
    }

    /** See JPOS */
    public void directIO(int command, int[] data, Object object) throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
    }

    public void open(String logicalName, jpos.services.EventCallbacks cb) throws jpos.JposException {
        /** See JPOS */

        callbacks = cb;

        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
    }

    /** See JPOS */
    public void release() throws jpos.JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new jpos.JposException(JPOS_S_ERROR));
        }
    }
}


