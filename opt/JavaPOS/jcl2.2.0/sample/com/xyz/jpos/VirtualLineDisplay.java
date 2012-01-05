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
 * Sample VirtualLineDisplay device
 * @since 0.1 (Philly 99 meeting)
 * @author EMM 
 */
public interface VirtualLineDisplay
{
    //--------------------------------------------------------------------------
    // Common Properties
    //

    public void setDeviceEnabled( boolean deviceEnabled ) throws JposException;
   
    public void setFreezeEvents(boolean freezeEvents) throws JposException;
   
    //--------------------------------------------------------------------------
    // Properties
    //

    public int getPowerNotify() throws JposException;

    public void setPowerNotify(int powerNotify) throws JposException;

    public int getPowerState() throws JposException;

    public int getCharacterSet() throws JposException;

    public void setCharacterSet(int characterSet) throws JposException;

    public String getCharacterSetList() throws JposException;

    public int getColumns() throws JposException;

    public int getCurrentWindow() throws JposException;

    public void setCurrentWindow(int currentWindow) throws JposException;

    public int getCursorColumn() throws JposException;

    public void setCursorColumn(int cursorColumn) throws JposException;

    public int getCursorRow() throws JposException;

    public void setCursorRow(int cursorRow) throws JposException;

    public boolean getCursorUpdate() throws JposException;

    public void setCursorUpdate(boolean cursorUpdate) throws JposException;

    public int getDeviceBrightness() throws JposException;

    public void setDeviceBrightness(int deviceBrightness ) throws JposException;

    public int getDeviceColumns() throws JposException;

    public int getDeviceDescriptors() throws JposException;

    public int getDeviceRows() throws JposException;

    public int getDeviceWindows() throws JposException;

    public int getInterCharacterWait() throws JposException;

    public void setInterCharacterWait(int interCharacterWait) throws JposException;

    public int getMarqueeFormat() throws JposException;

    public void setMarqueeFormat(int marqueeFormat) throws JposException;

    public int getMarqueeRepeatWait() throws JposException;

    public void setMarqueeRepeatWait(int marqueeRepeatWait) throws JposException;

    public int getMarqueeType() throws JposException;

    public void setMarqueeType(int marqueeType) throws JposException;

    public int getMarqueeUnitWait() throws JposException;

    public void setMarqueeUnitWait(int marqueeUnitWait) throws JposException;

    public int getRows() throws JposException;

    //--------------------------------------------------------------------------
    // Common methods
    //

    public void claim(int timeout) throws JposException;

    public void close() throws JposException;

    public void checkHealth(int level) throws JposException;

    public void directIO(int command, int[] data, Object object) throws JposException;

    public void open(String logicalName, EventCallbacks cb) throws JposException;

    public void release() throws JposException;

    //--------------------------------------------------------------------------
    // Methods
    //

    public void clearDescriptors() throws JposException;

    public void clearText() throws JposException;

    public void createWindow( int viewportRow, int viewportColumn, int viewportHeight,
                              int viewportWidth, int windowHeight, int windowWidth ) throws JposException;

    public void destroyWindow() throws JposException;

    public void displayText(String data, int attribute) throws JposException;

    public void displayTextAt(int row, int column, String data, int attribute) throws JposException;

    public void refreshWindow(int window) throws JposException;

    public void scrollText(int direction, int units) throws JposException;

    public void setDescriptor(int descriptor, int attribute) throws JposException;
}
