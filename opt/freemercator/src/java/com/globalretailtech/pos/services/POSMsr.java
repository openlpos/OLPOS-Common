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
public class POSMsr extends BaseServiceAdapter implements jpos.services.MSRService14 {

    public static int TRACK1_LEN = 24;
    public static int TRACK2_LEN = 24;
    public static int TRACK3_LEN = 24;

    private static boolean autoDisable;
    private static boolean dataEventEnabled;
    private static boolean decodeData;
    private static int errorReportingType;
    private static boolean parseDecodeData;
    private static int tracksToRead;
    private static int powerNotify;

    private static byte[] track1;
    private static byte[] track2;

    private static POSMsr thisMsr;

    public POSMsr() {

        super("POSMsr");

        thisMsr = this;

        autoDisable = false;
        dataEventEnabled = false;
        decodeData = false;
        errorReportingType = 0;
        parseDecodeData = false;
        tracksToRead = 0;
        powerNotify = 0;

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        devPanel().setLayout(gridbag);
        c.fill = GridBagConstraints.BOTH;

        ShareProperties msr = new ShareProperties(propList().getProperty("CreditCardFile", ""));

        // name, value, x, y, height, width

        int row = 0;
        for (Enumeration e = msr.propertyNames(); e.hasMoreElements();) {

            String tmp = msr.getProperty((String) e.nextElement());

            StringTokenizer st = new StringTokenizer(tmp, ",");

            String cardname = st.nextToken();
            String s1 = st.nextToken();
            String s2 = st.nextToken();

            c.gridx = 0;
            c.gridy = row++;
            c.gridheight = 1;
            c.gridwidth = 1;
            JButton b = new JButton(cardname);
            b.addActionListener(new MsrAction(s1, s2));
            gridbag.setConstraints(b, c);
            devPanel().add(b);
        }
        setVisible(true);
    }

    // MSRService12

    // Capabilities

    public boolean getCapISO() {
        return true;
    }

    public boolean getCapJISOne() {
        return true;
    }

    public boolean getCapJISTwo() {
        return true;
    }

    // Properties
    public String getAccountNumber() {
        return null;
    }

    public boolean getAutoDisable() {
        return true;
    }

    public void setAutoDisable(boolean value) {
        autoDisable = value;
    }

    public int getDataCount() {
        return 0;
    }

    public boolean getDataEventEnabled() {
        return true;
    }

    public void setDataEventEnabled(boolean value) {
        dataEventEnabled = value;
    }

    public boolean getDecodeData() {
        return true;
    }

    public void setDecodeData(boolean value) {
        decodeData = value;
    }

    public int getErrorReportingType() {
        return 0;
    }

    public void setErrorReportingType(int value) {
        errorReportingType = value;
    }

    public String getExpirationDate() {
        return null;
    }

    public String getFirstName() {
        return null;
    }

    public String getMiddleInitial() {
        return null;
    }

    public boolean getParseDecodeData() {
        return true;
    }

    public void setParseDecodeData(boolean value) {
        parseDecodeData = value;
    }

    public String getServiceCode() {
        return null;
    }

    public String getSuffix() {
        return null;
    }

    public String getSurname() {
        return null;
    }

    public String getTitle() {
        return null;
    }

    public byte[] getTrack1Data() {
        return track1;
    }

    public byte[] getTrack1DiscretionaryData() {
        return track1;
    }

    public byte[] getTrack2Data() {
        return track2;
    }

    public byte[] getTrack2DiscretionaryData() {
        return track2;
    }

    public byte[] getTrack3Data() {
        return new byte[TRACK3_LEN];
    }

    public int getTracksToRead() {
        return 1;
    }

    public void setTracksToRead(int value) {
        tracksToRead = value;
    }

    // Methods

    public void clearInput() {
        return;
    }

    // MSRService13

    public int getCapPowerReporting() {
        return 0;
    }

    public int getPowerNotify() {
        return 0;
    }

    public void setPowerNotify(int value) {
        powerNotify = value;
    }

    public int getPowerState() {
        return 0;
    }


    // MSRService14, none

    // action class
    class MsrAction implements ActionListener {

        byte[] t1;
        byte[] t2;

        public MsrAction(String s1, String s2) {
            t1 = s1.getBytes();
            t2 = s2.getBytes();
        }

        public void actionPerformed(ActionEvent e) {


            track1 = t1;
            track2 = t2;
            tracksToRead = 2;
            DataEvent event = new DataEvent(thisMsr, 0);
            fireEvent(event);
        }
    }
}




