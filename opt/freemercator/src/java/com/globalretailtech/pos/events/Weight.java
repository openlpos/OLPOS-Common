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

package com.globalretailtech.pos.events;



/**
 * For volume pricing, not implemented.
 *
 * @author  Quentin Olson
 */
public class Weight extends PosNumberDialog {

    private String prompttext;
    private String weighttext;
    private double weight;

    /** Prompt text for user dialog. */
    public String promptText() {
        return prompttext;
    }

    public double weight() {
        return weight / 100.0;
    }

    public void setPromptText(String value) {
        prompttext = value;
    }

    public void setWeight(double value) {
        weight = value;
    }

    /** Simple constructor */
    public Weight() {
        setWeight(0.0);
        type = PosNumberDialog.DECIMAL;
    }

    /**
     * Get the weight (scale amount) from the
     * context input line.
     */
    public void engage(int value) {

        context().clearInput();

        if (state() > 0) {

            popState();
            setPromptText(context().posParameters().getString("EnterItem"));
        } else {

            if (value == 0) {

                setPromptText(context().posParameters().getString("EnterWeight"));
                states().pushState(1);
                context().eventStack().pushEvent(this);
            } else {

                setPromptText(context().posParameters().getString("EnterItem"));
                setWeight((double) value);
                context().eventStack().pushEvent(this);
            }
        }

        context().operPrompt().update(this);
    }

    private int type;

    /** Implemntation of type for PosNumberDialog */
    public int type() {
        return type;
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "Weight";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


