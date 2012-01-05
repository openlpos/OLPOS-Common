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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Simple dialog to simulate an open cash drawer.
 *
 * @author  Quentin Olson
 * @see
 */
public class CloseCash extends JDialog implements ActionListener {

    protected transient JButton closebutton;

    /**
     * Constructor, calls super class consturctor, set modal, then setup ().
     */
    public CloseCash() {
        super();
        setModal(true);
        setup();
    }


    /**
     * Just create a button ands it to the
     * dialog.
     */
    protected void setup() {

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        setSize(200, 60);

        setTitle("Cash Drawer");

        closebutton = new JButton("Please close cash drawer");
        closebutton.addActionListener(this);

        contentPane.add(closebutton);

    }

    /**
     * If button is pressed remove
     * the dialog.
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {

        if (e.getSource() == closebutton) {
            setVisible(false);
        }
    }
}


