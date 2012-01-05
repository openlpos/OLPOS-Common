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

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jpos.JposException;

import com.globalretailtech.util.Application;
import com.globalretailtech.util.Format;
import com.globalretailtech.util.ShareProperties;
import com.globalretailtech.data.Employee;
import com.globalretailtech.pos.context.ContextSet;
import com.globalretailtech.pos.context.PosContext;
import com.globalretailtech.pos.events.ExecScript;
import com.globalretailtech.pos.hardware.Scanner;

import org.apache.log4j.Logger;

/**
 * A logon panel.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class LogonPanel extends JPanel {

	static Logger logger = Logger.getLogger(LogonPanel.class);

	private String logonFont;
	private int logonFontSize;
	private String userPrompt;
	private String passPrompt;
	private String logonFailed;
	private String tooManyRetrys;
	private String enterButton;
	private String clearButton;
	private String shutdownButton;
	private int retryLimit;
	private int retryCount;
	private Employee emp;
	private StringBuffer inputLine;
	private String user;
	private String pass;
	private String currentPrompt;
	private int userNo;
	private int passNo;
	private JTextField inputField;
	private int fieldLen;
	private int buttonColor;
	private int operatorColor;
	private int backgroundColor;
	private PosTabs parent;
	private LogonPanel thisPanel;
	private JLabel north;
	private JLabel east;
	private JLabel south;
	private JLabel west;
	private int siteID;
	private int posID;
	private int posNo;
	private int configNo;
	private int shutdownScriptIndex;
	private int shutdownKeyCode;

	JButton buttonShutdown;

	EmpScanner scanner;
	Map contexts = new HashMap(); // empid - context relations
	String empKeyPattern;

	private final int maxChars = 12;

	public LogonPanel(int sid, int pid, int pno, int cno, PosTabs p) {

		super();

		siteID = sid;
		posID = pid;
		posNo = pno;
		configNo = cno;
		parent = p;
		thisPanel = this;

		ShareProperties prop = new ShareProperties(this.getClass().getName());
		if (prop.Found()) {

			logonFont =
				prop.getProperty("LogonFont", "Lucida Sans Typewriter Regular");
			logonFontSize =
				new Integer(prop.getProperty("LogonFontSize", "16")).intValue();

			userPrompt = prop.getProperty("UserPrompt", "Enter User ID:");
			passPrompt = prop.getProperty("PassPrompt", "Enter Password:");
			logonFailed = prop.getProperty("LogonFailed", "Logon Failed,Retry");
			tooManyRetrys =
				prop.getProperty("TooManyRetrys", "Too Many Logon Attempts!");
			enterButton = prop.getProperty("EnterButtontText", "ENT");
			clearButton = prop.getProperty("ClearButtontText", "CLR");
			shutdownButton = prop.getProperty("ShutdownButtonText", "OFF");
			fieldLen =
				new Integer(prop.getProperty("FieldLen", "60")).intValue();
			retryLimit =
				new Integer(prop.getProperty("RetryLimit", "3")).intValue();
			operatorColor =
				Integer.parseInt(
					prop.getProperty("OperatorColor", "ECE5B6"),
					16);
			buttonColor =
				Integer.parseInt(prop.getProperty("ButtonColor", "ECE5B6"), 16);
			backgroundColor =
				Integer.parseInt(
					prop.getProperty("BackgroundColor", "FFFFFF"),
					16);
			shutdownScriptIndex =
				Integer.parseInt(prop.getProperty("ShutdownScriptIndex", "0"));
			shutdownKeyCode =
				Integer.parseInt(prop.getProperty("ShutdownKeyCode", "123"));
			//F12
			empKeyPattern =	prop.getProperty("EmployeeKeyPattern", "01[\\d]{5}0");
		} else {
			logonFont = new String("Lucida Sans Typewriter Regular");
			logonFontSize = 16;
			userPrompt = new String("Enter User ID:");
			passPrompt = new String("Enter Password:");
			logonFailed = new String("Logon Failed, Retry");
			tooManyRetrys = new String("Too Many Logon Attempts!");
			enterButton = new String("ENT");
			clearButton = new String("CLR");
			shutdownButton = new String("OFF");
			fieldLen = 60;
			retryLimit = 3;
			operatorColor = 0xECE5B6;
			buttonColor = 0xECE5B6;
			backgroundColor = 0xC9C299;
			shutdownScriptIndex = 0;
			shutdownKeyCode = 123; //F12
			empKeyPattern = "";
		}

		scanner = new EmpScanner(new jpos.Scanner(), "POSScanner", (ContextSet)p, empKeyPattern);

		LogonKeyListener keyListener =
			new LogonKeyListener(p, shutdownScriptIndex, shutdownKeyCode);
		p.addKeyListener(keyListener);
		addKeyListener(keyListener);
		// these are needed to create the context on the parent tab.

		setBackground(new Color(backgroundColor));
		inputLine = new StringBuffer();

		BorderLayout blayout = new BorderLayout(0, 0);
		setLayout(blayout);

		//		new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED)
		SoftBevelBorder border = new SoftBevelBorder(SoftBevelBorder.RAISED);

		// create another panel to use as the main layout
		JPanel main = new JPanel();
		main.addKeyListener(keyListener);
		main.setBackground(new Color(backgroundColor));
		main.setBorder(new LineBorder(Color.gray));

		GridBagLayout layout = new GridBagLayout();
		main.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridheight = 1;
		c.gridwidth = 4;

		JButton b = null;

		currentPrompt = userPrompt;
		inputField = new JTextField(currentPrompt, fieldLen);
		inputField.setBorder(null);
		inputField.setFont(new Font(logonFont, Font.PLAIN, logonFontSize));
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 4;
		layout.setConstraints(inputField, c);
		main.add(inputField);

		int row = 1;
		int col = 2;

		c.gridheight = 1;
		c.gridwidth = 1;

		for (int i = 9; i > 0; i--) {

			b = new JButton(Integer.toString(i));
			b.addKeyListener(keyListener);
			b.setBorder(border);
			b.setFont(new Font(logonFont, Font.PLAIN, logonFontSize));
			b.setBackground(new Color(buttonColor));
			b.addActionListener(new NumKey(Integer.toString(i)));

			c.gridx = col--;
			c.gridy = row;

			if ((i % 3) == 1) {
				row++;
				col = 2;
			}

			layout.setConstraints(b, c);
			main.add(b);
		}

		row++;

		b = new JButton("0");
		b.addKeyListener(keyListener);
		b.setBorder(border);
		b.setFont(new Font(logonFont, Font.PLAIN, logonFontSize));
		b.setBackground(new Color(buttonColor));
		b.addActionListener(new NumKey("0"));

		c.gridx = 0;
		c.gridy = row;
		c.gridheight = 1;
		c.gridwidth = 2;
		layout.setConstraints(b, c);
		main.add(b);

		b = new JButton(shutdownButton);
		buttonShutdown = b;
		b.addKeyListener(keyListener);
		b.setBorder(border);
		b.setFont(new Font(logonFont, Font.PLAIN, logonFontSize));
		b.setBackground(new Color(operatorColor));
		b.addActionListener(new ShutdownKey(shutdownScriptIndex));

		c.gridx = 2;
		c.gridy = row;
		c.gridheight = 1;
		c.gridwidth = 1;
		layout.setConstraints(b, c);
		main.add(b);

		b = new JButton(clearButton);
		b.addKeyListener(keyListener);
		b.setBorder(border);
		b.setFont(new Font(logonFont, Font.PLAIN, logonFontSize));
		b.setBackground(new Color(operatorColor));
		b.addActionListener(new ClearKey());

		c.gridx = 3;
		c.gridy = 1;
		c.gridheight = 2;
		c.gridwidth = 1;
		layout.setConstraints(b, c);
		main.add(b);

		b = new JButton(enterButton);
		b.addKeyListener(keyListener);
		b.setBorder(border);
		b.setFont(new Font(logonFont, Font.PLAIN, logonFontSize));
		b.setBackground(new Color(operatorColor));
		b.addActionListener(new EnterKey());

		c.gridx = 3;
		c.gridy = 3;
		c.gridheight = 3;
		c.gridwidth = 1;
		layout.setConstraints(b, c);
		main.add(b);
		b.requestFocus();

		String shareDir = System.getProperty("SHARE");
		north = new JLabel(new ImageIcon(shareDir + "/images/north.gif"));
		north.setPreferredSize(new Dimension(100, 100));
		south = new JLabel(new ImageIcon(shareDir + "/images/south.gif"));
		south.setPreferredSize(new Dimension(100, 100));
		east = new JLabel(new ImageIcon(shareDir + "/images/east.gif"));
		east.setPreferredSize(new Dimension(200, 200));
		west = new JLabel(new ImageIcon(shareDir + "/images/west.gif"));
		west.setPreferredSize(new Dimension(200, 200));

		add(main, BorderLayout.CENTER);

		add(north, BorderLayout.NORTH);
		add(south, BorderLayout.SOUTH);
		add(east, BorderLayout.EAST);
		add(west, BorderLayout.WEST);

	}

	/**
	 * goofy little method to see how big these areas
	 * are so we can make nice looking labels.
	 */
	public void sizes() {
		logger.info("north " + north.getBounds().toString());
		logger.info("east  " + east.getBounds().toString());
		logger.info("south " + south.getBounds().toString());
		logger.info("west  " + west.getBounds().toString());
	}

	private class ShutdownKey implements ActionListener {

		int scriptIndex;

		ShutdownKey(int scriptIndex) {
			this.scriptIndex = scriptIndex;
		}

		public void actionPerformed(ActionEvent e) {
			logger.warn("SHUTDOWN !");

			buttonShutdown.setForeground(Color.white);
			buttonShutdown.setBackground(Color.red);

			ExecScript script = new ExecScript();
			script.engage(scriptIndex);

			System.exit(0);
		}
	}

	private class NumKey implements ActionListener {

		String value;

		public NumKey(String v) {
			value = v;
		}

		public void actionPerformed(ActionEvent e) {
			inputLine.append(value);

			if (inputLine.length() < maxChars) {

				if (currentPrompt.equals(passPrompt)) {

					StringBuffer tmp = new StringBuffer(inputLine.toString());
					for (int i = 0; i < tmp.length(); i++)
						tmp.setCharAt(i, '*');
					inputField.setText(
						Format.print(
							currentPrompt,
							tmp.toString(),
							" ",
							inputField.getColumns()));
				} else {
					inputField.setText(
						Format.print(
							currentPrompt,
							inputLine.toString(),
							" ",
							inputField.getColumns()));
				}
			} else {
			}
		}
	}

	private class EnterKey implements ActionListener {


		int value;

		public EnterKey() {
		}

		public void actionPerformed(ActionEvent e) {

			if (user == null) {
				user = new String(inputLine);
				currentPrompt = passPrompt;
				inputField.setText(currentPrompt);
			} else {
				pass = new String(inputLine);
				try {
					userNo = new Integer(user).intValue();
					passNo = new Integer(pass).intValue();
				} catch (NumberFormatException format_err) {
					logger.warn("Number conversion error in LogonPanel");
					logger.warn(format_err.toString());
					return;
				}

				if (getLogon()) {

					try {
						login();

						// restore state
						(new ClearKey()).actionPerformed(null);

					} catch (Exception ex) {
						logger.fatal("Failed to create context", ex);
					}
				} else {

					user = null;
					pass = null;

					if (++retryCount < retryLimit) {
						currentPrompt = userPrompt;
						inputField.setText(currentPrompt);
					} else {
					}
				}
			}
			inputLine = new StringBuffer();
		}

	}

	private class ClearKey implements ActionListener {

		public ClearKey() {
		}

		public void actionPerformed(ActionEvent e) {

			user = null;
			pass = null;
			currentPrompt = userPrompt;
			inputField.setText(currentPrompt);

			if (inputLine.length() > 0) {
				inputLine.delete(0, inputLine.length());
				inputField.setText(currentPrompt);
			}
		}
	}

	/**
	 * Check the logon
	 */
	private boolean getLogon() {

		String fetchSpec = Employee.getByLogonNo(userNo);
		Vector v = Application.dbConnection().fetch(new Employee(), fetchSpec);

		if (v.size() == 1) {

			emp = (Employee) v.elementAt(0);

			if (emp.logonPass() == passNo)
				return true;
			else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void logout (){
		
		// be ready for the next input
		try {
			scanner.clearInput();
		} catch (JposException e) {
			logger.error("", e);
		}
	}
	
	private void login() {
		Integer empid = new Integer(emp.employeeID());
			
		PosContext ctx = (PosContext)contexts.get (empid);
			
		// is it dead already?
		if ( ctx != null && ctx.tabIndex() < 0){
			contexts.remove(empid);
			ctx = null;
		}
		
		if ( ctx == null ){
			// create new context 
			Component root = SwingUtilities.getRoot((Component)this);
			if (root != null && root.isShowing())
				root.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			int tmpConfig =	(emp.posProfile().configNo() != 0)
					? emp.posProfile().configNo()
					: configNo;
			PosContext context =
				new PosContext(siteID, posID, posNo, tmpConfig);

			context.pushEmployee(emp);

			if (root != null && root.isShowing())
				root.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			parent.addLogon(parent.indexOfComponent(thisPanel), context);
			// save context into hash
			contexts.put(empid, context);
		}else {
			// focus employee context
			parent.focusContext(ctx);
		}
	}

	private class LogonKeyListener extends java.awt.event.KeyAdapter {
		PosTabs parent;
		String logonTitle;
		int script;
		int shutdownKey;

		public LogonKeyListener(
			PosTabs parent,
			int shutdownScriptIndex,
			int shutdownKey) {
			this.parent = parent;
			this.script = shutdownScriptIndex;
			this.shutdownKey = shutdownKey;
		}
		/**
		 * Only listens to the KeyPressed event when logon tab is active
		 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
		 */
		public void keyPressed(KeyEvent e) {
			// PosTabls.defaultTabLabel is Logon Tab Title
			if (parent
				.defaultTabLabel
				.equals(parent.getTitleAt(parent.getSelectedIndex()))) {
				if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
					(new NumKey(e.getKeyChar() + "")).actionPerformed(null);
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					(new EnterKey()).actionPerformed(null);
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					(new ClearKey()).actionPerformed(null);
				} else if (e.getKeyCode() == shutdownKey) {
					(new ShutdownKey(script)).actionPerformed(null);
				}
			}
		}
	}

	private class EmpScanner extends Scanner {
		Pattern pattern;
		
		public EmpScanner(jpos.Scanner c, String devicename, ContextSet set, String patternStr) {
			super(c, devicename, set);
			pattern = Pattern.compile (patternStr);
		}

		public void process() {
			
			String data = new String(getScanData());
			Matcher matcher = pattern.matcher(data);
			
			if (matcher.find()){
				logger.debug("key: [" + data + "]" + data.length());
				String fetchSpec = Employee.getByKey(data);
				Vector v =
					Application.dbConnection().fetch(new Employee(), fetchSpec);

				if (v.size() == 1) {

					emp = (Employee) v.elementAt(0);

					logger.debug("Found employee " + emp.employeeID());
				
					login();

				} else {
					logger.debug ("Employee not found");			
				}
			}else{
				super.process();
			}
		}
	}
}
