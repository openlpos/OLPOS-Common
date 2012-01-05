/*
 * Copyright (C) 2001 Global Retail Technology, LLC
 * <http://www.globalretailtech.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.globalretailtech.data;

import java.sql.*;
import java.util.Vector;

import com.globalretailtech.util.Application;
import org.apache.log4j.Logger;


/**
 * POS Total record.
 * (This may be depricated.)
 *
 * @author  Quentin Olson
 */
public class PosTotal extends DBRecord {

    static Logger logger = Logger.getLogger(PosTotal.class);

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "pos_total";

        columns = new String[5];

        columns[0] = "pos_total_id";
        columns[1] = "site_id";
        columns[2] = "pos_no";
        columns[3] = "emp_id";
        columns[4] = "drawer_no";

        col_types = new int[5];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.INT;
    }

    private int postotalid;
    private int siteid;
    private int posno;
    private int empid;
    private int drawerno;


    public PosTotal() {
    }

    public int posTotalID() {
        return postotalid;
    }

    public int siteID() {
        return siteid;
    }

    public int posNo() {
        return posno;
    }

    public int empID() {
        return empid;
    }

    public int drawerNo() {
        return drawerno;
    }

    public void setPosTotalID(int value) {
        postotalid = value;
    }

    public void setSiteID(int value) {
        siteid = value;
    }

    public void setPosNo(int value) {
        posno = value;
    }

    public void setEmpID(int value) {
        empid = value;
    }

    public void setDrawerNo(int value) {
        drawerno = value;
    }

    public static String getByID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }

    public static String getBySiteAndPos(int id, int pos) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(id));
        s.append(" and ");
        s.append(columns[2]);
        s.append(" = ");
        s.append(Integer.toString(pos));

        return new String(s.toString());
    }

    public static String getBySiteAndPosAndDr(int id, int pos, int dr) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(id));
        s.append(" and ");
        s.append(columns[2]);
        s.append(" = ");
        s.append(Integer.toString(pos));
        s.append(columns[3]);
        s.append(" = ");
        s.append(Integer.toString(dr));

        return new String(s.toString());
    }

    public void delete() {

        // first remove the relations and save
        // cash in drawer amount
        
		double cashInDrawer = 0;
        for (int i = 0; i < totals().size(); i++) {

            Total total = (Total) totals().elementAt(i);
            if ( total.totalType() == Total.CASH_IN_DRAWER){
            	cashInDrawer = total.totalAmount();
				logger.debug ("Cash In Drawer before zero: "+cashInDrawer);
            }
            total.delete();
        }

		// second remove pos_total record
		
        StringBuffer s = new StringBuffer("delete from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(posTotalID());

        try {
            Application.dbConnection().execute(s.toString());
            Application.dbConnection().commit();
        } catch (SQLException e) {
            Application.dbConnection().setException(e);
            logger.warn("Zero Totals failed " + e.toString(), e);
        }
        
        // third, create new pos_total record,
        // insert saved cash in drawer amount if not 0
        if ( cashInDrawer > 0 ){
			try {
				int posTotalID =
					insert(Application.dbConnection(), siteID(), posNo());
				Total.insert(
					Application.dbConnection(),
					Total.CASH_IN_DRAWER,
					cashInDrawer,
					posTotalID);
			} catch (Exception e) {
				logger.warn ("Can't save CASH_IN_DRAWER total");
			}
        }
        
    }

    public DBRecord copy() {
        PosTotal b = new PosTotal();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setPosTotalID(rset.getInt("pos_total_id"));
            setSiteID(rset.getInt("site_id"));
            setPosNo(rset.getInt("pos_no"));
            setEmpID(rset.getInt("emp_id"));
            setDrawerNo(rset.getInt("drawer_no"));
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    public boolean save() {

        StringBuffer tmp = new StringBuffer();

        tmp.append("insert into ");
        tmp.append(table);
        tmp.append(" values (");
        tmp.append(Integer.toString(posTotalID()));
        tmp.append(", ");
        tmp.append(Integer.toString(siteID()));
        tmp.append(", ");
        tmp.append(Integer.toString(posNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(empID()));
        tmp.append(", ");
        tmp.append(Integer.toString(drawerNo()));
        tmp.append(")");

        try {
            Application.dbConnection().execute(tmp.toString());
            Application.dbConnection().commit();
        } catch (SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;

    }

    public boolean update() {
        return true;
    }

    //
    // Relations
    //

    private Vector totals;

    public Vector totals() {
        return totals;
    }

    public void setTotals(Vector value) {
        totals = value;
    }

    public void relations() {
        String fetchSpec = Total.getByPosTotalID(posTotalID());
        setTotals(Application.dbConnection().fetch(new Total(), fetchSpec));
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(posTotalID()));
        objs.addElement(new Integer(siteID()));
        objs.addElement(new Integer(posNo()));
        objs.addElement(new Integer(empID()));
        objs.addElement(new Integer(drawerNo()));

        return objs;
    }

	/**
	 * Inserts new pos_total record
	 */
	public static int insert (DBContext conn, int siteID, int posNo) throws SQLException {

		conn.execute("insert into pos_total (site_id, pos_no, emp_id, drawer_no) values (" + siteID + ", " + posNo + ", 0, 0)");
		//this is a bug in the existing stored proc.  it had it inserting postotalid where I have "null" as a java var.
		//but, in this code path, posttotalid is null
		//GUESS:  the inserted pos_total_id should be the result of the above insert.

		ResultSet rs = conn.executeWithResult("select max (pos_total_id) from pos_total");
		rs.next();

		return rs.getInt(1);
	}
}


