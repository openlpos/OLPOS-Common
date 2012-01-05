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
* Created on 07.07.2003
*
*/
package com.globalretailtech.admin;

/**
 * @author isemenko
 *
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.globalretailtech.data.DBContext;
import com.globalretailtech.util.Application;
import com.globalretailtech.util.xmlservices.CommonImplSQLConverter;
import com.globalretailtech.util.xmlservices.HsqldbSQLConverter;
import com.globalretailtech.util.xmlservices.InterbaseSQLConverter;
import com.globalretailtech.util.xmlservices.PostgresSQLConverter;
import com.globalretailtech.util.xmlservices.SQLConverter;
import com.globalretailtech.util.xmlservices.SQLDataGenerator;
import com.globalretailtech.util.xmlservices.SQLMismatchedTableItem;
import com.globalretailtech.util.xmlservices.SQLMissingTableDef;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class DataSchemaLoader {

	private static final String DATA = "data";
	private static final String SCHEMA = "schema";

	String properties = null;
	String dir = null;
	String fileOrMask = ".*.xml$";
	StringBuffer sb = new StringBuffer();
	String shareDir;
	String mode = DATA;
	boolean reload = false;

	static org.apache.log4j.Logger logger =
		org.apache.log4j.Logger.getLogger(DataSchemaLoader.class);
		
	public DataSchemaLoader (String[] args){	
		//parse command-line arguments
		LongOpt[] longopts = new LongOpt[6];

		longopts[0] = new LongOpt("props", LongOpt.REQUIRED_ARGUMENT, null, 'p');
		longopts[1] = new LongOpt("dir", LongOpt.REQUIRED_ARGUMENT, null, 'D');
		longopts[2] = new LongOpt("file", LongOpt.REQUIRED_ARGUMENT, null, 'f');
		longopts[3] = new LongOpt("data", LongOpt.NO_ARGUMENT, null, 'd');
		longopts[4] = new LongOpt("schema", LongOpt.NO_ARGUMENT, null, 's');
		longopts[5] = new LongOpt("reload", LongOpt.NO_ARGUMENT, null, 'r');

		Getopt g = new Getopt("DataSchemaLoader", args, ":p:D:f:d:s:r", longopts);
		g.setOpterr(false); // We'll do our own error handling

		String arg;
		int c;
		while ((c = g.getopt()) != -1) {
			switch (c) {
				case 'p' :
					properties = g.getOptarg();
					break;
				case 'D' :
					dir = g.getOptarg();
					break;
				case 'f' :
					fileOrMask = g.getOptarg();
					break;
				case 'd' :
					mode = DATA;
					break;
				case 's' :
					mode = SCHEMA;
					break;
				case 'r' :
					reload = true;
					break;
				case ':' :
					System.out.println(
						"Oops! You need an argument for option "
							+ (char) g.getOptopt());
					break;
				case '?' :
					System.out.println(
						"The option '"
							+ (char) g.getOptopt()
							+ "' is not valid");
					break;
				default :
					System.out.println(
						"getopt() returned "
							+ c
							+ " but we couldn't handle it");
					break;
			}
		}
	}

	public void run (){
		try {
			logger.debug("props :[" + properties+"]");
			logger.debug("dir   :[" + dir+"]");
			logger.debug("file  :[" + fileOrMask+"]");
			logger.debug("mode  :" + mode);
			logger.debug("reload:" + reload);

			Properties props = new Properties();
			File propsFile = new File(properties);
			if ( ! propsFile.exists() ){
				logger.warn("Properties file[" + propsFile.getAbsolutePath()+ "] doesn't exist");
				System.exit (1);
			}
			
			InputStream propsIs = new FileInputStream(propsFile);
			props.load(propsIs);

			DBContext dbContext = new DBContext();
			dbContext.init(props);
			if (!dbContext.makeConnection()) {
				logger.fatal("Can't make db connection");
				System.exit (1);
			}
			Application.setDbConnection(dbContext);

			File dataDir = new File(dir);
			if (!dataDir.exists()) {
				logger.fatal("Directory [" + dir + "] doesn't exist");
				System.exit (1);
			}
			FilenameFilter filter = new FilenameRegexFilter(fileOrMask);
			String[] files = dataDir.list(filter);

			if (files == null || files.length == 0) {
				logger.warn("No files match pattern [" + fileOrMask + "]");
				System.exit (1);
			}

			if (mode.equals(SCHEMA)) {
				doCreateSchema(dbContext, dataDir, files);
			} else {
				doProcessData (dbContext, dataDir,  files);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		Logger logger = Logger.getRootLogger();
		logger.setLevel(Level.INFO);
// logger.warn ("log4j.configuration: "+System.getProperty("log4j.configuration"));				
		DataSchemaLoader loader = new DataSchemaLoader(args);
		
		loader.run();


		// clean db
		//		DBContext conn = Application.dbConnection();
		//		for (int i = 0; i < DBContext.tableList.length; i++) {
		//			String tableName = DBContext.tableList[i];
		//			try {
		//				conn.execute("DELETE FROM " + tableName);
		//			} catch (SQLException e1) {
		//				logger.warn("SQLException trying to clean " + tableName + ":" + e1, e1);
		//			}
		//		}
		System.exit(0);
	}

	/**
	 * @param dbContext
	 * @param files
	 */
	private void doCreateSchema(DBContext dbContext, File baseDir, String[] files) throws FileNotFoundException, IOException, SAXException {
		for (int i = 0; i < files.length; i++) {
			String file = files[i];
			// create schema
			SQLConverter conv = new SQLConverter(new FileReader(new File (baseDir, file)));

			CommonImplSQLConverter gen = null;

			if (dbContext.getDriver().indexOf("postgres") != -1) {
				gen = new PostgresSQLConverter(conv);
			} else if (dbContext.getDriver().indexOf("interbase") != -1) {
				gen = new InterbaseSQLConverter(conv);
			} else if (dbContext.getDriver().indexOf("hsqldb") != -1) {
				gen = new HsqldbSQLConverter(conv);
			}

			if (gen != null) {
				try {
					dbContext.execute(gen.generateCreationSQL());
				} catch (SQLException e) {
					logger.warn("Can't execute sql statement", e);
				}
			} else {
				logger.fatal("Unrecognised DB type, quitting.");
			}
		}
	}

	/**
	 * Processes "tablesinsert" "tableupdate" "tabledelete" xml commands
	 * 
	 * @param dbContext
	 * @param files
	 */
	private void doProcessData (DBContext dbContext, File baseDir, String[] files)
		throws
			FileNotFoundException,
			IOException,
			SAXException,
			SQLException,
			SQLMissingTableDef,
			SQLMismatchedTableItem {
		for (int i = 0; i < files.length; i++) {
			String file = files[i];
			// import data...
			SQLConverter conv = new SQLConverter(new FileReader(new File (baseDir, file)));

			SQLDataGenerator gen = new SQLDataGenerator(conv);

			if (reload){
				dbContext.execute(gen.generateSQLEmptyTables());
			}
			
			dbContext.execute(gen.generateSQLInserts());

			dbContext.execute(gen.generateSQLUpdates(dbContext.getConn()));

			dbContext.execute(gen.generateSQLDeletes(dbContext.getConn()));
		}
	}

}
	class FilenameRegexFilter implements FilenameFilter {

		Pattern pattern;
		public FilenameRegexFilter(String regexMask) {
			pattern = Pattern.compile(regexMask);
		}

		/* 
		 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
		 */
		public boolean accept(File dir, String name) {
			Matcher m = pattern.matcher(name);
			if (m.find())
				return true;
			return false;
		}

	}
