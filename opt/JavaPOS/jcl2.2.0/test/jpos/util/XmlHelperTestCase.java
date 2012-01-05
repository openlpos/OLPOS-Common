package jpos.util;

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

import java.io.IOException;
import java.io.File;

import jpos.*;

/**
 * A JUnit TestCase for the XmlHelper class
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 * @since 1.3 (SF 2K meeting)
 */
public class XmlHelperTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public XmlHelperTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	// 

	protected void setUp()
	{
		xmlHelper = new XmlHelper();
	}

	protected void tearDown()
	{
		xmlHelper = null;
	}

    
	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testXmlHelperRemoveDirs()
	{
		String dirName = "." + File.separator + "mike" +
						 File.separator + "max" +       
						 File.separator + "profile";

		File dir = new File( dirName );
		 
		if( !dir.exists() )
			dir.mkdirs();

		try{ xmlHelper.removeDirs( dirName ); }
		catch( IOException ioe ) 
		{ fail( "XmlHelper.removeDirs( String ) failed with message = " + ioe.getMessage() ); }
	}

	public void testXmlHelperJclDtd()
	{
		xmlHelper.setDtdFileName( "jcl.dtd" );
		xmlHelper.setDtdFilePath( "jpos" + File.separator + "res" );

		boolean dtdFileExisted = ( new File( "." + File.separator + xmlHelper.getDtdFilePath() + xmlHelper.getDtdFileName() ) ).exists();

		xmlHelper.checkAndCreateTempDtd();

		File dtdFile = new File( "." + File.separator + xmlHelper.getDtdFilePath() + xmlHelper.getDtdFileName() );
		assertTrue( "JCL DTD file was not extracted or does not exist...", dtdFile.exists() );

		xmlHelper.removeTempDtd();
		
		if( dtdFileExisted )
		{
			dtdFile = new File( "." + File.separator + xmlHelper.getDtdFilePath() + xmlHelper.getDtdFileName() );
			assertTrue( "JCL DTD file should still exists...", dtdFile.exists() );
		}
		else
		{
			dtdFile = new File( "." + File.separator + xmlHelper.getDtdFilePath() + xmlHelper.getDtdFileName() );
			assertTrue( "JCL DTD file should have been deleted...", dtdFile.exists() == false );
		}
	}

	public void testXmlHelperJclProfileDtd()
	{
		xmlHelper.setDtdFileName( "jcl_profile.dtd" );
		xmlHelper.setDtdFilePath( "jpos" + File.separator + "res" );

		boolean dtdFileExisted = ( new File( "." + File.separator + xmlHelper.getDtdFilePath() + xmlHelper.getDtdFileName() ) ).exists();

		xmlHelper.checkAndCreateTempDtd();

		File dtdFile = new File( "." + File.separator + xmlHelper.getDtdFilePath() + xmlHelper.getDtdFileName() );
		assertTrue( "JCL profile DTD file was not extracted or does not exist...", dtdFile.exists() );

		xmlHelper.removeTempDtd();
		
		if( dtdFileExisted )
		{
			dtdFile = new File( "." + File.separator + xmlHelper.getDtdFilePath() + xmlHelper.getDtdFileName() );
			assertTrue( "JCL DTD file should still exists...", dtdFile.exists() );
		}
		else
		{
			dtdFile = new File( "." + File.separator + xmlHelper.getDtdFilePath() + xmlHelper.getDtdFileName() );
			assertTrue( "JCL DTD file should have been deleted...", dtdFile.exists() == false );
		}
	}

    //-------------------------------------------------------------------------
	// Instance variables
	//

	private XmlHelper xmlHelper = null;
}

