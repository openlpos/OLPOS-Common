package com.globalretailtech.temp;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

/*
 * Copyright (C) 2003 Casey Haakenson
 * <casey@haakenson.com>
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

public class DBBrowser {

    public static void main(String[] args) {
        try {
            Class.forName("org.hsqldb.jdbcDriver");

            // connect to the database.   This will load the db files and start the
            // database if it is not alread running.
            // db_file_name_prefix is used to open or create files that hold the state
            // of the db.
            // It can contain directory names relative to the
            // current working directory
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:"
                                               + "site.hsqldb",   // filenames
                                               "sa",                    // username
                                               "");
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("Select * from pos");
            while (rs.next()){
                System.out.println(rs.getObject(0));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
