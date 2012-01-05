package com.globalretailtech.admin.nav.instance;

import com.globalretailtech.util.Application;
import com.globalretailtech.admin.settings.SettingsContainer;
import com.globalretailtech.admin.settings.instance.EmployeeAdd;
import com.globalretailtech.admin.nav.instance.EmployeeNode;
import com.globalretailtech.admin.nav.NavNode;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Hashtable;

import org.apache.log4j.Logger;

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

public class EmployeesFolder extends NavNode {

    public static EmployeesFolder folder;

    public static final String TABLE_NAME = "employee";

    protected Hashtable children = new Hashtable();

    static Logger logger = Logger.getLogger(EmployeesFolder.class);

    public EmployeesFolder() {
        super("Employees");
        folder = this;
        try {
        ResultSet rs = Application.dbConnection().executeWithResult("select * from " + TABLE_NAME);
        if (rs != null){
            while (rs.next()) {
                int emp_id = rs.getInt("employee_id");
                String name = rs.getString("fname") + " " + rs.getString("lname");
                this.add(new EmployeeNode(name, emp_id));
            }
        }
        } catch (SQLException e){
            logger.fatal("Caught this trying to get employees" + e, e);
        }

    }

    public JPopupMenu getPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem add = new JMenuItem("Add Employee");
        add.addActionListener(new EmployeeAddListener());
        popup.add(add);
        return popup;
    }

    class EmployeeAddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SettingsContainer.show(new EmployeeAdd());
        }
    }
}
