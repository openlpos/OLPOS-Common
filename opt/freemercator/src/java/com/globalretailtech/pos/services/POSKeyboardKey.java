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

package com.globalretailtech.pos.services;

import jpos.events.*;
import java.awt.event.*;
import javax.swing.JButton;

/**
 *
 * @author  Quentin Olson
 * @see
 */
public class POSKeyboardKey extends JButton {

    private String desc;
    private int value;
    private POSKeyboard parent;
    private POSKeyboardKey thisKey;

    public int Value() {
        return value;
    }

    public POSKeyboardKey(POSKeyboard p, String d, int v) {

        super(d);
        parent = p;
        desc = d;
        value = v;
        thisKey = this;

        MouseListener ml = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                DataEvent event = new DataEvent(thisKey, 0);
                try {
                    parent.setDataCount(1);
                    parent.setPOSKeyData(value);
                } catch (jpos.JposException jpe) {
                }

                parent.fireEvent(event);
            }
        };

        addMouseListener(ml);
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return desc;
    }
}


