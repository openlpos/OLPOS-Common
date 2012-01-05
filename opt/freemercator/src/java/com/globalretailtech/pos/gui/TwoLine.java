package com.globalretailtech.pos.gui;

import com.globalretailtech.pos.devices.PosPrompt;
import com.globalretailtech.pos.context.PosContext;
import com.globalretailtech.util.ShareProperties;
import com.globalretailtech.util.Format;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/*
 * Copyright (C) 2003 Casey Haakenson
 * <casey@haakenson.com>
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

public class TwoLine  extends JPanel implements PosPrompt, PosGui {

    private int promptWidth;
    private int clockWidth;
    private int sleepInterval;
    private Font OneLineFont;
    private PosContext context;
    private JTextField clock;
    private JTextField prompt;
    private ClockThread timer;
    public static final Font clockFont = new Font("Courier", Font.PLAIN, 20);
    public static final Font promptFont = new Font("Courier", Font.BOLD, 24);

    public TwoLine() {
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
            OneLineFont = new Font(p.getProperty("OneLineFont", "Courier"), Font.PLAIN,
                    Integer.valueOf(p.getProperty("OneLineFontSize", "10")).intValue());
            promptWidth = Integer.valueOf(p.getProperty("PromptWidth", "40")).intValue();
            clockWidth = Integer.valueOf(p.getProperty("ClockWidth", "20")).intValue();
            sleepInterval = Integer.valueOf(p.getProperty("SleepInterval", "10000")).intValue();
        } else {
            OneLineFont = new Font("Courier", Font.PLAIN, 9);
            promptWidth = 40;
            clockWidth = 20;
            sleepInterval = 20;
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

        add(prompt, BorderLayout.CENTER);

        clock = new JTextField(clockWidth);
        String text = Format.getLongDate(new Date());
        clock.setFont(clockFont);
        clock.setText(text);

        JPanel clockPanel = new JPanel();
        clockPanel.setLayout(new BorderLayout());
        clockPanel.add(clock, BorderLayout.CENTER);
//        clockPanel.setPreferredSize( new Dimension (clock.getFontMetrics(clock.getFont()).stringWidth(text) + 20,50));

        clock.setEditable(false);

        add(clockPanel, BorderLayout.NORTH);

        timer = new ClockThread();
        timer.start();
        revalidate();
        repaint();

    }


    public void clear() { /*prompt.setText ("");*/
    }

    public void home() {
        timer.updateTimer();
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

    class ClockThread extends Thread {

        public ClockThread() {
        }

        public void run() {

            while (true) {
                updateTimer();
                try {
                    Thread.sleep(sleepInterval);
                } catch (java.lang.InterruptedException e) {
                }
            }
        }

        public void updateTimer() {
            String newDate = Format.getLongDate(new Date());
//            if (clock.getText().equals(newDate) == false){
                clock.setText(newDate);
//            }
            prompt.setText(prompt.getText());
            repaint();
        }
    }
}


