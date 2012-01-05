package com.globalretailtech.admin.reports.instance;

import com.globalretailtech.admin.reports.Report;
import com.globalretailtech.admin.settings.SettingsContainer;
import com.globalretailtech.util.Application;

import java.util.Vector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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

public class GlobalTimesheetReport extends Report {

    static Logger logger = Logger.getLogger(GlobalTimesheetReport.class);

    String[] filterTypes = new String[]{"Un-paid records", "All Records"};

    public GlobalTimesheetReport(boolean showUnPaid) {
        super("Timesheet Report For All Employees");

        if (!showUnPaid){
            super.setFilterTypes(filterTypes, 0);
        } else {
            super.setFilterTypes(filterTypes, 1);
        }

        String[] columns = new String[]{"Employee Name", "Hours Worked", "Amount Owed"};

        Vector timesheetEntries = new Vector();

        String paidFilter = "";
        if (!showUnPaid){
            paidFilter = " AND employee_timesheet.amount_owed != -1 ";
        }

        ResultSet empRS = Application.dbConnection().executeWithResult("SELECT * from employee");
        try {
            while (empRS.next()){

                int emp_id = empRS.getInt("employee_id");
                String name = empRS.getString("fname") + " " + empRS.getString("lname");

                ResultSet rs = Application.dbConnection().executeWithResult("SELECT * from employee_timesheet, employee where employee_timesheet.employee_id = employee.employee_id " + paidFilter + " AND employee.employee_id = " + emp_id );

                float workerTime = 0;
                float owed = 0;

                while(rs.next()){
                    if (rs.getTimestamp("logoff_time") == null || rs.getTimestamp("logon_time") == null){
                        logger.error("Something is wrong with a timesheet entry for user " + name);
                        continue;
                    }
                    workerTime += rs.getTimestamp("logoff_time").getTime() - rs.getTimestamp("logon_time").getTime();
                    float owedForThis = rs.getFloat("amount_owed");
                    if (owedForThis != -1){
                        owed += owedForThis;
                    }
                }

                timesheetEntries.add(new TimesheetEntry(name, workerTime, owed));

            }
        } catch (SQLException e){
            logger.error("Error getting timesheet entries." , e);
        }

        Object[][] rows = new Object[timesheetEntries.size()][columns.length];

        double totalOwed  = 0.0d;
        long msWorked = 0;
        NumberFormat money =NumberFormat.getCurrencyInstance();
        NumberFormat nf = DecimalFormat.getNumberInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd hh:mm");

        for (int i = 0; i < timesheetEntries.size(); i++) {
            TimesheetEntry timesheetEntry = (TimesheetEntry) timesheetEntries.elementAt(i);

            msWorked += timesheetEntry.work_time;

            String owed = money.format(timesheetEntry.amount_owed);

            rows[i][0] = timesheetEntry.getName();
            rows[i][1] = nf.format(timesheetEntry.getWork_time()/ (1000.0d * 60.0d * 60.0d)) + " hours";
            totalOwed+=timesheetEntry.amount_owed;
            rows[i][2] = owed;
        }


        super.setModel(columns, rows);
        super.setTotals(new String[]{"Totals:","","", "", nf.format(msWorked / (1000.0d * 60.0d * 60.0d)) + " hours", money.format(totalOwed)});
    }

    public void filterChanged(int index) {
        if (index == 0){
            SettingsContainer.show(new GlobalTimesheetReport(false));
        } else {
            SettingsContainer.show(new GlobalTimesheetReport(true));
        }
    }
}
