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

import java.util.Vector;

import com.globalretailtech.pos.context.PosContext;

/**
 * Holds an input filter for retail operations, could be
 * a barcode or magnetic stripe filter. The apply method
 * applies the filter against the current input, if it matches
 * it performs some action with supplied PosContext.
 *
 * @author  Quentin Olson
 * @author  Igor Semenko
 * @see com.globalretailtech.data.InputFilter
 */
public class Filter {

	// MSR/Credit Card keys

	/** Display name for filter, example: Visa */
	public static final String DISPLAY_NAME = "DisplayName";
	/** Application filter name */
	public static final String FILTER_NAME = "FilterName";
	/** Account number key */
	public static final String ACCT_NO = "AccountNum";
	/** Expiration year key */
	public static final String EXPR_YEAR = "ExprYear";
	/** Expiration month key */
	public static final String EXPR_MONTH = "ExprMonth";
	/** Card holder Surname key */
	public static final String SURNAME = "SurName";
	/** Card holder first name key */
	public static final String FIRSTNAME = "FirstName";

	// UPC/EAN keys

	/** Upc filter key */
	public static final String UPC_TYPE = "UpcType";
	/** Upc Manufacturer key */
	public static final String UPC_MANUFACTURER = "UpcManufacturer";
	/** Upc Product Code key */
	public static final String UPC_PRODUCT_CODE = "UpcProductCode";
	/** Upc Check Digit key */
	public static final String UPC_CHECK_DIGIT = "UpcCheckDigit";

	/** Parse succeeded. */
	public static final int PARSE_OK = 0;
	/** Check digit Application failed. */
	public static final int CHECKDIGIT_FAILED = 1;


	private String regex;
	private String filterName;
	private String displayName;
	private CheckDigit checkdigit;
	private int type;
	private int subtype;
	private Vector fields;


    /**
     * Apply the filter by applying the regular expression
     * to the input string. If it matches perform any operation
     * with context.
     */
    public void apply(PosContext context, String s){
    }

	public String getDisplayName() {
		return displayName;
	}

	public String getFilterName() {
		return filterName;
	}

	public String getRegex() {
		return regex;
	}

	public int getSubtype() {
		return subtype;
	}

	public int getType() {
		return type;
	}

	public void setDisplayName(String string) {
		displayName = string;
	}

	public void setFilterName(String string) {
		filterName = string;
	}

	public void setRegex(String string) {
		regex = string;
	}

	public void setSubtype(int i) {
		subtype = i;
	}

	public void setType(int i) {
		type = i;
	}

	public Vector getFields() {
		return fields;
	}

	public void setFields(Vector vector) {
		fields = vector;
	}

}


