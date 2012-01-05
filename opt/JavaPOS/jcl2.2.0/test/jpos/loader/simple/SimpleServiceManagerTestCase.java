package jpos.loader.simple;

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
import jpos.loader.*;
import jpos.config.*;
import jpos.util.*;
import jpos.profile.*;

/**
 * A JUnit TestCase for the class SimpleServiceManager and JposServiceManager interface
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class SimpleServiceManagerTestCase extends jpos.loader.AbstractTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public SimpleServiceManagerTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() throws Exception
    {
		super.setUp();

		manager = JposServiceLoader.getManager();
		manager.reloadEntryRegistry();
    }

	protected void tearDown() throws Exception
    {
		super.tearDown();
    }

	protected boolean useSimpleProfile() { return false; }

	//-------------------------------------------------------------------------
	// Private methods
	//

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testCreateConnection() throws JposException
	{
		JposEntryRegistry registry = manager.getEntryRegistry();
		Enumeration entriesEnum = registry.getEntries();

		while( entriesEnum.hasMoreElements() )
		{
			JposEntry entry = (JposEntry)entriesEnum.nextElement();

			JposServiceConnection connection = manager.createConnection( entry.getLogicalName() );

			assertTrue( "Could not get connection for existing entry with logical name = " + entry.getLogicalName(),
					connection != null );

			assertTrue( "JposServiceConnection.getLogicalName() == " + connection.getLogicalName() + " is different than the JposEntry.getLogicalName() == " + entry.getLogicalName(),
					connection.getLogicalName().equals( entry.getLogicalName() ) );
		}
	}

	public void testGetEntryRegistry1()
	{
		JposEntryRegistry registry = manager.getEntryRegistry();

		assertTrue( "Manager returned a null JposEntryRegistry", registry != null );

		assertTrue( "Registry does not contain all entries that are supposed to be loaded from the properties file",
				JUnitUtility.isEquals( registry.getEntries(), getEntriesList().iterator() ) );
	}

	public void testGetEntryRegistry2()
	{
		JposEntryRegistry registry = manager.getEntryRegistry();

		assertTrue( "Manager returned a null JposEntryRegistry",
				registry != null );

		assertTrue( "Registry does not contain all entries that are supposed to be loaded from the properties file",
				JUnitUtility.isEquals( registry.getEntries(), getEntriesList().iterator() ) );

		List entriesList = getEntriesList();
		for( int i = 0; i < MAX_NEW_ENTRY; ++i )
		{
			JposEntry newEntry = createDefaultJposEntry( "SimpleServiceManagerTestCase" + i );
			entriesList.add( newEntry );
			registry.addJposEntry( newEntry );
		}

		assertTrue( "Registry does not contain the new entries that were added",
				JUnitUtility.isEquals( registry.getEntries(), entriesList.iterator() ) );
	}

	public void testGetProperties()
	{
		JposProperties properties = manager.getProperties();

		assertTrue( "Manager returned a null JposProperties",
				properties != null );

		assertTrue( "manager.getProperties() returned different values than what is properties file",
				JUnitUtility.isEquals( createHashtable( properties ), propertiesMap ) );
	}

	public void testGetRegPopulator()
	{
		JposRegPopulator populator = manager.getRegPopulator();

		assertTrue( "Manager returned a null JposRegPopulator",
				populator != null );
	}

	public void testGetProfilePopulator()
	{
		ProfileRegistry profileReg = manager.getProfileRegistry();

		assertTrue( "Manager returned a null ProfileRegistry",
				profileReg != null );
	}

	public void testLoadProfile()
	{
		emptyTest();
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private JposServiceManager manager = null;

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final int MAX_NEW_ENTRY = 10;
}
