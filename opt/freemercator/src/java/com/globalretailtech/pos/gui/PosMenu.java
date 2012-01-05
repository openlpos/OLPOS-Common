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
import com.globalretailtech.pos.events.*;

/**
 *
 * @author  Quentin Olson
 * @see
 */
public class PosMenu extends JPanel {

    private JPanel layout_panel;
    private Vector panels;
    private PosContext context;

    public PosContext context() {
        return context;
    }

    public PosMenu(PosContext c) {

        super();
        context = c;

        // main frame setup

        GridBagLayout gridbag = new GridBagLayout();
        layout_panel = new JPanel(gridbag);
        panels = new Vector();

        // create containers

        for (int i = 0; i < context.config().containers().size(); i++) {

            MenuRoot m = (MenuRoot) context.config().containers().elementAt(i);
            MenuContainer panel = new MenuContainer(context, m);
            gridbag.setConstraints(panel, panel.menuLocation());
            layout_panel.add(panel);
            panels.addElement(panel);
        }

        add(layout_panel);
    }

    public void update(PosEvent event) {
    }

    public void update(RegisterOpen event) {

        // address reg_open here to set the card layout
        // to the main menu

        for (int i = 0; i < panels.size(); i++) {
            JPanel p = (JPanel) panels.elementAt(i);
            CardLayout l = (CardLayout) p.getLayout();
            l.first(p);

        }
    }
}







