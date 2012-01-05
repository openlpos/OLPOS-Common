package com.globalretailtech.admin.nav.instance;

import com.globalretailtech.admin.settings.Settings;
import com.globalretailtech.admin.settings.instance.ButtonGroupSettings;
import com.globalretailtech.admin.nav.NavNode;


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

public class ButtonGroupNode extends NavNode {
    int config_id;
    String name;

    public ButtonGroupNode(String name, int config_id) {
        super(name);
        this.name = name;
        this.config_id = config_id;
    }

    public Settings createSettings() {
        return new ButtonGroupSettings(config_id);
    }

//    public JPopupMenu getPopupMenu() {
//        JPopupMenu popup = new JPopupMenu();
//        JMenuItem delete = new JMenuItem("Delete");
//        delete.addActionListener(new EmployeeNode.EmployeeDeleteHandler());
//        popup.add(delete);
//        return popup;
//    }

//    class EmployeeDeleteHandler implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//            int option = JOptionPane.showConfirmDialog(ConfigFrame.getConfigFrame(), "Are you sure you want to delete user \"" + getName() + "\"?" );
//            if (option == JOptionPane.OK_OPTION){
//                try {
//                    Application.dbConnection().execute("DELETE FROM employee where employee_id = " + menu_id);
//                    Enumeration enum = EmployeesFolder.folder.children();
//                    while(enum.hasMoreElements()){
//                        EmployeeNode node = (EmployeeNode)enum.nextElement();
//                        if (node.getID() == menu_id){
//                            NavTree.navTree.removeObject(node);
//                        }
//                    }
//                } catch (SQLException e1) {
//                    Log.fatal("Caught this trying to delete a user:" + e1);
//                }
//            }
//        }
//    }

    public String getName() {
        return name;
    }

    public int getID() {
        return config_id;
    }
}
