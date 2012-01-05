package com.globalretailtech.admin.nav.instance;

import com.globalretailtech.util.Application;

import com.globalretailtech.admin.nav.NavNode;

import java.util.Hashtable;
import java.sql.ResultSet;
import java.sql.SQLException;

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

public class ButtonGroupFolder extends NavNode {

    static Logger logger = Logger.getLogger(ButtonGroupFolder.class);

    public static ButtonGroupFolder folder;


    protected Hashtable children = new Hashtable();

    public ButtonGroupFolder() {
        super("Configurations");
        folder = this;
        try {
        ResultSet rs = Application.dbConnection().executeWithResult("select * from pos_config" );
        if (rs != null){
            while (rs.next()) {
                int config_id = rs.getInt("config_no");
                String name = rs.getString("name");
                this.add(new ButtonGroupNode(name, config_id));
            }
        }
        } catch (SQLException e){
            logger.fatal("Caught this trying to get employees" + e, e);
        }

    }

//    public JPopupMenu getPopupMenu() {
//        JPopupMenu popup = new JPopupMenu();
//        JMenuItem add = new JMenuItem("Add ButtonGroup");
//        add.addActionListener(new EmployeesFolder.EmployeeAddListener());
//        popup.add(add);
//        return popup;
//    }

//    class EmployeeAddListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//            SettingsContainer.show(new EmployeeAdd());
//        }
//    }
}
