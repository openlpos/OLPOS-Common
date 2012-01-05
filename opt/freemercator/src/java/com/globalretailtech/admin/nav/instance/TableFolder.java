package com.globalretailtech.admin.nav.instance;

import com.globalretailtech.admin.nav.NavNode;
import com.globalretailtech.util.Application;

import java.util.Hashtable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import javax.swing.*;

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

public class TableFolder  extends NavNode {

    public TableFolder folder;

    public String tableName;
    public String unique_id_name;
    public String displayName;
    public String display_col_name;

    protected Hashtable children = new Hashtable();

    static Logger logger = Logger.getLogger(TableNode.class);

    public TableFolder(String tableName, String unique_id_name, String displayName, String display_col_name) {
        super(displayName);

        this.tableName = tableName;
        this.unique_id_name = unique_id_name;
        this.displayName = displayName;
        this.display_col_name = display_col_name;
        folder = this;
        try {
        ResultSet rs = Application.dbConnection().executeWithResult("select * from " + tableName);
        if (rs != null){
            while (rs.next()) {
                int id = rs.getInt(unique_id_name);
                String name = rs.getString(display_col_name);
                this.add(new TableNode(folder, name, id));
            }
        }
        } catch (SQLException e){
            logger.fatal("Caught this trying to get table data:" + e, e);
        }

    }

    public JPopupMenu getPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem add = new JMenuItem("Add " + displayName );
        add.addActionListener(new TableAddListener(folder));
        popup.add(add);
        return popup;
    }


}