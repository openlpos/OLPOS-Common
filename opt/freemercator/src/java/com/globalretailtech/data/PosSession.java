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
import java.util.Date;
import java.util.Vector;

import com.globalretailtech.util.Application;

import com.globalretailtech.util.sql.*;

/**
 * POS Session records a POS inteaction with the
 * transport network.
 *
 * @author  Quentin Olson
 */
public class PosSession extends DBRecord {

    public static final int TRANS_UPLOAD = 0;
    public static final int CONFIG_UPLOAD = 1;
    public static final int CONFIG_DOWNOAD = 2;
    public static final int MONITOR = 3;

    public static final int COMPLETE = 0;
    public static final int IN_SESSION = 1;
    public static final int FAILED = 2;


    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "pos_session";

        columns = new String[7];

        columns[0] = "pos_session_id";
        columns[1] = "site_id";
        columns[2] = "pos_no";
        columns[3] = "session_type";
        columns[4] = "status";
        columns[5] = "start_date";
        columns[6] = "end_date";

        col_types = new int[7];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.INT;
        col_types[5] = DBRecord.DATE;
        col_types[6] = DBRecord.DATE;
    }

    private int possessionid;
    private int siteid;
    private int posno;
    private int sessiontype;
    private int status;
    private Date startdate;
    private Date enddate;

    public PosSession() {
    }

    public int posSessionID() {
        return possessionid;
    }

    public int siteID() {
        return siteid;
    }

    public int posNo() {
        return posno;
    }

    public int sessionType() {
        return sessiontype;
    }

    public int status() {
        return status;
    }

    public Date startDate() {
        return startdate;
    }

    public Date endDate() {
        return enddate;
    }

    public void setPosSessionID(int value) {
        possessionid = value;
    }

    public void setSiteID(int value) {
        siteid = value;
    }

    public void setPosNo(int value) {
        posno = value;
    }

    public void setSessionType(int value) {
        sessiontype = value;
    }

    public void setStatus(int value) {
        status = value;
    }

    public void setStartDate(Date value) {
        startdate = value;
    }

    public void setEndDate(Date value) {
        enddate = value;
    }

    public static String getAll() {

        StringBuffer s = new StringBuffer("select * from ");
        s.append(table);
        s.append("order by ");
        s.append("end_date");

        return new String(s.toString());
    }

    public static String getAllByType(int type) {

        StringBuffer s = new StringBuffer("select * from ");
        s.append(table);
        s.append(" where ");
        s.append(columns[3]);
        s.append(" = ");
        s.append(Integer.toString(type));
        s.append("order by ");
        s.append("end_date");

        return new String(s.toString());
    }

    public static String getLastByType(int type) {

        StringBuffer s = new StringBuffer("select * from ");
        s.append(table);
        s.append(" where ").append(columns[6]);
        s.append(" = (select max (");
        s.append(columns[6]).append(") from ");
        s.append(table).append(")");

        return new String(s.toString());
    }


    public DBRecord copy() {
        PosSession b = new PosSession();
        return b;
    }

    public Vector parents() {
        return null;
    }


    public void populate(ResultSet rset) {

        try {
            setPosSessionID(rset.getInt("pos_session_id"));
            setSiteID(rset.getInt("site_id"));
            setPosNo(rset.getInt("pos_no"));
            setSessionType(rset.getInt("session_type"));
            setStatus(rset.getInt("status"));
            setStartDate(rset.getDate("start_date"));
            setEndDate(rset.getDate("end_date"));
        } catch (SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    public boolean save() {
        ResultSet rs = null;
        try {
            rs = Application.dbConnection().executeWithResult(
                    new SQLProcedureCall("start_session",
                            new Object[]{
                                new Integer(siteID()),
                                new Integer(posNo()),
                                new Integer(sessionType()),
                                new Integer(status())}));
        } catch (UnknownSQLCall e) {
            return false;
        }
        try {
            if (rs != null && rs.next()) {

                setPosSessionID(rs.getInt(1));
                Application.dbConnection().commit();

            }
        } catch (SQLException je) {
            return false;
        }
        return true;
    }

    public boolean update() {
        return true;
    }

    public String toString() {
        return toXML();
    }

    private Vector sessionrecords;

    public Vector sessionRecords() {
        return sessionrecords;
    }

    public void setSessionRecords(Vector value) {
        sessionrecords = value;
    }

    public void relations() {

        String fetchSpec = PosTransUpload.getBySessionID(posSessionID());
        setSessionRecords(Application.dbConnection().fetch(new PosTransUpload(), fetchSpec));
    }


    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(posSessionID()));
        objs.addElement(new Integer(siteID()));
        objs.addElement(new Integer(posNo()));
        objs.addElement(new Integer(sessionType()));
        objs.addElement(new Integer(status()));
        objs.addElement(startDate());
        objs.addElement(endDate());

        return objs;
    }
}


