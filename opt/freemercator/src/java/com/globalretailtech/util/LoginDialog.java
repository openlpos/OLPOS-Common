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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

import com.globalretailtech.data.Subscriber;

public class LoginDialog extends JDialog {

    private JFrame parent;
    private JPanel container;
    private JPanel buttonPanel;
    private JPanel logPanel;
    private int status;
    private int trys;
    private int loginattempts;
    private String badname;
    private String badpass;
    private String toomany;
    private String goodlogin;
    private String systemerror;
    private JTextField user;
    private JPasswordField pass;
    private JTextArea log;
    private Subscriber sub;

    public static final int LOGIN_OK = 0;
    public static final int INVALID_USER_ID = 1;
    public static final int INVALID_PASSWORD = 2;
    public static final int DATABASE_ERROR = 3;
    public static final int CANCELLED = 4;
    public static final int TOO_MANY_ATTEMPTS = 5;


    public int Status() {
        return status;
    }

    public Subscriber Sub() {
        return sub;
    }

    public LoginDialog(JFrame f) {

        super(f, "", true);

        String pname = new String("properties." + this.getClass().getName());
        PropUtil p = new PropUtil(pname);

        loginattempts = Integer.valueOf(p.getProperty("LoginAttempts", "3")).intValue();
        badname = p.getProperty("BadName", "- bad user -");
        badpass = p.getProperty("BadPass", "- bad password -");
        toomany = p.getProperty("TooMany", "- too many attempts -");
        goodlogin = p.getProperty("GoodLogin", "- login ok -");
        systemerror = p.getProperty("SystemError", "- system error, press cancel -");

        container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createTitledBorder("Login"));

        user = new JTextField(20);
        pass = new JPasswordField(20);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton ok = new JButton(p.getProperty("OkText", "- ok -"));
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OKPressed();
            }
        }
        );

        JButton cancel = new JButton(p.getProperty("CancelText", "- cancel -"));
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CancelPressed();
            }
        }
        );

        JButton debug = new JButton("Debug");
        debug.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Application.setDebug(!Application.debug());
            }
        }
        );

        buttonPanel.add(user);
        buttonPanel.add(pass);
        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        buttonPanel.add(debug);

        logPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        log = new JTextArea(Integer.valueOf(p.getProperty("Rows", "4")).intValue(),
                Integer.valueOf(p.getProperty("Columns", "40")).intValue());
        log.setEditable(false);

        log.append(p.getProperty("LogInit", "- login -\n"));

        JScrollPane scroller = new JScrollPane(log);
        logPanel.add(scroller);

        container.add(buttonPanel, BorderLayout.NORTH);
        container.add(logPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(ok);

        getContentPane().add(container);
        pack();
        centerDialog();
        user.requestFocus();

        trys = 1;

    }

    protected void centerDialog() {
        Dimension screenSize = this.getToolkit().getScreenSize();
        Dimension size = this.getSize();
        screenSize.height = screenSize.height / 2;
        screenSize.width = screenSize.width / 2;
        size.height = size.height / 2;
        size.width = size.width / 2;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        this.setLocation(x, y);
    }

    public void CancelPressed() {
        sub = null;
        status = CANCELLED;
        this.dispose();
    }

    public void OKPressed() {

        if (trys++ > loginattempts) {
            log.append("\n");
            log.append(toomany);
            status = TOO_MANY_ATTEMPTS;
            this.setVisible(false);
            return;
        }

        String fetchSpec = Subscriber.getUser(user.getText());

        Vector v = Application.dbConnection().fetch(new Subscriber(), fetchSpec);

        if (v == null) {
            status = DATABASE_ERROR;
            log.append("\n");
            log.append(systemerror);
            return;
        }
        if (v.size() == 1) {
            sub = (Subscriber) v.elementAt(0);
            if (sub.subscriberPass().equals(new String(pass.getPassword()))) {
                status = LOGIN_OK;
                log.append("\n");
                log.append(goodlogin);
                this.setVisible(false);
                return;
            } else {
                status = INVALID_PASSWORD;
                log.append("\n");
                log.append(badpass);
                return;
            }
        } else {
            status = INVALID_USER_ID;
            log.append("\n");
            log.append(badname);
            return;
        }
    }
}


