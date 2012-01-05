package com.globalretailtech.admin.settings.instance;

import com.globalretailtech.admin.settings.XMLSettings;

import com.globalretailtech.admin.settings.handler.EmployeeAddHandler;

import com.globalretailtech.util.Application;


import java.sql.ResultSet;

import java.sql.SQLException;


import org.apache.log4j.Logger;

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

public class EmployeeAdd extends XMLSettings {

    static Logger logger = Logger.getLogger(EmployeeAdd.class);

    public EmployeeAdd() {
        super(new EmployeeAddHandler());
        ignoreField("EMPLOYEE_ID");
        allValuesEmpty(true);
        //don't care about records, just the result set
        //TODO: this will fail if we don't have any employees,
        ResultSet rs = Application.dbConnection().executeWithResult("SELECT * from employee");
        try {
            rs.next();
            setResultSet(rs);
        } catch (SQLException se) {
            if (se.getMessage().equalsIgnoreCase("no data is available")) {
                logger.warn("Empty database, attempting to add first employee");
                try {
                    handleEmptyTable(rs);
                } catch (SQLException e) {
                    logger.warn("Exception: " + e + "when attempting to add employee to empty table");
                }
            } else {
                logger.warn("Caught this trying to create Employee Settings:" + se, se);
            }
        }
    }
}

