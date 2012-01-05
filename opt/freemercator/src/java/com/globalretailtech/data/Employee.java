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

package com.globalretailtech.data;

import java.sql.*;
import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;


import com.globalretailtech.util.Application;
import org.apache.log4j.Logger;

/**
 * Employee information, adequate to manage an employee logon,
 * the employee profile, employment info, (ssn? must be a US thing?)
 *
 * @author  Quentin Olson
 */
public class Employee extends DBRecord {

    static Logger logger = Logger.getLogger(Employee.class);

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "employee";

        columns = new String[11];

        columns[0] = "employee_id";
        columns[1] = "logon_no";
        columns[2] = "logon_pass";
        columns[3] = "profile";
        columns[4] = "fname";
        columns[5] = "lname";
        columns[6] = "mi";
        columns[7] = "ssn";
        columns[8] = "sal_grade";
		columns[9] = "hourly_wage";
		columns[10] = "employee_key";

        col_types = new int[11];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.STRING;
        col_types[5] = DBRecord.STRING;
        col_types[6] = DBRecord.STRING;
        col_types[7] = DBRecord.STRING;
        col_types[8] = DBRecord.INT;
        col_types[9] = DBRecord.DOUBLE;
		col_types[10] = DBRecord.STRING;
    }

    private int empid;
    private int logonno;
    private int logonpass;
    private int profile;
    private String fname;
    private String lname;
    private String mi;
    private String ssn;
    private int salgrade;
    private double hourlywage;
    private String empkey;

    public Employee() {
    }

    public int employeeID() {
        return empid;
    }

    public int logonNo() {
        return logonno;
    }

    public int logonPass() {
        return logonpass;
    }

    public int profile() {
        return profile;
    }

    public String firstName() {
        return fname;
    }

    public String lastName() {
        return lname;
    }

    public String middleInitial() {
        return mi;
    }

    public String SSN() {
        return ssn;
    }

    public int salaryGrade() {
        return salgrade;
    }

	public double hourlyWage() {
		return hourlywage;
	}

	public String employeeKey() {
		return empkey;
	}

    public void setEmployeeID(int value) {
        empid = value;
    }

    public void setLogonNo(int value) {
        logonno = value;
    }

    public void setLogonPass(int value) {
        logonpass = value;
    }

    public void setProfile(int value) {
        profile = value;
    }

    public void setFirstName(String value) {
        fname = value;
    }

    public void setLastName(String value) {
        lname = value;
    }

    public void setMiddleInitial(String value) {
        mi = value;
    }

    public void setSSN(String value) {
        ssn = value;
    }

    public void setSalaryGrade(int value) {
        salgrade = value;
    }

	public void setHourlyWage(double hourlywage) {
		this.hourlywage = hourlywage;
	}

	public void setEmployeeKey(String key) {
		this.empkey = key;
	}

    public void logon(){
        Timestamp stamp = new Timestamp((new Date()).getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        try {
            Application.dbConnection().execute("INSERT into Employee_Timesheet (employee_id, logon_time, hourly_wage) VALUES (" + employeeID() + ",now(), " + hourlyWage() + ")");
        } catch (SQLException e) {
            logger.fatal("Could not insert logon time for employee:" + employeeID() + " who logged on at " + df.format(stamp), e);
        }
    }

    public void logoff(){
        Timestamp stamp = new Timestamp((new Date()).getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        try {
            ResultSet rs = Application.dbConnection().executeWithResult("SELECT max(employee_time_sheet_id) from Employee_TimeSheet where employee_id = " + employeeID());

            if (rs.next()){
                int timesheet_id = rs.getInt(1);

                Application.dbConnection().execute("UPDATE Employee_Timesheet SET logoff_time = now() WHERE employee_time_sheet_id = " + timesheet_id);

                ResultSet rs2 = Application.dbConnection().executeWithResult("SELECT logon_time, logoff_time, hourly_wage from Employee_Timesheet where employee_time_sheet_id = " + timesheet_id);
                rs2.next();

                float hourlyWage = rs2.getFloat("hourly_wage");

                //NOTE  This is hacked to get the logoff time back from the DB.  Some DB's apparently transpose the time, so as long as it's consistent amountOwed will be OK

                Timestamp logon_time = rs2.getTimestamp("logon_time");
                Timestamp logoff_time = rs2.getTimestamp("logoff_time");

                double hoursWorked = (logoff_time.getTime() - logon_time.getTime()) / (1000.0d * 60.0d * 60.0d);

                double amountOwed = hourlyWage * hoursWorked;

                Application.dbConnection().execute("UPDATE Employee_Timesheet SET amount_owed = " + amountOwed + " WHERE employee_time_sheet_id = " + timesheet_id);

            } else {
                logger.error("Could not find an appropriate logon entry for employee " + employeeID() + " who logged off at " + df.format(stamp));
            }
        } catch (SQLException e) {
            logger.fatal("Could not insert logoff time for employee:" + employeeID() + " who logged off at " + df.format(stamp), e);
        }
    }

    public static String getByID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }

    public static String getByLogonNo(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }

	public static String getByKey(String key) {

		StringBuffer s = new StringBuffer("select * from ");

		s.append(table);
		s.append(" where ");
		s.append(columns[10]);
		s.append(" = '");
		s.append(key);
		s.append("'");

		return new String(s.toString());
	}

    public DBRecord copy() {
        Employee b = new Employee();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setEmployeeID(rset.getInt("employee_id"));
            setLogonNo(rset.getInt("logon_no"));
            setLogonPass(rset.getInt("logon_pass"));
            setProfile(rset.getInt("profile"));
            setFirstName(rset.getString("fname"));
            setLastName(rset.getString("lname"));
            setMiddleInitial(rset.getString("mi"));
            setSSN(rset.getString("ssn"));
            setSalaryGrade(rset.getInt("sal_grade"));
            setHourlyWage((double)rset.getFloat("hourly_wage"));
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    public boolean save() {
        return true;
    }

    public boolean update() {
        return true;
    }

    public String toString() {
        return toXML();
    }

    public void update(int updateIndex [], Vector updateValue) {

        StringBuffer select = new StringBuffer();
        select.append(columns[0]).append(" = ").append(Integer.toString(employeeID()));

        String updateString = getUpdateString(updateIndex, updateValue, table, columns, col_types, select.toString());

        try {

            Application.dbConnection().execute(updateString);
            Application.dbConnection().commit();
        } catch (SQLException e) {
            logger.warn("Update failed " + updateString, e);
        }
    }

    public void add() {


        StringBuffer str = new StringBuffer();

        str.append("insert into ").append(table).append(" values (");
        str.append("null, ");  // trigger on ID
        str.append(Integer.toString(logonNo())).append(", ");
        str.append(Integer.toString(logonPass())).append(", ");
        str.append(Integer.toString(profile())).append(", ");
        str.append(quoteString(firstName())).append(", ");
        str.append(quoteString(lastName())).append(", ");
        str.append(quoteString(middleInitial())).append(", ");
        str.append(quoteString(SSN())).append(", ");
        str.append(Integer.toString(salaryGrade())).append(",");
        str.append(Double.toString(hourlyWage())).append(")");

        try {
            Application.dbConnection().execute(str.toString());
            Application.dbConnection().commit();
        } catch (SQLException e) {
            logger.warn("Update failed " + str.toString(), e);
        }

    }

    public String quoteString(String s) {
        if (s == null) {
            return "null";
        } else {
            return "'" + s + "'";
        }
    }

    //
    // Relations
    //

    private PosProfile posProfile;

    public PosProfile posProfile() {
        return posProfile;
    }

    public void relations() {
        String fetchSpec = PosProfile.getByID(profile());
        Vector v = Application.dbConnection().fetch(new PosProfile(), fetchSpec);
        if (v.size() > 0) {
            posProfile = (PosProfile) v.elementAt(0);
        } else {
            logger.warn("Pos Profile not found for employee " + employeeID());
        }
    }

    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(employeeID()));
        objs.addElement(new Integer(logonNo()));
        objs.addElement(new Integer(logonPass()));
        objs.addElement(new Integer(profile()));
        objs.addElement(new String(firstName() == null ? ":" : firstName()));
        objs.addElement(new String(lastName() == null ? ":" : lastName()));
        objs.addElement(new String(middleInitial() == null ? ":" : middleInitial()));
        objs.addElement(new String(SSN() == null ? ":" : SSN()));
        objs.addElement(new Integer(salaryGrade()));
        objs.addElement(new Double(hourlyWage()));

        return objs;
    }
}