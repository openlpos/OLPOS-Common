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
 * A JUnit TestCase for 
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class DevCatInfoListTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public DevCatInfoListTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
    {
		profile = new DefaultProfile( "JUnit" );

		list = new DefaultDevCatInfoList();

		devCatInfo0 = new DefaultDevCatInfo( profile, JposDevCats.CASHDRAWER_DEVCAT );
		devCatInfo1 = new DefaultDevCatInfo( profile, JposDevCats.POSPRINTER_DEVCAT );
		devCatInfo2 = new DefaultDevCatInfo( profile, JposDevCats.LINEDISPLAY_DEVCAT );
    }

	protected void tearDown() 
    {
		list = null;
    }

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	/** First test of the DevCatInfoList */
	public void testDevCatInfoList1()
	{
		//Add a few DevCatInfo and test size
		assertTrue( "Expects DevCatInfoList to be of size 0", list.getSize() == 0 );
		assertTrue( "Expects DevCatInfoList to be of size 0 and empty", list.isEmpty() );

		list.add( devCatInfo0 );
		list.add( devCatInfo1 );
		list.add( devCatInfo2 );

		assertTrue( "Expects DevCatInfoList to be of size 3", list.getSize() == 3 );
		assertTrue( "Expects DevCatInfoList to be of size 3 thus NOT empty", !list.isEmpty() );

		//Remove and test
		list.remove( devCatInfo1 );
		assertTrue( "Expects DevCatInfoList to be of size 2", list.getSize() == 2 );

		//Test contains
		assertTrue( "Expects list to have devCatInfo0", list.contains( devCatInfo0 ) );
		assertTrue( "Expects list to NOT have devCatInfo1", !list.contains( devCatInfo1 ) );
		assertTrue( "Expects list to have devCatInfo2", list.contains( devCatInfo2 ) );

		//Clear and test
		list.removeAll();
		assertTrue( "Expects DevCatInfoList to be of size 0", list.getSize() == 0 );
	}

	/** First test of the DevCatInfoList (test the iterator) */
	public void testDevCatInfoList2()
	{
		//Add a few DevCatInfo and test size
		assertTrue( "Expects DevCatInfoList to be of size 0", list.getSize() == 0 );
		assertTrue( "Expects DevCatInfoList to be of size 0 and empty", list.isEmpty() );

		list.add( devCatInfo0 );
		list.add( devCatInfo1 );
		list.add( devCatInfo2 );

		Vector vector1 = new Vector();
		
		vector1.add( devCatInfo0 );
		vector1.add( devCatInfo1 );
		vector1.add( devCatInfo2 );

		assertTrue( "Expects DevCatInfoList to be of size 3", list.getSize() == 3 );
		assertTrue( "Expects DevCatInfoList to be of size 3 thus NOT empty", !list.isEmpty() );

		//Get Iterator and verify contents
		DevCatInfoList.Iterator iterator = list.iterator();

		Vector vector2 = new Vector();

		while( iterator.hasNext() )
			vector2.add( iterator.next() );

		assertTrue( "Expects the DevCatInfoList.Iterator contents is <devCatInfo0, devCatInfo1, devCatInfo2>",
				JUnitUtility.isEquals( vector1, vector2 ) );
		assertTrue( "Expects the DevCatInfoList.Iterator contents to be identical as <devCatInfo0, devCatInfo1, devCatInfo2>",
				JUnitUtility.isIdentical( vector1, vector2 ) );

		//Remove one DevCatInfo and get Iterator and verify contents
		list.remove( devCatInfo0 );
		vector1.remove( devCatInfo0 );
		iterator = list.iterator();

		vector2.clear();

		while( iterator.hasNext() )
			vector2.add( iterator.next() );

		assertTrue( "Expects the DevCatInfoList.Iterator contents is <devCatInfo0, devCatInfo1, devCatInfo2>",
				JUnitUtility.isEquals( vector1, vector2 ) );
		assertTrue( "Expects the DevCatInfoList.Iterator contents to be identical as <devCatInfo0, devCatInfo1, devCatInfo2>",
				JUnitUtility.isIdentical( vector1, vector2 ) );

		//Remove one more DevCatInfo and get Iterator and verify contents
		list.remove( devCatInfo2 );
		vector1.remove( devCatInfo2 );
		iterator = list.iterator();

		vector2.clear();

		while( iterator.hasNext() )
			vector2.add( iterator.next() );

		assertTrue( "Expects the DevCatInfoList.Iterator contents is <devCatInfo0, devCatInfo1, devCatInfo2>",
				JUnitUtility.isEquals( vector1, vector2 ) );
		assertTrue( "Expects the DevCatInfoList.Iterator contents to be identical as <devCatInfo0, devCatInfo1, devCatInfo2>",
				JUnitUtility.isIdentical( vector1, vector2 ) );

		//Remove ALL DevCatInfo and get Iterator and verify contents
		list.removeAll();
		vector1.clear();
		iterator = list.iterator();

		vector2.clear();

		while( iterator.hasNext() )
			vector2.add( iterator.next() );

		assertTrue( "Expects the DevCatInfoList.Iterator contents is <devCatInfo0, devCatInfo1, devCatInfo2>",
				JUnitUtility.isEquals( vector1, vector2 ) );
		assertTrue( "Expects the DevCatInfoList.Iterator contents to be identical as <devCatInfo0, devCatInfo1, devCatInfo2>",
				JUnitUtility.isIdentical( vector1, vector2 ) );
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private Profile profile = null;
	private DevCatInfoList list = null;
	
	private DevCatInfo devCatInfo0 = null;
	private DevCatInfo devCatInfo1 = null;
	private DevCatInfo devCatInfo2 = null;
}
