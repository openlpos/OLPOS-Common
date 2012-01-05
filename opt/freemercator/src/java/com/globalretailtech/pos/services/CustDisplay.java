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

import javax.swing.*;



/**
 *
 * @author  Quentin Olson
 * @see
 */
public class CustDisplay extends BaseServiceAdapter implements jpos.services.LineDisplayService14 {

    JTextField[] fields;
    int current_row;
    int current_col;
    int rows;
    int cols;

    public CustDisplay() {

        super("CustDisplay");

        rows = Integer.valueOf(propList().getProperty("Rows", "")).intValue();
        cols = Integer.valueOf(propList().getProperty("Columns", "")).intValue();

        current_row = 0;
        current_col = 0;

        fields = new JTextField[rows];

        for (int i = 0; i < rows; i++) {

            fields[i] = new JTextField(cols);
            add(fields[i]);
        }

        String stext = propList().getProperty("StartupText", "");
        fields[0].setText(stext);
        setVisible(true);
    }

    public void setText(String s) {
        fields[0].setText(s);
    }

    // 1.2
    public int getCapBlink() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public boolean getCapBrightness() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return true;
    }

    public int getCapCharacterSet() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public boolean getCapDescriptors() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return true;
    }

    public boolean getCapHMarquee() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return true;
    }

    public boolean getCapICharWait() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return true;
    }

    public boolean getCapVMarquee() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return true;
    }

    // Properties
    public int getCharacterSet() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public void setCharacterSet(int characterSet) throws JposException {
    }

    public String getCharacterSetList() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return "";
    }

    public int getColumns() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return cols;
    }

    public int getCurrentWindow() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public void setCurrentWindow(int currentWindow) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public int getCursorColumn() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return current_col;
    }

    public void setCursorColumn(int cursorColumn) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        current_col = cursorColumn;  // check bounds!!!
    }

    public int getCursorRow() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return current_row;
    }

    public void setCursorRow(int cursorRow) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        current_row = cursorRow;  // check bounds!!!
    }

    public boolean getCursorUpdate() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return true;
    }

    public void setCursorUpdate(boolean cursorUpdate) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public int getDeviceBrightness() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public void setDeviceBrightness(int deviceBrightness) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public int getDeviceColumns() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public int getDeviceDescriptors() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public int getDeviceRows() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public int getDeviceWindows() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public int getInterCharacterWait() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public void setInterCharacterWait(int interCharacterWait) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public int getMarqueeFormat() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public void setMarqueeFormat(int marqueeFormat) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public int getMarqueeRepeatWait() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public void setMarqueeRepeatWait(int marqueeRepeatWait) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public int getMarqueeType() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public void setMarqueeType(int marqueeType) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public int getMarqueeUnitWait() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public void setMarqueeUnitWait(int marqueeUnitWait) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public int getRows() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return rows;
    }

    // Methods
    public void clearDescriptors() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public void clearText() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        fields[0].setText("");
    }

    public void createWindow(int viewportRow, int viewportColumn,
                             int viewportHeight, int viewportWidth, int windowHeight,
                             int windowWidth) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public void destroyWindow() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public void displayText(String data, int attribute)
            throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        fields[0].setText(data);
    }

    public void displayTextAt(int row, int column, String data,
                              int attribute) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        fields[row].setText(data);
    }

    public void refreshWindow(int window) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public void scrollText(int direction, int units) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public void setDescriptor(int descriptor, int attribute)
            throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    // 1.3 Capabilities
    public int getCapPowerReporting() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    // Properties
    public int getPowerNotify() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }

    public void setPowerNotify(int powerNotify) throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
    }

    public int getPowerState() throws JposException {
        if (getState() == JPOS_S_ERROR) {
            throw (new JposException(JPOS_S_ERROR));
        }
        return 0;
    }
}



