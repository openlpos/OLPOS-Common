package jpos.test;

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

/**
 * Utility class to create JUnit TestCase and TestSuite
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class JUnitUtility extends Object
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	/** Make ctor private to prevent construction */
	private JUnitUtility() {}

	//-------------------------------------------------------------------------
	// Public class methods
	//

	/**
	 * @return a Vector with contents of the Iterator
	 * @param iterator the Iterator object
	 */
	public static Vector createVector( Iterator iterator )
	{
		Vector v = new Vector();

		while( iterator.hasNext() )
			v.addElement( iterator.next() );

		return v;
	}

	/**
	 * @return a Vector with contents of the List
	 * @param iterator the Iterator object
	 */
	public static Vector createVector( List list )
	{
		return createVector( list.iterator() );
	}

	/**
	 * @return a Vector with contents of the Enumeration 
	 * @param enum the Enumeration object
	 */
	public static Vector createVector( Enumeration enum )
	{
		Vector v = new Vector();

		while( enum.hasMoreElements() )
			v.addElement( enum.nextElement() );

		return v;
	}

	/**
	 * @return a Vector with contents of the Object[] 
	 * @param array an Object[]
	 */
	public static Vector createVector( Object[] array )
	{
		Vector v = new Vector();

		for( int i = 0; i < array.length; ++ i)
			v.addElement( array[ i ] );

		return v;
	}

	/**
	 * @return true if the 2 List have the same Objects in same order
	 * @param l1 the first List
	 * @param 12 the second List
	 */
	public static boolean isIdentical( List l1, List l2 )
	{
		return isIdentical( new Vector( l1 ).elements(), new Vector( l2 ).elements() );
	}

	/**
	 * @return true if the 2 Iterator have the same Objects in same order
	 * @param i1 the first Iterator
	 * @param i2 the second Iterator
	 */
	public static boolean isIdentical( Iterator i1, Iterator i2 )
	{
		return isIdentical( createVector( i1 ).elements(), createVector( i2 ).elements() );
	}

	/**
	 * @return true if the 2 Enumeration have the same Objects in same order
	 * @param v1 the first Vector
	 * @param v2 the second Vector
	 */
	public static boolean isIdentical( Vector v1, Vector v2 )
	{
		return isIdentical( v1.elements(), v2.elements() );
	}

	/**
	 * @return true if the 2 Enumeration have the same Objects in same order 
	 * @param enum1 the first Enumeration
	 * @param enum2 the second Enumeration
	 */
	public static boolean isIdentical( Enumeration enum1, Enumeration enum2 )
	{
		if( enum1 == null || enum2 == null )
			return false;

		if( enum1.hasMoreElements() && ( enum2.hasMoreElements() == false ) )
			return false;

		if( enum2.hasMoreElements() && ( enum1.hasMoreElements() == false ) )
			return false;

		while( enum1.hasMoreElements() )
		{
			Object enum1Obj = enum1.nextElement();

			if( enum2.hasMoreElements() == false )
				return false;

			Object enum2Obj = enum2.nextElement();

			if( !enum1Obj.equals( enum2Obj ) )
				return false;
		}

		if( enum2.hasMoreElements() )
			return false;

		return true;
	}

	/**
	 * @return true if the two arguments have the same elements (don't need to be in same order)
	 * @param array an Object[]
	 * @param vector a Vector 
	 */
	public static boolean isIdentical( Object[] array, Vector vector )
	{
		return isIdentical( createVector( array ), vector );
	}

	/**
	 * @return true if the two arguments have the same elements (don't need to be in same order)
	 * @param array an Object[]
	 * @param enum an Enumeration of Objects 
	 */
	public static boolean isIdentical( Object[] array, Enumeration enum )
	{
		return isIdentical( createVector( array ).elements(), enum );
	}

	/**
	 * @return true if the two arguments have the same elements (don't need to be in same order)
	 * @param array an Object[]
	 * @param vector a Vector 
	 */
	public static boolean isEquals( Object[] array, Vector vector )
	{
		return isEquals( createVector( array ), vector );
	}

	/**
	 * @return true if the two arguments have the same elements (don't need to be in same order)
	 * @param array an Object[]
	 * @param vector a Vector 
	 */
	public static boolean isEquals( Object[] array, Enumeration enum )
	{
		return isEquals( createVector( array ).elements(), enum );
	}

	/**
	 * @return true if the 2 Enumeration have the same Objects (could be in different order)
	 * @param enum1 the first Enumeration
	 * @param enum2 the second Enumeration
	 */
	public static boolean isEquals( Enumeration enum1, Enumeration enum2 )
	{
		if( enum1 == null || enum2 == null )
			return false;

		return isEquals( createVector( enum1 ), createVector( enum2 ) );
	}			  

	/**
	 * @return true if the 2 Vector have the same Objects (could be in different order)
	 * @param v1 the first Vector
	 * @param v2 the second Vector
	 */
	public static boolean isEquals( Vector v1, Vector v2 )
	{
		if( v1 == null || v2 == null )
			return false;

		Vector v1Clone = (Vector)v1.clone();
		Vector v2Clone = (Vector)v2.clone();

		if( v1Clone.size() != v2Clone.size() )
			return false;

		for( int i = 0; i < v1Clone.size(); ++i )
		{
			Object v1CloneObj = v1Clone.elementAt( i );

			boolean found = false;
			Object v2CloneObj = null;

			for( int j = 0; j < v2Clone.size(); ++j )
			{
				v2CloneObj = v2Clone.elementAt( j );

				if( v1CloneObj.equals( v2CloneObj ) )
				{
					found = true;
					break;
				}
			}

			if( found )
				v2Clone.removeElement( v2CloneObj );
			else
				return false;
		}

		return ( v2Clone.size() == 0 ? true : false );
	}

	/**
	 * @return true if the 2 List have the same Objects (could be in different order)
	 * @param l1 the first List
	 * @param l2 the second List
	 */
	public static boolean isEquals( List l1, List l2 )
	{ return isEquals( createVector( l1 ), createVector( l2 ) ); }

	/**
	 * @return true if the 2 Iterator have the same Objects (could be in different order)
	 * @param i1 the first Iterator
	 * @param i2 the second Iterator
	 */
	public static boolean isEquals( Iterator i1, Iterator i2 )
	{ 
		return isEquals( createVector( i1 ), createVector( i2 ) ); 
	}

	/**
	 * @return true if the Enumeration and Iterator are equals
	 * @param enumeration the Enumeration
	 * @param iterator the Iterator
	 */
	public static boolean isEquals( Enumeration enumeration, Iterator iterator )
	{
		if( enumeration == null ) return false;
		if( iterator == null ) return false;

		return isEquals( enumeration, createVector( iterator ).elements() );
	}

	/**
	 * @return true if the 2 Hashtable passed are equals
	 * @param table1 the first Hashtable
	 * @param table2 the second Hashtable
	 */
	public static boolean isEquals( Hashtable table1, Hashtable table2 )
	{
		if( table1.size() != table2.size() )
			return false;

		Enumeration keys1 = table1.keys();

		while( keys1.hasMoreElements() )
		{
			Object key1 = keys1.nextElement();
			Object value1 = table1.get( key1 );

			if( !table2.containsKey( key1 ) )
				return false;

			if( table2.get( key1 ).equals( value1 ) == false )
				return false;
		}

		return true;
	}

	/**
	 * @return true if the Hashtable and HashMap passed are equals
	 * @param table the Hashtable
	 * @param map the HashMap
	 */
	public static boolean isEquals( Hashtable table, HashMap map )
	{
		return isEquals( table, createHashtable( map ) );
	}

	/**
	 * @return true if value passed is one of the values in the Vector of values passed
	 * @param byteValue the Byte value object
	 * @param list the List of Byte values
	 */
	public static boolean isInList( Byte value, List list )
	{
		Iterator iterator = list.iterator();

		while( iterator.hasNext() )
		{
			Object obj = iterator.next();

			if( obj instanceof Byte )
				if( ( (Byte)obj ).equals( value ) )
					return true;
		}

		return false;
	}

	/**
	 * @return true if value passed is one of the values in the Vector of values passed
	 * @param shortValue the Short value object
	 * @param list the List of Short values
	 */
	public static boolean isInList( Short value, List list )
	{
		Iterator iterator = list.iterator();

		while( iterator.hasNext() )
		{
			Object obj = iterator.next();

			if( obj instanceof Short )
				if( ( (Short)obj ).equals( value ) )
					return true;
		}

		return false;
	}

	/**
	 * @return true if value passed is one of the values in the Vector of values passed
	 * @param intValue the Integer value object
	 * @param list the List of Integer values
	 */
	public static boolean isInList( Integer value, List list )
	{
		Iterator iterator = list.iterator();

		while( iterator.hasNext() )
		{
			Object obj = iterator.next();

			if( obj instanceof Integer )
				if( ( (Integer)obj ).equals( value ) )
					return true;
		}

		return false;
	}

	/**
	 * @return true if value passed is one of the values in the Vector of values passed
	 * @param objValue the object
	 * @param list the List of objects of same type
	 */
	public static boolean isInList( Object value, List list )
	{
		Iterator iterator = list.iterator();

		while( iterator.hasNext() )
		{
			Object obj = iterator.next();
				
			if( obj.equals( value ) )
				return true;
		}

		return false;
	}

	/** 
	 * @return a List with contents from the Iterator objects
	 * @param iterator the Iterator of objects to put in List
	 */
	public static List createList( Iterator iterator )
	{
		List list = new ArrayList();

		while( iterator.hasNext() )
			list.add( iterator.next() );

		return list;
	}	

	/**
	 * @return a Hashtable from the HashMap passed
	 * @param map the HashMap
	 */
	public static Hashtable createHashtable( HashMap map )
	{
		Hashtable table = new Hashtable();

		Iterator keys = map.keySet().iterator();

		while( keys.hasNext() )
		{
			Object key = keys.next();
			table.put( key, map.get( key ) );
		}

		return table;
	}
}
