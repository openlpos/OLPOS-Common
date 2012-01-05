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

import java.io.*;

import jpos.*;

import org.w3c.dom.Document;

/**
 * A JUnit TestCase for the XercesProfileFactory class
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (SF 2K meeting)
 */
public class XercesProfileFactoryTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public XercesProfileFactoryTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	// 

	protected void setUp()
	{
		xProfFactory = new XercesProfileFactory();
		profileFileName = PROFILE_FILE_NAME;
		schemaProfileFileName = SCHEMA_PROFILE_FILE_NAME; 
	}

	protected void tearDown()
	{
		xProfFactory = null;
		profileFileName = "";
		schemaProfileFileName = "";
	}

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//
	
	public void testParse() throws ProfileException, IOException
	{
		File profileFile = new File( profileFileName );
		assertTrue( "JCL JUnit Profile file named = " + profileFileName + ", does not exist",
				profileFile.exists() );
		
		/*
		Document document = xProfFactory.parse( profileFileName );

		assertTrue( "XercesProfileFactory.parse returned a null object",
				    document != null );
		*/
	}

	/** Temporary disable test */
	public void _testParseSchema() throws ProfileException, IOException
	{
		File profileFile = new File( profileFileName );
		assertTrue( "JCL JUnit Schema Profile file named = " + schemaProfileFileName + ", does not exist",
				profileFile.exists() );

		Document document = xProfFactory.parseSchema( schemaProfileFileName );

		assertTrue( "XercesProfileFactory.parseSchema returned a null object",
				    document != null );
	}

	/** Temporary disable test */
	public void _testCreateProfile() throws ProfileException, IOException
	{
		File profileFile = new File( profileFileName );
		assertTrue( "JCL JUnit Profile file named = " + profileFileName + ", does not exist",
				profileFile.exists() );
		
		Profile profile = xProfFactory.createProfile( profileFileName );

		assertTrue( "XercesProfileFactory.createProfile returned a null object", profile != null );
		
		assertTrue( "Profile.name != " + PROFILE_NAME, profile.getName().equals( PROFILE_NAME ) );

		assertTrue( "Profile.version != " + PROFILE_VERSION, profile.getVersion().equals( PROFILE_VERSION ) );
		assertTrue( "Profile.vendorName != " + PROFILE_VENDOR_NAME, profile.getVendorName().equals( PROFILE_VENDOR_NAME ) );
		assertTrue( "Profile.vendorUrl != " + PROFILE_VENDOR_URL, profile.getVendorUrl().toString().equals( PROFILE_VENDOR_URL ) );
		assertTrue( "Profile.description != " + PROFILE_DESCRIPTION, profile.getDescription().equals( PROFILE_DESCRIPTION ) );

		//<temp>
		println( profile );
		//</temp>
	}

    //-------------------------------------------------------------------------
	// Private methods
	//

	private File copyFile( File file, File newDir ) throws IOException
	{
		BufferedInputStream bis = new BufferedInputStream( new FileInputStream( file ) );
		
		String newFileName = newDir.getAbsolutePath() + File.separator + file.getName();

		BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( newFileName ) );

		while( bis.available() > 0 )
		{
			int byteArraySize = bis.available();
			byte[] buffer = new byte[ byteArraySize ];

			bis.read( buffer );
			bos.write( buffer , 0, byteArraySize ); 
		}

		bis.close();
		bos.close();

		return new File( newFileName );
	}

    //-------------------------------------------------------------------------
	// Instance variables
	//

	private XercesProfileFactory xProfFactory = null;
	private String profileFileName = "";
	private String schemaProfileFileName = "";

	//-------------------------------------------------------------------------
	// Class constants
	//

	private static final String PROFILE_FILE_NAME = TEST_DATA_PATH + "jcl_junit_profile.xml";
	private static final String SCHEMA_PROFILE_FILE_NAME = TEST_DATA_PATH + "jcl_junit_schema_profile.xml";
	
	private static final String PROFILE_NAME = "JCL JUnit Corp. JavaPOS Profile";
	private static final String PROFILE_VERSION = "1.0";
	private static final String PROFILE_VENDOR_NAME = "JCL JUnit, Corp.";
	private static final String PROFILE_VENDOR_URL = "http://www.jcl-junit.com";
	private static final String PROFILE_DESCRIPTION = "Simple JCL profile XML file for JUnit testing";
}
