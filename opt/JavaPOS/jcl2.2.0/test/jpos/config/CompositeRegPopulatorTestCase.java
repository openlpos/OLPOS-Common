package jpos.config;

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
import java.io.*;
import java.net.URL;

import jpos.test.*;
import jpos.config.simple.*;
import jpos.loader.JposServiceLoader;

/**
 * A JUnit TestCase for the CompositeRegPopulator interface and implementing classes
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class CompositeRegPopulatorTestCase extends AbstractRegPopulatorTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public CompositeRegPopulatorTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() throws Exception 
    {
		createCompositePropFile();
		createEntriesFiles();
		compositePopulator = new DefaultCompositeRegPopulator();
    }

	protected void tearDown() throws Exception
    {
		deleteCompositePropFile();
		deleteEntriesFiles();
		compositePopulator = null;
    }

	//-------------------------------------------------------------------------
	// Private methods
	//

	private void deleteEntriesFiles() throws Exception
	{
		//Delete serialized files
		( new File( CFG_REG_POPULATOR_FILE_0 ) ).delete();
		( new File( CFG_REG_POPULATOR_FILE_1 ) ).delete();
						 
		//Delete XML files
		( new File( XML_REG_POPULATOR_FILE_0 ) ).delete();
		( new File( XML_REG_POPULATOR_FILE_1 ) ).delete(); 
	}

	private void createEntriesFiles() throws Exception
	{
		//Create serialized entries
		cfgPop0EntryList = new ArrayList();
		cfgPop0EntryList.clear();
		for( int i = 0; i < CFG_REG_POPULATOR_0_ENTRIES_SIZE ; i++ )
		{
			JposEntry entry = createDefaultJposEntry( CFG_REG_POPULATOR_FILE_0 + "_" + i );
			cfgPop0EntryList.add( entry );
		}
		createSerializedEntriesFile( cfgPop0EntryList.iterator(), CFG_REG_POPULATOR_FILE_0 );
		assertTrue( "File " + CFG_REG_POPULATOR_FILE_0 + " was not created",
					( new File( CFG_REG_POPULATOR_FILE_0 ) ).exists() );

		cfgPop1EntryList = new ArrayList();
		cfgPop1EntryList.clear();
		for( int i = 0; i < CFG_REG_POPULATOR_1_ENTRIES_SIZE; i++ )
		{
			JposEntry entry = createDefaultJposEntry( CFG_REG_POPULATOR_FILE_1 + "_" + i );
			cfgPop1EntryList.add( entry );
		}
		createSerializedEntriesFile( cfgPop1EntryList.iterator(), CFG_REG_POPULATOR_FILE_1 );
		assertTrue( "File " + CFG_REG_POPULATOR_FILE_1 + " was not created",
					( new File( CFG_REG_POPULATOR_FILE_1 ) ).exists() );

		//Create XML entries
		xmlPop0EntryList = new ArrayList();
		xmlPop0EntryList.clear();
		for( int i = 0; i < XML_REG_POPULATOR_0_ENTRIES_SIZE ; i++ )
		{
			JposEntry entry = createDefaultJposEntry( XML_REG_POPULATOR_FILE_0 + "_" + i );
			xmlPop0EntryList.add( entry );
		}
		createXmlEntriesFile( xmlPop0EntryList.iterator(), XML_REG_POPULATOR_FILE_0 );
		assertTrue( "File " + XML_REG_POPULATOR_FILE_0 + " was not created",
					( new File( XML_REG_POPULATOR_FILE_0 ) ).exists() );

		xmlPop1EntryList = new ArrayList();
		xmlPop1EntryList.clear();
		for( int i = 0; i < XML_REG_POPULATOR_1_ENTRIES_SIZE; i++ )
		{
			JposEntry entry = createDefaultJposEntry( XML_REG_POPULATOR_FILE_1 + "_" + i );
			xmlPop1EntryList.add( entry );
		}
		createXmlEntriesFile( xmlPop1EntryList.iterator(), XML_REG_POPULATOR_FILE_1 );
		assertTrue( "File " + XML_REG_POPULATOR_FILE_1 + " was not created",
					( new File( XML_REG_POPULATOR_FILE_1 ) ).exists() );

		popEntryLists = new List[ 4 ];
		popEntryLists[ 0 ] = cfgPop0EntryList;
		popEntryLists[ 1 ] = xmlPop0EntryList;
		popEntryLists[ 2 ] = cfgPop1EntryList;
		popEntryLists[ 3 ] = xmlPop1EntryList;
	}

	private void createCompositePropFile() throws IOException
	{
        Properties jclProps = new Properties();
		jclProps.put( "jpos.util.tracing", JPOS_UTIL_TRACING_VALUE );
		jclProps.put( "jpos.config.populator.class.0", "jpos.config.simple.SimpleRegPopulator" );
		jclProps.put( "jpos.config.populator.class.1", "jpos.config.simple.xml.SimpleXmlRegPopulator" );
		jclProps.put( "jpos.config.populator.class.2", "jpos.config.simple.SimpleRegPopulator" );
		jclProps.put( "jpos.config.populator.class.3", "jpos.config.simple.xml.SimpleXmlRegPopulator" );
		jclProps.put( "jpos.config.populator.file.0", CFG_REG_POPULATOR_FILE_0  );
		jclProps.put( "jpos.config.populator.file.1", XML_REG_POPULATOR_FILE_0  );
		jclProps.put( "jpos.config.populator.file.2", CFG_REG_POPULATOR_FILE_1  );
		jclProps.put( "jpos.config.populator.file.3", XML_REG_POPULATOR_FILE_1  );
		createPropFile( jclProps );

		JposServiceLoader.getManager().getProperties().loadJposProperties();
	}

	private void deleteCompositePropFile() throws IOException
	{
		restorePropFile();
	}

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testRegPopulatorGetUniqueId()
	{
		compositePopulator.load();

		assertTrue( "compositePopulator.getUniqueId() cannot be null",
				compositePopulator.getUniqueId() != null );

		assertTrue( "compositePopulator.getUniqueId() cannot be empty string",
				"".equals( compositePopulator.getUniqueId() ) != true );

		Iterator populators = compositePopulator.getPopulators();

		while( populators.hasNext() )
		{
			JposRegPopulator populator = (JposRegPopulator)populators.next();

			assertTrue( "populator.getUniqueId() cannot be null",
					populator.getUniqueId() != null );

			assertTrue( "populator.getUniqueId() cannot be empty string",
					"".equals( populator.getUniqueId() ) != true );
		}
	}

	public void testRegPopulatorGetClassName()
	{
		compositePopulator.load();

		assertTrue( "compositePopulator.getClassName() cannot be null",
				compositePopulator.getClassName() != null );

		assertTrue( "compositePopulator.getClassName() cannot be empty string",
				"".equals( compositePopulator.getClassName() ) != true );

		Iterator populators = compositePopulator.getPopulators();

		while( populators.hasNext() )
		{
			JposRegPopulator populator = (JposRegPopulator)populators.next();

			assertTrue( "populator.getClassName() cannot be null",
					populator.getClassName() != null );

			assertTrue( "populator.getClassName() cannot be empty string",
					"".equals( populator.getClassName() ) != true );
		}
	}

	public void testGetDefaultPopulator()
	{
		compositePopulator.load();

		assertTrue( "CompositeRegPopulator.getDefaultRegPopulator() != null",
				compositePopulator.getDefaultPopulator() != null );
	}

	public void testAddRemove()
	{
		compositePopulator.load();
		JposRegPopulator populator = new SimpleRegPopulator( "testAddPopulator" );

		int sizeBeforeAdd = compositePopulator.size();

		compositePopulator.add( populator );

		assertTrue( "Size of CompositeRegPopulator should be 1 more after add!",
				compositePopulator.size() == sizeBeforeAdd + 1 );

		assertTrue( "Added populator to composiste but not found in compositePop.getPopulators()",
				JUnitUtility.isInList( populator, JUnitUtility.createList( compositePopulator.getPopulators() ) ) );

		int sizeAfterAdd = compositePopulator.size();

		compositePopulator.remove( populator );

		assertTrue( "Size of CompositeRegPopulator should be 1 less after remove!",
				compositePopulator.size() == sizeAfterAdd - 1 );

		assertTrue( "Added populator to composiste but not found in compositePop.getPopulators()",
				JUnitUtility.isInList( populator, JUnitUtility.createList( compositePopulator.getPopulators() ) ) == false );
	}

	public void testSize()
	{
		compositePopulator.load();
		assertTrue( "If compositePopulator loaded then size should be: " + MAX_POPULATORS,
				compositePopulator.size() == MAX_POPULATORS );
	}

	public void testGetPopulator()
	{
		compositePopulator.load();

		for( int i = 0; i < 4; i++ )
		{
			assertTrue( "Populator by name :" + "jpos.config.populator.class." + i + " does not exist",
					compositePopulator.getPopulator( "jpos.config.populator.class." + i ) != null );
			assertTrue( "Populator by name :" + "jpos.config.populator.class." + i + " does not exist",
					compositePopulator.getPopulator( "jpos.config.populator.class." + i )
					.getUniqueId().equals( "jpos.config.populator.class." + i ) );
		}
	}

	public void testGetPopulators()
	{
		compositePopulator.load();
		assertTrue( "If compositePopulator loaded then size should be: " + MAX_POPULATORS,
				compositePopulator.size() == MAX_POPULATORS );

		int count = 0;

		Iterator populators = compositePopulator.getPopulators();

		while( populators.hasNext() )
			try
			{ 
				JposRegPopulator populator = (JposRegPopulator)populators.next();
				count++;
			}
			catch( ClassCastException cce )
			{ fail( "CompositeRegPopulator.getPopulators() should only return JposRegPopulator objects" ); }


		assertTrue( "CompositeRegPopulator.getPopulators() should contain " + MAX_POPULATORS + " populators",
				count == MAX_POPULATORS );
	}

	public void testLoad1()
	{
		compositePopulator.load();

		JposRegPopulator defaultPop = compositePopulator.getDefaultPopulator();

		assertTrue( "Default populator cannot be null", defaultPop != null );
		assertTrue( "Default populator unique ID should be: " + "jpos.config.populator.class.0",
				"jpos.config.populator.class.0".equals( defaultPop.getUniqueId() ) );

		Enumeration entries = defaultPop.getEntries();
		assertTrue( "Should load entries from file: " + CFG_REG_POPULATOR_FILE_0,
				JUnitUtility.isEquals( entries, JUnitUtility.createVector( cfgPop0EntryList.iterator() ).elements() ) ); 

		compositePopulator.load( CFG_REG_POPULATOR_FILE_1 );
		
		entries = defaultPop.getEntries();
		assertTrue( "Should load entries from file: " + CFG_REG_POPULATOR_FILE_1,
				JUnitUtility.isEquals( entries, JUnitUtility.createVector( cfgPop1EntryList.iterator() ).elements() ) ); 
	}

	public void testLoad2()
	{
		compositePopulator.load();
		
		for( int i = 0; i < POPULATOR_IDS.length; ++i )
		{
			JposRegPopulator populator = compositePopulator.getPopulator( POPULATOR_IDS[ i ] );

			assertTrue( "Populator cannot be null", populator != null );
			assertTrue( "Populator unique ID should be: " + POPULATOR_IDS[ i ],
					POPULATOR_IDS[ i ].equals( populator.getUniqueId() ) );

			Enumeration entries = populator.getEntries();

			assertTrue( "Should load entries from file: " + POPULATOR_FILES[ i ],
					JUnitUtility.isEquals( entries, JUnitUtility.createVector( popEntryLists[ i ].iterator() ).elements() ) ); 
		}
	}

	public void testSave1()
	{
		emptyTest();
	}

	public void testSave2()
	{
		emptyTest();
	}

	public void testGetEntries()
	{
		compositePopulator.load();

		List entriesList = new ArrayList();

		for( int i = 0; i < popEntryLists.length; ++i )
			for( int j = 0; j < popEntryLists[ i ].size(); ++j )
				entriesList.add( popEntryLists[ i ].get( j ) );

		Enumeration entries = compositePopulator.getEntries();
		assertTrue( "Should have load entries from file: " + CFG_REG_POPULATOR_FILE_0,
				JUnitUtility.isEquals( entries, JUnitUtility.createVector( entriesList.iterator() ).elements() ) ); 
	}

	public void testGetEntriesURL() throws Exception
	{
		compositePopulator.load();

		URL entriesUrl = compositePopulator.getEntriesURL();
		
		//assertTrue( "Entries should be in file: " + CFG_REG_POPULATOR_FILE_0,
		//		entriesUrl.equals( new URL( "file", "", CFG_REG_POPULATOR_FILE_0 ) ) );
	}

	public void testGetLastLoadException()
	{
		emptyTest();
	}

	//-------------------------------------------------------------------------
	// Instance variables
	//

	private CompositeRegPopulator compositePopulator = null;
	private JposRegPopulator[] populators = null;

	private List cfgPop0EntryList = null;
	private List cfgPop1EntryList = null;
	private List xmlPop0EntryList = null;
	private List xmlPop1EntryList = null;

	private List[] popEntryLists = null;

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final int MAX_POPULATORS = 4;

	public static final String CFG_REG_POPULATOR_FILE_0 = TEST_DATA_PATH + "populator0.cfg";
	public static final String CFG_REG_POPULATOR_FILE_1 = TEST_DATA_PATH + "populator1.cfg";

	public static final String XML_REG_POPULATOR_FILE_0 = TEST_DATA_PATH + "populator0.xml";
	public static final String XML_REG_POPULATOR_FILE_1 = TEST_DATA_PATH + "populator1.xml";

	public static final int CFG_REG_POPULATOR_0_ENTRIES_SIZE = 10;
	public static final int CFG_REG_POPULATOR_1_ENTRIES_SIZE = 20;

	public static final int XML_REG_POPULATOR_0_ENTRIES_SIZE = 5;
	public static final int XML_REG_POPULATOR_1_ENTRIES_SIZE = 15;

	public static final String[] POPULATOR_IDS = { "jpos.config.populator.class.0", "jpos.config.populator.class.1",
                                                   "jpos.config.populator.class.2", "jpos.config.populator.class.3" };

	public static final String[] POPULATOR_FILES = { CFG_REG_POPULATOR_FILE_0, XML_REG_POPULATOR_FILE_0,
                                                     CFG_REG_POPULATOR_FILE_1, XML_REG_POPULATOR_FILE_1 };
}
