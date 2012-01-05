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

import jpos.*;
import jpos.config.*;
import jpos.test.*;
import jpos.util.*;

/**
 * A JUnit TestCase for the JposEntry and implementation class 
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class SimpleEntryTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public SimpleEntryTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
    {
		entry = createJposEntry();
    }

	protected void tearDown() 
    {
		entry = null;

		propNameList.clear();
		propValueList.clear();
		maxVendorPropNameIndex = 0;
    }

	//-------------------------------------------------------------------------
	// Private methods
	//

	private JposEntry createJposEntry()
	{
		JposEntry entry = createJposEntry( JposEntry.LOGICAL_NAME_PROP_NAME,
										   JposEntry.SI_FACTORY_CLASS_PROP_NAME,
										   JposEntry.SERVICE_CLASS_PROP_NAME,
										   JposEntry.VENDOR_NAME_PROP_NAME,
										   JposEntry.VENDOR_URL_PROP_NAME,
										   JposEntry.DEVICE_CATEGORY_PROP_NAME,
										   JposEntry.JPOS_VERSION_PROP_NAME,
										   JposEntry.PRODUCT_NAME_PROP_NAME,
										   JposEntry.PRODUCT_DESCRIPTION_PROP_NAME,
										   JposEntry.PRODUCT_URL_PROP_NAME,
										   null );

		propNameList.add( JposEntry.LOGICAL_NAME_PROP_NAME );
		propNameList.add( JposEntry.SI_FACTORY_CLASS_PROP_NAME );
		propNameList.add( JposEntry.SERVICE_CLASS_PROP_NAME );    
		propNameList.add( JposEntry.VENDOR_NAME_PROP_NAME );   
		propNameList.add( JposEntry.VENDOR_URL_PROP_NAME );
		propNameList.add( JposEntry.DEVICE_CATEGORY_PROP_NAME );
		propNameList.add( JposEntry.JPOS_VERSION_PROP_NAME );
		propNameList.add( JposEntry.PRODUCT_NAME_PROP_NAME );
		propNameList.add( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME );
		propNameList.add( JposEntry.PRODUCT_URL_PROP_NAME );

		propValueList.add( JposEntry.LOGICAL_NAME_PROP_NAME );
		propValueList.add( JposEntry.SI_FACTORY_CLASS_PROP_NAME );
		propValueList.add( JposEntry.SERVICE_CLASS_PROP_NAME );    
		propValueList.add( JposEntry.VENDOR_NAME_PROP_NAME );   
		propValueList.add( JposEntry.VENDOR_URL_PROP_NAME );
		propValueList.add( JposEntry.DEVICE_CATEGORY_PROP_NAME );
		propValueList.add( JposEntry.JPOS_VERSION_PROP_NAME );
		propValueList.add( JposEntry.PRODUCT_NAME_PROP_NAME );
		propValueList.add( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME );
		propValueList.add( JposEntry.PRODUCT_URL_PROP_NAME );

		for( int i = 0; i < 5; ++i )
		{
			String name = VENDOR_PROP_NAME + i;
			Integer intValue = new Integer( i );

			entry.addProperty( name, intValue );

			propNameList.add( name );
			propValueList.add( intValue );
		}

		for( int i = 5; i < 10; ++i )
		{
			String name = VENDOR_PROP_NAME + i;
			String stringValue = VENDOR_PROP_VALUE + i;

			entry.addProperty( name, stringValue );

			propNameList.add( name );
			propValueList.add( stringValue );
		}

		for( int i = 10; i < 15; ++i )
		{
			String name = VENDOR_PROP_NAME + i;
			Boolean boolValue = new Boolean( ( i % 2 ) == 1 );

			entry.addProperty( name, boolValue );

			propNameList.add( name );
			propValueList.add( boolValue );
		}

		maxVendorPropNameIndex = 15;

		return entry;
	}

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

    public void tesGetPropertyCount()
	{
		assertEquals( "entry.getPropertyCount() does not match propNameList.size()",
					  propNameList.size(), entry.getPropertyCount() );
	}

    public void testGetPropertyNames()
	{
		assertTrue( "entry.getPropertyNames() does not match propNameList.iterator()",
				JUnitUtility.isEquals( entry.getPropertyNames(), propNameList.iterator() ) );
	}

    public void testHasPropertyWithName()
	{
		Iterator names = propNameList.iterator();

		while( names.hasNext() )
		{
			String name = (String)names.next();

			assertTrue( "Expect entry to have property with name: " + name,
					entry.hasPropertyWithName( name ) );
		}
	}

    public void testHasPropertyWithValue()
	{
		Iterator values = propValueList.iterator();

		while( values.hasNext() )
		{
			Object value = values.next();

			assertTrue( "Expect entry to have property with value: " + value,
					entry.hasPropertyWithValue( value ) );
		}
	}

    public void testGetPropertyValue()
	{
		Iterator names = propNameList.iterator();

		while( names.hasNext() )
		{
			String name = (String)names.next();

			Object value = entry.getPropertyValue( name );

			assertTrue( "Expect entry to have property value: " + value,
					JUnitUtility.isInList( value, propValueList ) );
		}
	}

    public void testGetPropertyType() throws JposConfigException
	{
		Iterator names = propNameList.iterator();

		while( names.hasNext() )
		{
			String name = (String)names.next();
			Object value = entry.getPropertyValue( name );
			Class type = entry.getPropertyType( name );

			assertTrue( "Expect entry to have a type != null", type != null );
			assertTrue( "Expect <value, type> to be a valid property <type, value>",
						JposEntryUtility.validatePropValue( value, type ) );
		}
	}

    public void testModifyPropertyValue()
	{
		String propName = JposEntry.VENDOR_NAME_PROP_NAME;

		assertTrue( "entry should have property by name: " + propName,
				entry.hasPropertyWithName( propName ) );

		String propValue = (String)entry.getPropertyValue( propName );

		entry.modifyPropertyValue( propName, "newValue" );

		assertTrue( "entry.getPropertyValue( propName ) should be: newValue",
				entry.getPropertyValue( propName ).equals( "newValue" ) );
	}

    public void testAddProperty()
	{
		String propName = "propName";
		String propValue = "propValue";

		assertTrue( "entry should NOT have property by name: " + propName,
				entry.hasPropertyWithName( propName ) == false );

		entry.addProperty( propName, propValue );

		assertTrue( "entry SHOULD have property by name: " + propName,
				entry.hasPropertyWithName( propName ) == true );
	}

    public void testRemoveProperty()
	{
		String propName = JposEntry.VENDOR_NAME_PROP_NAME;

		assertTrue( "entry should have property by name: " + propName,
				    entry.hasPropertyWithName( propName ) );

		entry.removeProperty( propName );

		assertTrue( "entry should no longer have property by name: " + propName,
				    entry.hasPropertyWithName( propName ) == false );
	}

    public void testEquals()
	{
		JposEntry entryCopy = ( (SimpleEntry)entry ).copy();

		assertTrue( "entry.copy() should be equal to entry",
				entry.equals( entryCopy ) );

		entryCopy.addProperty( "myProp", "myValue" );
		assertTrue( "modified entry.copy() should NOT be equal to entry",
				entry.equals( entryCopy ) == false );

		entryCopy.removeProperty( "myProp" );
		assertTrue( "entry.copy() should be equal to entry",
				entry.equals( entryCopy ) );
	}

	public void testGetRegPopulator()
	{
		assertTrue( "entry.getRegPopulator() should be null",
				entry.getRegPopulator() == null );
	}

	public void testGetLogicalName()
	{
		assertTrue( "entry MUST have a logicalName property != null",
				entry.getLogicalName() != null );

		assertTrue( "entry logicalName must be: logicalName",
				entry.getLogicalName().equals( "logicalName" ) );

		assertTrue( "entry logicalName must be: logicalName",
				entry.getLogicalName().equals( entry.getPropertyValue( JposEntry.LOGICAL_NAME_PROP_NAME ) ) );
	}

	public void testGetProp()
	{
		assertTrue( "entry MUST have a logicalName property != null",
				entry.getLogicalName() != null );

		JposEntry.Prop nullProp = entry.getProp( "thisIsAFakePropName" );

		assertTrue( "nullProp should be null", nullProp == null );

		for( int i = 0; i < maxVendorPropNameIndex; ++i )
		{
			String propName = VENDOR_PROP_NAME + i;
			JposEntry.Prop prop = entry.getProp( propName );

			assertTrue( "prop should NOT be null", prop != null );
			assertTrue( "prop.getName() should ==  " + propName, 
						prop.getName().equals( propName ) );

			assertTrue( "prop.getValue() should be != null", 
						prop.getValue() != null );
		}
	}

	public void testGetProps()
	{
		List propsList = new ArrayList();

		for( int i = 0; i < propNameList.size(); ++i )
			propsList.add( new SimpleEntry.Prop( (String)propNameList.get( i ), 
												 propValueList.get( i ) ) );

		assertTrue( "entry.getProps() matches the list of <value, pair> properties",
				    JUnitUtility.isEquals( propsList.iterator(), entry.getProps() ) );
	}

	public void testAddProp()
	{
		int oldSize = entry.getPropertyCount();
		JposEntry.Prop longProp = new SimpleEntry.Prop( "longProp", new Long( 12345 ) );

		entry.add( longProp );
		assertTrue( "new size should be one more", entry.getPropertyCount() == oldSize + 1 );
		assertTrue( "did not find added property", entry.hasPropertyWithName( longProp.getName() ) );
		assertTrue( "did not find added property", entry.hasPropertyWithValue( longProp.getValue() ) );
	}

	public void testRemoveProp()
	{
		int oldSize = entry.getPropertyCount();
		JposEntry.Prop longProp = new SimpleEntry.Prop( "longProp", new Long( 12345 ) );

		entry.add( longProp );
		assertTrue( "new size should be one more", entry.getPropertyCount() == oldSize + 1 );

		entry.remove( longProp );
		assertTrue( "new size should be old size", entry.getPropertyCount() == oldSize );
	}

	public void testHasProp()
	{
		assertTrue( "Property testHasProp should not exist", 
					entry.hasPropertyWithName( "testHasProp" ) == false );

		JposEntry.Prop testHasProp = new SimpleEntry.Prop( "testHasProp", Boolean.TRUE );

		entry.add( testHasProp );
		
		assertTrue( "Property testHasProp should exist", 
					entry.hasPropertyWithName( "testHasProp" ) == true );

		entry.remove( testHasProp );

		assertTrue( "Property testHasProp should not exist", 
					entry.hasPropertyWithName( "testHasProp" ) == false );
	}

	public void testModifyProp()
	{
		assertTrue( "Property testModifyProp should not exist", 
					entry.hasPropertyWithName( "testModifyProp" ) == false );

		JposEntry.Prop testModifyProp = new SimpleEntry.Prop( "testModifyProp", "" );

		entry.add( testModifyProp );
		
		assertTrue( "Property testModifyProp should exist", 
					entry.hasPropertyWithName( "testModifyProp" ) == true );

		assertTrue( "Property testModifyProp.getValue() should be empty string", 
					"".equals( testModifyProp.getValue() ) );

		testModifyProp.setValue( "testModifyProp" );

		entry.modify( testModifyProp );

		assertTrue( "Property testModifyProp.getValue() should be empty string", 
					"testModifyProp".equals( testModifyProp.getValue() ) );
	}

	public void testCreateProp()
	{
		emptyTest();
	}

	public void testCompareTo()
	{
		JposEntry aaaEntry = new SimpleEntry( "aaa" );
		JposEntry zzzEntry = new SimpleEntry( "zzz" );

		assertTrue( "aaaEntry.compareTo( aaaEntry ) should == 0", 
					aaaEntry.compareTo( aaaEntry ) == 0 );

		assertTrue( "zzzEntry.compareTo( zzzEntry ) should == 0", 
					zzzEntry.compareTo( zzzEntry ) == 0 );

		assertTrue( "zzzEntry.compareTo( aaaEntry ) should < 0", 
					zzzEntry.compareTo( aaaEntry ) > 0 );
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private JposEntry entry = null;
	private List propNameList = new ArrayList();
	private List propValueList = new ArrayList();
	private int maxVendorPropNameIndex = 0;

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String VENDOR_PROP_NAME = "vendor.prop.name";
	public static final String VENDOR_PROP_VALUE = "vendor.prop.value";
}
