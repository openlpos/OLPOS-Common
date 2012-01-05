/*
* Copyright (C) 2003 Igor Semenko
* igorsemenko@yahoo.com
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
*/
package net.sf.mercator.test.util;

import com.globalretailtech.util.Format;

import junit.framework.TestCase;

/**
 * @author Igor Semenko (igorsemenko@yahoo.com)
 *
 */
public class FormatTest extends TestCase {

	/**
	 * Constructor for FormatTest.
	 * @param arg0
	 */
	public FormatTest(String arg0) {
		super(arg0);
	}

	/*
	 * Test for String print(String, String, String, int)
	 */
	public void testPrintStringStringStringint() {
		String result = Format.print ("12345","678"," ",10);
		assertEquals ("12345  678",result);
		
		result = Format.print ("012345555555","678"," ",10);
		assertEquals ("012345 678",result);
	}

	public void testSubstitution (){
		String result = Format.substitute("1{0}3",new Integer(2));
		assertEquals ("123",result);
	}
}
