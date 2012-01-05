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
 * Total holds a single type of total. These
 * are grouped by PosTotal.
 *
 * @author  Quentin Olson
 */
public class Total extends DBRecord {

    static Logger logger = Logger.getLogger(Total.class);

    public static final int CASH = 1;
    public static final int CHECK = 2;
    public static final int TAXABLE = 3;
    public static final int NON_TAXABLE = 4;
    public static final int VOID = 5;
    public static final int RETURN = 6;
    public static final int COUPON = 7;
    public static final int RCVD_ON_ACCT = 8;
    public static final int PAID_IN = 9;
    public static final int PAID_OUT = 10;
    public static final int LOAN = 11;
    public static final int PICK_UP = 12;
    public static final int CASH_IN_DRAWER = 13;
    public static final int CHECK_IN_DRAWER = 14;

    public static final int CREDIT_CARD_BASE = 1000;
    public static final int DEPARTMENT_BASE = 2000;
	public static final int ALT_CURRENCY_BASE = 3000;
	public static final int CREDIT_BASE = 4000;
	public static final int GIFT_BASE = 5000;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "total";

        columns = new String[5];

        columns[0] = "total_id";
        columns[1] = "pos_total_id";
        columns[2] = "total_type";
        columns[3] = "total_count";
        columns[4] = "total_amount";

        col_types = new int[5];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.DOUBLE;
    }

    private int totalid;
    private int postotalid;
    private int totaltype;
    private int totalcount;
    private double totalamount;

    public Total() {
    }

    public int totalID() {
        return totalid;
    }

    public int posTotalID() {
        return postotalid;
    }

    public int totalType() {
        return totaltype;
    }

    public int totalCount() {
        return totalcount;
    }

    public double totalAmount() {
        return totalamount;
    }

    public void setTotalID(int value) {
        totalid = value;
    }

    public void setPosTotalID(int value) {
        postotalid = value;
    }

    public void setTotalTypeID(int value) {
        totaltype = value;
    }

    public void setTotalCount(int value) {
        totalcount = value;
    }

    public void setTotalAmount(double value) {
        totalamount = value;
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

    public static String getByPosTotalID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(id));
        s.append(" order by ");
		s.append(columns[2]);

        return new String(s.toString());
    }

    public DBRecord copy() {
        Total b = new Total();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setTotalID(rset.getInt("total_id"));
            setPosTotalID(rset.getInt("pos_total_id"));
            setTotalTypeID(rset.getInt("total_type"));
            setTotalCount(rset.getInt("total_count"));
            setTotalAmount(rset.getDouble("total_amount"));
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    public boolean save() {

        StringBuffer tmp = new StringBuffer();

        tmp.append("insert into ");
        tmp.append(table);
        tmp.append(" values (");
        tmp.append(Integer.toString(totalID()));
        tmp.append(", ");
        tmp.append(Integer.toString(posTotalID()));
        tmp.append(", ");
        tmp.append(Integer.toString(totalType()));
        tmp.append(", ");
        tmp.append(Integer.toString(totalCount()));
        tmp.append(", ");
        tmp.append(Double.toString(totalAmount()));
        tmp.append(",)");

        try {
            Application.dbConnection().execute(tmp.toString());
            Application.dbConnection().commit();
        } catch (SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;

    }

    public static boolean addToTotal(int siteid, int posno, int totaltype, double amount) {
        try {
            updateTotal(new Integer(siteid),
                                new Integer(posno),
                                new Integer(totaltype),
                                new Double(amount));
//            Application.dbConnection().execute(
//                    new SQLProcedureCall(
//                            "update_total",
//                            new Object[]{
//                                new Integer(siteid),
//                                new Integer(posno),
//                                new Integer(totaltype),
//                                new Double(amount)}));
//
//            Application.dbConnection().commit();
        } catch (SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;
    }
    /*
    declare
  postotalid integer;
  totalid integer;
begin
    postotalid := cast((select pos_total_id from pos_total where site_id = $1 and pos_no = $2) as integer);
    if (postotalid is not null)
    then
        totalid := cast((select pos_total_id from total where pos_total_id = postotalid and total_type = $3) as integer);
        if (totalid is not null)
        then
            update total set total_count = total_count + 1 where pos_total_id = postotalid and total_type = totaltype;
            update total set total_amount = total_amount + $4 where pos_total_id = postotalid and total_type = $3;
        else
            insert into total values (0, postotalid, $3, 1, $4);
        end if;
    else
        insert into pos_total values (0, $1, $2, 0, 0);
        insert into total values (0, postotalid, $3, 1, $4);
    end if;
    return 1;
end*/
    private static void updateTotal(Integer siteID, Integer posNo, Integer totalType, Double amount) throws SQLException {
        DBContext conn = Application.dbConnection();

        ResultSet rs = conn.executeWithResult("select pos_total_id from pos_total where site_id = " + siteID + " and pos_no = " + posNo);

        if (rs != null && rs.next()){
            int posTotalID = rs.getInt("pos_total_id");

            rs = conn.executeWithResult("select pos_total_id from total where pos_total_id = " + posTotalID + " and total_type = " + totalType);

            if (rs != null && rs.next()){
                int totalID = rs.getInt("pos_total_id");
                conn.execute("update total set " +                	" total_count = total_count + 1, " +                	" total_amount = total_amount + " + amount + " " +                	" where pos_total_id = " + posTotalID + " and total_type = " + totalType);
            } else {
                conn.execute("insert into total (pos_total_id, total_type, total_count, total_amount) values (" + posTotalID + ", " + totalType + ", 1, " + amount + ")");
            }

        } else {
			
			int posTotalID = PosTotal.insert (conn, siteID.intValue(), posNo.intValue());

            insert(conn, totalType.intValue(), amount.doubleValue(), posTotalID);
        }
    }

	public static void insert(DBContext conn,int totalType, double amount,	int posTotalID)
		throws SQLException {
			
		conn.execute("insert into total (pos_total_id, total_type, total_count, total_amount) values (" + posTotalID + ", " + totalType + ", 1, " + amount + ")");
	}

    public void delete() {

        StringBuffer s = new StringBuffer("delete from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(totalID());

        try {
            Application.dbConnection().execute(s.toString());
            Application.dbConnection().commit();
        } catch (SQLException e) {
            Application.dbConnection().setException(e);
            logger.warn("Zero Totals failed " + e.toString(), e);
        }
    }

    public boolean update() {
        return true;
    }

    //
    // Relations
    //

    private PosProfile posProfile;

    public PosProfile posProfile() {
        return posProfile;
    }

    public void relations() {
    }


    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(totalID()));
        objs.addElement(new Integer(posTotalID()));
        objs.addElement(new Integer(totalType()));
        objs.addElement(new Integer(totalCount()));
        objs.addElement(new Double(totalAmount()));

        return objs;
    }
}


