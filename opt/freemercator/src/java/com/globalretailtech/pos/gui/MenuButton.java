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

package com.globalretailtech.pos.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;

import com.globalretailtech.util.ShareProperties;
import com.globalretailtech.data.PosKey;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.*;
import com.globalretailtech.pos.devices.*;

/**
 * Implement virtual pos keypad keys.
 *
 * @author  Quentin Olson
 * @see
 */
public class MenuButton extends PosGuiButton implements KeyEnable {


    private com.globalretailtech.pos.events.KeyEvent keyevent;
    private JPanel container;
    private PosKey keyconfig;

    protected static final Border raisedBorder = BorderFactory.createMatteBorder(1,1,1,1, Color.gray);
    protected static final Border loweredBorder = BorderFactory.createMatteBorder(1,1,1,1, Color.black);

    public com.globalretailtech.pos.events.KeyEvent keyEvent() {
        return keyevent;
    }

    public JPanel container() {
        return container;
    }

    public void setKeyEvent(com.globalretailtech.pos.events.KeyEvent value) {
        keyevent = value;
    }


    public MenuButton(PosKey keyconfig) {

        super(keyconfig.keyText(),keyconfig.keyID());
        this.keyconfig = keyconfig;
        setColors();
    }

    public MenuButton(PosContext context, PosKey keyconfig, JPanel c) {
        super(keyconfig.keyText(),keyconfig.keyID());

        this.keyconfig = keyconfig;

        ShareProperties p = new ShareProperties(this.getClass().getName());
        Font buttonFont;

        if (p.Found()) {
            buttonFont = new Font(p.getProperty("ButtonFont", "Courier"), Font.PLAIN,
                    Integer.valueOf(p.getProperty("ButtonFontSize", "10")).intValue());
        } else {
            buttonFont = new Font("Courier", Font.PLAIN, 9);
        }

        setFont(buttonFont);
  		setBorder (raisedBorder);

        setKeyEvent((com.globalretailtech.pos.events.KeyEvent) context.keysByID().get(new Integer(keyconfig.keyID())));
        keyEvent().setEnabler(this);
        container = c;

        setColors();

        this.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e) {
                if (isEnabled()){
                    setBorder (loweredBorder);
                    Color c = getBackground();
                    int shift = -10;

					// some values may cause shifted color to be out of range
					Color newBG = c;                    
                    try {
						newBG = new Color(
								c.getRed() + shift,
								c.getGreen() + shift,
								c.getBlue() + shift);
					} catch (Exception ee) {
					}
                    setBackground(newBG);
                }
            }

            public void mouseReleased(MouseEvent e) {

                    setBorder (raisedBorder);
                    //reset the background color

                    setColors();
                
            }
        });
    }

    /**
     * Process the key press with validations:
     *
     * 1) check if the current state is ok
     * 2) check if this user has the correct profile
     */
    public void process(PosContext context) throws PosException {

        keyEvent().function().setContext(context);

        // is this a valid thing to be doing?

        if (!keyEvent().function().isValidEvent()) {

            context.eventStack().pushEvent(new PosError(context, PosError.INVALID_INPUT));
            context.operPrompt().update((PosError) context.eventStack().event());
            return;
        }

        /**
         * Does this operator have the appropriate profile? If not the
         * context creates a manager override dialog and inserts this event.
         * So even if this returns false the event may be executed.
         */
        if (keyEvent().function().employeeHasProfile()) {

			context.eventStack().pushEvent(keyEvent().function());
			context.eventStack().nextEvent(keyEvent().config().keyVal(), true);
        }
    }

    public boolean validTransition(PosContext context) {

        if (keyEvent().function() == null)
            return true;

        if (!keyEvent().function().isValidEvent()) {
            // 			context.updateDisplays (PosEvent.INVALID_INPUT);
            return false;
        }
        return true;
    }

    public void setColors() {
        if (isEnabled()){
            setForeground(new Color(keyEvent().config().fgColor()));
        } else {
            setForeground(Color.lightGray);
        }
        setBackground(new Color(keyEvent().config().bgColor()));
    }

    public void keyEnable() {
        setForeground(new Color(keyEvent().config().fgColor()));
        super.setEnabled(true);
    }

	public void keyDisable() {
		setForeground(Color.lightGray);
		super.setEnabled(false);
	}
	public boolean isEnabled() {
		return super.isEnabled();
	}

    public PosKey getKeyconfig() {
        return keyconfig;
    }
}


