package jpos.util;

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

import java.io.*;
import java.util.*;

import jpos.*;
import jpos.test.*;

/**
 * A JUnit test class for the jpos.util.JposProperties interface and
 * implementing classes
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class JposPropertiesTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public JposPropertiesTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
	{
		properties = new DefaultProperties();
	}
	
	protected void tearDown()
	{
		properties = null;
	}

	//-------------------------------------------------------------------------
	// Private methods
	//
	
	private void verifyPropFileExist() throws IOException
	{
		File propFile = new File( JPOS_JUNIT_PROPERTIES_FILE_NAME );

		InputStream propFileIs = ClassLoader.getSystemResourceAsStream( JPOS_JUNIT_PROPERTIES_FILE_NAME );

		if( !propFile.exists() && propFileIs == null )
			throw new IOException( "JUnit properties file: " + JPOS_JUNIT_PROPERTIES_FILE_NAME + " does not exist!" );
	}

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testIsLoadedAndGetLastException()
	{ 
		assertTrue( "Newly created properties should not be loaded", 
				properties.isLoaded() == false );

		assertTrue( "Newly created properties getLastException() should be null", 
				properties.getLastException() == null );

		properties.loadJposPropertiesByName( JPOS_JUNIT_PROPERTIES_FILE_NAME );

		assertTrue( "Loaded properties isLoaded() should return true", 
				properties.isLoaded() == true );

		assertTrue( "Loaded properties getLastException() should be null", 
				properties.getLastException() == null );
	}

	public void testFindProperties() throws IOException
	{
		verifyPropFileExist();

		Properties props = properties.findProperties( JPOS_JUNIT_PROPERTIES_FILE_NAME );

		assertTrue( "properties.findProperties( ... ) returned null Properties object",
				props != null );
	}

	public void testLoadJposProperties()
	{
		assertTrue( "Newly created properties should not be loaded", 
				properties.isLoaded() == false );

		properties.loadJposPropertiesByName( JPOS_JUNIT_PROPERTIES_FILE_NAME );

		assertTrue( "Loaded properties isLoaded() should return true", 
				properties.isLoaded() == true );
	}

	public void testGetPropertyString()
	{
		properties.loadJposPropertiesByName( JPOS_JUNIT_PROPERTIES_FILE_NAME );

		assertTrue( "All JposProperties should contain property \"jpos.loader.serviceManagerClass\"",
				properties.getPropertyString( "jpos.loader.serviceManagerClass" ) != null );


		assertTrue( "All JposProperties should contain property \"jpos.util.tracing\"",
				properties.getPropertyString( "jpos.util.tracing" ) != null );

		assertTrue( "JposProperties should NOT contain property \"__this.is.a.fake.property_name__\"",
				properties.getPropertyString( "__this.is.a.fake.property_name__" ) == null );
	}

	public void testIsPropertyDefined()
	{
		properties.loadJposPropertiesByName( JPOS_JUNIT_PROPERTIES_FILE_NAME );

		assertTrue( "All JposProperties should contain property \"jpos.loader.serviceManagerClass\"",
				properties.isPropertyDefined( "jpos.loader.serviceManagerClass" ) == true );


		assertTrue( "All JposProperties should contain property \"jpos.util.tracing\"",
				properties.isPropertyDefined( "jpos.util.tracing" ) == true );

		assertTrue( "JposProperties should NOT contain property \"__this.is.a.fake.property_name__\"",
				properties.isPropertyDefined( "__this.is.a.fake.property_name__" ) == false );
	}

	public void testGetPropertyNames()
	{
		properties.loadJposPropertiesByName( JPOS_JUNIT_PROPERTIES_FILE_NAME );

		Enumeration propNames = properties.getPropertyNames();

		assertTrue( "All JposProperties should contain some properties",
				propNames.hasMoreElements() == true );
	}

	public void testGetMultiProperty()
	{
		properties.loadJposPropertiesByName( JPOS_JUNIT_PROPERTIES_FILE_NAME );

		JposProperties.MultiProperty multiProp = properties.getMultiProperty( MULTI_PROP_BASE_NAME );

		assertTrue( "Should contain a multiproperty with name = " + MULTI_PROP_BASE_NAME,
				    multiProp != null );

		assertTrue( "Should contain a multiproperty with name = " + MULTI_PROP_BASE_NAME,
				    multiProp.getBasePropertyName().equals( MULTI_PROP_BASE_NAME ) );

		for( int i = 0; i < MULTI_PROP_MAX_NUMBER; ++i )
		{
			String propName = MULTI_PROP_BASE_NAME + "." + i;
			String propValue = propName + ".value";

			assertTrue( "Should contain a multiproperty with name = " + propName,
					    multiProp.getPropertyString( propName ) != null &&
					    multiProp.getPropertyString( propName ).equals( propValue ) );

			assertTrue( "Should contain a multiproperty with name = " + propName,
					    multiProp.getPropertyString( propName ) != null &&
					    multiProp.getPropertyString( i ).equals( propValue ) );
		}
	}

	public void testHasMultiProperty()
	{
		properties.loadJposPropertiesByName( JPOS_JUNIT_PROPERTIES_FILE_NAME );

		assertTrue( "Should contain a multiproperty with name = " + MULTI_PROP_BASE_NAME,
				properties.hasMultiProperty( MULTI_PROP_BASE_NAME ) );

		assertTrue( "Should NOT contain a multiproperty with name = __xyzMultiProp__",
				properties.hasMultiProperty( "__xyzMultiProp__" ) == false );
	}

	public void testGetProps()
	{
		properties.loadJposPropertiesByName( JPOS_JUNIT_PROPERTIES_FILE_NAME );

		Enumeration names = properties.getPropertyNames();
		Iterator props = properties.getProps();

		Vector namesVector = new Vector();

		while( props.hasNext() )
		{
			JposProperties.Prop prop = (JposProperties.Prop)props.next();
			namesVector.add( prop.getName() );
		}

		assertTrue( "All names in properties should be in getProps() JposProperties.Prop list",
				JUnitUtility.isEquals( names, namesVector.elements() ) );
	}

	public void testSortGetProps()
	{
		properties.loadJposPropertiesByName( JPOS_JUNIT_PROPERTIES_FILE_NAME );

		Enumeration names = properties.getPropertyNames();
		Iterator props = properties.getProps();

		List nameList = new ArrayList();
		List propList = new ArrayList();
		List propNameList = new ArrayList();

		while( names.hasMoreElements() )
			nameList.add( names.nextElement() );

		while( props.hasNext() )
			propList.add( props.next() );

		Collections.sort( nameList );

        Collections.sort( propList, DefaultProperties.propComparator() );

		for( int i = 0; i < propList.size(); ++i )
			propNameList.add( ( (JposProperties.Prop)propList.get( i ) ) .getName() );


		assertTrue( "Sorting both properties.getPropertyNames() and getProps().getName() list should yield same list",
				JUnitUtility.isEquals( nameList, propNameList ) );
	}
	
	public void testGetStringListProperty()
	{
	}
	
	//-------------------------------------------------------------------------
	// Instance variables
	//

	private DefaultProperties properties = null;

	//-------------------------------------------------------------------------
	// Class variables
	//

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String JPOS_JUNIT_PROPERTIES_FILE_NAME = "jpos" + File.separator +
																 "res" + File.separator +
																 "jpos_junit.properties";

	public static final String MULTI_PROP_BASE_NAME = "multiProp";
	
	public static int MULTI_PROP_MAX_NUMBER = 5;
}
