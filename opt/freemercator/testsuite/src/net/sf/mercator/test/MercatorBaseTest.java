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
package net.sf.mercator.test;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.BasicConfigurator;

import com.globalretailtech.data.DBContext;
import com.globalretailtech.util.Application;

import junit.framework.TestCase;

/**
 * @author Igor Semenko (igorsemenko@yahoo.com)
 *
 */
public class MercatorBaseTest extends TestCase {

	static DBContext db;
	static boolean isLog4jConfigured;
	
	/**
	 * Constructor for MercatorTest.
	 * @param arg0
	 */
	public MercatorBaseTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		if ( ! isLog4jConfigured ){

			initLog4j();

		}
		
		if ( db == null){
			
			createDBContext();
		}
		
		
		//TODO setup PosContext()
		 
	}

	protected void createDBContext() throws SQLException {
		db = new DBContext();
		boolean connected = db.makeConnection(
						  "org.hsqldb.jdbcDriver",
						  "jdbc:hsqldb:db/site.hsqldb",
						  "sa",
						  "");
		assertTrue (connected);
		
		// make sure we are connected to the real db
		ResultSet rs = db.executeWithResult("select * from site");
		assertTrue (rs.next());				  
		
		Application.setDbConnection(db);
		
	}


	protected void initLog4j() {
		BasicConfigurator.configure();
		isLog4jConfigured = true;
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
