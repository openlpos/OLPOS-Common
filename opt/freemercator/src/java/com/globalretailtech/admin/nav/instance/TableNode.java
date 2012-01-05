package com.globalretailtech.admin.nav.instance;

import com.globalretailtech.admin.nav.NavNode;
import com.globalretailtech.admin.nav.NavTree;
import com.globalretailtech.admin.settings.Settings;
import com.globalretailtech.admin.settings.instance.TableSettings;
import com.globalretailtech.admin.ConfigFrame;
import com.globalretailtech.util.Application;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
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

public class TableNode  extends NavNode {

    static Logger logger = Logger.getLogger(TableNode.class);

    int id;
    String name;
    TableFolder folder;

    public TableNode(TableFolder folder, String name, int id) {
        super(name);
        this.name = name;
        this.id = id;
        this.folder = folder;
    }

    public Settings createSettings() {
        return new TableSettings(folder, id);
    }

    public JPopupMenu getPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(new TableDeleteHandler());
        popup.add(delete);

        JMenuItem dup = new JMenuItem("Duplicate");
        dup.addActionListener(new TableAddListener(folder, id));
        popup.add(dup);
        return popup;
    }

    class TableDeleteHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int option = JOptionPane.showConfirmDialog(ConfigFrame.getConfigFrame(), "Are you sure you want to delete \"" + getName() + "\" from " + folder.tableName +"?" );
            if (option == JOptionPane.OK_OPTION){
                try {
                    Application.dbConnection().execute("DELETE FROM " + folder.tableName + " where " + folder.unique_id_name + " = " + id);
                    Enumeration enum = folder.children();
                    while(enum.hasMoreElements()){
                        TableNode node = (TableNode)enum.nextElement();
                        if (node.getID() == id){
                            NavTree.navTree.removeObject(node);
                        }
                    }
                } catch (SQLException e1) {
                    logger.fatal("Caught this trying to delete a table entry:" + e1, e1);
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }
}
