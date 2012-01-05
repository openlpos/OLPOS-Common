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

/**
 * A JUnit test class for the jpos.util.JposProperties.MultiProperty interface and
 * implementing class in DefaultProperties.DefaultMultiProp inner class
 * @author E. Michael Maximilien
 */
public class MultiPropertyTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public MultiPropertyTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
	{
		properties = new DefaultProperties();
		multiProp = properties.new MultiProp( "multiPropName" );

		propNames = new ArrayList();
		propValues = new ArrayList();

		for( int i = 0; i < MAX_NUMBER; ++i )
		{
			String propName = MULTIPROP_BASE_NAME + "." + i;
			String propValue = propName + ".value";

			propNames.add( propName );
			propValues.add( propValue );

			( (DefaultProperties.MultiProp)multiProp ).add( propName, propValue  );
		}

		try
		{
			( (DefaultProperties.MultiProp)multiProp ).add( "multiPropName.", "multiPropName.5.value"  );
			fail( "Expected an IllegalArgumentException here!" );
		}
		catch( IllegalArgumentException e ) {}

		try
		{
			( (DefaultProperties.MultiProp)multiProp ).add( "multiProp.6", "multiPropName.5.value"  );
			fail( "Expected an IllegalArgumentException here!" );
		}
		catch( IllegalArgumentException e ) {}
	}

	protected void tearDown()
	{
		properties = null;
		multiProp = null;

		propNames = null;
		propValues = null;
	}

	//-------------------------------------------------------------------------
	// Private methods
	//
	
	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testGetBasePropertyName()
	{
		String basePropName = multiProp.getBasePropertyName();

		assertTrue( "getBasePropertyName() should not return null",
				basePropName != null );

		assertTrue( "getBasePropertyName() should return \"multiPropName\"",
				"multiPropName".equals( basePropName ) );
	}

	public void testGetPropertyNames()
	{
		Iterator names = multiProp.getPropertyNames();
		assertTrue( "getPropertyNames() returned an Iterator with values with different values then what was added",
				JUnitUtility.isEquals( names, propNames.iterator() ) );
	}

	public void testGetSortedPropertyNames()
	{
		Iterator names = multiProp.getSortedPropertyNames();

		Collections.sort( propNames );

		assertTrue( "getSortedPropertyNames() returned an Iterator with values with different values then what was added",
				JUnitUtility.isIdentical( names, propNames.iterator() ) );
	}

	public void testGetPropertyValues()
	{
		Iterator values = multiProp.getPropertyValues();
		assertTrue( "getPropertyValues() returned an Iterator with values with different values then what was added",
				JUnitUtility.isEquals( values, propValues.iterator() ) );
	}

	public void testGetPropertyStringByName()
	{
		for( int i = 0; i < MAX_NUMBER; ++i )
		{
			String propName = MULTIPROP_BASE_NAME + "." + i;
			assertTrue( "value of propName = " + propName + " should be != null",
					multiProp.getPropertyString( propName ).equals( propValues.get( i ) ) );
		}

		String propName = MULTIPROP_BASE_NAME + "." + ( MAX_NUMBER + 1 );
		assertTrue( "value of propName = " + propName + " should BE null",
				multiProp.getPropertyString( propName ) == null );
	}

	public void testGetPropertyStringByNumber()
	{
		for( int i = 0; i < MAX_NUMBER; ++i )
		{
			String propName = MULTIPROP_BASE_NAME + "." + i;
			assertTrue( "value of propName = " + propName + " should be != null",
					multiProp.getPropertyString( i ).equals( propValues.get( i ) ) );
		}

		String propName = MULTIPROP_BASE_NAME + "." + ( MAX_NUMBER + 1 );
		assertTrue( "value of propName = " + propName + " should BE null",
				multiProp.getPropertyString( 10 ) == null );
	}


	//-------------------------------------------------------------------------
	// Instance variables
	//

	private DefaultProperties properties = null;
	private JposProperties.MultiProperty multiProp = null;

	private List propNames = null;
	private List propValues = null;

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String MULTIPROP_BASE_NAME = "multiPropName";
	public static final int MAX_NUMBER = 5;
}
