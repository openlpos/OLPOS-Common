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

import jpos.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jpos.events.*;

import com.globalretailtech.util.ShareProperties;


/**
 * Service implementation of a pos cash drawer. Creates a
 * virtual cash drawer using JFC.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class POSCashDrawer extends BaseServiceAdapter implements jpos.services.CashDrawerService14 {

    private static boolean drawerOpen = false;
    private static POSCashDrawer thisCashDrawer;
    private JRadioButton indicator;

    public POSCashDrawer() {

        super("POSCashDrawer");

        thisCashDrawer = this;

        drawerOpen = false;

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        devPanel().setLayout(gridbag);
        c.fill = GridBagConstraints.BOTH;

        ShareProperties msr = new ShareProperties(propList().getProperty("CreditCardFile", ""));

        // name, value, x, y, height, width

        int row = 0;
        c.gridx = 0;
        c.gridy = row;
        c.gridheight = 1;
        c.gridwidth = 1;
        indicator = new JRadioButton();
        indicator.setSelected(drawerOpen);
        gridbag.setConstraints(indicator, c);
        devPanel().add(indicator);

        c.gridx = 1;
        c.gridy = row;
        c.gridheight = 1;
        c.gridwidth = 1;
        JButton b = new JButton("Close");
        b.addActionListener(new CashDrawerAction());
        gridbag.setConstraints(b, c);
        devPanel().add(b);

        setVisible(true);
    }

    // CashDrawerSevice12

    // Capabilities
    public boolean getCapStatus() throws JposException {
        return true;
    }

    // Properties
    public boolean getDrawerOpened() throws JposException {
        return drawerOpen;
    }

    // Methods
    public void openDrawer() throws JposException {
        drawerOpen = true;
        indicator.setSelected(drawerOpen);
    }

    public void waitForDrawerClose(int beepTimeout, int beepFrequency,
                                   int beepDuration, int beepDelay) throws JposException {
    }

    // CashDrawerSevice13



    public int getCapPowerReporting() throws JposException {
        return 0;
    }

    // Properties
    public int getPowerNotify() throws JposException {
        return 0;
    }

    public void setPowerNotify(int powerNotify) throws JposException {
        return;
    }

    public int getPowerState() throws JposException {
        return 0;
    }

    // CashDrawerService14, none

    // action class

    class CashDrawerAction implements ActionListener {

        public CashDrawerAction() {
        }

        public void actionPerformed(ActionEvent e) {

            if (indicator.isSelected()) {
                DirectIOEvent event = new DirectIOEvent(thisCashDrawer, 0, 0, null);
                fireEvent(event);
                indicator.setSelected(false);
            }
        }
    }
}




