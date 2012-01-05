package com.globalretailtech.util.xmlservices;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.FileReader;

import com.globalretailtech.util.Application;


public class SQLDataGenerator {
    Hashtable tables;
	Vector inserts;
	Vector updates;
	Vector deletes;
	org.apache.log4j.Logger logger =
		org.apache.log4j.Logger.getLogger(SQLDataGenerator.class.getName());
		

    public SQLDataGenerator(SQLConverter conv) {
        this(new SQLConverter[]{conv});
    }

    public SQLDataGenerator(SQLConverter[] convs) {
        tables = new Hashtable();
		inserts = new Vector();
		updates = new Vector();
		deletes = new Vector();
        for (int i = 0; i < convs.length; i++) {
			if (convs[i].getTableInserts() != null) {
				inserts.addAll(convs[i].getTableInserts());
			}
			if (convs[i].getTableUpdates() != null) {
				updates.addAll(convs[i].getTableUpdates());
			}
			if (convs[i].getTableDeletes() != null) {
				deletes.addAll(convs[i].getTableDeletes());
			}
//            No longer needed if the XML file contains the column name
            if (convs[i].getTables() != null) {
                for (Enumeration e = convs[i].getTables().elements();
                     e.hasMoreElements();) {
                    SQLTable table = (SQLTable) e.nextElement();
                    if (table.getColumnSize() != 0)
                        tables.put(table.getName(), table);
                }
            }
        }
    }

	public String generateSQLInserts()
			throws SQLMissingTableDef, SQLMismatchedTableItem {
		StringBuffer ret = new StringBuffer();

		for (Enumeration e = inserts.elements(); e.hasMoreElements();) {
			ret.append(generateStringForInsert(
					(SQLTableInsert) e.nextElement()));
		}
		return ret.toString();
	}

	public String generateSQLUpdates(Connection conn)
			throws SQLMissingTableDef, SQLMismatchedTableItem {
		StringBuffer ret = new StringBuffer();

		for (Enumeration e = updates.elements(); e.hasMoreElements();) {
			ret.append(generateStringForUpdate(
					(SQLTableUpdate) e.nextElement(),conn));
		}
		return ret.toString();
	}
	
	public String generateSQLDeletes(Connection conn)
			throws SQLMissingTableDef, SQLMismatchedTableItem {
		StringBuffer ret = new StringBuffer();

		for (Enumeration e = deletes.elements(); e.hasMoreElements();) {
			ret.append(generateStringForDelete(
					(SQLTableUpdate) e.nextElement(),conn));
		}
		return ret.toString();
	}

	public String generateSQLEmptyTables()
			throws SQLMissingTableDef, SQLMismatchedTableItem {

		StringBuffer ret = new StringBuffer();

		Set keys = tables.keySet();
		for (Iterator i = keys.iterator(); i.hasNext();) {
			SQLTable t = (SQLTable) tables.get (i.next());
			ret.append("delete from ");
			ret.append(t.getName());
			ret.append(";\n");			
		}

		return ret.toString();
	}


    private boolean haveColumnNames(SQLTableInsertItem ins) {
        for (int i = 0; i < ins.getColumnSize(); i++) {
            if (ins.getColumnName(i) != null) {
                return true;
            }
        }
        return false;
    }

    private String convertColRep(SQLColumn col, String val) {
        //if the value is null, don't insert the string null, insert it as null
        if (val == null || val.equals("null")){
            return "null";
        }
        if (col.getType().toLowerCase().indexOf("integer") != -1) {
            // type checking
            try {
                Integer tmp = new Integer(val);
                return val.toString();
            } catch (NumberFormatException e) {
                System.err.println("Error parsing " + val + " as number " +
                        "for column " + col.getName());
                throw(e);
            }
        } else if (col.getType().toLowerCase().indexOf("char") != -1 
					|| col.getType().toLowerCase().indexOf("date") != -1
					|| col.getType().toLowerCase().indexOf("timestamp") != -1) {
			//TODO remove hack with back slash problem in Postgresql
			if ( Application.dbConnection().getDriver().indexOf("postgres") != -1){
				val = val.replaceAll("\\\\","\\\\\\\\"); // hard mix of java escape and regex escape
			}
						
            if (val == null) {
                return null;
            } else {
                return "'" + val.replace('\'','`') + "'";
            }
        } else {
            return val;
        }
    }

    private String convertItemSimple(SQLTable tb, SQLTableInsertItem item) {
        StringBuffer ret = new StringBuffer();
        ret.append("insert into ");
        ret.append(tb.getName());
        ret.append(" values (\n");
        for (int i = 0; i < item.getColumnSize(); i++) {
            ret.append("  ");
            ret.append(convertColRep(tb.getColumn(i), item.getColumnValue(i)));
            if (i < (item.getColumnSize() - 1)) {
                ret.append(",\n");
            } else {
                ret.append("\n");
            }
        }
        ret.append(");\n");
        return ret.toString();
    }

    private class ColInfo {
        public String name;
        public int position;

        public ColInfo(String n, int p) {
            name = n;
            position = p;
        }
    }

    private Vector generateColumnInfo(SQLTable tb, SQLTableInsertItem item) {
        Vector ret = new Vector();
        for (int i = 0; i < tb.getColumnSize(); i++) {
            SQLColumn col = tb.getColumn(i);
            if (!col.isUnique() 
            	&& !isBadTimestamp(col, item, i)
            	&& item.containsColumn (col.getName())) {
                ret.add(new ColInfo(col.getName(), i));
            }
        }
        return ret;
    }

	private String convertItemComplex(SQLTable tb, SQLTableInsertItem item) {
		StringBuffer ret = new StringBuffer();
		StringBuffer colnames = new StringBuffer();
		StringBuffer colvalues = new StringBuffer();

		ret.append("insert into ");
		ret.append(tb.getName());
		ret.append(" (\n");
		Vector colInfo = generateColumnInfo(tb, item);
		for (Enumeration e = colInfo.elements(); e.hasMoreElements();) {
			ColInfo info = (ColInfo) e.nextElement();
			colnames.append("  ");
			colvalues.append("  ");
			colnames.append(info.name);
			colvalues.append(
					convertColRep(tb.getColumn(info.name),
							item.getColumnValue(info.name)));
			if (e.hasMoreElements()) {
				colnames.append(",\n");
				colvalues.append(",\n");
			} else {
				colnames.append("\n");
				colvalues.append("\n");
			}
		}
		ret.append(colnames);
		ret.append(")\n");
		ret.append("values (\n");
		ret.append(colvalues);
		ret.append(");\n");
		return ret.toString();
	}
	private String convertItemComplexUpdate (SQLTable tb, SQLTableInsertItem item, String pk) {
		StringBuffer ret = new StringBuffer();
		ret.append("update ");
		ret.append(tb.getName());
		ret.append(" set\n");
//		Vector colInfo = generateColumnInfo(tb, item);
//		for (Enumeration e = colInfo.elements(); e.hasMoreElements();) {
//			ColInfo info = (ColInfo) e.nextElement();
//			ret.append (info.name);
//			ret.append ("=");
//			ret.append (convertColRep(tb.getColumn(info.position),
//				item.getColumnValue(info.position)));
//			if (e.hasMoreElements()) {
//				ret.append(",\n");
//			} else {
//				ret.append("\n");
//			}
//		}
		for (int i = 0; i < item.getColumnSize(); i++) {
			ret.append (item.getColumnName(i));
			ret.append ("=");
			ret.append (convertColRep(tb.getColumn(item.getColumnName(i)),
				item.getColumnValue(i)));
			if (i < item.getColumnSize()-1) {
				ret.append(",\n");
			} else {
				ret.append("\n");
			}
		}
		ret.append(whereCond (tb, item, pk));
		ret.append(";\n");
		return ret.toString();
	}
	
	private String convertItemComplexDelete (SQLTable tb, SQLTableInsertItem item, String pk) {
		StringBuffer ret = new StringBuffer();
		ret.append("delete from ");
		ret.append(tb.getName());
		ret.append(" ");
		ret.append(whereCond (tb, item, pk));
		ret.append(";\n");
		return ret.toString();
	}

    private boolean haveUniqueColumn(SQLTable tb) {
        for (int i = 0; i < tb.getColumnSize(); i++) {
            if (tb.getColumn(i).isUnique()) {
                return true;
            }
        }
        return false;
    }

    private boolean isBadTimestamp(SQLColumn col, SQLTableInsertItem item,
                                   int i) {
        if (col.getType().indexOf("timestamp") != -1) {
            if (item == null) {
                return true;
            }
            if (item.getColumnValue(i) == null) {
                return false;
            }

            if (item.getColumnValue(i).equals("0") ||
                    item.getColumnValue(i).equals("")) {
                return true;
            }
        }
        return false;
    }

    private boolean hasBadTimestamp(SQLTable tb, SQLTableInsertItem item) {
        for (int i = 0; i < tb.getColumnSize(); i++) {
            if (isBadTimestamp(tb.getColumn(i), item, i)) {
                return true;
            }
        }
        return false;
    }

	private String generateStringForInsert(SQLTableInsert ins)
			throws SQLMissingTableDef, SQLMismatchedTableItem {
		StringBuffer ret = new StringBuffer();
		SQLTable tb = (SQLTable) tables.get(ins.getTableName());
		if (tb == null) {
			throw new SQLMissingTableDef(ins.getTableName());
		}
//		  boolean uniqueExists = haveUniqueColumn(tb);

		for (int i = 0; i < ins.getInsertSize(); i++) {
			SQLTableInsertItem item = ins.getInsertItem(i);

			//we ARE now handling mismatched numbers, etc.  Anything will work.
//			  if (!haveColumnNames(item)) {
				//only use complex so we can be sure the table names map appropriately
//				  if (uniqueExists || hasBadTimestamp(tb, item)) {
					ret.append(convertItemComplex(tb, item));
//				  } else {
//					  ret.append(convertItemSimple(tb, item));
//				  }
//			  } else {
//				  // obviously we are not handling names and mismatched
//				  // numbers yet.  Don't do it.
//				  throw new SQLMismatchedTableItem("For table: " + tb.getName() +
//						  " colnames existed or " +
//						  "columns weren't same size");
//			  }
		}
		return ret.toString();
	}


	private String generateStringForUpdate(SQLTableUpdate upd, Connection conn)
			throws SQLMissingTableDef, SQLMismatchedTableItem {
		StringBuffer ret = new StringBuffer();
		SQLTable tb = (SQLTable) tables.get(upd.getTableName());
		if (tb == null) {
			throw new SQLMissingTableDef(upd.getTableName());
		}
		String pk = upd.getPKName();
		
		// for each item to updated, check whether an item with 
		// specified primary key exists, 
		// if exists - generate update statement
		// if doesn't exist - insert statement
		for (int i = 0; i < upd.getInsertSize(); i++) {
			SQLTableInsertItem item = upd.getInsertItem(i);
			boolean itemExists = isExisting (tb, item, pk, conn);
			if ( itemExists )
				ret.append (convertItemComplexUpdate(tb, item, pk));
			else			
				ret.append(convertItemComplex(tb, item));
		}
		return ret.toString();
	}

	private String generateStringForDelete(SQLTableUpdate upd, Connection conn)
			throws SQLMissingTableDef, SQLMismatchedTableItem {
		StringBuffer ret = new StringBuffer();
		SQLTable tb = (SQLTable) tables.get(upd.getTableName());
		if (tb == null) {
			throw new SQLMissingTableDef(upd.getTableName());
		}
		String pk = upd.getPKName();
		
		// for each item to be deleted, check whether an item with 
		// specified primary key exists, 
		// if exists - generate delete statement
		// if doesn't exist - insert empty string
		for (int i = 0; i < upd.getInsertSize(); i++) {
			SQLTableInsertItem item = upd.getInsertItem(i);
			boolean itemExists = isExisting (tb, item, pk, conn);
			if ( itemExists )
				ret.append (convertItemComplexDelete(tb, item, pk));
		}
		return ret.toString();
	}

    /**
     * Checks whether item with specified pk exists in the table
	 */
	private boolean isExisting(SQLTable table, SQLTableInsertItem item, String pk, Connection conn) {
		StringBuffer sql = new StringBuffer();
		sql.append ("select * from ");
		sql.append (table.getName());
		sql.append (whereCond(table, item, pk));
		
		try {
			logger.debug (sql.toString());
			ResultSet rs = conn.createStatement().executeQuery(sql.toString());
			return rs != null && rs.next();
		} catch (SQLException e) {
			logger.error("isExisting()", e);
		}
		
		return false;
	}

	private String whereCond(SQLTable table, SQLTableInsertItem item, String pkeys) {
		StringBuffer sql = new StringBuffer();
		sql.append (" where ");
		String sep = "";
		StringTokenizer st = new StringTokenizer (pkeys,",");
		while (st.hasMoreTokens()){
			String pk = st.nextToken().trim();
			sql.append (sep);
			sql.append (pk);
			sql.append (" = ");
			SQLColumn col = table.getColumn(pk);
			String val = item.getColumnValue(pk);
			String valSqlStr = convertColRep (col,val);
			sql.append (valSqlStr);
			sep = " AND ";
		}
		return sql.toString();
	}
/*************************************************************************/
	static public void main(String[] args)
            throws java.io.FileNotFoundException, java.io.IOException,
            org.xml.sax.SAXException, SQLMissingTableDef, SQLMismatchedTableItem {
            	
        SQLConverter[] convs = new SQLConverter[args.length];

        for (int i = 0; i < args.length; i++) {
            convs[i] = new SQLConverter(new FileReader(args[i]));
        }

        SQLDataGenerator gen = new SQLDataGenerator(convs);

        System.out.println(gen.generateSQLInserts());
    }


}

