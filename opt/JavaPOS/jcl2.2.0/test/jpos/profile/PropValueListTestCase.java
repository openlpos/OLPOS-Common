package jpos.profile;

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

import jpos.test.*;

import junit.framework.*;

/**
 * A JUnit TestCase for the PropValueList
 * @author E. Michael Maximilien
 */
public class PropValueListTestCase extends TestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public PropValueListTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
    {
		list = new DefaultPropValueList();

		propValue0 = new DefaultPropValue( "String", StringPropType.getInstance() );
		propValue1 = new DefaultPropValue( new Integer( 1 ), IntegerPropType.getInstance() );
		propValue2 = new DefaultPropValue( new Character( 'c' ), CharacterPropType.getInstance() );
    }

	protected void tearDown() 
    {
		list = null;
    }

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	/** First test of the PropValueList */
	public void testPropValueList1()
	{
		//Add a few PropValue and test size
		assertTrue( "Expects PropValueList to be of size 0", list.getSize() == 0 );
		assertTrue( "Expects PropValueList to be of size 0 and empty", list.isEmpty() );

		list.add( propValue0 );
		list.add( propValue1 );
		list.add( propValue2 );

		assertTrue( "Expects PropValueList to be of size 3", list.getSize() == 3 );
		assertTrue( "Expects PropValueList to be of size 3 thus NOT empty", !list.isEmpty() );

		//Remove and test
		list.remove( propValue1 );
		assertTrue( "Expects PropValueList to be of size 2", list.getSize() == 2 );

		//Test contains
		assertTrue( "Expects list to have propValue0", list.contains( propValue0 ) );
		assertTrue( "Expects list to NOT have propValue1", !list.contains( propValue1 ) );
		assertTrue( "Expects list to have propValue2", list.contains( propValue2 ) );

		//Clear and test
		list.removeAll();
		assertTrue( "Expects PropValueList to be of size 0", list.getSize() == 0 );
	}

	/** First test of the PropValueList (test the iterator) */
	public void testPropValueList2()
	{
		//Add a few PropValue and test size
		assertTrue( "Expects PropValueList to be of size 0", list.getSize() == 0 );
		assertTrue( "Expects PropValueList to be of size 0 and empty", list.isEmpty() );

		list.add( propValue0 );
		list.add( propValue1 );
		list.add( propValue2 );

		Vector vector1 = new Vector();
		
		vector1.add( propValue0 );
		vector1.add( propValue1 );
		vector1.add( propValue2 );

		assertTrue( "Expects PropValueList to be of size 3", list.getSize() == 3 );
		assertTrue( "Expects PropValueList to be of size 3 thus NOT empty", !list.isEmpty() );

		//Get Iterator and verify contents
		PropValueList.Iterator iterator = list.iterator();

		Vector vector2 = new Vector();

		while( iterator.hasNext() )
			vector2.add( iterator.next() );

		assertTrue( "Expects the PropValueList.Iterator contents is <propValue0, propValue1, propValue2>",
				JUnitUtility.isEquals( vector1, vector2 ) );
		assertTrue( "Expects the PropValueList.Iterator contents to be identical as <propValue0, propValue1, propValue2>",
				JUnitUtility.isIdentical( vector1, vector2 ) );

		//Remove one PropValue and get Iterator and verify contents
		list.remove( propValue0 );
		vector1.remove( propValue0 );
		iterator = list.iterator();

		vector2.clear();

		while( iterator.hasNext() )
			vector2.add( iterator.next() );

		assertTrue( "Expects the PropValueList.Iterator contents is <propValue0, propValue1, propValue2>",
				JUnitUtility.isEquals( vector1, vector2 ) );
		assertTrue( "Expects the PropValueList.Iterator contents to be identical as <propValue0, propValue1, propValue2>",
				JUnitUtility.isIdentical( vector1, vector2 ) );

		//Remove one more PropValue and get Iterator and verify contents
		list.remove( propValue2 );
		vector1.remove( propValue2 );
		iterator = list.iterator();

		vector2.clear();

		while( iterator.hasNext() )
			vector2.add( iterator.next() );

		assertTrue( "Expects the PropValueList.Iterator contents is <propValue0, propValue1, propValue2>",
				JUnitUtility.isEquals( vector1, vector2 ) );
		assertTrue( "Expects the PropValueList.Iterator contents to be identical as <propValue0, propValue1, propValue2>",
				JUnitUtility.isIdentical( vector1, vector2 ) );

		//Remove ALL PropValue and get Iterator and verify contents
		list.removeAll();
		vector1.clear();
		iterator = list.iterator();

		vector2.clear();

		while( iterator.hasNext() )
			vector2.add( iterator.next() );

		assertTrue( "Expects the PropValueList.Iterator contents is <propValue0, propValue1, propValue2>",
				JUnitUtility.isEquals( vector1, vector2 ) );
		assertTrue( "Expects the PropValueList.Iterator contents to be identical as <propValue0, propValue1, propValue2>",
				JUnitUtility.isIdentical( vector1, vector2 ) );
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private PropValueList list = null;
	
	private PropValue propValue0 = null;
	private PropValue propValue1 = null;
	private PropValue propValue2 = null;
}
