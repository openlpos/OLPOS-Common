/*
 * Copyright (C) 2001 Global Retail Technology, LLC
 * <http://www.globalretailtech.com>
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

package com.globalretailtech.pos.gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import com.globalretailtech.data.*;
import com.globalretailtech.pos.context.*;

/**
 *
 * @author  Quentin Olson
 * @see
 */
public class MenuContainer extends JPanel implements PosGui {

    private MenuRoot container;
    private CardLayout layout;
    private Vector containers;
    private int numComponents;

    public CardLayout Lauout() {
        return layout;
    }

    public MenuContainer(PosContext context, MenuRoot c) {

        super();
        container = c;
        layout = new CardLayout();
        containers = new Vector();
        Font font = new Font("Helvetica", Font.ITALIC, 9);
        numComponents = 0;

        setBorder(BorderFactory.createEtchedBorder());
        setLayout(layout);

        for (int i = 0; i < container.subMenus().size(); i++) {
            numComponents++;
            SubMenu menu = (SubMenu) container.subMenus().elementAt(i);

//            String name = menu.subMenuName();
			String name = menu.subMenuID()+"";

            MenuPanel p = new MenuPanel(context, menu, this, 
            		(int)(c.pixWidth()*PosFrame.xScale()), 
            		(int)(c.pixHeight()*PosFrame.yScale()));
            add(p, name);
        }

        // List of containers is used to home the layouts

        context.guis().add(this);
        revalidate();
    }

    public JComponent getGui() {
        return this;
    }

    public void init(PosContext context) {
    }

    public void open() {
    }

    public void close() {
    }

    /**
     * Home all of the CardLayouts.
     */
    public void home() {

        if (numComponents > 1) {
            layout.first(this);
        }
        revalidate();
    }

    public void clear() {
    }

    public GridBagConstraints menuLocation() {

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.;
        c.weighty = 0.;
        c.gridx = container.menuXLoc();
        c.gridy = container.menuYLoc();
        c.gridheight = container.menuHeight();
        c.gridwidth = container.menuWidth();
        c.insets = new Insets(0,0,0,0);
        return c;
    }
}


