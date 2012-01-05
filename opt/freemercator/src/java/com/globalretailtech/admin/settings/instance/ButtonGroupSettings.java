package com.globalretailtech.admin.settings.instance;

import com.globalretailtech.admin.settings.Settings;
import com.globalretailtech.admin.settings.handler.ButtonGroupSettingsHandler;
import com.globalretailtech.util.Application;
import com.globalretailtech.util.PositionLayout;
import com.globalretailtech.util.PositionConstraints;
import com.globalretailtech.data.PosKey;
import com.globalretailtech.data.MenuRoot;
import com.globalretailtech.data.SubMenu;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.*;

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

public class ButtonGroupSettings extends Settings {

    static Logger logger = Logger.getLogger(ButtonGroupSettings.class);

    int config_id;
    PositionLayout pl = new PositionLayout();
    PositionConstraints pc = new PositionConstraints();

    public ButtonGroupSettings(int config_id) {
        super(new ButtonGroupSettingsHandler());
        this.config_id = config_id;

        this.setLayout(pl);
        this.setBackground(Color.magenta);

        this.setSize(800,600);

        ResultSet rs = Application.dbConnection().executeWithResult("SELECT * from menu_root where config_no = " + config_id);
        try {
            while (rs.next()){

                MenuRoot menu = new MenuRoot();
                menu.populate(rs);
                pc.height = menu.menuHeight();
                pc.width = menu.menuWidth();
                pc.x = menu.menuXLoc();
                pc.y = menu.menuYLoc();
                MenuEditor me = new MenuEditor(menu);
                pl.setConstraints(me, pc);
                add(me);

            }
        } catch (SQLException e) {
            logger.fatal("Error editing menus:" + e, e);
        }

    }

    class MenuEditor extends JPanel {
        MenuRoot menu;

        public MenuEditor(MenuRoot menu) {
            this.menu = menu;

            this.setBackground(Color.red);

//            this.setSize(new Dimension(800,600));

            this.setLayout(new GridLayout(1,1));
            this.setBorder(BorderFactory.createLineBorder(Color.black));

            this.setSize(menu.pixWidth(), menu.pixHeight());
//            this.setLocation(menu.menuXLoc(), menu.menuYLoc());

            ResultSet rs = Application.dbConnection().executeWithResult("SELECT * from sub_menu where menu_id = " + menu.menuID() + " ORDER BY sub_menu_id ASC");
            try {
                rs.next();
                SubMenu subMenu = new SubMenu();
                subMenu.populate(rs);
                add(new SubMenuEditor(subMenu, menu));
            } catch (SQLException e) {
                logger.fatal("Error editing menus:" + e, e);
            }
        }


    }

    class SubMenuEditor extends JPanel {
        SubMenu subMenu;
        MenuRoot parentMenu;

        public SubMenuEditor(SubMenu subMenu, MenuRoot parentMenu) {
            this.subMenu = subMenu;
            this.parentMenu = parentMenu;

//            this.setBackground(Color.white);
            this.setLayout(new GridLayout(parentMenu.menuWidth(), parentMenu.menuHeight()));
            this.setBorder(BorderFactory.createLineBorder(Color.green));

//            this.setSize(parentMenu.pixWidth(), parentMenu.pixHeight());

            ResultSet rs = Application.dbConnection().executeWithResult("SELECT * from pos_key where sub_menu_id = " + subMenu.subMenuID());
            try {
                while (rs.next()){
                    PosKey posKey = new PosKey();
                    posKey.populate(rs);
                    add(new KeyEditor(posKey));
                }
            } catch (SQLException e) {
                logger.fatal("Error editing menus:" + e, e);
            }
        }
    }

    class KeyEditor extends JButton {
        PosKey posKey;

        public KeyEditor(PosKey posKey) {
            super(posKey.keyText());
            this.posKey = posKey;
            this.setBackground(Color.blue);

            this.setSize(posKey.keyWidth(), posKey.keyHeight());
//            this.add(new JLabel(posKey.keyText()));
        }
    }
}
