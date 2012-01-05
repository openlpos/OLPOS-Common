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

package com.globalretailtech.pos.operators;

import com.globalretailtech.util.Application;
import org.apache.log4j.Logger;

/**
 * Simple implementation of the pos fields interface.
 *
 * @author  Quentin Olson
 */
public class SimpleFields implements PosFields {

    static Logger logger = Logger.getLogger(SimpleFields.class);

    public SimpleFields() {
    }

    public String toCustNo(String s) {

        if (s == null)
            return "";
        StringBuffer sbuf = new StringBuffer(s);
        if (s.length() < 10) {
            for (int i = 0; i < 10 - s.length(); i++) {
                sbuf.insert(0, "0");
            }
        }
        return sbuf.toString();
    }

    public String toPhoneNo(String s) {

        if (s == null)
            return "";
        StringBuffer sbuf = new StringBuffer(s);

        if (Application.locale().getCountry().equals("EN")) {

            switch (s.length()) {
                case 7:
                    sbuf.insert(2, "-");
                    break;

                case 10:
                    sbuf.insert(0, "(");
                    sbuf.insert(4, ") ");
                    sbuf.insert(9, "-");
                    break;
                default:
                    logger.warn("Invalid phone number length");
            }
        } else {
            logger.warn("Unsupported locale in SimpleFields");
        }
        return sbuf.toString();
    }
}


