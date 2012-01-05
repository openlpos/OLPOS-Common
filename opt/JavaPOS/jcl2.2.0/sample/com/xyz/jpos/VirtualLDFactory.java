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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Factory class to create a VirtualLD device
 * @since 0.1 (Philly 99 meeting)
 * @author EMM 
 */
public class VirtualLDFactory extends Object
{
    public VirtualLDFactory() {}

    public VirtualLineDisplay createSwingLineDisplay( String logicalName ) { return new SwingLD( logicalName ); }

    public VirtualLineDisplay createConsoleLineDisplay( String logicalName ) { return new ConsoleLD( logicalName ); }

    public VirtualLineDisplay createLineDisplay( String logicalName )
    {
        VirtualLineDisplay vLD = null;

        try
        {
            Class.forName( "javax.swing.JFrame" );

            vLD = createSwingLineDisplay( logicalName );
        }
        catch( Exception e )
        { vLD = createConsoleLineDisplay( logicalName ); }

        return vLD;
    }
}

/**
 * Swing implementation of the VirtualLineDisplay interface
 * @since 0.1 (Philly 99 meeting)
 * @author EMM 
 */
class SwingLD extends JFrame implements VirtualLineDisplay
{
    public SwingLD( String logicalName )
    {
        super( "VirtualLineDisplay: " + logicalName );

        getContentPane().setLayout( new FlowLayout() );

        getContentPane().add( ldTextArea );

        setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );

        addWindowListener(  new WindowAdapter()
                            {
                                public void windowClosing( WindowEvent e )
                                { exit(); }
                            }
                         );

        pack();
        centerFrame();
    }

    //--------------------------------------------------------------------------
    // Instance variables
    //

    private LDTextArea ldTextArea = this.new LDTextArea();

    //--------------------------------------------------------------------------
    // Private/protected helper methods
    //

    private void centerFrame()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setLocation( ( screenSize.width / 2 ) - ( getSize().width / 2 ),
                     ( screenSize.height / 2 ) - ( getSize().height / 2 ) );
    }

    private void exit() {}

    //--------------------------------------------------------------------------
    // Inner classes
    //

    private class LDTextArea extends JTextArea
    {
        public LDTextArea()
        {
            super( 2, 20 );

            setFont( new Font( "Courier", Font.BOLD, 20 ) );
            setBackground( Color.green );
            setBorder( BorderFactory.createLineBorder( Color.black ) );
            setEnabled( false );
        }
    }

    //--------------------------------------------------------------------------
    // Common properties
    //

    public void setDeviceEnabled( boolean deviceEnabled ) throws JposException
    { setVisible( deviceEnabled ); }

    public void setFreezeEvents( boolean freezeEvents ) throws JposException
    {
    }

    //--------------------------------------------------------------------------
    // Properties
    //

    public int getPowerNotify() throws JposException { return JposConst.JPOS_PN_DISABLED; }

    public void setPowerNotify(int powerNotify) throws JposException {}

    public int getPowerState() throws JposException { return JposConst.JPOS_PS_UNKNOWN; }

    public int getCharacterSet() throws JposException { return 0; }

    public void setCharacterSet(int characterSet) throws JposException
    {
    }

    public String getCharacterSetList() throws JposException { return ""; }

    public int getColumns() throws JposException { return 0; }

    public int getCurrentWindow() throws JposException { return 0; }

    public void setCurrentWindow(int currentWindow) throws JposException
    {
    }

    public int getCursorColumn() throws JposException { return 0; }

    public void setCursorColumn(int cursorColumn) throws JposException
    {
    }

    public int getCursorRow() throws JposException { return 0; }

    public void setCursorRow(int cursorRow) throws JposException
    {
    }

    public boolean getCursorUpdate() throws JposException { return false; }

    public void setCursorUpdate(boolean cursorUpdate) throws JposException
    {
    }

    public int getDeviceBrightness() throws JposException { return 0; }

    public void setDeviceBrightness(int deviceBrightness ) throws JposException
    {
    }

    public int getDeviceColumns() throws JposException { return 0; }

    public int getDeviceDescriptors() throws JposException { return 0; }

    public int getDeviceRows() throws JposException { return 0; }

    public int getDeviceWindows() throws JposException { return 0; }

    public int getInterCharacterWait() throws JposException { return 0; }

    public void setInterCharacterWait(int interCharacterWait) throws JposException
    {
    }

    public int getMarqueeFormat() throws JposException { return 0; }

    public void setMarqueeFormat(int marqueeFormat) throws JposException
    {
    }

    public int getMarqueeRepeatWait() throws JposException { return 0; }

    public void setMarqueeRepeatWait(int marqueeRepeatWait) throws JposException       
    {
    }

    public int getMarqueeType() throws JposException { return 0; }

    public void setMarqueeType(int marqueeType) throws JposException
    {
    }

    public int getMarqueeUnitWait() throws JposException { return 0; }

    public void setMarqueeUnitWait(int marqueeUnitWait) throws JposException
    {
    }

    public int getRows() throws JposException { return 0; }

    //--------------------------------------------------------------------------
    // Common methods
    //

    public void claim(int timeout) throws JposException
    {
    }

    public void close() throws JposException
    {
        ldTextArea.setText( "" );
        setVisible( false );
    }

    public void checkHealth(int level) throws JposException {}

    public void directIO(int command, int[] data, Object object) throws JposException {}

    public void open(String logicalName, EventCallbacks cb) throws JposException {}

    public void release() throws JposException
    {
    }

    //--------------------------------------------------------------------------
    // Methods
    //

    public void clearDescriptors() throws JposException
    {
    }

    public void clearText() throws JposException { ldTextArea.setText( "" ); }

    public void createWindow( int viewportRow, int viewportColumn, int viewportHeight,
                              int viewportWidth, int windowHeight, int windowWidth ) throws JposException {}

    public void destroyWindow() throws JposException {}

    public void displayText(String data, int attribute) throws JposException
    {
        ldTextArea.append( data );
    }

    public void displayTextAt(int row, int column, String data, int attribute) throws JposException
    {
        StringBuffer sb1 = new StringBuffer( 20 );
        StringBuffer sb2 = new StringBuffer( 20 );

        String contents = ldTextArea.getText();

        if( contents.length() > 20 )
            sb1.append( contents.substring( 0, 19 ) );

        if( contents.length() > 20 )
            sb2.append( contents.substring( 20, contents.length() ) );

        if( row == 0 )
        {
            String row0 = "";

            String begin = sb1.toString().substring( 0, column );

            if( ( data.length() < ( 20 - column ) ) && ( sb1.toString().length() >= column + data.length() ) )
                row0 = begin + data + sb1.toString().substring( column + data.length() );

            sb1 = new StringBuffer( row0 );
        }
        else
        {
            String row1 = "";

            String begin = sb2.toString().substring( 0, column );

            if( ( data.length() < ( 20 - column ) ) && ( sb2.toString().length() >= column + data.length() ) )
                row1 = begin + data + sb2.toString().substring( column + data.length() );

            sb2 = new StringBuffer( row1 );
        }

        ldTextArea.setText( "" );
        ldTextArea.append( sb1.toString() + sb2.toString() );
    }

    public void refreshWindow(int window) throws JposException {}

    public void scrollText(int direction, int units) throws JposException
    {
    }

    public void setDescriptor(int descriptor, int attribute) throws JposException
    {
    }
}

/**
 * Implements the VirutalLineDisplay by displaying all text on the console
 * @since 0.1 (Philly 99 meeting)
 * @author EMM 
 */
class ConsoleLD extends JFrame implements VirtualLineDisplay
{
    public ConsoleLD( String logicalName )
    {
        this.logicalName = logicalName;
    }

    //--------------------------------------------------------------------------
    // Common properties
    //

    public void setDeviceEnabled( boolean deviceEnabled ) throws JposException
    {
    }

    public void setFreezeEvents(boolean freezeEvents) throws JposException
    {
    }

    //--------------------------------------------------------------------------
    // Properties
    //

    public int getPowerNotify() throws JposException { return JposConst.JPOS_PN_DISABLED; }

    public void setPowerNotify(int powerNotify) throws JposException {}

    public int getPowerState() throws JposException { return JposConst.JPOS_PS_UNKNOWN; }

    public int getCharacterSet() throws JposException { return characterSet; }

    public void setCharacterSet(int characterSet) throws JposException
    {
        this.characterSet = characterSet;
    }

    public String getCharacterSetList() throws JposException { return characterSetList; }

    public int getColumns() throws JposException { return columns; }

    public int getCurrentWindow() throws JposException { return currentWindow; }

    public void setCurrentWindow(int currentWindow) throws JposException
    {
    }

    public int getCursorColumn() throws JposException { return cursorColumn; }

    public void setCursorColumn(int cursorColumn) throws JposException
    {
    }

    public int getCursorRow() throws JposException { return cursorRow; }

    public void setCursorRow(int cursorRow) throws JposException
    {
    }

    public boolean getCursorUpdate() throws JposException { return cursorUpdate; }

    public void setCursorUpdate(boolean cursorUpdate) throws JposException
    {
    }

    public int getDeviceBrightness() throws JposException { return deviceBrightness; }

    public void setDeviceBrightness(int deviceBrightness ) throws JposException
    {
    }

    public int getDeviceColumns() throws JposException { return deviceColumns; }

    public int getDeviceDescriptors() throws JposException { return deviceDescriptors; }

    public int getDeviceRows() throws JposException { return deviceRows; }

    public int getDeviceWindows() throws JposException { return deviceWindows; }

    public int getInterCharacterWait() throws JposException { return interCharacterWait; }

    public void setInterCharacterWait(int interCharacterWait) throws JposException
    {
    }

    public int getMarqueeFormat() throws JposException { return marqueeFormat; }

    public void setMarqueeFormat(int marqueeFormat) throws JposException
    {
    }

    public int getMarqueeRepeatWait() throws JposException { return marqueeRepeatWait; }

    public void setMarqueeRepeatWait(int marqueeRepeatWait) throws JposException       
    {
    }

    public int getMarqueeType() throws JposException { return marqueeType; }

    public void setMarqueeType(int marqueeType) throws JposException
    {
    }   

    public int getMarqueeUnitWait() throws JposException { return marqueeUnitWait; }

    public void setMarqueeUnitWait(int marqueeUnitWait) throws JposException
    {
    }

    public int getRows() throws JposException { return rows; }

    //--------------------------------------------------------------------------
    // Common methods
    //

    public void claim(int timeout) throws JposException
    {
    }

    public void close() throws JposException
    {
    }

    public void checkHealth(int level) throws JposException {}

    public void directIO(int command, int[] data, Object object) throws JposException {}

    public void open(String logicalName, EventCallbacks cb) throws JposException {}

    public void release() throws JposException
    {
    }

    //--------------------------------------------------------------------------
    // Methods
    //

    public void clearDescriptors() throws JposException
    {
    }

    public void clearText() throws JposException
    {
    }

    public void createWindow( int viewportRow, int viewportColumn, int viewportHeight,
                              int viewportWidth, int windowHeight, int windowWidth ) throws JposException
    {
    }

    public void destroyWindow() throws JposException
    {
    }

    public void displayText(String data, int attribute) throws JposException
    {
    }

    public void displayTextAt(int row, int column, String data, int attribute) throws JposException
    {
    }

    public void refreshWindow(int window) throws JposException
    {
    }

    public void scrollText(int direction, int units) throws JposException
    {
    }

    public void setDescriptor(int descriptor, int attribute) throws JposException
    {
    }

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

    private String logicalName       = "Unknown";
}
