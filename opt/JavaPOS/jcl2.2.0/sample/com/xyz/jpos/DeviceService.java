package com.xyz.jpos;

///////////////////////////////////////////////////////////////////////////////
//
// This software is provided "AS IS".  The JavaPOS working group (including
// each of the Corporate members, contributors and individals)  MAKES NO
// REPRESENTATIONS OR WARRRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED 
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
// NON-INFRINGEMENT. The JavaPOS working group shall not be liable for
// any damages suffered as a result of using, modifying or distributing this
// software or its derivatives. Permission to use, copy, modify, and distribute
// the software and its documentation for any purpose is hereby granted. 
//
///////////////////////////////////////////////////////////////////////////////

import jpos.*;
import jpos.services.*;

import jpos.config.JposEntry;
import jpos.loader.JposServiceInstance;

/**
 * Sample DeviceService example
 * NOTE: Uses a virtual LineDisplay implemented in Swing or AWT
 * NOTE2: Modified to show how to pass the JposEntry from the factory
 * NOTE3: This class (and all service class) must implement JposServiceInstance 
 * directly or indirectly since BaseService no longer extends JposServiceInstance
 * for further intitialization
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public abstract class DeviceService extends Object implements BaseService, JposServiceInstance
{
    /** Default ctor */
    public DeviceService() {}

    //--------------------------------------------------------------------------
    // Common properties
    //

    public String getCheckHealthText() throws JposException
    {
        return checkHealthText;
    }

    public boolean getClaimed() throws JposException
    {
        return claimed;
    }

    public boolean getDeviceEnabled() throws JposException
    {
        return deviceEnabled;
    }

    public void setDeviceEnabled( boolean deviceEnabled ) throws JposException
    {
        this.deviceEnabled = deviceEnabled;
    }

    public String getDeviceServiceDescription() throws JposException
    {
        return deviceServiceDescription;
    }

    public int getDeviceServiceVersion() throws JposException
    {
        return deviceServiceVersion;
    }

    public boolean getFreezeEvents() throws JposException
    {
        return freezeEvents;
    }

    public void setFreezeEvents(boolean freezeEvents) throws JposException
    {
    }

    public String getPhysicalDeviceDescription() throws JposException
    {
        return physicalDeviceDescription;
    }

    public String getPhysicalDeviceName() throws JposException
    {
        return physicalDeviceName;
    }

    public int getState() throws JposException
    {
        return state;
    }

    //--------------------------------------------------------------------------
    // Common methods
    //

    public void claim(int timeout) throws JposException
    {
        claimed = true;
    }

    public void close() throws JposException
    {
        checkHealthText = "";                     
        claimed = false;                  
        deviceEnabled = false;                  
        deviceServiceDescription = "";                     
        deviceServiceVersion = 1004000;                 
        freezeEvents = false;                  
        physicalDeviceDescription = "";                     
        physicalDeviceName = "";                     
        state = JposConst.JPOS_S_CLOSED;
                                                             
        EventCallbacks eventCallbacks      = null;                   
    }

    public void checkHealth(int level) throws JposException {}

    public void directIO(int command, int[] data, Object object) throws JposException {}

    public void open(String logicalName, EventCallbacks cb) throws JposException
    {
        eventCallbacks = cb;

        checkHealthText = "All is well and dandy!";
        claimed = false;                  
        deviceEnabled = false;                  
        deviceServiceDescription    = "JavaPOS LineDisplayService version 1.5";
        deviceServiceVersion = 1005000;
        freezeEvents = false;
        physicalDeviceDescription   = "A JavaPOS example of a LineDisplay Service";                            
        physicalDeviceName = "com.xxx.jpos.VirtualLineDisplay Example";
        state = JposConst.JPOS_S_IDLE;
    }

    public void release() throws JposException
    {
    }

    //--------------------------------------------------------------------------
    // jpos.config.JposServiceInstance methods
    //

    /**
     * Called when the JposServiceConnection is disconnected (i.e. service is closed)
	 * @since 1.2 (NY2K meeting)
     */
    public void deleteInstance() throws JposException {}

    //--------------------------------------------------------------------------
    // Package methods
    //

	/**
	 * Allows the JposServiceInstanceFactory to set the JposEntry associated with
	 * this DeviceService.  Subclasses can access the JposEntry with getter
	 * @since 1.2 (SF2K meeting)
	 */
	void setJposEntry( JposEntry entry ) 
	{ 
		jposEntry = entry; 

		//Of course after setting one can do further intialization here...
	}

	/** 
	 * @return the JposEntry object associated with this DeviceService
	 * @since 1.2 (SF2K meeting)
	 */
	JposEntry getJposEntry() { return jposEntry; }

    //--------------------------------------------------------------------------
    // Instance variables
    //

    protected String checkHealthText             = "";
    protected boolean claimed                    = false;
    protected boolean deviceEnabled              = false;
    protected String deviceServiceDescription    = "";
    protected int deviceServiceVersion           = 1005000;
    protected boolean freezeEvents               = false;
    protected String physicalDeviceDescription   = "";
    protected String physicalDeviceName          = ""; 
    protected int state                          = JposConst.JPOS_S_CLOSED;

    protected EventCallbacks eventCallbacks      = null;

	protected JposEntry jposEntry				 = null;
}

