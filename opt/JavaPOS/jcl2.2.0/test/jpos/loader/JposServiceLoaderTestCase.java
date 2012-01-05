package jpos.loader;

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
 * A JUnit TestCase for the class JposServiceLoader
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class JposServiceLoaderTestCase extends AbstractTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public JposServiceLoaderTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() throws Exception 
    {
		super.setUp();
    }

	protected void tearDown() throws Exception 
    {
		super.tearDown();
    }

	protected boolean useSimpleProfile() { return true; }

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testFindService() throws JposException
	{
		JposServiceLoader.getManager().getProperties().loadJposProperties();
		JposEntryRegistry registry = JposServiceLoader.getManager().getEntryRegistry();

		Enumeration entriesEnum = registry.getEntries();

		while( entriesEnum.hasMoreElements() )
		{
			JposEntry entry = (JposEntry)entriesEnum.nextElement();

			JposServiceConnection connection = JposServiceLoader.findService( entry.getLogicalName() );

			assertTrue( "Could not get connection for existing entry with logical name = " + entry.getLogicalName(),
					connection != null );

			assertTrue( "JposServiceConnection.getLogicalName() == " + connection.getLogicalName() + " is different than the JposEntry.getLogicalName() == " + entry.getLogicalName(),
					connection.getLogicalName().equals( entry.getLogicalName() ) );
		}
	}

	public void testGetManager()
	{
		assertTrue( "JposServiceLoader.getManager() returned null",
				JposServiceLoader.getManager() != null );

		assertTrue( "JposServiceLoader.getManager() must be an instance of JposServiceManager",
				JposServiceLoader.getManager() instanceof JposServiceManager );
	}
}
