package com.globalretailtech.admin.settings;

import javax.swing.*;

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

public class Field {
    JComponent textField;
    String className;
    String name;

    public Field(JComponent textField, String name, String className) {
        this.textField = textField;
        this.className = className;
        this.name = name;
    }

    public String getStringValue(){
        if (textField instanceof JTextField){
            JTextField f = (JTextField)textField;
            if (f.getText() == null || f.getText().equals("")){
                return "null";
            } else {
                return f.getText();
            }
        } else if (textField instanceof JComboBox){
            return "something";
        } else {
            return "null";
        }
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public JComponent getTextField() {
        return textField;
    }
}
