package com.globalretailtech.admin.reports.instance;

import com.globalretailtech.admin.reports.Report;
import com.globalretailtech.admin.settings.SettingsContainer;
import com.globalretailtech.util.Application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.text.DecimalFormat;

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

public class EmployeeTimesheet extends Report {

    static Logger logger = Logger.getLogger(EmployeeTimesheet.class);

    int emp_id;
    String[] filterTypes = new String[]{"Un-paid records", "All Records"};

    public EmployeeTimesheet(int emp_id, boolean showUnPaid) {
        super("Employee Timesheet");

        if (!showUnPaid){
            super.setFilterTypes(filterTypes, 0);
        } else {
            super.setFilterTypes(filterTypes, 1);
        }

        this.emp_id = emp_id;

        String[] columns = new String[]{"Employee Name", "Logon Time", "Logoff Time", "Hourly Wage", "Amount Owed"};

        Vector timesheetEntries = new Vector();

        String paidFilter = "";
        if (!showUnPaid){
            paidFilter = " AND employee_timesheet.amount_owed != -1 ";
        }

        ResultSet rs = Application.dbConnection().executeWithResult("SELECT * from employee_timesheet, employee where employee_timesheet.employee_id = employee.employee_id " + paidFilter + " AND employee_timesheet.employee_id = " + emp_id);
        try {
            while(rs.next()){
                String name = rs.getString("fname") + " " + rs.getString("lname");
                timesheetEntries.add(new TimesheetEntry(name, rs.getTimestamp("logon_time"), rs.getTimestamp("logoff_time"), rs.getFloat("amount_owed"), rs.getFloat("hourly_wage")));
            }
        } catch (SQLException e){
            logger.error("Error getting timesheet entries for employee" + emp_id, e);
        }

        Object[][] rows = new Object[timesheetEntries.size()][columns.length];

        double totalOwed  = 0.0d;
        long msWorked = 0;
        NumberFormat money =NumberFormat.getCurrencyInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd hh:mm");

        for (int i = 0; i < timesheetEntries.size(); i++) {
            TimesheetEntry timesheetEntry = (TimesheetEntry) timesheetEntries.elementAt(i);

            if (timesheetEntry.logoff_time == null || timesheetEntry.logon_time == null){
                logger.error("Something is wrong with a timesheet entry for user " + timesheetEntry.getName());
                continue;
            }

            msWorked += timesheetEntry.logoff_time.getTime() - timesheetEntry.getLogon_time().getTime();

            String owed = money.format(timesheetEntry.amount_owed);

            rows[i][0] = timesheetEntry.getName();
            rows[i][1] = df.format(timesheetEntry.getLogon_time());
            rows[i][2] = df.format(timesheetEntry.getLogoff_time());
            rows[i][3] = money.format(timesheetEntry.getHourly_wage());

            if (timesheetEntry.amount_owed == -1){
                rows[i][4] = money.format(((timesheetEntry.logoff_time.getTime() - timesheetEntry.getLogon_time().getTime()) / (1000.0d * 60.0d * 60.0d)) * timesheetEntry.getHourly_wage()) + " (paid)";
            } else {
                totalOwed+=timesheetEntry.amount_owed;
                rows[i][4] = owed;
            }

        }
        NumberFormat nf = DecimalFormat.getNumberInstance();

        super.setModel(columns, rows);
        super.setTotals(new String[]{"Totals:","","", "", nf.format(msWorked / (1000.0d * 60.0d * 60.0d)) + " hours", money.format(totalOwed)});
    }

    public void filterChanged(int index) {
        SettingsContainer.show(new EmployeeTimesheet(emp_id, true));
    }

}
