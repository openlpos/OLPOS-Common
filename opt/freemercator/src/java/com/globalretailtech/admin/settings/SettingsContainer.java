package com.globalretailtech.admin.settings;

import javax.swing.*;
import java.awt.*;

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

public class SettingsContainer extends JPanel {

    protected static JScrollPane scroller;

    protected static JPanel inset;
    protected static CardLayout cardLayout;

    public SettingsContainer() {
        this.setLayout(new GridLayout(1,1));
        this.add(scroller = new JScrollPane());
        inset = new JPanel();

        cardLayout = new CardLayout(0,0);
        inset.setLayout(cardLayout);
        scroller.setViewportView(inset);
    }

    public static void show(Component s){
        if (s != null && inset != null && cardLayout != null && scroller != null){
            inset.removeAll();
            cardLayout.addLayoutComponent(s, s.toString());
            inset.add(s, s.toString());

//            cardLayout.last(inset);
            cardLayout.show(inset, s.toString());
            scroller.revalidate();
            scroller.repaint();
        }
    }

    public static void closeAll(){
        show(new JPanel());
    }
}
