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

import java.util.*;

import jpos.*;
import jpos.test.*;
import jpos.config.*;
import jpos.config.simple.SimpleEntry;

/**
 * A JUnit test class for the jpos.util.JposEntryUtility class
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class JposEntryUtilityTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public JposEntryUtilityTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp()
	{
		entryProto = JposEntryUtility.getEntryPrototype();
	}
	
	protected void tearDown() 
	{
		entryProto = null;
	}

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testGetEntryPrototype()
	{
		JposEntry entry = JposEntryUtility.getEntryPrototype();
		assertTrue( "JposEntryUtility.getEntryPrototype() return null", entry != null );

		assertTrue( "JposEntryUtility.getEntryPrototype() return invalid entry", 
				JposEntryUtility.isValidJposEntry( entry ) );

		JposEntry entry2 = JposEntryUtility.getEntryPrototype();
		assertTrue( "JposEntryUtility.getEntryPrototype() return null", entry2 != null );
		assertTrue( "JposEntryUtility.getEntryPrototype() return same entry", entry != entry2 );
	}

	public void testIsValidJposEntry()
	{
		JposEntry entry = new SimpleEntry();
		assertTrue( "Expected INVALID entry for new SimpleEntry()", 
				JposEntryUtility.isValidJposEntry( entry ) == false );

		entry.addProperty( "logicalName", "logicalName" );
		entry.addProperty( "factoryClass", "factoryClass" );
		entry.addProperty( "serviceClass", "serviceClass" );
		assertTrue( "Expected INVALID entry for new SimpleEntry()", 
				JposEntryUtility.isValidJposEntry( entry ) == false );

		JposEntry protoEntry = JposEntryUtility.getEntryPrototype();
		assertTrue( "Expected VALID entry for JposEntryUtility.getEntryPrototype()", 
				JposEntryUtility.isValidJposEntry( protoEntry ) == true );

		JposEntryUtility.addMissingRequiredProps( entry );
		assertTrue( "Expected VALID entry for JposEntryUtility.addMissingRequiredPropNames( entry )", 
				JposEntryUtility.isValidJposEntry( entry ) == true );

		JposEntry entry2 = createJposEntry( "logicalName", "factoryClass", "serviceClass",
											"vendorName", "vendorUrl", "deviceCategory",
											"jposVersion", "productName", "productDescription", "productUrl", null );
		assertTrue( "Expected VALID entry for createJposEntry( ... )", 
				JposEntryUtility.isValidJposEntry( entry2 ) == true );


	}

	public void testIsRequiredProp()
	{
		assertTrue( "Expected string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "serviceInstanceFactoryClass" ) );
		assertTrue( "Expected string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "logicalName" ) );
		assertTrue( "Expected string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "serviceClass" ) );
		assertTrue( "Expected string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "vendorName" ) );
		assertTrue( "Expected string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "vendorURL" ) );
		assertTrue( "Expected string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "deviceCategory" ) );
		assertTrue( "Expected string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "jposVersion" ) );
		assertTrue( "Expected string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "productName" ) );
		assertTrue( "Expected string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "productDescription" ) );
		assertTrue( "Expected string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "productURL" ) );

		assertTrue( "DO NOT expect string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "" ) == false );
		assertTrue( "DO NOT expect string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "1234" ) == false );
		assertTrue( "DO NOT expect string passed to be a REQUIRED property", 
				JposEntryUtility.isRequiredPropName( "abc" ) == false );
	}													 

	public void testGetMissingRequiredPropNames()
	{
		JposEntry entry1 = new SimpleEntry();
		Enumeration missingPropNames = JposEntryUtility.getMissingRequiredPropNames( entry1 );
		assertTrue( "new SimpleEntry() should be missing all required properties",
				JUnitUtility.isEquals( JposEntryConst.REQUIRED_PROPS, missingPropNames ) == true );

		JposEntryUtility.addMissingRequiredProps( entry1 );
		missingPropNames = JposEntryUtility.getMissingRequiredPropNames( entry1 );
		assertTrue( "All required properties should be there",
				missingPropNames.hasMoreElements() == false );

		entry1.removeProperty( JposEntry.SERVICE_CLASS_PROP_NAME );
		missingPropNames = JposEntryUtility.getMissingRequiredPropNames( entry1 );
		String[] missingPropNamesArray = { "serviceClass" };
		assertTrue( "{ serviceClass } is the only missong property",
				JUnitUtility.isEquals( missingPropNamesArray , missingPropNames ) == true );

		entry1.removeProperty( JposEntry.DEVICE_CATEGORY_PROP_NAME );
		entry1.removeProperty( JposEntry.SI_FACTORY_CLASS_PROP_NAME );
		missingPropNames = JposEntryUtility.getMissingRequiredPropNames( entry1 );
		String[] missingPropNamesArray2 = { "serviceClass", "deviceCategory", "serviceInstanceFactoryClass" };
		assertTrue( "{ serviceClass } is the only missong property",
				JUnitUtility.isEquals( missingPropNamesArray2 , missingPropNames ) == true );

		JposEntryUtility.addMissingRequiredProps( entry1 );
		missingPropNames = JposEntryUtility.getMissingRequiredPropNames( entry1 );
		assertTrue( "All required properties should be there",
				missingPropNames.hasMoreElements() == false );
	}

	public void testAddMissingRequiredProps()
	{
		JposEntry entry1 = new SimpleEntry();
		JposEntryUtility.addMissingRequiredProps( entry1 );
		Enumeration missingPropNames = JposEntryUtility.getMissingRequiredPropNames( entry1 );
		assertTrue( "All required properties should be there",
				missingPropNames.hasMoreElements() == false );

		JposEntryUtility.addMissingRequiredProps( entry1 );
		missingPropNames = JposEntryUtility.getMissingRequiredPropNames( entry1 );
		assertTrue( "All required properties should be there",
				missingPropNames.hasMoreElements() == false );

		entry1.removeProperty( JposEntry.DEVICE_CATEGORY_PROP_NAME );
		entry1.removeProperty( JposEntry.SI_FACTORY_CLASS_PROP_NAME );
		entry1.removeProperty( JposEntry.JPOS_VERSION_PROP_NAME );
		
		missingPropNames = JposEntryUtility.getMissingRequiredPropNames( entry1 );
		assertTrue( "There should be some missing properties",
				missingPropNames.hasMoreElements() == true );

		JposEntryUtility.addMissingRequiredProps( entry1 );
		missingPropNames = JposEntryUtility.getMissingRequiredPropNames( entry1 );
		assertTrue( "All required properties should be there",
				missingPropNames.hasMoreElements() == false );

	}

	public void testGetMissingRS232PropNames()
	{
		SimpleEntry entry1 = new SimpleEntry();
		Enumeration missingRS232PropNames = JposEntryUtility.getMissingRS232PropNames( entry1 );
		assertTrue( "new SimpleEntry() should be missing all RS232 properties",
				    JUnitUtility.isEquals( JposEntryConst.RS232_PROPS, missingRS232PropNames ) == true );

		entry1.addProperty( JposEntryConst.RS232_PORT_NAME_PROP_NAME, "COM1" );
		
		entry1.addProperty( JposEntryConst.RS232_BAUD_RATE_PROP_NAME, 
							JposEntryConst.RS232_BAUD_RATE_VALUES[ 0 ] );

		entry1.addProperty( JposEntryConst.RS232_DATA_BITS_PROP_NAME,
							JposEntryConst.RS232_DATA_BITS_VALUES[ 0 ] );

		entry1.addProperty( JposEntryConst.RS232_PARITY_PROP_NAME,
							JposEntryConst.RS232_PARITY_MARK );

		entry1.addProperty( JposEntryConst.RS232_STOP_BITS_PROP_NAME,
							JposEntryConst.RS232_STOP_BITS_1_5 );

		entry1.addProperty( JposEntryConst.RS232_FLOW_CONTROL_PROP_NAME,
							JposEntryConst.RS232_FLOW_CONTROL_VALUES[ 1 ] );

		missingRS232PropNames = JposEntryUtility.getMissingRS232PropNames( entry1 );
		assertTrue( "All RS232 properties should be present",
				    missingRS232PropNames.hasMoreElements() == false );
	}

	public void testGetVendorPropNames()
	{
		assertTrue( "Expected VALID entry for JposEntryUtility.getEntryPrototype()", 
				    JposEntryUtility.isValidJposEntry( entryProto ) == true );

		List propNameList = new ArrayList();
		List propValueList = new ArrayList();

		for( int i = 0; i < 10; i++ )
		{
			String name = VENDOR_PROP_NAME + i;
			String value = VENDOR_PROP_VALUE + i;

			propNameList.add( name );
			propValueList.add( value );

			entryProto.addProperty( name, value );
		}

		Enumeration vendorPropNames = JposEntryUtility.getVendorPropNames( entryProto );
		assertTrue( "Added vendor property names do not match JposEntryUtility.getVendorPropNames( entryProto )",
				    JUnitUtility.isEquals( vendorPropNames, propNameList.iterator() ) );
	}

	public void testGetStandardPropNames()
	{
		List list = new ArrayList();

		for( int i = 0; i < JposEntryConst.REQUIRED_PROPS.length; ++i )
			list.add( JposEntryConst.REQUIRED_PROPS[ i ] );

		list.add( JposEntryConst.DEVICE_BUS_PROP_NAME );

		for( int i = 0; i < JposEntryConst.RS232_PROPS.length; ++i )
			list.add( JposEntryConst.RS232_PROPS[ i ] );

		assertTrue( "List of standard property names does not match JposEntryUtility.getStandardPropNames()",
		   		    JUnitUtility.isEquals( JposEntryUtility.getStandardPropNames(), list.iterator() ) );
	}

	public void testIsRS232PropName()
	{
		emptyTest();
	}

	public void testRemoveAllRS232Props()
	{
		emptyTest();
	}

	public void testGetNonRequiredPropNames()
	{
		emptyTest();
	}

	public void testShortClassName()
	{
		assertTrue( "JposEntryUtility.shortClassName( String.class ) did not return String",
					"String".equals( JposEntryUtility.shortClassName( String.class ) ) );

		assertTrue( "JposEntryUtility.shortClassName( Long.class ) did not return Long",
					"Long".equals( JposEntryUtility.shortClassName( Long.class ) ) );

		assertTrue( "JposEntryUtility.shortClassName( JposEntry.class ) did not return JposEntry",
					"JposEntry".equals( JposEntryUtility.shortClassName( JposEntry.class ) ) );
	}

	public void testValidatePropValue()
	{
		emptyTest();
	}

	public void testIsValidPropType()
	{
		emptyTest();
	}

	public void testParsePropValue()
	{
		emptyTest();
	}

	public void testPropTypeFromString() throws JposConfigException
	{
		String typeString = "java.lang.String";
		String typeShort = "Short";

		String badTypeInteger = "integer";
		String badTypeLong = "java.util.Long";

		assertTrue( "Should return a non-null Class object",
					JposEntryUtility.propTypeFromString( typeString ) != null );
		assertTrue( "Should return the String.class object",
					JposEntryUtility.propTypeFromString( typeString ).equals( String.class ) );

		assertTrue( "Should return a non-null Class object",
					JposEntryUtility.propTypeFromString( typeShort ) != null );
		assertTrue( "Should return the String.class object",
					JposEntryUtility.propTypeFromString( typeShort ).equals( Short.class ) );

		try
		{ 
			Class type = JposEntryUtility.propTypeFromString( badTypeInteger ); 
			fail( "Should have thrown a JposConfigException" );
		}
		catch( JposConfigException jce ) {}

		try
		{ 
			Class type = JposEntryUtility.propTypeFromString( badTypeLong ); 
			fail( "Should have thrown a JposConfigException" );
		}
		catch( JposConfigException jce ) {}
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private JposEntry entryProto = null;

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String VENDOR_PROP_NAME = "vendor.prop.name";
	public static final String VENDOR_PROP_VALUE = "vendor.prop.value";
}
