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

import jpos.*;
import jpos.test.*;

/**
 * A JUnit TestCase for the ProfileInfoList
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class PropInfoListTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public PropInfoListTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
    {
		profile = new DefaultProfile( "JUnit" );

		list = new DefaultPropInfoList();

		propInfo0 = new DefaultPropInfo( "propInfo0", profile );
		propInfo1 = new DefaultPropInfo( "propInfo1", profile );
		propInfo2 = new DefaultPropInfo( "propInfo2", profile );
    }

	protected void tearDown() 
    {
		list = null;
    }

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	/** First test of the PropInfoList */
	public void testPropInfoList1()
	{
		//Add a few PropInfo and test size
		assertTrue( "Expects PropInfoList to be of size 0", list.getSize() == 0 );
		assertTrue( "Expects PropInfoList to be of size 0 and empty", list.isEmpty() );

		list.add( propInfo0 );
		list.add( propInfo1 );
		list.add( propInfo2 );

		assertTrue( "Expects PropInfoList to be of size 3", list.getSize() == 3 );
		assertTrue( "Expects PropInfoList to be of size 3 thus NOT empty", !list.isEmpty() );

		//Remove and test
		list.remove( propInfo1 );
		assertTrue( "Expects PropInfoList to be of size 2", list.getSize() == 2 );

		//Test contains
		assertTrue( "Expects list to have propInfo0", list.contains( propInfo0 ) );
		assertTrue( "Expects list to NOT have propInfo1", !list.contains( propInfo1 ) );
		assertTrue( "Expects list to have propInfo2", list.contains( propInfo2 ) );

		//Clear and test
		list.removeAll();
		assertTrue( "Expects PropInfoList to be of size 0", list.getSize() == 0 );
	}

	/** First test of the PropInfoList (test the iterator) */
	public void testPropInfoList2()
	{
		//Add a few PropInfo and test size
		assertTrue( "Expects PropInfoList to be of size 0", list.getSize() == 0 );
		assertTrue( "Expects PropInfoList to be of size 0 and empty", list.isEmpty() );

		list.add( propInfo0 );
		list.add( propInfo1 );
		list.add( propInfo2 );

		Vector vector1 = new Vector();
		
		vector1.add( propInfo0 );
		vector1.add( propInfo1 );
		vector1.add( propInfo2 );

		assertTrue( "Expects PropInfoList to be of size 3", list.getSize() == 3 );
		assertTrue( "Expects PropInfoList to be of size 3 thus NOT empty", !list.isEmpty() );

		//Get Iterator and verify contents
		PropInfoList.Iterator iterator = list.iterator();

		Vector vector2 = new Vector();

		while( iterator.hasNext() )
			vector2.add( iterator.next() );

		assertTrue( "Expects the PropInfoList.Iterator contents is <propInfo0, propInfo1, propInfo2>",
				JUnitUtility.isEquals( vector1, vector2 ) );
		assertTrue( "Expects the PropInfoList.Iterator contents to be identical as <propInfo0, propInfo1, propInfo2>",
				JUnitUtility.isIdentical( vector1, vector2 ) );

		//Remove one PropInfo and get Iterator and verify contents
		list.remove( propInfo0 );
		vector1.remove( propInfo0 );
		iterator = list.iterator();

		vector2.clear();

		while( iterator.hasNext() )
			vector2.add( iterator.next() );

		assertTrue( "Expects the PropInfoList.Iterator contents is <propInfo0, propInfo1, propInfo2>",
				JUnitUtility.isEquals( vector1, vector2 ) );
		assertTrue( "Expects the PropInfoList.Iterator contents to be identical as <propInfo0, propInfo1, propInfo2>",
				JUnitUtility.isIdentical( vector1, vector2 ) );

		//Remove one more PropInfo and get Iterator and verify contents
		list.remove( propInfo2 );
		vector1.remove( propInfo2 );
		iterator = list.iterator();

		vector2.clear();

		while( iterator.hasNext() )
			vector2.add( iterator.next() );

		assertTrue( "Expects the PropInfoList.Iterator contents is <propInfo0, propInfo1, propInfo2>",
				JUnitUtility.isEquals( vector1, vector2 ) );
		assertTrue( "Expects the PropInfoList.Iterator contents to be identical as <propInfo0, propInfo1, propInfo2>",
				JUnitUtility.isIdentical( vector1, vector2 ) );

		//Remove ALL PropInfo and get Iterator and verify contents
		list.removeAll();
		vector1.clear();
		iterator = list.iterator();

		vector2.clear();

		while( iterator.hasNext() )
			vector2.add( iterator.next() );

		assertTrue( "Expects the PropInfoList.Iterator contents is <propInfo0, propInfo1, propInfo2>",
				JUnitUtility.isEquals( vector1, vector2 ) );
		assertTrue( "Expects the PropInfoList.Iterator contents to be identical as <propInfo0, propInfo1, propInfo2>",
				JUnitUtility.isIdentical( vector1, vector2 ) );
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private Profile profile = null;
	private PropInfoList list = null;
	private PropInfo propInfo0 = null;
	private PropInfo propInfo1 = null;
	private PropInfo propInfo2 = null;
}
