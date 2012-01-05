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
/**
 * POS Trans Upload records individual upload information
 * for POS sessions.
 *
 * @author  Quentin Olson
 */
public class PosTransUpload extends DBRecord {

    public static final int TRANS_UPLOAD = 0;
    public static final int CONFIG_UPLOAD = 1;
    public static final int CONFIG_DOWNOAD = 2;
    public static final int MONITOR = 3;

    private static String table = "pos_trans_upload";
    private static String[] columns = {"upload_session_id",
                                       "pos_session_id",
                                       "trans_start",
                                       "trans_end"};

    private static int[] col_types = {DBRecord.INT,
                                      DBRecord.INT,
                                      DBRecord.INT,
                                      DBRecord.INT};

    private int uploadsessionid;
    private int possessionid;
    private int transstart;
    private int transend;

    public PosTransUpload() {
    }

    public int uploadSessionID() {
        return uploadsessionid;
    }

    public int posSessionID() {
        return possessionid;
    }

    public int transStart() {
        return transstart;
    }

    public int transEnd() {
        return transend;
    }

    public void setUploadSessionID(int value) {
        uploadsessionid = value;
    }

    public void setPosSessionID(int value) {
        possessionid = value;
    }

    public void setTransStart(int value) {
        transstart = value;
    }

    public void setTransEnd(int value) {
        transend = value;
    }

    public static String getBySessionID(int sessionid) {

        StringBuffer s = new StringBuffer("select * from ");
        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(sessionid));

        return new String(s.toString());
    }

    public DBRecord copy() {
        PosTransUpload b = new PosTransUpload();
        return b;
    }

    public Vector parents() {
        return null;
    }


    public void populate(ResultSet rset) {

        try {
            setUploadSessionID(rset.getInt("upload_session_id"));
            setPosSessionID(rset.getInt("pos_session_id"));
            setTransStart(rset.getInt("trans_start"));
            setTransEnd(rset.getInt("trans_end"));

        } catch (SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    public boolean save() {

        StringBuffer tmp = new StringBuffer();

        tmp.append("insert into ");
        tmp.append(table);
        tmp.append(" values (");
        tmp.append(Integer.toString(uploadSessionID()));
        tmp.append(", ");
        tmp.append(Integer.toString(posSessionID()));
        tmp.append(", ");
        tmp.append(Integer.toString(transStart()));
        tmp.append(", '");
        tmp.append(Integer.toString(transEnd()));
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


    public String toString() {
        return toXML();
    }

    public void relations() {
    }  // no relations (yet)

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(uploadSessionID()));
        objs.addElement(new Integer(posSessionID()));
        objs.addElement(new Integer(transStart()));
        objs.addElement(new Integer(transEnd()));

        return objs;
    }


}


