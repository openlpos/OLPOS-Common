package com.globalretailtech.admin.settings.handler;

import com.globalretailtech.admin.settings.SettingsHandler;
import com.globalretailtech.admin.settings.Field;
import com.globalretailtech.admin.nav.NavTree;
import com.globalretailtech.admin.nav.instance.TableNode;
import com.globalretailtech.admin.nav.instance.TableFolder;
import org.apache.log4j.Logger;

import java.util.Hashtable;

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

public class TableAddHandler  extends SettingsHandler {
    static Logger logger = Logger.getLogger(TableAddHandler.class);

    TableFolder folder;

    public TableAddHandler(TableFolder folder) {
        this.folder = folder;
    }

    public void commit(Hashtable fields) {
        int newID = addTable(fields, folder.tableName, folder.unique_id_name);
        if (newID != -1){
            String newName = ((Field)fields.get(folder.display_col_name.toLowerCase())).getStringValue();
            TableNode node = new TableNode(folder, newName, newID);
            NavTree.navTree.addObject(folder, node, true);
        } else {
            logger.warn("User ID not returned correctly from addUser");
        }
    }

    public void cancel() {
    }
}
