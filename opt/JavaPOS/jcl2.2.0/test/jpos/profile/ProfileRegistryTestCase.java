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
 * A JUnit TestCase for the PropfileRegistry
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class ProfileRegistryTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public ProfileRegistryTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
    {
		registry = new DefaultProfileRegistry();

		profile0 = new DefaultProfile( "profile0" );
		profile1 = new DefaultProfile( "profile1" );
		profile2 = new DefaultProfile( "profile2" );
		profile3 = new DefaultProfile( "profile3" );
    }

	protected void tearDown() 
    {
		registry = null;

		profile0 = null;
		profile1 = null;
		profile2 = null;
		profile3 = null;
    }

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	/** First test of the ProfileRegistry */
	public void testProfileRegistry1()
	{
		//Test getSize() and isEmpty()
		assertTrue( "registry should be empty", registry.isEmpty() );
		assertTrue( "registry size should be 0", registry.getSize() == 0 );

		//Add a few profile and test
		Vector vector = new Vector();

		registry.addProfile( profile0 );
		registry.addProfile( profile1 );
		registry.addProfile( profile2 );
		registry.addProfile( profile3 );

		vector.add( profile0 );
		vector.add( profile1 );
		vector.add( profile2 );
		vector.add( profile3 );

		assertTrue( "registry should NOT be empty", registry.isEmpty() == false );
		assertTrue( "registry size should be 4", registry.getSize() == 4 );

		assertTrue( "registry contents should be <profile0, profile1, profile2, profile3>",
				JUnitUtility.isEquals( registry.getProfiles(), vector.elements() ) );

		//Remove profiles and test
		registry.removeProfile( profile1 );
		registry.removeProfile( profile3 );

		vector.remove( profile1 );
		vector.remove( profile3 );

		assertTrue( "registry should NOT be empty", registry.isEmpty() == false );
		assertTrue( "registry size should be 2", registry.getSize() == 2 );
		assertTrue( "registry contents should be <profile0, profile1, profile2, profile3>",
				    JUnitUtility.isEquals( registry.getProfiles(), vector.elements() ) );

	}

	/** First test of the ProfileRegistry (test the iterator) */
	public void testProfileRegistry2()
	{
		//Add profiles and check test enumeration
		emptyTest();
		//Remove profiles and check test enumeration
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private ProfileRegistry registry = null;

	private Profile profile0 = null;
	private Profile profile1 = null;
	private Profile profile2 = null;
	private Profile profile3 = null;
}
