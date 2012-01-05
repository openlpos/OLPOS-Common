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

package com.globalretailtech.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WarnDialog extends JDialog implements ActionListener {

    protected transient JButton closebutton;
    private Font buttonFont;

    public WarnDialog(String text) {
        super();
        setModal(true);
        setup(text);
        centerDialog();
        setVisible(true);
    }


    protected void setup(String text) {

        ShareProperties p = new ShareProperties(this.getClass().getName());

        if (p.Found()) {
            buttonFont = new Font(p.getProperty("ButtonFont", "Courier"), Font.PLAIN,
                    Integer.valueOf(p.getProperty("ButtonFontSize", "10")).intValue());
        } else {
            buttonFont = new Font("Courier", Font.PLAIN, 9);
        }

        setSize(300, 200);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        setTitle("WARNING");
        closebutton = new JButton(text);
        closebutton.setFont(buttonFont);
        closebutton.addActionListener(this);
        contentPane.add(closebutton);

    }

    public void actionPerformed(java.awt.event.ActionEvent e) {

        if (e.getSource() == closebutton) {
            setVisible(false);
        }
    }

    protected void centerDialog() {
        Dimension screenSize = this.getToolkit().getScreenSize();
        Dimension size = this.getSize();
        screenSize.height = screenSize.height / 2;
        screenSize.width = screenSize.width / 2;
        size.height = size.height / 2;
        size.width = size.width / 2;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        this.setLocation(x, y);
    }
}


