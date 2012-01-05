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

package com.globalretailtech.pos.devices;


/**
 * An interface for coupling Application logic to psuedo
 * and real display devices.
 *
 * @author  Quentin Olson
 */
public interface PosTicket {

    public boolean isOpen();

    public int getQtyWidth();

    public int getItemWidth();

    public int getItemDescWidth();

    public int getAmountWidth();

    public int getColumns();

    public void setQty(String value);

    public void setItem(String value);

    public void setDesc(String value);

    public void setAmount(String value);

    public void setTrxNo(String value);

    public void setPosNo(String value);

    public void setOperator(String value);

    public void setDate(String value);

    public void println();

    public void println(String value);

    public void printHeader();

    public void printTrailer();

    public void clear();

    public void clearln();
}


