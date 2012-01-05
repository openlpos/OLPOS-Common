package jpos.test;

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

/**
 * A JUnit TestCase for the JUnitUtility
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class JUnitUtilityTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public JUnitUtilityTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	// 

	protected void setUp()
	{
		v1 = new Vector();
		v2 = new Vector();

		v1.addElement( "String1" );
		v1.addElement( "String2" );
		v1.addElement( "String3" );

		v2.addElement( "String1" );
		v2.addElement( "String2" );
		v2.addElement( "String3" );
	}

	protected void tearDown()
	{
		v1 = null;
		v2 = null;
	}

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testIsIdenticalEnum()
	{
		assertTrue( "v1.equals( v2 ) == true", JUnitUtility.isIdentical( v1.elements(), v2.elements() ) );
		assertTrue( "v2.equals( v1 ) == true", JUnitUtility.isIdentical( v2.elements(), v1.elements() ) );

		assertTrue( "v1.equals( null ) == false", JUnitUtility.isIdentical( v1.elements(), null ) == false );
		assertTrue( "v2.equals( null ) == false", JUnitUtility.isIdentical( v2.elements(), null ) == false );

		v2.addElement( "String4" );

		assertTrue( "v1.equals( v2 ) == false", JUnitUtility.isIdentical( v1.elements(), v2.elements() ) == false );
		assertTrue( "v2.equals( v1 ) == false", JUnitUtility.isIdentical( v2.elements(), v1.elements() ) == false );

		v2.addElement( "String5" );

		v1.addElement( "String5" );
		v1.addElement( "String4" );

		assertTrue( "v1.equals( v2 ) == false", JUnitUtility.isIdentical( v1.elements(), v2.elements() ) == false );
		assertTrue( "v2.equals( v1 ) == false", JUnitUtility.isIdentical( v2.elements(), v1.elements() ) == false );
	}

	public void testIsEqualsEnum()
	{
		v1.addElement( "String6" );
		v1.addElement( "String5" );
		v1.addElement( "String4" );

		v2.addElement( "String4" );
		v2.addElement( "String5" );
		v2.addElement( "String6" );

		assertTrue( "v1.equals( v2 ) == true", JUnitUtility.isEquals( v1.elements(), v2.elements() ) );
		assertTrue( "v2.equals( v1 ) == true", JUnitUtility.isEquals( v2.elements(), v1.elements() ) );

		assertTrue( "v1.equals( null ) == false", JUnitUtility.isEquals( v1.elements(), (Enumeration)null ) == false );
		assertTrue( "v2.equals( null ) == false", JUnitUtility.isEquals( v2.elements(), (Iterator)null ) == false );

		v2.addElement( "String7" );

		assertTrue( "v1.equals( v2 ) == false", JUnitUtility.isEquals( v1.elements(), v2.elements() ) == false );
		assertTrue( "v2.equals( v1 ) == false", JUnitUtility.isEquals( v2.elements(), v1.elements() ) == false );
	}

	public void testIsEqualsVector()
	{
		assertTrue( "v1.equals( v2 ) == true", JUnitUtility.isEquals( v1, v2 ) );
		assertTrue( "v2.equals( v1 ) == true", JUnitUtility.isEquals( v2, v1 ) );

		assertTrue( "v1.equals( null ) == false", JUnitUtility.isEquals( v1, null ) == false );
		assertTrue( "v2.equals( null ) == false", JUnitUtility.isEquals( v2, null ) == false );

		v2.addElement( "String4" );

		assertTrue( "v1.equals( v2 ) == false", JUnitUtility.isEquals( v1, v2 ) == false );
		assertTrue( "v2.equals( v1 ) == false", JUnitUtility.isEquals( v2, v1 ) == false );

		v2.addElement( "String5" );

		v1.addElement( "String5" );
		v1.addElement( "String4" );

		assertTrue( "v1.equals( v2 ) == true", JUnitUtility.isEquals( v1, v2 ) );
		assertTrue( "v2.equals( v1 ) == true", JUnitUtility.isEquals( v2, v1 ) );
	}            
	
	//-------------------------------------------------------------------------
	// Instance variables
	//

	private Vector v1 = new Vector();
	private Vector v2 = new Vector();
}
