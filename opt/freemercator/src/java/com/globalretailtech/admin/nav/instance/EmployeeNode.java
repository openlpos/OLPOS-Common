package com.globalretailtech.admin.nav.instance;

import com.globalretailtech.admin.settings.Settings;
import com.globalretailtech.admin.settings.SettingsContainer;
import com.globalretailtech.admin.settings.instance.EmployeeSettings;
import com.globalretailtech.admin.ConfigFrame;
import com.globalretailtech.admin.reports.instance.EmployeeTimesheet;
import com.globalretailtech.admin.nav.NavNode;
import com.globalretailtech.admin.nav.NavTree;
import com.globalretailtech.util.Application;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Enumeration;

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

public class EmployeeNode extends NavNode {

    static Logger logger = Logger.getLogger(EmployeeNode.class);

    int emp_id;
    String name;

    public EmployeeNode(String name, int emp_id) {
        super(name);
        this.name = name;
        this.emp_id = emp_id;
    }

    public Settings createSettings() {
        return new EmployeeSettings(emp_id);
    }

    public JPopupMenu getPopupMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(new EmployeeDeleteHandler());
        popup.add(delete);

        JMenuItem timesheet = new JMenuItem("Show Timesheet...");
        timesheet.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.show(new EmployeeTimesheet(emp_id, false));
            }
        });
        popup.add(timesheet);

        JMenuItem pay = new JMenuItem("Reset Timesheet (Pay Employee)");
        pay.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(ConfigFrame.getConfigFrame(), "Are you sure you want to change the status to 'paid' on all timesheet entries for user \"" + getName() + "\"?" );
                if (option == JOptionPane.OK_OPTION){
                    try {
                        Application.dbConnection().execute("UPDATE employee_timesheet SET amount_owed = -1 where employee_id = " + emp_id);
                    } catch (SQLException e1) {
                        logger.fatal("Caught this trying to update timesheet:" + e1, e1);
                    }
                }
            }
        });
        popup.add(pay);

        return popup;
    }

    class EmployeeDeleteHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int option = JOptionPane.showConfirmDialog(ConfigFrame.getConfigFrame(), "Are you sure you want to delete user \"" + getName() + "\"?" );
            if (option == JOptionPane.OK_OPTION){
                try {
                    Application.dbConnection().execute("DELETE FROM employee where employee_id = " + emp_id);
                    Enumeration enum = EmployeesFolder.folder.children();
                    while(enum.hasMoreElements()){
                        EmployeeNode node = (EmployeeNode)enum.nextElement();
                        if (node.getID() == emp_id){
                            NavTree.navTree.removeObject(node);
                        }
                    }
                } catch (SQLException e1) {
                    logger.fatal("Caught this trying to delete a user:" + e1, e1);
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return emp_id;
    }
}
