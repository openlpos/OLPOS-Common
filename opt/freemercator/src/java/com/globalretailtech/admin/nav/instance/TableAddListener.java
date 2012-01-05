package com.globalretailtech.admin.nav.instance;

import com.globalretailtech.admin.settings.instance.TableAdd;
import com.globalretailtech.admin.settings.SettingsContainer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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

public class TableAddListener implements ActionListener {
        int id = -1;
        TableFolder folder;

        public TableAddListener(TableFolder folder, int id){
            this.folder = folder;
            this.id = id;
        }

        public TableAddListener(TableFolder folder) {
            this.folder = folder;
        }

        public void actionPerformed(ActionEvent e) {
            TableAdd add = new TableAdd(folder);
            if (id != -1){
                add.duplicate(id);
            }
            SettingsContainer.show(add);
        }
    }