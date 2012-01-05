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


import org.apache.log4j.Logger;

/**
 * Pos holds the basic information for a single point of
 * sale (Application).
 *
 * @author  Quentin Olson
 * @see Site
 * @see BusinessUnit
 */
public class Pos extends BusinessObject implements java.io.Serializable {

    static Logger logger = Logger.getLogger(Pos.class);

    public static final int TYPE = 3;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "pos";

        columns = new String[7];

        columns[0] = "pos_id";
        columns[1] = "pos_no";
        columns[2] = "num_drawers";
        columns[3] = "short_desc";
        columns[4] = "create_date";
		columns[5] = "modify_date";
		columns[6] = "z";

        col_types = new int[7];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.STRING;
        col_types[4] = DBRecord.DATE;
        col_types[5] = DBRecord.DATE;
		col_types[6] = DBRecord.INT;
    }

    private int posid;
    private int posno;
    private int numdrawers;
    private String shortdesc;
    private Date createdate;
    private Date modifydate;
    private int z;

    public Pos() {
    }

    public int posID() {
        return posid;
    }

    public int posNo() {
        return posno;
    }

    public int numDrawers() {
        return numdrawers;
    }

    public String shortDesc() {
        return shortdesc;
    }

    public Date createDate() {
        return createdate;
    }

    public Date modifyDate() {
        return modifydate;
    }

	public int z() {
		return z;
	}

    public void setPosID(int value) {
        posid = value;
    }

    public void setPosNo(int value) {
        posno = value;
    }

    public void setNumDrawers(int value) {
        numdrawers = value;
    }

    public void setShortDesc(String value) {
        shortdesc = value;
    }

    public void setCreateDate(Date value) {
        createdate = value;
    }

    public void setModifyDate(Date value) {
        modifydate = value;
    }

	public void setZ(int value) {
		z = value;
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

    public static String getByPosNo(int no) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(no));

        return new String(s.toString());
    }

    /**
     * Gets all pos associated with a given site.
     */
    public static Vector getAllBySite(int siteid) {

        Vector v = new Vector();
        BoMap map;
        Pos p;

        String fetchSpec = BoMap.getChildrenByIdAndType(siteid, Site.TYPE);
        Vector tmp = Application.dbConnection().fetch(new BoMap(), fetchSpec);

        if (tmp.size() > 0) {

            for (int i = 0; i < tmp.size(); i++) {

                BoMap bo = (BoMap) tmp.elementAt(i);
                v.addElement(bo.businessObject());
            }
        }
        return v;
    }

    public DBRecord copy() {
        Pos b = new Pos();
        return b;
    }

    public Vector parents() {

        String fetchSpec = BoMap.getByIdAndType(posID(), TYPE);
        Vector v = Application.dbConnection().fetch(new BoMap(), fetchSpec);
        Vector bus = null;

        for (int i = 0; i < v.size(); i++) {
            BoMap bo = (BoMap) v.elementAt(i);
            String fs2 = Site.getByID(bo.parentBoID());
            bus = Application.dbConnection().fetch(new Site(), fs2);
        }
        return bus;
    }


    public void populate(ResultSet rset) {

        try {
            setPosID(rset.getInt("pos_id"));
            setPosNo(rset.getInt("pos_no"));
            setNumDrawers(rset.getInt("num_drawers"));
            setShortDesc(rset.getString("short_desc"));
            setCreateDate(rset.getDate("create_date"));
            setModifyDate(rset.getDate("modify_date"));
            setZ (rset.getInt ("z"));
        } catch (SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

	public boolean save() {

		StringBuffer tmp = new StringBuffer();

		tmp.append("insert into ");
		tmp.append(table);
		tmp.append(" values (");
		tmp.append(Integer.toString(posID()));
		tmp.append(", ");
		tmp.append(Integer.toString(posNo()));
		tmp.append(", ");
		tmp.append(Integer.toString(numDrawers()));
		tmp.append(", '");
		tmp.append(shortDesc());
		tmp.append("', '");
		tmp.append(Format.toDbDateString(createDate()));
		tmp.append("', '");
		tmp.append(Format.toDbDateString(modifyDate()));
		tmp.append("', ");
		tmp.append(Integer.toString(z()));
		tmp.append(")");

		try {
			Application.dbConnection().execute(tmp.toString());
			Application.dbConnection().commit();
		} catch (SQLException e) {
			Application.dbConnection().setException(e);
			return false;
		}
		return true;
	}

	public boolean update() {

		StringBuffer tmp = new StringBuffer();

		tmp.append("update ");
		tmp.append(table);
		tmp.append(" set ");
		tmp.append(columns[0] +"=");
		tmp.append(Integer.toString(posID()));
		tmp.append(", "+columns[1]+"=");
		tmp.append(Integer.toString(posNo()));
		tmp.append(", "+columns[2]+"=");
		tmp.append(Integer.toString(numDrawers()));
		tmp.append(", "+columns[3]+"= '");
		tmp.append(shortDesc());
		tmp.append("', "+columns[4]+"= '");
		tmp.append(Format.toDbDateString(createDate()));
		tmp.append("', "+columns[5]+"= '");
		tmp.append(Format.toDbDateString(modifyDate()));
		tmp.append("', "+columns[6]+"= ");
		tmp.append(Integer.toString(z()));
		tmp.append(" where ");
		tmp.append(columns[0]+"=");
		tmp.append(Integer.toString(posID()));

		try {
			Application.dbConnection().execute(tmp.toString());
			Application.dbConnection().commit();
		} catch (SQLException e) {
			Application.dbConnection().setException(e);
			return false;
		}
		return true;
	}

    public int create(int site) {
        int siteid = -1;
        try {
            siteid = createPOS(new Integer(site),
                            new Integer(posNo()),
                            new Integer(numDrawers()),
                            shortDesc(),
                            new Integer(Pos.TYPE),
                            new Integer(Site.TYPE));
        } catch (SQLException je) {
            logger.fatal("Error creating POS" + je, je);
        }

        return siteid;
    }

    private int createPOS(Integer site, Integer posNo, Integer numDrawers, String desc, Integer posType, Integer siteType) throws SQLException {
        DBContext conn = Application.dbConnection();
        Timestamp stamp = new Timestamp((new Date()).getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//.fffffffff
        conn.execute("INSERT INTO POS (pos_no, num_drawers, short_desc, create_date, modify_date) values " +
                                      "(" + posNo + ","+ numDrawers + ",'"+ desc + "','"+ df.format(stamp) + "','"+ df.format(stamp) + "')"
                );
        ResultSet rs = conn.executeWithResult("SELECT max(pos_id) from pos");
        rs.next();
//        if (!rs.next()) throw new SQLException("POS not inserted correctly");
        int myID = rs.getInt(1);

        rs = conn.executeWithResult("select site_id from site where site_no = " + site);
        rs.next();
//        if (!rs.next()) throw new SQLException("Invalid Site ID");
        int parentID = rs.getInt("site_id");

        conn.execute("insert into bo_map (bo_id, obj_type, parent_bo_id, pobj_type) values " +
                                  "(" + myID + ","+ posType + ","+ parentID + ","+ siteType + ")"
                );
        return myID;
    }


    public int boID() {
        return posID();
    }

    public int boType() {
        return TYPE;
    }

    public String toString() {
        return toXML();
    }

    // Relations

    private Vector datastores;

    public Vector dataStores() {
        return datastores;
    }

    /**
     * pick up and secondary data store information
     */
    public void relations() {

        // 		String fetchSpec = SiteDataStore.getSecondary (posID ());
        // 		ejLines ().addAll (Application.dbConnection ().fetch (new TransItem (), fetchSpec));
    }

    private BoMap bomap;

    public BoMap boMap() {
        return bomap;
    }

    public void setBoMap(BoMap value) {
        bomap = value;
    }


    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(posID()));
        objs.addElement(new Integer(posNo()));
        objs.addElement(new Integer(numDrawers()));
        objs.addElement(new String(shortDesc() == null ? "" : shortDesc()));
        objs.addElement(createDate());
        objs.addElement(modifyDate());

        return objs;
    }
}


