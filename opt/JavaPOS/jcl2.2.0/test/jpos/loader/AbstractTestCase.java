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
import java.io.*;

import jpos.*;
import jpos.config.*;
import jpos.util.*;

/**
 * Super class of all jpos.loader.*
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public abstract class AbstractTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public AbstractTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected methods
	//

	protected void setUp() throws Exception 
    {
		if( useSimpleProfile() )
            createSimplePropFile();
		else
			createMultiPropFile();
    }

	protected void tearDown() throws Exception 
    {
		if( useSimpleProfile() )
            deleteSimplePropFile();
		else
			deleteMultiPropFile();
    }

	protected abstract boolean useSimpleProfile();

	protected List getEntriesList()
	{
		if( useSimpleProfile() )
			return entryList;
		else
		{
			List list = new ArrayList();

			for( int i = 0; i < multiEntryLists.length; ++i )
			{
				Iterator entries = multiEntryLists[ i ].iterator();

				while( entries.hasNext() )
					list.add( entries.next() );
			}

			return list;
		}
	}

	protected void printEntries( Enumeration entriesEnum )
	{ 
		List list = new LinkedList();

		while( entriesEnum.hasMoreElements() )
			list.add( entriesEnum.nextElement() );

		printEntries( list.iterator() ); 
	}

	protected void printEntries( List entriesList )
	{ printEntries( entriesList.iterator() ); }

	protected void printEntries( Iterator entriesIterator )
	{
		StringBuffer sb = new StringBuffer();

		sb.append( "<printEntries>\n" );

		int count = 0;
		while( entriesIterator.hasNext() )
		{
			JposEntry entry = (JposEntry)entriesIterator.next();
			sb.append( "entry" + count + ".LogicalName = " + entry.getLogicalName() + "\n" );
			count++;
		}
		
		sb.append( "</printEntries>\n" );

		println( sb.toString() );
	}

	//-------------------------------------------------------------------------
	// Private methods
	//

	private void createPropertiesMap( Properties prop )
	{
		propertiesMap = new HashMap();

		Enumeration keys = prop.keys();

		while( keys.hasMoreElements() )
		{
			Object key = keys.nextElement();
			propertiesMap.put( key, prop.get( key ) );
		}
	}

	private void createSimplePropFile() throws Exception
	{
        Properties jclProps = new Properties();
		jclProps.put( "jpos.util.tracing", JPOS_UTIL_TRACING_VALUE );
		jclProps.put( "jpos.loader.serviceManagerClass", MANAGER_CLASS );
		jclProps.put( "jpos.config.regPopulatorClass", POPULATOR_CLASS );
		jclProps.put( "jpos.config.populatorFile",  POPULATOR_FILE );
		createPropFile( jclProps );

		createPropertiesMap( jclProps );

		entryList = createDefaultSerializedJposEntriesFile( POPULATOR_FILE, MAX_JPOSENTRIES );
	}

	private void deleteSimplePropFile() throws IOException
	{
		( new File( POPULATOR_FILE ) ).delete();

        restorePropFile();
		
		entryList = null;
		
		propertiesMap = null;
	}

	private void createMultiPropFile() throws Exception
	{
        Properties jclProps = new Properties();
		jclProps.put( "jpos.util.tracing", JPOS_UTIL_TRACING_VALUE );
		jclProps.put( "jpos.loader.serviceManagerClass", MANAGER_CLASS );
		
		int startIndex = 0;

		for( int i = 0; i < populatorClasses.length; ++i )
		{
			jclProps.put( JposProperties.JPOS_CONFIG_POPULATOR_CLASS_MULTIPROP_NAME + "." + i,
						  populatorClasses[ i ] );

			jclProps.put( JposProperties.JPOS_CONFIG_POPULATOR_FILE_MULTIPROP_NAME + "." + i,
						  populatorFiles[ i ] );

			if( populatorTypes[ i ].equals( "XML" ) )
			{
                multiEntryLists[ i ] = createDefaultXmlJposEntriesFile( populatorFiles[ i ], startIndex, multiEntrySizes[ i ] );

				assertTrue(  "Could not create populator file: " + populatorFiles[ i ],
							 ( new File( populatorFiles[ i ] ) ).exists() );
			}
			else
			{
                multiEntryLists[ i ] = createDefaultSerializedJposEntriesFile( populatorFiles[ i ], startIndex, multiEntrySizes[ i ] );
				
				assertTrue(  "Could not create populator file: " + populatorFiles[ i ],
							 ( new File( populatorFiles[ i ] ) ).exists() );
			}

			startIndex += multiEntrySizes[ i ];
		}
		
		createPropertiesMap( jclProps );

		createPropFile( jclProps );
	}

	private void deleteMultiPropFile() throws IOException
	{
		for( int i = 0; i < populatorFiles.length; ++i )
			( new File( populatorFiles[ i ] ) ).delete();

		restorePropFile();
		
		populatorClasses = null;
		populatorFiles = null;
		multiEntrySizes = null;
		multiEntryLists = null;

		propertiesMap = null;
	}

	//-------------------------------------------------------------------------
	// Public testXyz methods
	//

	//-------------------------------------------------------------------------
	// Instance variables
	//
	
	protected List entryList = null;

	protected String[] populatorTypes = { "XML", "Serialized", "XML" };

	protected String[] populatorClasses = { "jpos.config.simple.xml.SimpleXmlRegPopulator",
                                            "jpos.config.simple.SimpleRegPopulator",
											"jpos.config.simple.xml.SimpleXmlRegPopulator" };

	protected String[] populatorFiles = { TEST_DATA_PATH + "populator0.xml",
										  TEST_DATA_PATH + "populator1.cfg",
										  TEST_DATA_PATH + "populator2.xml" };

	protected int[] multiEntrySizes = { 5, 10, 15 };
	
	protected List[] multiEntryLists = new List[ 3 ];

	protected HashMap propertiesMap = null;

	//-------------------------------------------------------------------------
	// Class constants variables
	//

	public static final int MAX_JPOSENTRIES = 10;

	public static final String MANAGER_CLASS = "jpos.loader.simple.SimpleServiceManager";
	public static final String POPULATOR_CLASS = "jpos.config.simple.SimpleRegPopulator";
	public static final String POPULATOR_FILE = TEST_DATA_PATH + "ServiceLoaderTestCase.cfg";
}
