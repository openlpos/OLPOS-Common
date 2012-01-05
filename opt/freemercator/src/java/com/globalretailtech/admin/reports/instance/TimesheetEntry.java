package com.globalretailtech.admin.reports.instance;

import java.sql.Timestamp;

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

public class TimesheetEntry {

    Timestamp logon_time;
    Timestamp logoff_time;
    float amount_owed;
    float hourly_wage;
    float work_time;
    String name;

    public TimesheetEntry(String name, float work_time, float amount_owed) {
        this.amount_owed = amount_owed;
        this.work_time = work_time;
        this.name = name;
    }

    public TimesheetEntry(String name, Timestamp logon_time, Timestamp logoff_time, float amount_owed, float hourly_wage) {
        this.name = name;
        this.logon_time = logon_time;
        this.logoff_time = logoff_time;
        this.amount_owed = amount_owed;
        this.hourly_wage = hourly_wage;
    }

    public String getName() {
        return name;
    }

    public Timestamp getLogon_time() {
        return logon_time;
    }

    public void setLogon_time(Timestamp logon_time) {
        this.logon_time = logon_time;
    }

    public Timestamp getLogoff_time() {
        return logoff_time;
    }

    public void setLogoff_time(Timestamp logoff_time) {
        this.logoff_time = logoff_time;
    }

    public float getAmount_owed() {
        return amount_owed;
    }

    public float getHourly_wage() {
        return hourly_wage;
    }

    public void setAmount_owed(float amount_owed) {
        this.amount_owed = amount_owed;
    }

    public float getWork_time() {
        return work_time;
    }
}
