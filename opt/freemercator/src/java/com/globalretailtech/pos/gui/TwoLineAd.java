package com.globalretailtech.pos.gui;

import com.globalretailtech.pos.devices.PosPrompt;
import com.globalretailtech.pos.context.PosContext;
import com.globalretailtech.util.ShareProperties;

import javax.swing.*;
import java.awt.*;

/*
 * Copyright (C) 2003 Igor Semenko
 * <igorsemenko@yahoo.com>
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

public class TwoLineAd  extends JPanel implements PosPrompt, PosGui {

    private int promptWidth;
    private int clockWidth;
    private int sleepInterval;
    private Font defaultFont;
    private PosContext context;
    private JTextField clock;
    private JTextField prompt;
    public static final Font clockFont = new Font("Courier", Font.PLAIN, 20);
    public static final Font promptFont = new Font("Helvetica", Font.BOLD, 24);

	// dynamic content
	String companyName;
	
    public TwoLineAd() {
        super();
    }

    public JComponent getGui() {
        return this;
    }

    public boolean isOpen() {
        return true;
    }  // or we wouldn't be here

    public void init(PosContext ctx) {

        context = ctx;

        ShareProperties p = new ShareProperties(this.getClass().getName());

        if (p.Found()) {
            defaultFont = new Font(p.getProperty("DefaultFont", "Courier"), Font.PLAIN,
            Integer.valueOf(p.getProperty("DefaultFontSize", "10")).intValue());
            promptWidth = Integer.valueOf(p.getProperty("PromptWidth", "40")).intValue();
            clockWidth = Integer.valueOf(p.getProperty("ClockWidth", "20")).intValue();
			sleepInterval = Integer.valueOf(p.getProperty("SleepInterval", "10000")).intValue();
			companyName = p.getProperty("CompanyName", "Your Company Name");
        } else {
            defaultFont = new Font("Courier", Font.PLAIN, 9);
            promptWidth = 40;
            clockWidth = 20;
            sleepInterval = 20;
            companyName = "Your Company Name";
        }

        setLayout(new BorderLayout());

        prompt = new JTextField(promptWidth);
        prompt.setText(" ");
//        prompt.setSize( new Dimension (500,50));
        prompt.setFont(promptFont);
        prompt.setEditable(false);
//        prompt.setBackground(Color.white);
        prompt.setForeground(Color.black);
        prompt.setAlignmentX(1);

        add(prompt, BorderLayout.SOUTH);



        clock = new JTextField(clockWidth);
        clock.setFont(clockFont);
        clock.setText(companyName);

        JPanel clockPanel = new JPanel();
        clockPanel.setLayout(new BorderLayout());
		clockPanel.add(clock, BorderLayout.NORTH);
		clockPanel.add(clock, BorderLayout.SOUTH);
//        clockPanel.setPreferredSize( new Dimension (clock.getFontMetrics(clock.getFont()).stringWidth(text) + 20,50));

        clock.setEditable(false);

        add(clockPanel, BorderLayout.CENTER);

        revalidate();
        repaint();

    }


    public void clear() { /*prompt.setText ("");*/
    }

    public void home() {
    }

    public void open() {
    }

    public void close() {
    }

    /**
     *
     */
    public void setText(String text) {
        prompt.setText(text);
    }

    public void setText(String text, int row, int col) {
        prompt.setText(text);
    }

    public int getWidth() {
        return promptWidth;
    }

}


