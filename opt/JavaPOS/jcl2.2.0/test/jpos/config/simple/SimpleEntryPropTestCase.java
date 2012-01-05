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

/**
 * A JUnit TestCase for the JposEntry.Prop and implementation class 
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class SimpleEntryPropTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public SimpleEntryPropTestCase( String name ) { super( name ); }

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

		stringProp = new SimpleEntry.Prop( STRING_VENDOR_PROP_NAME, STRING_VENDOR_PROP_NAME );
		booleanProp = new SimpleEntry.Prop( BOOLEAN_VENDOR_PROP_NAME, Boolean.FALSE );
		byteProp = new SimpleEntry.Prop( BYTE_VENDOR_PROP_NAME, new Byte( (byte)0xA ) )		;
		characterProp = new SimpleEntry.Prop( CHARACTER_VENDOR_PROP_NAME, new Character( 'A' ) );
		doubleProp = new SimpleEntry.Prop( DOUBLE_VENDOR_PROP_NAME, new Double( 3.14159 ) );
		floatProp = new SimpleEntry.Prop( FLOAT_VENDOR_PROP_NAME, new Float( 3.14159 ) );
		longProp = new SimpleEntry.Prop( LONG_VENDOR_PROP_NAME, new Long( 1234 ) );
		integerProp = new SimpleEntry.Prop( INTEGER_VENDOR_PROP_NAME, new Integer( 1234 ) );

		propList.add( stringProp );
		propList.add( booleanProp );
		propList.add( byteProp ); 
		propList.add( characterProp );
		propList.add( doubleProp ); 
		propList.add( floatProp );
		propList.add( longProp );
		propList.add( integerProp );

		Iterator iterator = propList.iterator();

		while( iterator.hasNext() )
		{
			JposEntry.Prop prop = (JposEntry.Prop)iterator.next();
			entry.add( prop );

			propNameList.add( prop.getName() );
			propValueList.add( prop.getValue() );
		}

		return entry;
	}

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testGetName()
	{
		Iterator iterator = propList.iterator();

		while( iterator.hasNext() )
		{
			JposEntry.Prop prop = (JposEntry.Prop)iterator.next();
			
			assertTrue( "Got a JposEntry.Prop with null name", prop.getName() != null );
			assertTrue( "prop.getName() is not in entry", 
						entry.hasPropertyWithName( prop.getName() ) );
		}
	}

	public void testGetValue()
	{
		Iterator iterator = propList.iterator();

		while( iterator.hasNext() )
		{
			JposEntry.Prop prop = (JposEntry.Prop)iterator.next();
			
			assertTrue( "Got a JposEntry.Prop with null value", prop.getValue() != null );
			assertTrue( "prop.getName() is not in entry", 
						entry.hasPropertyWithName( prop.getName() ) );
			assertTrue( "prop.getValue() is not in entry", 
						entry.hasPropertyWithValue( prop.getValue() ) );
		}
	}

	public void testGetValueAsString()
	{
		Iterator iterator = entry.getProps();

		while( iterator.hasNext() )
		{
			JposEntry.Prop prop = (JposEntry.Prop)iterator.next();
			
			String stringValue = prop.getValueAsString();

			assertTrue( "stringValue cannot be null", stringValue != null );
			assertTrue( "stringValue different then prop.getValue().toString()", 
						stringValue.equals( prop.getValue().toString() ) );
		}
	}

	public void testGetType()
	{
		assertTrue( "stringProp.getType() is invalid", stringProp.getType().equals( String.class ) );
		assertTrue( "booleanProp.getType() is invalid", booleanProp.getType().equals( Boolean.class ) );
		assertTrue( "byteProp.getType() is invalid", byteProp.getType().equals( Byte.class ) );
		assertTrue( "characterProp.getType() is invalid", characterProp.getType().equals( Character.class ) );
		assertTrue( "doubleProp.getType() is invalid", doubleProp.getType().equals( Double.class ) );
		assertTrue( "floatProp.getType() is invalid", floatProp.getType().equals( Float.class ) );
		assertTrue( "longProp.getType() is invalid", longProp.getType().equals( Long.class ) );
		assertTrue( "integerProp.getType() is invalid", integerProp.getType().equals( Integer.class ) );
	}

	public void testSetNameString() throws IllegalArgumentException
	{
		String propName = stringProp.getName();
		Object propValue = stringProp.getValue();
		Class propType = stringProp.getType();

		String newStringPropName = null;

		try{ stringProp.setName( newStringPropName  ); fail( "Allowed setName( null )" ); }
		catch( IllegalArgumentException iae ) {}
		
		newStringPropName = "newStringPropName";

		stringProp.setName( newStringPropName );

		assertTrue( "Changed name of property but did not get new name from getName()",
					stringProp.getName().equals( newStringPropName ) );
	}

	public void testSetValueString() throws IllegalArgumentException
	{
		String propName = stringProp.getName();
		Object propValue = stringProp.getValue();
		Class propType = stringProp.getType();

		String newPropValue = null;

		try{ stringProp.setValue( newPropValue ); fail( "Allowed setValue( null )" ); }
		catch( IllegalArgumentException iae ) {}

		newPropValue = "newPropValue";

		stringProp.setValue( newPropValue );

		assertTrue( "stringProp.getName().equals( propName ) == false",
					stringProp.getName().equals( propName ) );
		assertTrue( "stringProp.getValue().equals( newPropValue ) == false",
					stringProp.getValue().equals( newPropValue ) );
		assertTrue( "stringProp.getType().equals( propType ) == false",
					stringProp.getType().equals( propType ) );
	}

	public void testSetValueBoolean() throws IllegalArgumentException
	{
		String propName = booleanProp.getName();
		Object propValue = booleanProp.getValue();
		Class propType = booleanProp.getType();

		Boolean newPropValue = null;

		try{ booleanProp.setValue( newPropValue ); fail( "Allowed setValue( null )" ); }
		catch( IllegalArgumentException iae ) {}

		newPropValue = new Boolean( true );

		booleanProp.setValue( newPropValue );

		assertTrue( "booleanProp.getName().equals( propName ) == false",
					booleanProp.getName().equals( propName ) );
		assertTrue( "booleanProp.getValue().equals( newPropValue ) == false",
					booleanProp.getValue().equals( newPropValue ) );
		assertTrue( "booleanProp.getType().equals( propType ) == false",
					booleanProp.getType().equals( propType ) );
	}

	public void testSetValueByte() throws IllegalArgumentException
	{
		String propName = byteProp.getName();
		Object propValue = byteProp.getValue();
		Class propType = byteProp.getType();

		Byte newPropValue = null;

		try{ byteProp.setValue( newPropValue ); fail( "Allowed setValue( null )" ); }
		catch( IllegalArgumentException iae ) {}

		newPropValue = new Byte( (byte)0xB );

		byteProp.setValue( newPropValue );

		assertTrue( "byteProp.getName().equals( propName ) == false",
					byteProp.getName().equals( propName ) );
		assertTrue( "byteProp.getValue().equals( newPropValue ) == false",
					byteProp.getValue().equals( newPropValue ) );
		assertTrue( "byteProp.getType().equals( propType ) == false",
					byteProp.getType().equals( propType ) );
	}

	public void testSetValueCharacter() throws IllegalArgumentException
	{
		String propName = characterProp.getName();
		Object propValue = characterProp.getValue();
		Class propType = characterProp.getType();

		Character newPropValue = null;

		try{ characterProp.setValue( newPropValue ); fail( "Allowed setValue( null )" ); }
		catch( IllegalArgumentException iae ) {}

		newPropValue = new Character( 'Z' );

		characterProp.setValue( newPropValue );

		assertTrue( "characterProp.getName().equals( propName ) == false",
					characterProp.getName().equals( propName ) );
		assertTrue( "characterProp.getValue().equals( newPropValue ) == false",
					characterProp.getValue().equals( newPropValue ) );
		assertTrue( "characterProp.getType().equals( propType ) == false",
					characterProp.getType().equals( propType ) );
	}

	public void testSetValueDouble() throws IllegalArgumentException
	{
		String propName = doubleProp.getName();
		Object propValue = doubleProp.getValue();
		Class propType = doubleProp.getType();

		Double newPropValue = null;

		try{ doubleProp.setValue( newPropValue ); fail( "Allowed setValue( null )" ); }
		catch( IllegalArgumentException iae ) {}

		newPropValue = new Double( 2.7818 );

		doubleProp.setValue( newPropValue );

		assertTrue( "doubleProp.getName().equals( propName ) == false",
					doubleProp.getName().equals( propName ) );
		assertTrue( "doubleProp.getValue().equals( newPropValue ) == false",
					doubleProp.getValue().equals( newPropValue ) );
		assertTrue( "doubleProp.getType().equals( propType ) == false",
					doubleProp.getType().equals( propType ) );
	}

	public void testSetValueFloat() throws IllegalArgumentException
	{
		String propName = floatProp.getName();
		Object propValue = floatProp.getValue();
		Class propType = floatProp.getType();

		Float newPropValue = null;

		try{ floatProp.setValue( newPropValue ); fail( "Allowed setValue( null )" ); }
		catch( IllegalArgumentException iae ) {}

		newPropValue = new Float( 2.7818 );

		floatProp.setValue( newPropValue );

		assertTrue( "floatProp.getName().equals( propName ) == false",
					floatProp.getName().equals( propName ) );
		assertTrue( "floatProp.getValue().equals( newPropValue ) == false",
					floatProp.getValue().equals( newPropValue ) );
		assertTrue( "floatProp.getType().equals( propType ) == false",
					floatProp.getType().equals( propType ) );
	}

	public void testSetValueLong() throws IllegalArgumentException
	{
		String propName = longProp.getName();
		Object propValue = longProp.getValue();
		Class propType = longProp.getType();

		Long newPropValue = null;

		try{ longProp.setValue( newPropValue ); fail( "Allowed setValue( null )" ); }
		catch( IllegalArgumentException iae ) {}

		newPropValue = new Long( 54321 );

		longProp.setValue( newPropValue );

		assertTrue( "longProp.getName().equals( propName ) == false",
					longProp.getName().equals( propName ) );
		assertTrue( "longProp.getValue().equals( newPropValue ) == false",
					longProp.getValue().equals( newPropValue ) );
		assertTrue( "longProp.getType().equals( propType ) == false",
					longProp.getType().equals( propType ) );
	}

	public void testSetValueInteger() throws IllegalArgumentException
	{
		String propName = integerProp.getName();
		Object propValue = integerProp.getValue();
		Class propType = integerProp.getType();

		Integer newPropValue = null;

		try{ integerProp.setValue( newPropValue ); fail( "Allowed setValue( null )" ); }
		catch( IllegalArgumentException iae ) {}

		newPropValue = new Integer( 54321 );

		integerProp.setValue( newPropValue );

		assertTrue( "integerProp.getName().equals( propName ) == false",
					integerProp.getName().equals( propName ) );
		assertTrue( "integerProp.getValue().equals( newPropValue ) == false",
					integerProp.getValue().equals( newPropValue ) );
		assertTrue( "integerProp.getType().equals( propType ) == false",
					integerProp.getType().equals( propType ) );
	}

	public void testIsOfType()
	{
		assertTrue( "stringProp.isOfType() returned invalid answer", 
					stringProp.isOfType( String.class ) );
		assertTrue( "booleanProp.isOfType() returned invalid answer", 
					booleanProp.isOfType( Boolean.class ) );
		assertTrue( "byteProp.isOfType() returned invalid answer", 
					byteProp.isOfType( Byte.class ) );
		assertTrue( "characterProp.isOfType() returned invalid answer", 
					characterProp.isOfType( Character.class ) );
		assertTrue( "doubleProp.isOfType() returned invalid answer", 
					doubleProp.isOfType( Double.class ) );
		assertTrue( "floatProp.isOfType() returned invalid answer", 
					floatProp.isOfType( Float.class ) );
		assertTrue( "longProp.isOfType() returned invalid answer", 
					longProp.isOfType( Long.class ) );
		assertTrue( "integerProp.isOfType() returned invalid answer", 
					integerProp.isOfType( Integer.class ) );

		assertTrue( "integerProp.isOfType() returned invalid answer", 
					integerProp.isOfType( String.class ) == false );
	}

	public void testEquals()
	{
		assertTrue( "integerProp.equals( null ) should be false", 
					integerProp.equals( null ) == false );

		assertTrue( "integerProp.equals( integerProp ) should be true", 
					integerProp.equals( integerProp ) == true );

		assertTrue( "integerProp.equals( integerProp.copy() ) should be true", 
					integerProp.equals( integerProp.copy() ) == true );

		assertTrue( "integerProp.equals( stringProp ) should be false", 
					integerProp.equals( stringProp ) == false );
	}

	public void testCopy()
	{
		Iterator iterator = propList.iterator();

		while( iterator.hasNext() )
		{
			JposEntry.Prop prop = (JposEntry.Prop)iterator.next();
			JposEntry.Prop propCopy = prop.copy();

			assertTrue( "propCopy == null", propCopy != null );
			assertTrue( "propCopy.equals( prop ) == false", propCopy.equals( prop ) );

			assertTrue( "propCopy.getName().equals( prop.getName() ) == false", 
						propCopy.getName().equals( prop.getName() ) );
			assertTrue( "propCopy.getValue().equals( prop.getValue() ) == false", 
						propCopy.getValue().equals( prop.getValue() ) );
			assertTrue( "propCopy.getType().equals( prop.getType() ) == false", 
						propCopy.getType().equals( prop.getType() ) );
		}
	}

	public void testCompareTo() throws JposConfigException	
	{
		JposEntry entry = new SimpleEntry( "SimpleEntry" );

		JposEntry.Prop aaaEntryProp = entry.createProp( "aaa", new Boolean( "true" ), Boolean.class );
		JposEntry.Prop zzzEntryProp = entry.createProp( "zzz", new Integer( "1234" ), Integer.class );

		assertTrue( "aaaEntryProp.compareTo( aaaEntryProp ) should == 0", 
					aaaEntryProp.compareTo( aaaEntryProp ) == 0 );

		assertTrue( "zzzEntryProp.compareTo( zzzEntryProp ) should == 0", 
					zzzEntryProp.compareTo( zzzEntryProp ) == 0 );

		assertTrue( "zzzEntryProp.compareTo( aaaEntryProp ) should < 0", 
					zzzEntryProp.compareTo( aaaEntryProp ) > 0 );
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private JposEntry entry = null;
	private List propNameList = new LinkedList();
	private List propValueList = new LinkedList();
	private List propList = new LinkedList();

	private JposEntry.Prop stringProp = null;
	private JposEntry.Prop booleanProp = null;
	private JposEntry.Prop byteProp = null;
	private JposEntry.Prop characterProp = null;
	private JposEntry.Prop doubleProp = null;
	private JposEntry.Prop floatProp = null;
	private JposEntry.Prop longProp = null;
	private JposEntry.Prop integerProp = null;

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String VENDOR_PROP_NAME = "vendor.prop.name";
	
	public static final String STRING_VENDOR_PROP_NAME 		=  String.class.getName() + "_" + VENDOR_PROP_NAME;
	public static final String BOOLEAN_VENDOR_PROP_NAME 	=  Boolean.class.getName() + "_" + VENDOR_PROP_NAME;
	public static final String BYTE_VENDOR_PROP_NAME 		=  Byte.class.getName() + "_" + VENDOR_PROP_NAME;
	public static final String CHARACTER_VENDOR_PROP_NAME 	=  Character.class.getName() + "_" + VENDOR_PROP_NAME;
	public static final String DOUBLE_VENDOR_PROP_NAME 		=  Double.class.getName() + "_" + VENDOR_PROP_NAME;
	public static final String FLOAT_VENDOR_PROP_NAME 		=  Float.class.getName() + "_" + VENDOR_PROP_NAME;
	public static final String LONG_VENDOR_PROP_NAME 		=  Long.class.getName() + "_" + VENDOR_PROP_NAME;
	public static final String INTEGER_VENDOR_PROP_NAME 	=  Integer.class.getName() + "_" + VENDOR_PROP_NAME;
}
