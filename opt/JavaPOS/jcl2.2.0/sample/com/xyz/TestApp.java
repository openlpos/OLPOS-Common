package com.xyz;

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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import jpos.config.JposEntry;
import jpos.config.JposEntryRegistry;
import jpos.config.simple.SimpleEntry;
import jpos.config.simple.editor.JposEntryEditor;
import jpos.loader.JposServiceLoader;

/**
 * Sample Swing application using the sample virtual LineDisplay service
 * @since 0.1 (Philly 99 meeting)
 * @author EMM 
 */
public class TestApp extends Object
{
    //--------------------------------------------------------------------------
    // Class methods
    //

    /**
     * Creates an entry for the sample com.xyz.LineDisplay 
     * @since 0.1 (Philly 99 meeting)
     */
    private static void createJposEntry()
    {
        JposEntry jposEntry = new SimpleEntry();

        jposEntry.addProperty( JposEntry.LOGICAL_NAME_PROP_NAME, "com.xyz.MyLD" );
        jposEntry.addProperty( JposEntry.SI_FACTORY_CLASS_PROP_NAME, "com.xyz.jpos.XyzJposServiceInstanceFactory" );
        jposEntry.addProperty( JposEntry.SERVICE_CLASS_PROP_NAME, "com.xyz.jpos.LineDisplayService" );
        jposEntry.addProperty( JposEntry.VENDOR_NAME_PROP_NAME, "Xyz, Corp." );
        jposEntry.addProperty( JposEntry.VENDOR_URL_PROP_NAME, "http://www.javapos.com" );
        jposEntry.addProperty( JposEntry.DEVICE_CATEGORY_PROP_NAME, "LineDisplay" );
        jposEntry.addProperty( JposEntry.JPOS_VERSION_PROP_NAME, "1.4a" );
        jposEntry.addProperty( JposEntry.PRODUCT_NAME_PROP_NAME, "Virtual LineDisplay JavaPOS Service" );
        jposEntry.addProperty( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME, "Example virtual LineDisplay JavaPOS Service from virtual Xyz Corporation" );
        jposEntry.addProperty( JposEntry.PRODUCT_URL_PROP_NAME, "http://www.javapos.com" );

		JposEntryRegistry registry = JposServiceLoader.getManager().getEntryRegistry();
		registry.addJposEntry( jposEntry );
    }

    /**
     * Main entry point for the sample TestApp
     * @since 0.1 (Philly 99 meeting)
     */
    public static void main( String[] args )
    {
        createJposEntry();

        ( new TestAppFrame() ).setVisible( true );
    }
}

/**
 * Sample Swing frame for the test application
 * @since 0.1 (Philly 99 meeting)
 * @author EMM 
 */
class TestAppFrame extends JFrame
{
    /**
     * Default ctor
     * @since 0.1 (Philly 99 meeting)
     */
    public TestAppFrame()
    {
        super( "Sample com.xyz.jpos.LineDisplay Test Application" );

        getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS ) );

        JPanel jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        jPanel.add( new JLabel( "Logical name:" ) );
        jPanel.add( logicalNameTextField );
        getContentPane().add( jPanel );

        jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        jPanel.add( openButton );
        jPanel.add( closeButton );
        jPanel.add( claimButton );
        jPanel.add( enableCheckBox );
        getContentPane().add( jPanel );
      
        jPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
        jPanel.add( new JLabel( "Text to display:" ) );
        jPanel.add( displayTextField );
        jPanel.add( displayTextButton );
        jPanel.add( clearTextButton );
        getContentPane().add( jPanel );

        JMenu fileMenu = new JMenu( "File" );
        JMenuItem editorMenuItem = new JMenuItem( "Editor..." );
        JMenuItem exitMenuItem = new JMenuItem( "Exit" );

        fileMenu.add( editorMenuItem );
        fileMenu.addSeparator();
        fileMenu.add( exitMenuItem );

        JMenuBar menuBar = new JMenuBar();
        menuBar.add( fileMenu );

        setJMenuBar( menuBar );

        addWindowListener(  new WindowAdapter()
                            {
                                public void windowClosing(WindowEvent e) 
                                { exit(); }
                            }
                         );

        openButton.addActionListener(   new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e) 
                                            { open(); }
                                        }
                                    );

        closeButton.addActionListener(  new ActionListener()                         
                                        {                                            
                                            public void actionPerformed(ActionEvent e)
                                            { close(); }                               
                                        }                                            
                                     );                                             

        claimButton.addActionListener(  new ActionListener()                         
                                        {                                            
                                            public void actionPerformed(ActionEvent e)
                                            { claim(); }
                                        }
                                     );                                             
                                                                                  

        enableCheckBox.addActionListener(   new ActionListener()                         
                                            {                                            
                                                public void actionPerformed(ActionEvent e)
                                                { deviceEnabled(); }                               
                                            }                                            
                                        );                                             
                                                                                     
        displayTextButton.addActionListener(    new ActionListener()                         
                                                {                                            
                                                    public void actionPerformed(ActionEvent e)
                                                    { displayText(); }                               
                                                }                                            
                                           );                                             
                                                                                        
        clearTextButton.addActionListener(  new ActionListener()
                                            {                                            
                                                public void actionPerformed(ActionEvent e)
                                                { clearText(); }                               
                                            }                                            
                                         );


        editorMenuItem.addActionListener(   new ActionListener()
                                            {
                                                public void actionPerformed( ActionEvent e ) 
                                                { editor(); }
                                            }
                                        );

        exitMenuItem.addActionListener(   new ActionListener()                            
                                          {                                               
                                              public void actionPerformed( ActionEvent e )
                                              { exit(); }                               
                                          }                                               
                                      );                                                  

        pack();
        centerFrame();

        logicalNameTextField.setText( TEST_LOGICAL_NAME );
    }

    //--------------------------------------------------------------------------
    // Private instance methods
    //

    /**
     * Handles all JposException by showing some message
     * @param e the JposException object
     * @since 0.1 (Philly 99 meeting)
     */
    private void handleJposException( JposException e )
    {
        JOptionPane.showMessageDialog( this, "JposException with message = " + e.getMessage() + " and errorCode = " + e.getErrorCode(),
                                       "JposException occurred", JOptionPane.ERROR_MESSAGE );
    }

    /**
     * Centers the frame on the screen
     * @since 0.1 (Philly 99 meeting)
     */
    private void centerFrame()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setLocation( ( screenSize.width / 2 ) - ( getSize().width / 2 ),
                     ( screenSize.height / 2 ) - ( getSize().height / 2 ) );
    }

    //--------------------------------------------------------------------------
    // Public Instance methods
    //

    /**
     * Called when exit or close is clicked
     * @since 0.1 (Philly 99 meeting)
     */
    void exit()
    {
        try
        {
            if( ld.getState() != JposConst.JPOS_S_CLOSED )
                ld.close();
        }
        catch( JposException e ) { handleJposException( e ); }

        System.exit( 0 );
    }

    /**
     * Called when "Open" button is clicked
     * @since 0.1 (Philly 99 meeting)
     */
    void open()
    {
        try { ld.open( logicalNameTextField.getText() ); }
        catch( JposException e ) { handleJposException( e ); }
    }

    /**
     * Called when "Close" button is clicked
     * @since 0.1 (Philly 99 meeting)
     */
    void close()
    {
        try { ld.close(); }
        catch( JposException e ) { handleJposException( e ); }
    }

    /**
     * Called when "Claim" button is clicked 
     * @since 0.1 (Philly 99 meeting)
     */
    void claim()
    {
        try { ld.claim( 1000 ); }
        catch( JposException e ) { handleJposException( e ); }
    }

    /**
     * Called when "DeviceEnabled" button is clicked
     * @since 0.1 (Philly 99 meeting)
     */
    void deviceEnabled()
    {
        try { ld.setDeviceEnabled( enableCheckBox.isSelected() ); }
        catch( JposException e ) { handleJposException( e ); }
    }

    /**
     * Called when "DisplayText" button is clicked
     * @since 0.1 (Philly 99 meeting)
     */
    void displayText()
    {
        try { ld.displayText( displayTextField.getText(), 0 ); }
        catch( JposException e ) { handleJposException( e ); }
    }

    /**
     * Called when "ClearText" button is clicked
     * @since 0.1 (Philly 99 meeting)
     */
    void clearText()
    {
        try { ld.clearText(); }
        catch( JposException e ) { handleJposException( e ); }
    }

    /**
     * Called when the "Editor" menu item is selected
     * @since 1.2 (NY 2K meeting)
     */
    void editor()
    {
        Cursor currentCursor = getCursor();

        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

        JposEntryEditor.setDefaultFrameCloseOperation( JposEntryEditor.HIDE_ON_CLOSE );
        JposEntryEditor.setFrameVisible( true );

        setCursor( currentCursor );
    }

    //--------------------------------------------------------------------------
    // Instance variables
    //

    private LineDisplay ld = new LineDisplay();

    private JTextField logicalNameTextField = new JTextField( 10 );
    private JTextField displayTextField = new JTextField( 20 );
   
    private JButton openButton = new JButton( "Open" );
    private JButton closeButton = new JButton( "Close" );
    private JButton claimButton = new JButton( "Claim" );
    private JCheckBox enableCheckBox = new JCheckBox( "DeviceEnabled" );

    private JButton displayTextButton = new JButton( "displayText" );
    private JButton clearTextButton = new JButton( "clearText" );

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String TEST_LOGICAL_NAME = "com.xyz.MyLD";
}
