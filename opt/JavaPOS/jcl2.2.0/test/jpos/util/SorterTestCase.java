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

import java.util.*;

import jpos.*;
import jpos.test.*;
import jpos.config.JposEntry;
import jpos.config.simple.SimpleEntry;
import jpos.config.simple.editor.JposEntryComparable;

/**
 * A JUnit TestCase for vector sorting class jpos.util.Sorter
 * @author Manuel M Monserrate
 */
public class SorterTestCase extends JposTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public SorterTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	// 

	protected void setUp()
	{
		sorted = new Vector();
		unsorted = new Vector();

        JposEntry entry1 = new SimpleEntry();
        entry1.addProperty( JposEntry.LOGICAL_NAME_PROP_NAME, "aentry1" );

        JposEntry entry2 = new SimpleEntry();
        entry2.addProperty( JposEntry.LOGICAL_NAME_PROP_NAME, "bentry2" );

        JposEntry entry3 = new SimpleEntry();
        entry3.addProperty( JposEntry.LOGICAL_NAME_PROP_NAME, "centry3" );

        JposEntry entry4 = new SimpleEntry();
        entry4.addProperty( JposEntry.LOGICAL_NAME_PROP_NAME, "dentry4" );

		sorted.addElement( entry1 );
		sorted.addElement( entry2 );
		sorted.addElement( entry3 );
        sorted.addElement( entry4 );

		unsorted.addElement( entry3 );
		unsorted.addElement( entry1 );
		unsorted.addElement( entry4 );
        unsorted.addElement( entry2 );
        
	}

	protected void tearDown()
	{
		sorted = null;
		unsorted = null;
	}

    
	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	/** Test the Sorter for comparing two vectors */
	public void testSorter1()
	{
        Vector comparables = new Vector();

        Enumeration enum1 = unsorted.elements();

		while( enum1.hasMoreElements() )
        {
            JposEntryComparable comparable = new JposEntryComparable( (JposEntry)enum1.nextElement() );
            comparables.add( comparable );
        }

        Vector sortedComparables = Sorter.insertionSort( comparables );

        Enumeration enum2 = sortedComparables.elements();

        unsorted.clear();

        while( enum2.hasMoreElements() )
        {
            JposEntry entry = ( (JposEntryComparable)enum2.nextElement() ).getJposEntry();
            unsorted.addElement( entry );
        }
        
        assertTrue( "Vector is not sorted...", JUnitUtility.isIdentical( sorted, unsorted ) );
	}


    //-------------------------------------------------------------------------
	// Instance variables
	//

    private Vector sorted = new Vector();
	private Vector unsorted = new Vector();
}

