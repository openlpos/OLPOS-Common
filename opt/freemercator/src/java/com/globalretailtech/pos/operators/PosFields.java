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

package com.globalretailtech.pos.operators;

/**
 * Interface for formating Application type fields.
 * based on locale or other local criteria, Examle,
 * a ten digit phone number is stored as 0123456789
 * but in the US we want to see (012) 345-6789.
 *
 * @author  Quentin Olson
 */
public interface PosFields {

    /** Format customer number */
    public String toCustNo(String s);

    /** Format phone number */
    public String toPhoneNo(String s);
}


