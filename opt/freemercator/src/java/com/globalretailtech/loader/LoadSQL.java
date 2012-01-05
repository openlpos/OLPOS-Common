package com.globalretailtech.loader;

import com.globalretailtech.util.xmlservices.SQLConverter;
import com.globalretailtech.util.xmlservices.HsqldbSQLConverter;
import com.globalretailtech.util.Application;
import com.globalretailtech.data.DBContext;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

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

public class LoadSQL {


    public static void main(String[] args) {

        SQLConverter[] convs = new SQLConverter[args.length];
        Application.setDebug(true);
        Application.setLogEvents(true);
        Application.setDbConnection(new DBContext());
        if (!Application.dbConnection().init()) {
//            Log.fatal("Database initialization failure");
            System.exit(0);
        }

        for (int i = 0; i < args.length; i++) {
            try {
                convs[i] = new SQLConverter(new FileReader(args[i]));
                try {
                    HsqldbSQLConverter gen = new HsqldbSQLConverter(convs[i]);
                    Application.dbConnection().execute(gen.generateCreationSQL());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        System.out.println("Done");
        try {
            Application.dbConnection().getConn().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
