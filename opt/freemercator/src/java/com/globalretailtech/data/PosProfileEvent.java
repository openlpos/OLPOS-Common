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
 * POS Profile defines the operations an user can perform.
 * A user is associated with a profile.
 *
 * @author  Quentin Olson
 */
public class PosProfileEvent extends DBRecord implements java.io.Serializable {

    private static String table = "pos_profile_event";
    private static String[] columns = {"profile_event_id",
                                       "profile_id",
                                       "help_id",
                                       "pos_event"};

    private static int[] col_types = {DBRecord.INT,
                                      DBRecord.INT,
                                      DBRecord.INT,
                                      DBRecord.STRING};

    private int profileeventid;
    private int profileid;
    private int helpid;
    private String posevent;

    public PosProfileEvent() {
    }

    public int profileEventID() {
        return profileeventid;
    }

    public int profileID() {
        return profileid;
    }

    public int helpID() {
        return helpid;
    }

    public String posEvent() {
        return posevent;
    }

    public void setProfileEventID(int value) {
        profileeventid = value;
    }

    public void setProfileID(int value) {
        profileid = value;
    }

    public void setHelpID(int value) {
        helpid = value;
    }

    public void setPosEvent(String value) {
        posevent = value;
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

    public static String getByProfileID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }


    public DBRecord copy() {
        PosProfileEvent b = new PosProfileEvent();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setProfileEventID(rset.getInt("profile_event_id"));
            setProfileID(rset.getInt("profile_id"));
            setHelpID(rset.getInt("help_id"));
            setPosEvent(rset.getString("pos_event"));
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

        objs.addElement(new Integer(profileEventID()));
        objs.addElement(new Integer(profileID()));
        objs.addElement(new Integer(helpID()));
        objs.addElement(new String(posEvent()));

        return objs;
    }
}


