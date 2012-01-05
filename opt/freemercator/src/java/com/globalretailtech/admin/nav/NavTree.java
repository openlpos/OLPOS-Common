package com.globalretailtech.admin.nav;

import com.globalretailtech.admin.settings.SettingsContainer;
import com.globalretailtech.admin.nav.instance.EmployeesFolder;
import com.globalretailtech.admin.nav.instance.TableFolder;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

public class NavTree extends JTree {

    DefaultMutableTreeNode root;
    DefaultTreeModel model;

    public static NavTree navTree;



    public NavTree(DefaultMutableTreeNode root) {
        super(root);
        navTree = this;
        this.setRootVisible(false);

//        this.setMinimumSize(new Dimension(200, 500));

        this.setShowsRootHandles(true);

        model = new DefaultTreeModel(root);
        this.setModel(model);

        NavNode system = new NavNode("System");
        root.add(system);

        system.add(new EmployeesFolder());
//        system.add(new ButtonGroupFolder());
        system.add(new TableFolder("menu_root", "menu_id", "Main Menu", "name"));
        system.add(new TableFolder("pos_config", "config_id", "POS Config", "name"));
        system.add(new TableFolder("sub_menu", "sub_menu_id", "Sub Menu", "sub_menu_name"));
        system.add(new TableFolder("item", "item_id", "Items", "short_desc"));
//        system.add(new TableFolder("plu", "plu", "PLU", "plu"));

//        root.add(new NavNode("Reports"));

        this.setExpandedState(new TreePath(new Object[]{root}), true);



        this.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                int row = getRowForLocation(evt.getX(), evt.getY());
                TreePath path = getPathForRow(row);
//                if (evt.getClickCount() == 2){

                    if (path != null){
                        NavNode node = (NavNode)path.getLastPathComponent();
                        if (node != null){
                            SettingsContainer.show(node.createSettings());
                        }
                    }
//                }

                if (evt.isMetaDown()) {

                    setSelectionRow(row);
                }

                if (evt.isPopupTrigger()){
                    if (path != null){
                        NavNode node = (NavNode)path.getLastPathComponent();
                        if (node != null && node.getPopupMenu() != null){
                            node.getPopupMenu().show(evt.getComponent(), evt.getX(), evt.getY());
                        }
                    }

                }
            }
        });

    }

    public DefaultMutableTreeNode addObject(NavNode parent,
                                            NavNode childNode,
                                            boolean shouldBeVisible) {

        model.insertNodeInto(childNode, parent, parent.getChildCount());

        // Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    public void removeObject(NavNode node){
        model.removeNodeFromParent(node);
    }

    public void updateObjectName(NavNode node){
        model.reload(node);
    }

}
