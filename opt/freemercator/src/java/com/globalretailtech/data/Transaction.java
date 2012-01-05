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
import java.util.Date;
import java.util.Vector;
import java.text.SimpleDateFormat;

import com.globalretailtech.util.Application;

import com.globalretailtech.util.Format;
import com.globalretailtech.util.sql.*;
import org.apache.log4j.Logger;

/**
 * Contains the transaction header. All Trans...
 * type entities are tied to this record by the
 * trans_id. Trans_no is the user visible transaction
 * number.
 *
 * @author  Quentin Olson
 */
public class Transaction extends BusinessObject {

    static Logger logger = Logger.getLogger(Transaction.class);

    // bu type

    public static final int TYPE = 4;

    // types

    public static final int SALES = 0;
    public static final int BANK = 1;
    public static final int LOGON = 2;
    public static final int LOGOFF = 3;
    public static final int OPEN = 3;
    public static final int CLOSE = 3;
    public static final int RETURN = 4;
    public static final int BALANCE = 5;
	public static final int COUNT = 6;
	public static final int ZREPORT = 7;

    // states

    public static final int INCOMPLETE = 0;
    public static final int IN_PROGRESS = 1;
    public static final int COMPLETE = 2;
    public static final int SUSPEND = 3;
    public static final int VOIDED = 4;
	public static final int PRINTING = 5;
	public static final int FAILED = 6;


    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "trans";

        columns = new String[21];

        columns[0] = "trans_id";
        columns[1] = "trans_no";
        columns[2] = "site_id";
        columns[3] = "pos_no";
        columns[4] = "drawer_no";
        columns[5] = "config_no";
        columns[6] = "trans_type";
        columns[7] = "state";
        columns[8] = "reason_code";
        columns[9] = "customer_no";
        columns[10] = "start_time";
        columns[11] = "complete_time";
        columns[12] = "emp_no";
        columns[13] = "carry_out";
        columns[14] = "training_mode";
        columns[15] = "locale_language";
        columns[16] = "locale_variant";
        columns[17] = "archive_date";
        columns[18] = "upload_date";
		columns[19] = "replication_date";
		columns[20] = "z";

        col_types = new int[21];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.INT;
        col_types[5] = DBRecord.INT;
        col_types[6] = DBRecord.INT;
        col_types[7] = DBRecord.INT;
        col_types[8] = DBRecord.INT;
        col_types[9] = DBRecord.STRING;
        col_types[10] = DBRecord.DATE;
        col_types[11] = DBRecord.DATE;
        col_types[12] = DBRecord.INT;
        col_types[13] = DBRecord.BOOLEAN;
        col_types[14] = DBRecord.BOOLEAN;
        col_types[15] = DBRecord.STRING;
        col_types[16] = DBRecord.STRING;
        col_types[17] = DBRecord.DATE;
        col_types[18] = DBRecord.DATE;
        col_types[19] = DBRecord.DATE;
		col_types[20] = DBRecord.INT;
    }

    private int transid;
    private int transno;
    private int siteid;
    private int posno;
    private int drawerno;
    private int configno;
    private int transtype;
    private int state;
    private int reasoncode;
    private String customerno;
    private Date starttime;
    private Date completetime;
    private int empno;
    private boolean carryout;
    private boolean trainingmode;
    private String localelanguage;
    private String localevariant;
    private Date arcdate;
    private Date uploaddate;
    private Date repdate;
    private int z;

    public int transID() {
        return transid;
    }

    public int transNo() {
        return transno;
    }

    public int siteID() {
        return siteid;
    }  // this should be site no ???

    public int posNo() {
        return posno;
    }

    public int drawerNo() {
        return drawerno;
    }

    public int configNo() {
        return configno;
    }

    public int transType() {
        return transtype;
    }

    public int state() {
        return state;
    }

    public int reasonCode() {
        return reasoncode;
    }

    public String customerNo() {
        return customerno;
    }

    public Date startTime() {
        return starttime;
    }

    public Date completeTime() {
        return completetime;
    }

    public int empNo() {
        return empno;
    }

    public boolean carryOut() {
        return carryout;
    }

    public boolean trainingMode() {
        return trainingmode;
    }

    public String localeLanguage() {
        return localelanguage;
    }

    public String localeVariant() {
        return localevariant;
    }

    public Date archiveDate() {
        return arcdate;
    }

    public Date uploadDate() {
        return uploaddate;
    }

    public Date replicationDate() {
        return repdate;
    }
    
    public int z(){
    	return z;
    }

    public void setTransID(int value) {
        transid = value;
    }

    public void setTransNo(int value) {
        transno = value;
    }

    public void setSiteID(int value) {
        siteid = value;
    }

    public void setPosNo(int value) {
        posno = value;
    }

    public void setDrawerNo(int value) {
        drawerno = value;
    }

    public void setConfigNo(int value) {
        configno = value;
    }

    public void setTransType(int value) {
        transtype = value;
    }

    public void setState(int value) {
        state = value;
    }

    public void setReasonCode(int value) {
        reasoncode = value;
    }

    public void setCustomerNo(String value) {
        customerno = value;
    }

    public void setStartTime(Date value) {
        starttime = value;
    }

    public void setCompleteTime(Date value) {
        completetime = value;
    }

    public void setEmpNo(int value) {
        empno = value;
    }

    public void setCarryOut(boolean value) {
        carryout = value;
    }

    public void setTrainingMode(boolean value) {
        trainingmode = value;
    }

    public void setLocaleLanguage(String value) {
        localelanguage = value;
    }

    public void setLocaleVariant(String value) {
        localevariant = value;
    }

    public void setArchive(Date value) {
        arcdate = value;
    }

    public void setUploadDate(Date value) {
        uploaddate = value;
    }

    public void setReplicationDate(Date value) {
        repdate = value;
    }
    
    public void setZ (int z){
    	this.z = z;
    }

    public Transaction() {
    }

    public Transaction(int site, int pos) {

        // get the last trans from the database for the new trans number

        String fetchSpec = getByPosNo(pos);
        Vector v = Application.dbConnection().fetch(new Transaction(), fetchSpec);

        if (v.size() > 0) {

            Transaction t = (Transaction) v.elementAt(v.size() - 1);

            setTransID(t.transID() + 1);
            setTransNo(t.transNo() + 1);
            setSiteID(pos);
            setPosNo(pos);
            setDrawerNo(0);
            setConfigNo(0);
            setTransType(SALES);
            setTransType(0);
            setState(INCOMPLETE);
            setReasonCode(0);
            setStartTime(new Date());
            setCompleteTime(new Date());
            setEmpNo(0);
            setCarryOut(false);
            setTrainingMode(false);
            setLocaleLanguage(Application.localeLanguage());
            setLocaleVariant(Application.localeVariant());
            setArchive(null);
            setUploadDate(null);
            setReplicationDate(null);
        } else {
            setTransID(1);
            setTransNo(1);
            setSiteID(0);
            setPosNo(0);
            setDrawerNo(0);
            setConfigNo(0);
            setTransType(SALES);
            setTransType(0);
            setState(INCOMPLETE);
            setReasonCode(0);
            setStartTime(new Date());
            setCompleteTime(new Date());
            setEmpNo(0);
            setCarryOut(false);
            setTrainingMode(false);
            setLocaleLanguage(Application.localeLanguage());
            setLocaleVariant(Application.localeVariant());
            setArchive(null);
            setUploadDate(null);
            setReplicationDate(null);
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


    public static String getByPosNo(int pos) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[3]);
        s.append(" = ");
        s.append(Integer.toString(pos));

        return new String(s.toString());
    }

    public static String getLogonsByPosNoAndDrawerNo(int pos, int drawer) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[3]);
        s.append(" = ");
        s.append(Integer.toString(pos));
        s.append(" and ");
        s.append(columns[4]);
        s.append(" = ");
        s.append(Integer.toString(drawer));
        s.append(" and (");
        s.append(columns[6]);
        s.append(" = ");
        s.append(Integer.toString(LOGON));
        s.append(" or ");
        s.append(columns[6]);
        s.append(" = ");
        s.append(Integer.toString(LOGOFF));
        s.append(") order by start_time");

        return new String(s.toString());
    }

	public static String getByPosAndTrans(int pos, int trans) {

		StringBuffer s = new StringBuffer("select * from ");

		s.append(table);
		s.append(" where ");
		s.append(columns[3]);
		s.append(" = ");
		s.append(Integer.toString(pos));
		s.append(" and ");
		s.append(columns[1]);
		s.append(" = ");
		s.append(Integer.toString(trans));

		return new String(s.toString());
	}

	public static String getByPosAndState(int pos, int state) {

		StringBuffer s = new StringBuffer("select * from ");

		s.append(table);
		s.append(" where ");
		s.append(columns[3]);
		s.append(" = ");
		s.append(Integer.toString(pos));
		s.append(" and ");
		s.append(columns[7]);
		s.append(" = ");
		s.append(Integer.toString(state));

		return new String(s.toString());
	}

    public static String getMaxByPosNo(int pos) {

        StringBuffer s = new StringBuffer("select * from ").append(table);
        s.append(" where ").append(columns[0]).append(" = (select max (trans_id) from ").append(table).append(")");
        return s.toString();
    }

    /**
     *
     */
    public static String getBySiteAndPosNo(int siteid, int posno) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[2]);
        s.append(" = ");
        s.append(Integer.toString(siteid));
        s.append(" and ");
        s.append(columns[3]);
        s.append(" = ");
        s.append(Integer.toString(posno));

        return new String(s.toString());
    }

    public static String getByCustomerNo(String customer) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[7]);
        s.append(" = '");
        s.append(customer);
        s.append("'");

        return new String(s.toString());
    }
    // parent relations

    public Vector parents() {

        String fetchSpec = BoMap.getByIdAndType(transID(), TYPE);
        Vector v = Application.dbConnection().fetch(new BoMap(), fetchSpec);
        Vector bus = null;

        for (int i = 0; i < v.size(); i++) {
            BoMap bo = (BoMap) v.elementAt(i);
            String fs2 = Pos.getByID(bo.parentBoID());
            bus = Application.dbConnection().fetch(new Pos(), fs2);
        }
        return bus;
    }

    public DBRecord copy() {
        Transaction b = new Transaction();
        return b;
    }


    public Object clone() {

        Transaction b = new Transaction();

        b.setTransID(transID());
        b.setTransNo(transNo());
        b.setSiteID(siteID());
        b.setPosNo(posNo());
        b.setDrawerNo(drawerNo());
        b.setConfigNo(configNo());
        b.setTransType(transType());
        b.setState(state());
        b.setReasonCode(reasonCode());
        b.setCustomerNo(customerNo());
        b.setStartTime(startTime());
        b.setLocaleLanguage(localeLanguage());
        b.setLocaleVariant(localeVariant());
        b.setCompleteTime(completeTime());
        b.setEmpNo(empNo());
        b.setCarryOut(carryOut());
        b.setTrainingMode(trainingMode());
        b.setZ(z());

        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setTransID(rset.getInt("trans_id"));
            setTransNo(rset.getInt("trans_no"));
            setSiteID(rset.getInt("site_id"));
            setPosNo(rset.getInt("pos_no"));
            setDrawerNo(rset.getInt("drawer_no"));
            setConfigNo(rset.getInt("config_no"));
            setTransType(rset.getInt("trans_type"));
            setState(rset.getInt("state"));
            setReasonCode(rset.getInt("reason_code"));
            setCustomerNo(rset.getString("customer_no"));
            setStartTime(rset.getDate("start_time"));
            setCompleteTime(rset.getDate("complete_time"));
            setEmpNo(rset.getInt("emp_no"));
            setCarryOut(rset.getInt("carry_out") > 0);
            setTrainingMode(rset.getInt("training_mode") > 0);
            setLocaleLanguage(rset.getString("locale_language"));
            setLocaleVariant(rset.getString("locale_variant"));
            setArchive(rset.getDate("archive_date"));
            setUploadDate(rset.getDate("upload_date"));
            setReplicationDate(rset.getDate("replication_date"));
            setZ (rset.getInt("z"));
        } catch (SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    public boolean save() {

        StringBuffer tmp = new StringBuffer();

        tmp.append("insert into ");
        tmp.append(table);
        tmp.append(" values (");
        tmp.append(Integer.toString(transID()));
        tmp.append(", ");
        tmp.append(Integer.toString(transNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(siteID()));
        tmp.append(", ");
        tmp.append(Integer.toString(posNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(drawerNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(configNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(transType()));
        tmp.append(", ");
        tmp.append(Integer.toString(state()));
        tmp.append(", ");
        tmp.append(Integer.toString(reasonCode()));
        tmp.append(", '");
        tmp.append(customerNo());
        tmp.append("', '");
        tmp.append(Format.toDbDateString(startTime()));
        tmp.append("', '");
        tmp.append(Format.toDbDateString(completeTime()));
        tmp.append("', ");
        tmp.append(Integer.toString(empNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(carryOut() ? 1 : 0));
        tmp.append(", ");
        tmp.append(Integer.toString(trainingMode() ? 1 : 0));
        tmp.append(", '");
        tmp.append(localeLanguage());
        tmp.append("', '");
        tmp.append(localeVariant());
        tmp.append("', '");
        tmp.append(Format.toDbDateString(archiveDate()));
        tmp.append("', '");
        tmp.append(Format.toDbDateString(uploadDate()));
        tmp.append("', '");
        tmp.append(Format.toDbDateString(replicationDate()));
		tmp.append("', ");
		tmp.append(Integer.toString(z()));
        tmp.append(")");

        try {
            Application.dbConnection().execute(tmp.toString());
        } catch (SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;

    }

    public boolean save(DBContext db) {

        StringBuffer tmp = new StringBuffer();

        tmp.append("insert into ");
        tmp.append(table);
        tmp.append(" values (");
        tmp.append(Integer.toString(transID()));
        tmp.append(", ");
        tmp.append(Integer.toString(transNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(siteID()));
        tmp.append(", ");
        tmp.append(Integer.toString(posNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(drawerNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(configNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(transType()));
        tmp.append(", ");
        tmp.append(Integer.toString(state()));
        tmp.append(", ");
        tmp.append(Integer.toString(reasonCode()));
        tmp.append(", '");
        tmp.append(customerNo());
        tmp.append("', '");
        tmp.append(Format.toDbDateString(startTime()));
        tmp.append("', '");
        tmp.append(Format.toDbDateString(completeTime()));
        tmp.append("', ");
        tmp.append(Integer.toString(empNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(carryOut() ? 1 : 0));
        tmp.append(", ");
        tmp.append(Integer.toString(trainingMode() ? 1 : 0));
        tmp.append(", '");
        tmp.append(localeLanguage());
        tmp.append("', '");
        tmp.append(localeVariant());
        tmp.append("', '");
        tmp.append(Format.toDbDateString(archiveDate()));
        tmp.append("', '");
        tmp.append(Format.toDbDateString(uploadDate()));
		tmp.append("', '");
		tmp.append(Format.toDbDateString(replicationDate()));
		tmp.append("', ");
		tmp.append(Integer.toString(z()));
        tmp.append(")");

        try {
            db.execute(tmp.toString());
        } catch (SQLException e) {
            db.setException(e);
            return false;
        }
        return true;
    }

    public int startTrans(int parentID) {
        int newtransid = -1;
//        ResultSet rs = null;
        try {
            newtransid = startTrans(
                                new Integer(transNo()),
                                new Integer(siteID()),
                                new Integer(posNo()),
                                new Integer(drawerNo()),
                                new Integer(configNo()),
                                new Integer(transType()),
                                new Integer(state()),
                                new Integer(reasonCode()),
                                customerNo(),
                                new Integer(empNo()),
                                localeLanguage(),
                                localeVariant(),
                                new Integer(Transaction.TYPE),
                                new Integer(parentID),
                                new Integer(Pos.TYPE));
        } catch (SQLException e) {
            logger.error("Exception trying to start transaction", e);
            return newtransid;
        }

//        if (rs != null) {
//            try {
//                setTransID(newtransid = rs.getInt(1));
//                setTransNo(transID());
//                Application.dbConnection().commit();
//            } catch (SQLException je) {
//            }
//
//        }
        return newtransid;
    }
    /*
    cur_time := current_timestamp;
  insert into trans (site_id, pos_no, drawer_no, config_no,
                     trans_type, state, reason_code, customer_no, start_time,
                     emp_no, locale_language, locale_variant)
             values ($2, $3, $4, $5,
                     $6, $7, $8, $9, cur_time,
                     $10, $11, $12);
  id := cast((select trans_id from trans where start_time = cur_time) as integer);
  update trans set trans_no = id + 1 where trans_id = id;
  insert into bo_map values (id, $13, $14, $15);
  RETURN id;*/

    private int startTrans(Integer transNo, Integer siteID, Integer posNo, Integer drawerNo, Integer configNo, Integer transType, Integer state, Integer reasonCode, String customerNo, Integer empNo, String language, String variant, Integer type, Integer parentID, Integer posType) throws SQLException {
        int newtransid = -1;
        Timestamp stamp = new Timestamp((new Date()).getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");//.fffffffff
        DBContext conn = Application.dbConnection();

		// check and reuse last transaction header if it's state is incomplete (1)
		// or in_progress (0)
		ResultSet rs = conn.executeWithResult("SELECT max(trans_id) from trans");
		if ( rs.next() ){
			int transId = rs.getInt(1);
			String fetchSpec = Transaction.getByID(transId);
			Vector v = Application.dbConnection().fetch(new Transaction(), fetchSpec);
			if ((v != null) && (v.size() > 0)) {
				Transaction t = (Transaction)v.elementAt(0);
				if ( t.state() == Transaction.IN_PROGRESS 
				|| t.state() == Transaction.INCOMPLETE ) 
					return t.transID();		
			}
		}

        conn.execute("insert into trans (site_id, pos_no, drawer_no, config_no," +
                     "trans_type, state, reason_code, customer_no, start_time,"+
                     "emp_no, locale_language, locale_variant) values (" +
                     siteID + ", " + posNo + ", " + drawerNo + ", " + configNo
                     + ", " + transType + ", " + state + ", " + reasonCode + ", '" + customerNo  + "', now(), " + empNo + ", '" + language + "', '" + variant + "')");

        rs = conn.executeWithResult("SELECT max(trans_id) from trans");
        rs.next();

        int myID = rs.getInt(1);

        conn.execute("insert into bo_map (bo_id, obj_type, parent_bo_id, pobj_type) values " +
                                  "(" + myID + ","+ type + ","+ parentID + ","+ posType + ")"
                );

        return myID;
    }


    public boolean updateState(int state) {
        try {
//            Application.dbConnection().execute(
//                    new SQLProcedureCall("update_state",
//                            new Object[]{
//                                new Integer(transID()),
//                                new Integer(transNo()),
//                                new Integer(state)}));
//            Timestamp stamp = new Timestamp((new Date()).getTime());
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");//.fffffffff

            DBContext conn = Application.dbConnection();
            conn.execute("update trans set " +            	" state = " + state + ", " +            	" trans_no = " + transNo() + "," +            	" complete_time = now()," +            	" z="+z()+" " +            	" where trans_id = " + transID() + "");

            Application.dbConnection().commit();
            
            setState (state);
            
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;
    }


    public boolean updateStateAndType(int state, int type) {

        StringBuffer tmp = new StringBuffer();

        tmp.append("update ").append(table);
        tmp.append(" set ").append("state").append(" = ").append(Integer.toString(state));
        tmp.append(", ").append("trans_type").append(" = ").append(Integer.toString(type));
        tmp.append(" where ").append("trans_id").append(" = ").append(Integer.toString(transID()));

        try {
            Application.dbConnection().execute(tmp.toString());
            Application.dbConnection().commit();
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;
    }

    public boolean updateStateAndDrawer(int state, int drawer) {

        StringBuffer tmp = new StringBuffer();

        tmp.append("update ").append(table);
        tmp.append(" set ").append("state").append(" = ").append(Integer.toString(state));
        tmp.append(", ").append("drawer_no").append(" = ").append(drawer);
        tmp.append(" where ").append("trans_id").append(" = ").append(Integer.toString(transID()));

        try {
            Application.dbConnection().execute(tmp.toString());
            Application.dbConnection().commit();
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;
    }

    public boolean updateStateAndReason(int state, int reason) {

        StringBuffer tmp = new StringBuffer();

        tmp.append("update ").append(table);
        tmp.append(" set ").append("state").append(" = ").append(Integer.toString(state));
        tmp.append(", ").append("reason_code").append(" = ").append(Integer.toString(reason));
        tmp.append(" where ").append("trans_id").append(" = ").append(Integer.toString(transID()));

        try {
            Application.dbConnection().execute(tmp.toString());
            Application.dbConnection().commit();
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;
    }

    public boolean updateCustomer(String custno) {
        try {
            Application.dbConnection().execute(
                    new SQLProcedureCall("update_customer",
                            new Object[]{
                                new Integer(transID()),
                                custno}));
            Application.dbConnection().commit();
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        } catch (UnknownSQLCall ue) {
            return false;
        }
        return true;
    }

    public boolean updateLogon(int state, int type, int logonid) {

        StringBuffer tmp = new StringBuffer();

        tmp.append("update ").append(table);
        tmp.append(" set ").append("state").append(" = ").append(Integer.toString(state));
        tmp.append(", ").append("trans_type").append(" = ").append(Integer.toString(type));
        tmp.append(", ").append("emp_no").append(" = ").append(Integer.toString(logonid));
        tmp.append(" where ").append("trans_id").append(" = ").append(Integer.toString(transID()));

        try {
            Application.dbConnection().execute(tmp.toString());
            Application.dbConnection().commit();
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;
    }

    public boolean update() {
        return true;
    }

    public int boID() {
        return transID();
    }

    public int boType() {
        return TYPE;
    }

    public String toString() {
        return toXML();
    }

    //  relations, all the other transaction records...

    private Vector ejlines;

    public Vector ejLines() {
        return ejlines;
    }

    public void setEjLines(Vector value) {
        ejlines = value;
    }

    public void relations() {

        // 		setEjLines (new Vector ());

        // 		ejLines ().addElement (this);

        // 		String fetchSpec = TransItem.getByID (transID ());
        // 		ejLines ().addAll (Application.dbConnection ().fetch (new TransItem (), fetchSpec));

        // 		fetchSpec = TransItemLink.getByID (transID ());
        // 		ejLines ().addAll (Application.dbConnection ().fetch (new TransItemLink (), fetchSpec));

        // 		fetchSpec = TransTax.getByID (transID ());
        // 		ejLines ().addAll (Application.dbConnection ().fetch (new TransTax (), fetchSpec));

        // 		fetchSpec = TransTotal.getByID (transID ());
        // 		ejLines ().addAll (Application.dbConnection ().fetch (new TransTotal (), fetchSpec));

        // 		fetchSpec = TransTender.getByID (transID ());
        // 		ejLines ().addAll (Application.dbConnection ().fetch (new TransTender (), fetchSpec));

        // 		TransBank.getByID (transID ());
        // 		ejLines ().addAll (Application.dbConnection ().fetch (new TransBank (), fetchSpec));


    }


    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(transID()));
        objs.addElement(new Integer(transNo()));
        objs.addElement(new Integer(siteID()));
        objs.addElement(new Integer(posNo()));
        objs.addElement(new Integer(drawerNo()));
        objs.addElement(new Integer(configNo()));
        objs.addElement(new Integer(transType()));
        objs.addElement(new Integer(state()));
        objs.addElement(new Integer(reasonCode()));
        objs.addElement(new String(customerNo()));
        objs.addElement(startTime());
        objs.addElement(completeTime());
        objs.addElement(new Integer(empNo()));
        objs.addElement(new Boolean(carryOut()));
        objs.addElement(new Boolean(trainingMode()));
        objs.addElement(new String(localeLanguage()));
        objs.addElement(new String(localeVariant()));
        objs.addElement(archiveDate());
        objs.addElement(uploadDate());
        objs.addElement(replicationDate());

        return objs;
    }
}


