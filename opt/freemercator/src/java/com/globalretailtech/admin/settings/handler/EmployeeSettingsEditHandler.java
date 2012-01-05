package com.globalretailtech.admin.settings.handler;

import com.globalretailtech.admin.settings.SettingsHandler;
import com.globalretailtech.admin.settings.Field;
import com.globalretailtech.admin.nav.instance.EmployeesFolder;
import com.globalretailtech.admin.nav.instance.EmployeeNode;
import com.globalretailtech.admin.nav.NavTree;


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

public class EmployeeSettingsEditHandler extends SettingsHandler {

    int employee_id;

    public EmployeeSettingsEditHandler(int employee_id) {
        this.employee_id = employee_id;
    }

    public void commit(Hashtable fields){
        updateTable(fields, "employee", "employee_id", new Integer(employee_id));
        Enumeration e = EmployeesFolder.folder.children();
        String newName = ((Field)fields.get("fname")).getStringValue() + ((Field)fields.get("lname")).getStringValue();
        while(e.hasMoreElements()){
            EmployeeNode node = (EmployeeNode)e.nextElement();
            if (node.getID() == employee_id){
                node.setUserObject(newName);
                NavTree.navTree.updateObjectName(node);
            }
        }
    }

    public void cancel() {
    }
}
