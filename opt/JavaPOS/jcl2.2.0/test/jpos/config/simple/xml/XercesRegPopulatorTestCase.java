package jpos.config.simple.xml;

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

import jpos.config.*;
import jpos.config.simple.*;
import jpos.test.*;

/**
 * A JUnit TestCase for the Loading/saving XML entries
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class XercesRegPopulatorTestCase extends AbstractRegPopulatorTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public XercesRegPopulatorTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
    {
        xercesRegPopulator = new XercesRegPopulator();

        try
        {
            File file = new File( JCL_JUNIT_XML_FILE_NAME );
            file.delete();
        }                       
        catch( SecurityException se )
        {
            println( "Could not delete XML test file: " + JCL_JUNIT_XML_FILE_NAME );
            println( "   Exception message = " + se.getMessage() );
        }

		addToClasspath( TEST_DATA_PATH );
    }

	protected void tearDown() 
    {
        xercesRegPopulator = null;
    }

	//-------------------------------------------------------------------------
	// Private methods
	//

    private JposEntry createJposEntry( String logicalName, String factoryClass,
                                       String serviceClass, String vendorName,
                                       String vendorURL, String deviceCategory,
                                       String jposVersion, String productName,
                                       String productDescription, String productURL )
    {
        JposEntry jposEntry = new SimpleEntry();

        jposEntry.addProperty( JposEntry.LOGICAL_NAME_PROP_NAME, logicalName );
        jposEntry.addProperty( JposEntry.SI_FACTORY_CLASS_PROP_NAME, factoryClass );
        jposEntry.addProperty( JposEntry.SERVICE_CLASS_PROP_NAME, serviceClass );
        jposEntry.addProperty( JposEntry.VENDOR_NAME_PROP_NAME, vendorName );
        jposEntry.addProperty( JposEntry.VENDOR_URL_PROP_NAME, vendorURL );
        jposEntry.addProperty( JposEntry.DEVICE_CATEGORY_PROP_NAME, deviceCategory );
        jposEntry.addProperty( JposEntry.JPOS_VERSION_PROP_NAME, jposVersion );
        jposEntry.addProperty( JposEntry.PRODUCT_NAME_PROP_NAME, productName );
        jposEntry.addProperty( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME, productDescription );
        jposEntry.addProperty( JposEntry.PRODUCT_URL_PROP_NAME, productURL );

        return jposEntry;
    }

    private Enumeration searchEntriesForVendorName( Enumeration entries, String vendorName )
    {
        Vector v = new Vector();

        while( entries.hasMoreElements() )
        {
            JposEntry jposEntry = (JposEntry)entries.nextElement();

            if( jposEntry.hasPropertyWithName( JposEntry.VENDOR_NAME_PROP_NAME ) )
                if( jposEntry.getPropertyValue( JposEntry.VENDOR_NAME_PROP_NAME ).
                    toString().equals( JUNIT_CORP_STRING ) )
                    v.addElement( jposEntry );
        }

        return v.elements();
    }

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	/** 
     * Test the loading/saving of XML entries using the XercesRegPopulator 
     */
	public void testXercesPopulator1()
	{
        //Save and load an empty set of registry entries
        Vector v1 = new Vector();

        try
        {
            xercesRegPopulator.save( v1.elements(), JCL_JUNIT_XML_FILE_NAME );
            xercesRegPopulator.load( JCL_JUNIT_XML_FILE_NAME );

            Enumeration entries = xercesRegPopulator.getEntries();

            assertTrue( "Expected an empty set of entries...", JUnitUtility.isIdentical( entries, v1.elements() ) );
            assertTrue( "Expected an empty set of entries...", JUnitUtility.isEquals( entries, v1.elements() ) );
        }
        catch( Exception e )
        { fail( "Got unexpected Exception from XercesRegPopulator.save method with message = " + e.getMessage() ); }

        //Add some entries and save and load
        JposEntry entry1 = createJposEntry( "entry1", "com.xyz.jpos.XyzJposServiceInstanceFactory",
                                            "com.xyz.jpos.LineDisplayService", "Xyz, Corp.", 
                                            "http://www.javapos.com", "LineDisplay", "1.4a",
                                            "Virtual LineDisplay JavaPOS Service", 
                                            "Example virtual LineDisplay JavaPOS Service from virtual Xyz Corporation",
                                            "http://www.javapos.com" );

        JposEntry entry2 = createJposEntry( "entry2", "com.xyz.jpos.XyzJposServiceInstanceFactory",
                                            "com.xyz.jpos.LineDisplayService", "Xyz, Corp.", 
                                            "http://www.javapos.com", "LineDisplay", "1.4a",
                                            "Virtual LineDisplay JavaPOS Service", 
                                            "Example virtual LineDisplay JavaPOS Service from virtual Xyz Corporation",
                                            "http://www.javapos.com" );

        try
        {
            v1.clear();
            v1.addElement( entry1 );
            v1.addElement( entry2 );

            xercesRegPopulator.save( v1.elements(), JCL_JUNIT_XML_FILE_NAME );
            xercesRegPopulator.load( JCL_JUNIT_XML_FILE_NAME );

            Enumeration entries = xercesRegPopulator.getEntries();

            assertTrue( "Expected 2 entries...", JUnitUtility.isEquals( entries, v1.elements() ) );
        }
        catch( Exception e )
        { assertTrue( "Got unexpected Exception from XercesRegPopulator.save method with message = " + e.getMessage(), true ); }
        
        //Remove entries save and load reg
        v1.remove( entry1 );


        try
        {
            xercesRegPopulator.save( v1.elements(), JCL_JUNIT_XML_FILE_NAME );
            xercesRegPopulator.load( JCL_JUNIT_XML_FILE_NAME );

            Enumeration entries = xercesRegPopulator.getEntries();

            assertTrue( "Expected 1 entries...", JUnitUtility.isEquals( entries, v1.elements() ) );
        }
        catch( Exception e )
        { assertTrue( "Got unexpected Exception from XercesRegPopulator.save method with message = " + e.getMessage(), true ); }
	}

	/** 
     * Test the loading/saving of XML entries using the XercesRegPopulator 
     */
	public void testXercesPopulator2()
	{
        Vector v1 = new Vector();

        for( int i = 0; i < 100; i++ )
        {

            JposEntry entry = createJposEntry( "entry" + i, "com.xyz.jpos.XyzJposServiceInstanceFactory",
                                               "com.xyz.jpos.LineDisplayService", "Xyz, Corp.", 
                                               "http://www.javapos.com", "LineDisplay", "1.4a",
                                               "Virtual LineDisplay JavaPOS Service", 
                                               "Example virtual LineDisplay JavaPOS Service from virtual Xyz Corporation",
                                               "http://www.javapos.com" );
            v1.addElement( entry );
        }

        try
        {
            xercesRegPopulator.save( v1.elements(), JCL_JUNIT_XML_FILE_NAME );
            xercesRegPopulator.load( JCL_JUNIT_XML_FILE_NAME );

            Enumeration entries = xercesRegPopulator.getEntries();

            assertTrue( "Expected 100 entries...", JUnitUtility.isEquals( entries, v1.elements() ) );
        }
        catch( Exception e )
        { fail( "Got unexpected Exception from XercesRegPopulator.save method with message = " + e.getMessage() ); }
	}

	public void testGetName()
	{
		xercesRegPopulator.load( JCL_JUNIT_XML_FILE_NAME );

		assertEquals( XercesRegPopulator.XERCES_REG_POPULATOR_NAME_STRING,
					  xercesRegPopulator.getName() );
	}

	public void testLoad1()
	{
        try
        {
			assertTrue( "Expected file: " + TEST_DATA_PATH + DEFECT_6562_XML_FILE + " to exist",
						( new File( TEST_DATA_PATH + DEFECT_6562_XML_FILE ) ).exists() );

            xercesRegPopulator.load( DEFECT_6562_XML_FILE );
            Enumeration entries = xercesRegPopulator.getEntries();

			JposEntry defect6562Entry = (JposEntry)entries.nextElement();

			assertTrue( "defect6562Entry == null", defect6562Entry != null );
			assertTrue( "defect6562Entry.logicalName != defect6562", 
						defect6562Entry.getLogicalName().equals( "defect6562" ) );

        }
        catch( Exception e ) { fail( "Unexpected exception.message = " + e.getMessage() ); }
	}

	public void testLoadwithPropType()
	{
        try
        {
            assertTrue( "Expected file: " + TEST_DATA_PATH + JCL_JUNIT_TEST_PROP_TYPE_XML_FILE + " to exist",
						( new File( TEST_DATA_PATH + JCL_JUNIT_TEST_PROP_TYPE_XML_FILE ) ).exists() );

            xercesRegPopulator.load( JCL_JUNIT_TEST_PROP_TYPE_XML_FILE );
            Enumeration entries = xercesRegPopulator.getEntries();

			JposEntry testPropTypeEntry = (JposEntry)entries.nextElement();

			//<temp>
			//System.out.println( testPropTypeEntry );
			//</temp>

			assertTrue( "testPropTypeEntry == null", testPropTypeEntry != null );
			assertTrue( "testPropTypeEntry.logicalName != testPropType", 
						testPropTypeEntry.getLogicalName().equals( "testPropType" ) );

			assertTrue( "testPropTypeEntry.getProp( \"stringProp\" ).getType() != String.class", 
						testPropTypeEntry.getProp( "stringProp" ).getType().equals( String.class ) );

			assertTrue( "testPropTypeEntry.getProp( \"booleanProp\" ).getType() != Boolean.class", 
						testPropTypeEntry.getProp( "booleanProp" ).getType().equals( Boolean.class ) );
			
			assertTrue( "testPropTypeEntry.getProp( \"byteProp\" ).getType() != Byte.class", 
						testPropTypeEntry.getProp( "byteProp" ).getType().equals( Byte.class ) );
			
			assertTrue( "testPropTypeEntry.getProp( \"characterProp\" ).getType() != Character.class", 
						testPropTypeEntry.getProp( "characterProp" ).getType().equals( Character.class ) );
			
			assertTrue( "testPropTypeEntry.getProp( \"doubleProp\" ).getType() != Double.class", 
						testPropTypeEntry.getProp( "doubleProp" ).getType().equals( Double.class ) );
			
			assertTrue( "testPropTypeEntry.getProp( \"floatProp\" ).getType() != Float.class", 
						testPropTypeEntry.getProp( "floatProp" ).getType().equals( Float.class ) );
			
			assertTrue( "testPropTypeEntry.getProp( \"integerProp\" ).getType() != Integer.class", 
						testPropTypeEntry.getProp( "integerProp" ).getType().equals( Integer.class ) );
			
			assertTrue( "testPropTypeEntry.getProp( \"longProp\" ).getType() != Long.class", 
						testPropTypeEntry.getProp( "longProp" ).getType().equals( Long.class ) );

			assertTrue( "testPropTypeEntry.getProp( \"shortProp\" ).getType() != Short.class", 
						testPropTypeEntry.getProp( "shortProp" ).getType().equals( Short.class ) );
        }
        catch( Exception e ) { fail( "Unexpected exception.message = " + e.getMessage() ); }
	}

	public void testSaveWithPropType()
	{
		emptyTest();
	}

	public void testGetLastLoadException()
	{
		emptyTest();
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

    private XercesRegPopulator xercesRegPopulator = null;

	//-------------------------------------------------------------------------
	// Instance variables
	//

    public static final String JUNIT_CORP_STRING = "JUnit Corp.";
    
	public static final String JCL_JUNIT_XML_FILE_NAME = TEST_DATA_PATH + 
														   "jcl-junit.xml";
	public static final String DEFECT_6562_XML_FILE = "defect6562.xml";
	public static final String JCL_JUNIT_TEST_PROP_TYPE_XML_FILE = 
								 "jcl_junit_test_prop_type.xml";
}