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

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import jpos.events.*;

import com.globalretailtech.util.ShareProperties;


/**
 * Service implementation of a pos keyboard. Creates a
 * virtual keyboard using JFC.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class POSScanner extends BaseServiceAdapter implements jpos.services.ScannerService14 {

    private static boolean autoDisable;
    private static boolean dataEventEnabled;
    private static boolean decodeData;
    private static int dataCount;
    private static int powerNotify;
    private static byte[] scanData;
    private static int scanDataType;

    private static POSScanner thisScanner;

    private static Vector scanDataSet;
    private static JComboBox dataList;

    public POSScanner() {

        super("POSScanner");


        thisScanner = this;

        autoDisable = false;
        dataEventEnabled = false;
        decodeData = false;
        powerNotify = 0;

        devPanel().setLayout(new BorderLayout());

        ShareProperties scanner = new ShareProperties(propList().getProperty("ScannerFile", ""));

        // name, value, x, y, height, width

        Vector data = new Vector();
        int row = 0;
        scanDataSet = new Vector();

        for (Enumeration e = scanner.propertyNames(); e.hasMoreElements();) {

            String tmp = scanner.getProperty((String) e.nextElement());
            StringTokenizer st = new StringTokenizer(tmp, ",");
            String cardname = st.nextToken();
            String s = st.nextToken();
            if (cardname.equals("Random")) {
                scanDataSet.addElement(new String("Random"));

            } else {
                scanDataSet.addElement(s);
            }
            data.addElement(new String(cardname));
        }

        dataList = new JComboBox(data);

        dataList.addActionListener(new ScannerAction());

        devPanel().add(dataList, BorderLayout.CENTER);
        setVisible(true);
    }

    // action classes
    class ScannerAction implements ActionListener {

        byte[] data;

        public ScannerAction() {
        }

        public void actionPerformed(ActionEvent e) {

            String s = (String) scanDataSet.elementAt(dataList.getSelectedIndex());
            if (s.equals("Random")) {
                scanData = getRandom().getBytes();
            } else {
                scanData = s.getBytes();
            }
            DataEvent event = new DataEvent(thisScanner, 0);
            fireEvent(event);
        }
    }

    private String getRandom() {

        int r;
        byte[] data;
        java.util.Random rand;

        r = 100;
        rand = new java.util.Random();

        int x = rand.nextInt(r);
        String s = String.valueOf(x);
        if (x < 10000) {
            for (int i = 0; i < (5 - s.length()); i++) {
                s = "0" + s;
            }
        }
        s = "099999" + s + "1";
        return s;
    }


    // SCANNERService12

    public boolean getAutoDisable() {
        return autoDisable;
    }

    public void setAutoDisable(boolean value) {
        autoDisable = value;
    }

    public int getDataCount() {
        return dataCount;
    }

    public boolean getDataEventEnabled() {
        return dataEventEnabled;
    }

    public void setDataEventEnabled(boolean value) {
        dataEventEnabled = value;
    }

    public boolean getDecodeData() {
        return decodeData;
    }

    public void setDecodeData(boolean value) {
        decodeData = value;
    }

    public byte[] getScanData() {
        return scanData;
    }

    public byte[] getScanDataLabel() {
        return null;
    }

    public int getScanDataType() {
        return scanDataType;
    }

    // Methods

    public void clearInput() {
    }

    // SCANNERService13

    // Capabilities



    public int getCapPowerReporting() {
        return 0;
    }

    // Properties
    public int getPowerNotify() {
        return powerNotify;
    }

    public void setPowerNotify(int value) {
        powerNotify = value;
    }

    public int getPowerState() {
        return 0;
    }

    // SCANNERService14, none

}




