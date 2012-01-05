package com.globalretailtech.admin.settings.handler;

import com.globalretailtech.admin.settings.SettingsHandler;
import com.globalretailtech.admin.settings.Field;
import com.globalretailtech.admin.nav.NavTree;
import com.globalretailtech.admin.nav.instance.TableFolder;
import com.globalretailtech.admin.nav.instance.TableNode;

import java.util.Hashtable;
import java.util.Enumeration;

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

public class TableSettingsHandler  extends SettingsHandler {

    int id;
    TableFolder folder;

    public TableSettingsHandler(TableFolder folder, int id) {
        this.id = id;
        this.folder = folder;
    }

    public void commit(Hashtable fields){
        updateTable(fields, folder.tableName, folder.unique_id_name, new Integer(id));
        Enumeration e = folder.children();
        String newName = ((Field)fields.get(folder.display_col_name.toLowerCase())).getStringValue();
        while(e.hasMoreElements()){
            TableNode node = (TableNode)e.nextElement();
            if (node.getID() == id){
                node.setUserObject(newName);
                NavTree.navTree.updateObjectName(node);
            }
        }
    }

    public void cancel() {
    }
}
