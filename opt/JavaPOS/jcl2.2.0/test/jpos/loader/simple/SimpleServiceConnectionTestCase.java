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
import jpos.config.*;
import jpos.loader.*;
import jpos.test.*;


/**
 * A JUnit TestCase for the SimpleServiceConnection and JposServiceConnection interface
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class SimpleServiceConnectionTestCase extends jpos.loader.AbstractTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public SimpleServiceConnectionTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() throws Exception
    {
		super.setUp();

		manager = JposServiceLoader.getManager();
		manager.reloadEntryRegistry();

		registry = manager.getEntryRegistry();
    }

	protected void tearDown() throws Exception
    {
		manager = null;
		registry = null;

		super.tearDown();
    }

	protected boolean useSimpleProfile() { return false; }

	protected JposEntry createDefaultJposEntry( String logicalName )
	{
		return createJposEntry( logicalName, 
								XYZ_FACTORY_CLASS, 
								XYZ_SERVICE_CLASS, 
								"Xyz, Corp.",
								"http://www.javapos.com", 
								"LineDisplay",
								"1.5",
								"Virtual LineDisplay JavaPOS Service",
								"Example virtual LineDisplay JavaPOS Service from virtual Xyz Corporation",
								"http://www.javapos.com", 
								null );
	}

	//-------------------------------------------------------------------------
	// Private methods
	//

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testGetService() throws JposException, ClassNotFoundException
	{
		Enumeration entries = registry.getEntries();

		while( entries.hasMoreElements() )
		{
			JposEntry entry = (JposEntry)entries.nextElement();

			JposServiceConnection connection = manager.createConnection( entry.getLogicalName() );

			JposServiceInstance service = connection.getService();
			assertTrue( "connection.getService() should be null since no connection.connect() call was made",
					service == null );

			connection.connect();

			service = connection.getService();
			assertTrue( "connection.getService() returned null after successful connection.connect() call",
					service != null );

			assertTrue( "connection.getService() should be instanceof " + XYZ_SERVICE_CLASS,
					Class.forName( XYZ_SERVICE_CLASS ).isInstance( service ) );

			connection.disconnect();
		}
	}

	public void testGetLogicalName() throws JposException
	{
		Iterator registryLogicalNames = logicalNames( JUnitUtility.createVector( registry.getEntries() ).iterator() ).iterator();
		Iterator expectedLogicalNames = logicalNames( getEntriesList() ).iterator();
		
		assertTrue( "All logicalName in registry should match added entries logicalNames",
				JUnitUtility.isEquals( registryLogicalNames, expectedLogicalNames ) );

		List logicalNameList = new ArrayList();
		Enumeration entries = registry.getEntries();

		while( entries.hasMoreElements() )
		{
			JposEntry entry = (JposEntry)entries.nextElement();

			JposServiceConnection connection = manager.createConnection( entry.getLogicalName() );

			logicalNameList.add( connection.getLogicalName() );
		}

		assertTrue( "All logicalName in registry should match added entries logicalNames",
				JUnitUtility.isEquals( logicalNameList, logicalNames( getEntriesList() ) ) );
	}

	public void testConnect() throws JposException
	{
		Enumeration entries = registry.getEntries();

		while( entries.hasMoreElements() )
		{
			JposEntry entry = (JposEntry)entries.nextElement();

			JposServiceConnection connection = manager.createConnection( entry.getLogicalName() );

			connection.connect();
		}
	}

	public void testDisconnect() throws JposException
	{
		Enumeration entries = registry.getEntries();

		while( entries.hasMoreElements() )
		{
			JposEntry entry = (JposEntry)entries.nextElement();

			JposServiceConnection connection = manager.createConnection( entry.getLogicalName() );

			connection.connect();
			connection.disconnect();
		}
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private JposServiceManager manager = null;
	private JposEntryRegistry registry = null;

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String XYZ_SERVICE_CLASS = "com.xyz.jpos.LineDisplayService";
	public static final String XYZ_FACTORY_CLASS = "com.xyz.jpos.XyzJposServiceInstanceFactory";
}
