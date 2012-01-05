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

/**
 * Sample LineDisplayService example
 * NOTE: Uses a virtual LineDisplay implemented in Swing or AWT
 * @since 0.1 (Philly 99 meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class LineDisplayService extends DeviceService implements LineDisplayService15
{
    /** Default ctor */
    public LineDisplayService() {}

    //--------------------------------------------------------------------------
    // Capabilities
    //

    public int getCapPowerReporting() throws JposException { return JposConst.JPOS_PR_NONE; }

    public int getCapBlink() throws JposException { return LineDisplayConst.DISP_CB_NOBLINK; }

    public boolean getCapBrightness() throws JposException { return false; }

    public int getCapCharacterSet() throws JposException { return LineDisplayConst.DISP_CCS_ASCII; }

    public boolean getCapDescriptors() throws JposException { return false; }

    public boolean getCapHMarquee() throws JposException { return false; }

    public boolean getCapICharWait() throws JposException { return false; }

    public boolean getCapVMarquee() throws JposException { return false; }

    //--------------------------------------------------------------------------
    // Common properties
    //

    public void setDeviceEnabled( boolean deviceEnabled ) throws JposException
    {
        super.setDeviceEnabled( deviceEnabled );
        vLD.setDeviceEnabled( deviceEnabled );
    }

    //--------------------------------------------------------------------------
    // Properties
    //

    public int getPowerNotify() throws JposException { return JposConst.JPOS_PN_DISABLED; }

    public void setPowerNotify(int powerNotify) throws JposException 
    {
    }

    public int getPowerState() throws JposException { return JposConst.JPOS_PS_UNKNOWN; }

    public int getCharacterSet() throws JposException
    {
        return characterSet;
    }

    public void setCharacterSet(int characterSet) throws JposException
    {
    }

    public String getCharacterSetList() throws JposException
    {
        return characterSetList;
    }

    public int getColumns() throws JposException
    {
        return columns;
    }

    public int getCurrentWindow() throws JposException
    {
        return currentWindow;
    }

    public void setCurrentWindow(int currentWindow) throws JposException
    {
    }

    public int getCursorColumn() throws JposException
    {
        return cursorColumn;
    }

    public void setCursorColumn(int cursorColumn) throws JposException
    {
    }

    public int getCursorRow() throws JposException
    {
        return cursorRow;
    }

    public void setCursorRow(int cursorRow) throws JposException
    {
    }

    public boolean getCursorUpdate() throws JposException
    {
        return cursorUpdate;
    }

    public void setCursorUpdate(boolean cursorUpdate) throws JposException
    {
    }

    public int getDeviceBrightness() throws JposException
    {
        return deviceBrightness;
    }

    public void setDeviceBrightness(int deviceBrightness ) throws JposException
    {
    }

    public int getDeviceColumns() throws JposException
    {
        return deviceColumns;
    }

    public int getDeviceDescriptors() throws JposException
    {
        return deviceDescriptors;
    }

    public int getDeviceRows() throws JposException
    {
        return deviceRows;
    }

    public int getDeviceWindows() throws JposException
    {
        return deviceWindows;
    }

    public int getInterCharacterWait() throws JposException
    {
        return interCharacterWait;
    }

    public void setInterCharacterWait(int interCharacterWait) throws JposException
    {
    }

    public int getMarqueeFormat() throws JposException
    {
        throw new JposException( JposConst.JPOS_E_ILLEGAL, "MarqeeType is not supported by the com.xxx.jpos.LineDisplayService" );
    }

    public void setMarqueeFormat(int marqueeFormat) throws JposException
    {
        throw new JposException( JposConst.JPOS_E_ILLEGAL, "MarqeeType is not supported by the com.xxx.jpos.LineDisplayService" );
    }

    public int getMarqueeRepeatWait() throws JposException
    {
        throw new JposException( JposConst.JPOS_E_ILLEGAL, "MarqeeType is not supported by the com.xxx.jpos.LineDisplayService" );
    }

    public void setMarqueeRepeatWait(int marqueeRepeatWait) throws JposException
    {
        throw new JposException( JposConst.JPOS_E_ILLEGAL, "MarqeeType is not supported by the com.xxx.jpos.LineDisplayService" );
    }

    public int getMarqueeType() throws JposException
    {
        throw new JposException( JposConst.JPOS_E_ILLEGAL, "MarqeeType is not supported by the com.xxx.jpos.LineDisplayService" );
    }

    public void setMarqueeType(int marqueeType) throws JposException
    {
        throw new JposException( JposConst.JPOS_E_ILLEGAL, "MarqeeType is not supported by the com.xxx.jpos.LineDisplayService" );
    }

    public int getMarqueeUnitWait() throws JposException 
    { 
        throw new JposException( JposConst.JPOS_E_ILLEGAL, "MarqeeUnitWait is not supported by the com.xxx.jpos.LineDisplayService" );
    }

    public void setMarqueeUnitWait(int marqueeUnitWait) throws JposException
    {
        throw new JposException( JposConst.JPOS_E_ILLEGAL, "MarqeeUnitWait is not supported by the com.xxx.jpos.LineDisplayService" );
    }

    public int getRows() throws JposException
    {
        return rows;
    }

    //--------------------------------------------------------------------------
    // Common overridden methods
    //

    public void open(String logicalName, EventCallbacks cb) throws JposException
    {
        super.open( logicalName, cb );

        vLD = ( new VirtualLDFactory() ).createLineDisplay( logicalName );

        if( vLD == null ) throw new JposException( JposConst.JPOS_E_FAILURE, "Could not create VirtualLineDisplay" );
    }

    public void close() throws JposException
    {
        super.close();

        vLD.close();
    }

    //--------------------------------------------------------------------------
    // Methods
    //

    public void clearDescriptors() throws JposException
    {
        vLD.clearDescriptors();
    }

    public void clearText() throws JposException
    {
        vLD.clearText();
    }

    public void createWindow( int viewportRow, int viewportColumn, int viewportHeight,
                              int viewportWidth, int windowHeight, int windowWidth ) throws JposException
    {                         

        vLD.createWindow( viewportRow, viewportColumn, viewportHeight, 
                          viewportWidth, windowHeight, windowWidth );

    }

    public void destroyWindow() throws JposException
    {
        vLD.destroyWindow();
    }

    public void displayText(String data, int attribute) throws JposException
    {
        vLD.displayText( data, attribute );
    }

    public void displayTextAt(int row, int column, String data, int attribute) throws JposException
    {
        vLD.displayTextAt( row, column, data, attribute );
    }

    public void refreshWindow(int window) throws JposException
    {
        vLD.refreshWindow( window );
    }

    public void scrollText(int direction, int units) throws JposException
    {
        vLD.scrollText( direction, units );
    }

    public void setDescriptor(int descriptor, int attribute) throws JposException
    {
        vLD.setDescriptor( descriptor, attribute );
    }

    //--------------------------------------------------------------------------
    // Private instance methods
    //

    //--------------------------------------------------------------------------
    // Instance variables
    //

    private int characterSet         = 0;
    private String characterSetList  = "";
    private int columns              = 0;
    private int currentWindow        = 0;
    private int cursorColumn         = 0;
    private int cursorRow            = 0;
    private boolean cursorUpdate     = false;
    private int deviceBrightness     = 0;
    private int deviceColumns        = 0;
    private int deviceDescriptors    = 0;
    private int deviceRows           = 0;
    private int deviceWindows        = 0;
    private int interCharacterWait   = 0;
    private int marqueeFormat        = 0;
    private int marqueeRepeatWait    = 0;
    private int marqueeType          = 0;
    private int marqueeUnitWait      = 0;
    private int rows                 = 0;

    private VirtualLineDisplay vLD   = null;
}

