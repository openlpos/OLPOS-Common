package jpos.config.simple;

///////////////////////////////////////////////////////////////////////////////
//
// This software is provided "AS IS".  The JavaPOS working group (including
// each of the Corporate members, contributors and individuals)  MAKES NO
// REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED 
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
// NON-INFRINGEMENT. The JavaPOS working group shall not be liable for
// any damages suffered as a result of using, modifying or distributing this
// software or its derivatives. Permission to use, copy, modify, and distribute
// the software and its documentation for any purpose is hereby granted. 
//
///////////////////////////////////////////////////////////////////////////////

import java.util.*;
import java.io.*;
import java.net.URL;

import jpos.config.*;
import jpos.loader.*;
import jpos.test.*;

/**
 * A JUnit TestCase for the JposEntryRegistry interface and the 
 * SimpleEntryRegistry implementation class
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class SimpleRegPopulatorTestCase extends AbstractRegPopulatorTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public SimpleRegPopulatorTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() throws IOException
    {
		populator = new SimpleRegPopulator();
		populator2 = new SimpleRegPopulator();

		entriesVector = new Vector();
		entries = new JposEntry[ MAX_ENTRIES ];
		for( int i = 0; i < MAX_ENTRIES; ++i )
		{
			entries[ i ] = createJposEntry( LOGICAL_NAME_PROP_NAME + i, SI_FACTORY_CLASS_PROP_NAME,
											SERVICE_CLASS_PROP_NAME, VENDOR_NAME_PROP_NAME,
											VENDOR_URL_PROP_NAME, DEVICE_CATEGORY_PROP_NAME,
											JPOS_VERSION_PROP_NAME, PRODUCT_NAME_PROP_NAME,
											PRODUCT_DESCRIPTION_PROP_NAME, PRODUCT_URL_PROP_NAME,
											populator );
			entriesVector.add( entries[ i ] );
		}

		deleteFileIfExists( JCL_CFG_FILE_NAME );
		
		( new File( JCL_CFG_FILE_NAME ) ).createNewFile();

		assertTrue( "Could not create the empty file: " + JCL_CFG_FILE_NAME,
					( new File( JCL_CFG_FILE_NAME ) ).exists() );
    }

	protected void tearDown() throws IOException 
    {
		populator = null;
		populator2 = null;
		for( int i = 0; i < MAX_ENTRIES; ++i )
			entries[ i ] = null;

		entries = null;

		deleteFileIfExists( JCL_CFG_FILE_NAME );
    }

	//-------------------------------------------------------------------------
	// Private methods
	//

	private void deleteFileIfExists( String fileName ) throws IOException
	{
		File cfgFile = new File( fileName );
		if( cfgFile.exists() ) cfgFile.delete();
	}

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testRegPopulatorGetUniqueId()
	{
		assertTrue( "populator.getUniqueId() cannot be null",
				    populator.getUniqueId() != null );

		assertTrue( "populator.getUniqueId() cannot be empty string",
				    "".equals( populator.getUniqueId() ) != true );
	}

	public void testRegPopulatorGetClassName()
	{
		assertTrue( "populator.getClassName() cannot be null",
				    populator.getClassName() != null );

		assertTrue( "populator.getClassName() cannot be empty string",
				    "".equals( populator.getClassName() ) != true );

		assertTrue( "populator.getClassName() should be jpos.config.simple.SimpleRegPopulator",
				    "jpos.config.simple.SimpleRegPopulator".equals( populator.getClassName() ) );
	}

    public void testRegPopulatorSave1() throws Exception
	{
        Properties jclProps = new Properties();
		jclProps.put( "jpos.util.tracing", JPOS_UTIL_TRACING_VALUE );
		jclProps.put( "jpos.config.regPopulatorClass", "jpos.config.simple.SimpleRegPopulator" );
		jclProps.put( "jpos.config.populatorFile", JCL_CFG_FILE_NAME );
		createPropFile( jclProps );

		JposServiceLoader.getManager().getProperties().loadJposProperties();

		populator.load();
		assertTrue( "Should not find any entries here from populatorFile",
				    populator.getEntries().hasMoreElements() == false );
	
		populator.save( entriesVector.elements() );

		File cfgFile = new File( JCL_CFG_FILE_NAME );
		assertTrue( JCL_CFG_FILE_NAME + " was not created after calling populator.save()",
				    cfgFile.exists() );

		populator.load();
		assertTrue( "Should find entries here from populatorFile",
				    populator.getEntries().hasMoreElements() == true );
		
		assertTrue( "Entries should equals the entries saved",
				    JUnitUtility.isEquals( entriesVector.elements(), populator.getEntries() ) );

		populator.save( ( new Vector() ).elements() );

		cfgFile = new File( JCL_CFG_FILE_NAME );
		assertTrue( JCL_CFG_FILE_NAME + " was not created after calling populator.save()",
				    cfgFile.exists() );

		populator.load();
		assertTrue( "Should find entries here from populatorFile",
				    populator.getEntries().hasMoreElements() == false );
		
		restorePropFile();
	}

	public void testRegPopulatorSave2() throws Exception
	{
		populator.save( entriesVector.elements(), JCL_CFG_FILE_NAME );

		File cfgFile = new File( JCL_CFG_FILE_NAME );

		assertTrue( JCL_CFG_FILE_NAME + " was not created after calling populator.save()",
				    cfgFile.exists() );
	}			 

	public void testRegPopulatorLoad1() throws Exception
	{
        Properties jclProps = new Properties();
		jclProps.put( "jpos.util.tracing", JPOS_UTIL_TRACING_VALUE );
		jclProps.put( "jpos.config.regPopulatorClass", "jpos.config.simple.SimpleRegPopulator" );
		jclProps.put( "jpos.config.populatorFile", JCL_CFG_FILE_NAME );
		createPropFile( jclProps );

		JposServiceLoader.getManager().getProperties().loadJposProperties();

		populator.save( entriesVector.elements() );

		assertTrue( JCL_CFG_FILE_NAME + " was not created after calling populator.save()",
				    ( new File( JCL_CFG_FILE_NAME ) ).exists() );

		populator.load();
		assertTrue( "Should find entries here from populatorFile",
		 		    populator.getEntries().hasMoreElements() == true );
		
		assertTrue( "Entries should equals the entries saved",
				    JUnitUtility.isEquals( entriesVector.elements(), populator.getEntries() ) );

		restorePropFile();
	}

	public void testRegPopulatorLoad2() throws Exception
	{
		populator.save( entriesVector.elements(), JCL_CFG_FILE_NAME );
		
		populator2.load( JCL_CFG_FILE_NAME );

		Enumeration entries2 = populator2.getEntries();

		assertTrue( "Did not load( fileName ) any JposEntry objects",
				entries2 != null );

		assertTrue( "JposEntry found are not the ones saved",
				JUnitUtility.isEquals( entriesVector.elements(), entries2 ) );
	}

	public void testGetEntriesURL() throws Exception
	{
		populator2.load();

		URL entriesUrl = populator2.getEntriesURL();
		
		//<need-fixing>
		//assertTrue( "Entries should be in file: " + JCL_CFG_FILE_NAME,
		//		entriesUrl.equals( new URL( "file", "", JCL_CFG_FILE_NAME ) ) );
		//</need-fixing>
	}

	public void testGetName()
	{
		populator2.load();

		assertEquals( SimpleRegPopulator.SIMPLE_REG_POPULATOR_NAME_STRING,
					  populator2.getName() );
	}

	public void testGetLastLoadException()
	{
		emptyTest();
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private JposRegPopulator populator2 = null;
	private JposEntry[] entries = null;
	private Vector entriesVector = null;					   

	//-------------------------------------------------------------------------
	// Instance variables
	//

    public static final String JCL_CFG_FILE_NAME = TEST_DATA_PATH + "jcl_cfg_file.cfg";
	public static final int MAX_ENTRIES = 10;

	public static final String LOGICAL_NAME_PROP_NAME 			= "logicalName";
	public static final String SI_FACTORY_CLASS_PROP_NAME 		= "factoryClass";
	public static final String SERVICE_CLASS_PROP_NAME 			= "serviceClass";
	public static final String VENDOR_NAME_PROP_NAME			= "vendorName";
	public static final String VENDOR_URL_PROP_NAME 			= "http://www.vendorURL.com";
	public static final String DEVICE_CATEGORY_PROP_NAME 		= "POSPrinter";
	public static final String JPOS_VERSION_PROP_NAME 			= "1.6";
	public static final String PRODUCT_NAME_PROP_NAME 			= "productName";
	public static final String PRODUCT_DESCRIPTION_PROP_NAME 	= "productDescription";
	public static final String PRODUCT_URL_PROP_NAME 			= "http://www.productURL.com";
}																	   		
