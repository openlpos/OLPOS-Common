package com.globalretailtech.admin.settings.instance;

import com.globalretailtech.admin.settings.XMLSettings;
import com.globalretailtech.admin.settings.handler.TableSettingsHandler;
import com.globalretailtech.admin.nav.instance.TableFolder;
import com.globalretailtech.util.Application;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

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

public class TableSettings extends XMLSettings {

    static Logger logger = Logger.getLogger(TableSettings.class);

    int id;
    TableFolder folder;

    public TableSettings(TableFolder folder, int id) {
        super(new TableSettingsHandler(folder, id));
        this.folder = folder;
        ignoreField(folder.unique_id_name);
        this.id = id;

        ResultSet rs = Application.dbConnection().executeWithResult("SELECT * from " + folder.tableName + " where " + folder.unique_id_name + " = " + id);
        try {
            rs.next();
            setResultSet(rs);
        } catch (SQLException se){
            logger.warn("Caught this trying to create Settings:" + se, se);
        }
    }
}
