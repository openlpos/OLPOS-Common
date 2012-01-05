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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.  */

package com.globalretailtech.pos.gui;

import java.awt.*;
import javax.swing.*;

/**
 * An extension of JButton that wraps text too long
 * for the button.
 */
public class PosGuiButton extends JButton {

	int keyID;
	
    public PosGuiButton() {
    }

	public PosGuiButton(String text) {
		setText(text);
		repaint();
	}

	public PosGuiButton(String text, int keyID) {
		setText(text);
		setKeyID(keyID);
		repaint();
	}

    public void setText(String text) {
        super.setText(text);
        repaint();
    }

    public String getText() {
        return super.getText();
    }

    public void appendText(String text) {
        setText(getText() + text);
        repaint();
    }

    public void clearText() {
        setText("");
        repaint();
    }

    public void insertText(String text, int index) {
        StringBuffer sb = new StringBuffer(getText());
        sb.insert(index, text);
        setText(sb.toString());
        repaint();
    }

    public void setBorder(boolean border) {
        setBorder(border);
    }

    /**
     * New paint method.
     */
    public void paintComponent(Graphics g) {

        // Clear thet button
        g.setColor(this.getBackground());
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);

        g.setColor(this.getForeground());
        g.setFont(this.getFont());
        FontMetrics fm = this.getFontMetrics(this.getFont());

        int max_length = getMaxStringLength(fm);
        int line = fm.getHeight();

		// image
		String shareDir = System.getProperty("SHARE");
		String disabled = (isEnabled())?"":"d";
		java.io.File img = new java.io.File (shareDir+"/buttons",getKeyID()+disabled+".gif");
		if ( img.exists() ){
			ImageIcon icon = new ImageIcon(img.getAbsolutePath());
			int x = (getWidth()>icon.getIconWidth())?getWidth()-icon.getIconWidth():0;
			int y = (getHeight()>icon.getIconHeight())?getHeight()-icon.getIconHeight():0;
			g.drawImage(icon.getImage() ,(int)x/2,(int)y/2,icon.getIconWidth(),icon.getIconHeight(),null);
		}
		
        // This one doesn't wrap
        if (getText().length() <= max_length) {
            g.drawString(getText(), 3, line);
            return;
        }

        int start = 0; // shifting starting point in text
        String subs = ""; // substring in text
        String next = " "; // string to hold the next charater after subs
        String todraw = ""; // string to draw

        // Compute wrap
        // loop over entire text indices

        for (int i = 0; i < getText().length(); i++) {

            // extract substring
            subs = getText().substring(start, start + max_length);

            if ((start + max_length) < getText().length())
                next = getText().substring(start + max_length, start + max_length + 1);
            else
                next = " ";

            // time for new line
            if ((i % max_length) == 0) {
                if ((!next.equals(" ")) && (!(subs.substring(subs.length() - 1, subs.length())).equals(" "))) // word breakup

                // shorten the substring based on last available space
                    if (findLastSpace(subs) != 0)
                        subs = subs.substring(0, findLastSpace(subs));

                // (if the word length is greater than the canvas width, it must be broken up)
                if ((subs.substring(0, 1)).equals(" "))
                    todraw = subs.substring(1, subs.length()); // check for a space beginning line
                else
                    todraw = subs;
                g.drawString(todraw, 3, line); // draw string
                start = start + subs.length(); // increment starting point

                // end near; adust max_length
                if ((start + max_length) >= getText().length())
                    max_length = getText().length() - start;
                if (max_length == 0)
                    return;

                line = line + fm.getHeight(); // move to next line
            }
        }
    }

    /**
     * Reduce ficker
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * method to find the last space prior to the current word
     */
    public int findLastSpace(String s) {
        for (int i = s.length() - 1; i > 0; i--)
            if ((s.substring(i - 1, i)).equals(" "))
                return i - 1;
        return 0;
    }

    /**
     * method to get the maximum characters allowed from the canvas width
     */
    public int getMaxStringLength(FontMetrics fm) {
        for (int i = 0; i < getText().length(); i++)
            if ((fm.stringWidth(getText().substring(0, i)) > ((this.getSize()).width - fm.stringWidth(" ") * 3)))
                return i; // 3 character safeguard
        return getText().length();
    }


    public void keyEnable() {
        setEnabled(true);
    }

    public void keyDisable() {
        setForeground(Color.lightGray);
        setEnabled(false);
    }
	public int getKeyID() {
		return keyID;
	}

	public void setKeyID(int i) {
		keyID = i;
	}

}
