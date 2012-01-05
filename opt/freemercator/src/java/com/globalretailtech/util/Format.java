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

package com.globalretailtech.util;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

public class Format {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int CENTER = 2;

    static String longDateFormat;
    static String shortDateFormat;
    static String timeFormat;
    static String moneyFormat;
    static {
        ShareProperties p = new ShareProperties(Format.class.getName());
        longDateFormat = p.getProperty("LongDateFormat","EEEEE, dd.MM.yy HH:mm");
        shortDateFormat = p.getProperty("ShortDateFormat","dd.MM HH:MM");
		timeFormat = p.getProperty("TimeFormat","HH:mm");
		moneyFormat = p.getProperty("MoneyFormat");
    }

    static SimpleDateFormat longDateFormatter = new SimpleDateFormat (longDateFormat);
    static SimpleDateFormat shortDateFormatter = new SimpleDateFormat (shortDateFormat);
	static SimpleDateFormat timeFormatter = new SimpleDateFormat (timeFormat);

    public static String print(String s, String fill, int width, int justify) {

        int i = 0;
        if (s.length() > width) {
            s = s.substring(0, width - 1);
        }

        StringBuffer tmp = new StringBuffer();

        switch (justify) {

            case LEFT:
                tmp.append(s);
                for (i = 0; i < width - s.length(); i++)
                    tmp.append(fill);
                break;
            case RIGHT:
                for (i = 0; i < width - s.length() - 1; i++)
                    tmp.append(fill);
                tmp.append(s);
                break;

            case CENTER:
                int left = (width - s.length()) / 2;
                int right = left;

                if (((width - s.length()) % 2) > 0)
                    right++;

                for (i = 0; i < left; i++)
                    tmp.append(fill);
                tmp.append(s);
                for (i = 0; i < right; i++)
                    tmp.append(fill);
                break;
            default:
                tmp.append(s);
                for (i = 0; i < width - s.length(); i++)
                    tmp.append(fill);
                break;
        }
        return tmp.toString();
    }

    public static String print(String s1, String s2, String fill, int width) {

        int i = 0;
        StringBuffer tmp = new StringBuffer();
		
		if ( s1 == null)
			s1 = "";

		if ( s2 == null)
			s2 = "";
			
        if ( width > 0 && (s1.length() + s2.length()) > width) {
            tmp.append(s1.substring(0,width-s2.length()-1)+" ");  // cut if oversized
            tmp.append(s2);

            return tmp.toString().substring(0, width);
        }

        tmp.append(s1);

        int fillwidth = width - (s1.length() + s2.length());

        for (i = 0; i < fillwidth; i++)
            tmp.append(fill);
        tmp.append(s2);
        return tmp.toString();

    }

    public static String justify(String s1, String s2, String s3, String fill, int width) {

        int slen = s1.length() + s2.length() + s3.length();
        int spaces = (width - slen) / 2;
        StringBuffer fillbuf = new StringBuffer();
        for (int i = 0; i < spaces; i++)
            fillbuf.append(fill);
        StringBuffer tmp = new StringBuffer();
        tmp.append(s1).append(fillbuf.toString()).append(s2).append(fillbuf.toString()).append(s3);
        return tmp.toString();
    }

    public static String StripZeros(String s) {
        int i;

        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '0') {
                break;
            }
        }
        return s.substring(i, s.length());
    }

    /**
     *
     */

    //  public static String toMoney (String s) {

    //      Double val = new Double (s);
    //      java.text.DecimalFormat Format = (java.text.DecimalFormat) java.text.NumberFormat.getCurrencyInstance ();

    //      Format.setNegativePrefix ("- $");
    //      Format.setNegativeSuffix ("");

    //      return (Format.Format(val.doubleValue () / 100.0));
    //  }

    public static String toMoney(String s, java.util.Locale locale) {

        Double val = new Double(s);
        if (locale == null)
            locale = Application.locale();
        java.text.DecimalFormat format = (java.text.DecimalFormat) java.text.NumberFormat.getCurrencyInstance(locale);
        if (moneyFormat != null){
        	format.applyPattern(moneyFormat);
        }

        //      Format.setNegativePrefix ("- ?");
        //      Format.setNegativeSuffix (" -");

        return (format.format(val.doubleValue() / 100.0));
    }

    /**
     *
     */
    public static String zeroFill(int d, int len, String pattern) {

        java.text.DecimalFormat f = new java.text.DecimalFormat();
        f.applyPattern(pattern);
        f.setMinimumIntegerDigits(len);
        return (f.format((long) d));
    }

    /**
     *
     */
    public static String zeroFill(String s, int len, String pattern) {

        java.text.DecimalFormat f = new java.text.DecimalFormat();
        f.applyPattern(pattern);
        f.setMinimumIntegerDigits(len);
        return (f.format(new Long(s).longValue()));
    }

    public static String getTime(Date date) {
        return timeFormatter.format (date);
    }

    public static String getShortTimeDate(Date date) {
        return shortDateFormatter.format (date);
    }

    public static String getLongDate(Date date) {
        return longDateFormatter.format (date);
    }

    public static String getDate(Date date) {

        StringBuffer str = new StringBuffer();
        java.text.DecimalFormat f = new java.text.DecimalFormat();
        f.applyPattern("####");
        f.setMinimumIntegerDigits(2);
        f.setMaximumIntegerDigits(4);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        str.append(f.format(cal.get(Calendar.MONTH) + 1)).append("/");
        str.append(f.format(cal.get(Calendar.DAY_OF_MONTH))).append("/");
        str.append(f.format(cal.get(Calendar.YEAR)));

        return str.toString();
    }

    /**
     * Return a database compliant timestamp string in the form
     * '1-Jan-2000 00:00'.
     */
    public static String toDbDateString(Date date) {

        if (date == null) {
            date = new Date();
        }

        StringBuffer str = new StringBuffer();
        java.text.DecimalFormat f = new java.text.DecimalFormat();
        f.applyPattern("##");

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        str.append(cal.get(Calendar.MONTH) + 1).append("/");
        str.append(cal.get(Calendar.DAY_OF_MONTH)).append("/");
        str.append(cal.get(Calendar.YEAR)).append(" ");
        str.append(f.format(cal.get(Calendar.HOUR))).append(":");
        str.append(f.format(cal.get(Calendar.MINUTE)));

        return str.toString();
    }

	/**
	 * Substitutes "{0}" with passed object 
	 */
	public static String substitute(String tmpl, Object o) {
		return tmpl.replaceAll("\\{0\\}",o.toString());
	}
    
}


