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
public class PosProfile extends DBRecord implements java.io.Serializable {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "pos_profile";

        columns = new String[4];

        columns[0] = "profile_id";
        columns[1] = "config_no";
        columns[2] = "help_id";
        columns[3] = "profile_name";

        col_types = new int[4];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.STRING;
    }

    private int profileid;
    private int configno;
    private int helpid;
    private String profilename;

    private Vector events;

    public Vector events() {
        return events;
    }

    public PosProfile() {
    }

    public int posProfileID() {
        return profileid;
    }

    public int configNo() {
        return configno;
    }

    public int helpID() {
        return helpid;
    }

    public String profileName() {
        return profilename;
    }

    public void setPosProfileID(int value) {
        profileid = value;
    }

    public void setConfigNo(int value) {
        configno = value;
    }

    public void setHelpID(int value) {
        helpid = value;
    }

    public void setProfileName(String value) {
        profilename = value;
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

    public static String getByIDAndConfigigNo(int id, int cno) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(Integer.toString(id));
        s.append(" and ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(cno));

        return new String(s.toString());
    }

    public static String getByConfigNo(int cno) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(cno));

        return new String(s.toString());
    }

    public DBRecord copy() {
        PosProfile b = new PosProfile();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setPosProfileID(rset.getInt("profile_id"));
            setConfigNo(rset.getInt("config_no"));
            setHelpID(rset.getInt("help_id"));
            setProfileName(rset.getString("profile_name"));
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
        }

        // set up the provile values

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

        // Get POS Events

        String fetchSpec = PosProfileEvent.getByProfileID(posProfileID());
        events = Application.dbConnection().fetch(new PosProfileEvent(), fetchSpec);
    }

    private Help help;

    public Help getHelp() {

        if (help == null) {

            String fetchSpec = Help.getByID(helpID());
            Vector v = Application.dbConnection().fetch(new Help(), fetchSpec);

            if (v.size() > 0) {
                help = (Help) v.elementAt(0);
            }
        } else {
            help = new Help();
            help.setDisplayName("-- unknown --");
            help.setDisplayText("-- unknown --");
        }
        return help;
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(posProfileID()));
        objs.addElement(new Integer(configNo()));
        objs.addElement(new Integer(helpID()));
        objs.addElement(new String(profileName() == null ? "" : profileName()));

        return objs;
    }
}


