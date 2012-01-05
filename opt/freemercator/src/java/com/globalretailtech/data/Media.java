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
 * Media tracks payment types.
 *
 * @author  Quentin Olson
 */
public class Media extends DBRecord {

    public static final int CASH = 1;
    public static final int CHECK = 2;
    public static final int VISA = 3;
    public static final int MASTER_CARD = 4;
    public static final int AMEX = 5;
    public static final int DEBIT_CARD = 6;
    public static final int EURO = 7;
    public static final int DEM = 8;
    public static final int DKK = 9;
	public static final int CREDIT = 10;
	public static final int GIFT = 11;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "media";

        columns = new String[7];

        columns[0] = "media_id";
        columns[1] = "config_no";
        columns[2] = "media_type";
        columns[3] = "halo";
        columns[4] = "lalo";
        columns[5] = "media_desc";
        columns[6] = "media_class";

        col_types = new int[7];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.INT;
        col_types[5] = DBRecord.STRING;
        col_types[6] = DBRecord.STRING;
    }

    private int mediaid;
    private int configno;
    private int mediatype;
    private int halo;             // qo, should be double
    private int lalo;              // qo, should be double
    private String mediadesc;
    private String mediaclass;

    public Media() {
    }

    public int mediaID() {
        return mediaid;
    }

    public int configNo() {
        return configno;
    }

    public int mediaType() {
        return mediatype;
    }

    public int halo() {
        return halo;
    }

    public int lalo() {
        return lalo;
    }

    public String mediaDesc() {
        return mediadesc;
    }

    public String mediaClass() {
        return mediaclass;
    }

    public void setMediaID(int value) {
        mediaid = value;
    }

    public void setConfigNo(int value) {
        configno = value;
    }

    public void setMediaType(int value) {
        mediatype = value;
    }

    public void setHalo(int value) {
        halo = value;
    }

    public void setLalo(int value) {
        lalo = value;
    }

    public void setMediaDesc(String value) {
        mediadesc = value;
    }

    public void setMediaClass(String value) {
        mediaclass = value;
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

    public static String getByType(int type) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[2]);
        s.append(" = ");
        s.append(Integer.toString(type));

        return new String(s.toString());
    }

    public static String getAll() {

        StringBuffer s = new StringBuffer("select * from ");
        s.append(table);

        return new String(s.toString());
    }

    public DBRecord copy() {
        Media b = new Media();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setMediaID(rset.getInt("media_id"));
            setConfigNo(rset.getInt("config_no"));
            setMediaType(rset.getInt("media_type"));
            setHalo(rset.getInt("halo"));
            setLalo(rset.getInt("lalo"));
            setMediaDesc(rset.getString("media_desc"));
            setMediaClass(rset.getString("media_class"));
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    public boolean save() {
        return true;
    }

    public boolean update() {
        return true;
    }

    //
    // Relations
    //

    public void relations() {
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(mediaID()));
        objs.addElement(new Integer(configNo()));
        objs.addElement(new Integer(mediaType()));
        objs.addElement(new Integer(halo()));
        objs.addElement(new Integer(lalo()));
        objs.addElement(mediaDesc() == null ? "" : mediaDesc());
        objs.addElement(mediaClass() == null ? "" : mediaClass());

        return objs;
    }
}


