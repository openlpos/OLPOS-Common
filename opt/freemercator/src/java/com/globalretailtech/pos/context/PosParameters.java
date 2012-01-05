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

package com.globalretailtech.pos.context;

import java.util.Hashtable;
import java.util.Vector;


import com.globalretailtech.util.Application;
import com.globalretailtech.data.PosParameter;
import org.apache.log4j.Logger;

/**
 * POS parameters
 *
 *
 *
 * @author  Quentin Olson
 */
public class PosParameters extends Hashtable {

    static Logger logger = Logger.getLogger(PosParameters.class);

    static final String UNDEF = "** undefined **";

    public PosParameters(int configNo) {

        super();

        String fetchSpec = PosParameter.getByConfigNo(configNo);
        Vector parameters = Application.dbConnection().fetch(new PosParameter(), fetchSpec);

        for (int i = 0; i < parameters.size(); i++) {

            PosParameter param = (PosParameter) parameters.elementAt(i);

            switch (param.paramType()) {

                case PosParameter.INT:

                    try {
                        put(param.paramName(), new Integer(param.paramValue()));
                    } catch (java.lang.Exception e) {
                        logger.warn("Bad input parameter conversion, int, " + param.paramName(), e);
                    }
                    break;

                case PosParameter.DOUBLE:

                    try {
                        put(param.paramName(), new Double(param.paramValue()));
                    } catch (java.lang.Exception e) {
                        logger.warn("Bad input parameter conversion, double, " + param.paramName(), e);
                    }
                    break;

                case PosParameter.STRING:

                    try {
                        put(param.paramName(), new String(param.paramValue()));
                    } catch (java.lang.Exception e) {
                        logger.warn("Bad input parameter conversion, string, " + param.paramName(), e);
                    }
                    break;

                case PosParameter.BOOLEAN:

                    try {
                        put(param.paramName(), new Boolean(param.paramValue()));
                    } catch (java.lang.Exception e) {
                        logger.warn("Bad input parameter conversion, bool, " + param.paramName(), e);
                    }
                    break;
            }
        }
    }

    /**
     *
     */
    public int getInt(String key) {

        Object obj = get(key);

        if (obj == null) {
            logger.warn("Parameter not found " + key);
            return 0;
        } else {
            int value = 0;
            try {
                value = ((Integer) obj).intValue();
            } catch (java.lang.ClassCastException e) {
                logger.warn("Invalid cast to int in PosParameters " + key);
                return 0;
            }
            return value;
        }
    }

    /**
     *
     */
    public String getString(String key) {

        Object obj = get(key);

        if (obj == null) {
            logger.warn("Parameter not found " + key);
            return UNDEF;
        } else {
            String value = UNDEF;
            try {
                value = (String) obj;
            } catch (java.lang.ClassCastException e) {
                logger.warn("Invalid cast to string in PosParameters " + key, e);
            }
            return value;
        }
    }

    /**
     *
     */
    public double getDouble(String key) {

        Object obj = get(key);

        if (obj == null) {
            logger.warn("Parameter not found " + key);
            return 0;
        } else {
            double value = 0.0;
            try {
                value = ((Double) obj).doubleValue();
            } catch (java.lang.ClassCastException e) {
                logger.warn("Invalid cast to double in PosParameters " + key, e);
            }
            return value;
        }
    }

    /**
     *
     */
    public boolean getBoolean(String key) {

        Object obj = get(key);

        if (obj == null) {
            logger.warn("Parameter not found " + key);
            return false;
        } else {
            boolean value = false;
            try {
                value = ((Boolean) obj).booleanValue();
            } catch (java.lang.ClassCastException e) {
                logger.warn("Invalid cast to boolean in PosParameters " + key, e);
            }
            return value;
        }
    }
}




