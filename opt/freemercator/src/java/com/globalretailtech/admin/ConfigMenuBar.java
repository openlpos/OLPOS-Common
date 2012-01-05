package com.globalretailtech.admin;

import com.globalretailtech.util.Application;
import com.globalretailtech.util.xmlservices.XMLExport;
import com.globalretailtech.util.xmlservices.SQLConverter;
import com.globalretailtech.util.xmlservices.SQLDataGenerator;
import com.globalretailtech.data.DBContext;
import com.globalretailtech.admin.settings.SettingsContainer;
import com.globalretailtech.admin.settings.instance.EmployeeAdd;
import com.globalretailtech.admin.settings.instance.ChangeDBSettings;
import com.globalretailtech.admin.reports.instance.GlobalTimesheetReport;

import javax.swing.*;

import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jpos.config.simple.editor.JposEntryEditor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.xml.sax.SAXException;
import org.apache.log4j.Logger;

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

public class ConfigMenuBar extends JMenuBar {

    static Logger logger = Logger.getLogger(ConfigMenuBar.class);

    public ConfigMenuBar() {

        JMenu file = new JMenu("File");

        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic('x');

        file.add(exit);

        this.add(file);

        JMenu db = new JMenu("Database");

        JMenuItem change = new JMenuItem("Change DB Backend...");

        change.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.show(new ChangeDBSettings());
            }
        });

        db.add(change);

        JMenuItem export = new JMenuItem("Export Data XML...");
        export.setMnemonic('x');

        export.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                //throw a file dialog that let's the user pick a suitable directory.

				JFileChooser chooser = new JFileChooser("..");//(System.getProperty("user.dir"));
                chooser.setFileHidingEnabled(true);
                chooser.setMultiSelectionEnabled(false);
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogType(JFileChooser.OPEN_DIALOG);

                int returnVal = chooser.showDialog(ConfigFrame.configFrame, "Export XML");


                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File chosenDir = chooser.getSelectedFile();
                    logger.debug("Chosen file:" + chosenDir.getName() + ":" + chosenDir.isDirectory());
                    if (chosenDir.isDirectory()){
                        XMLExport.exportXML(chosenDir);
                    } else {
                        JOptionPane.showMessageDialog(ConfigFrame.configFrame, "Must choose a directory:" + chosenDir.getName());
                    }
                }
            }
        });

        db.add(export);

        JMenuItem importXML = new JMenuItem("Import Data XML...");

        importXML.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("..");//(System.getProperty("user.dir"));
                chooser.setMultiSelectionEnabled(true);
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setDialogType(JFileChooser.OPEN_DIALOG);

                int returnVal = chooser.showDialog(ConfigFrame.configFrame, "Import XML");


                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] files = chooser.getSelectedFiles();
                    for (int i = 0; i < files.length; i++) {
                        File file = files[i];
                        try {
                            if (file.getName().indexOf(".xml") > 0){
                                SQLConverter conv  = new SQLConverter(new FileReader(file));

                                SQLDataGenerator gen = new SQLDataGenerator(conv);

								Application.dbConnection().execute(gen.generateSQLInserts());

								Application.dbConnection().execute(gen.generateSQLUpdates(Application.dbConnection().getConn()));
                            }

                        } catch (SQLException se){
                            logger.fatal("Caught SQL Exception trying to init SQL Data:" + se, se);
                        } catch (IOException ioe){
                            logger.fatal("Caught IO Exception trying to init SQL Data:" + ioe, ioe);
                        } catch (SAXException ioe){
                            logger.fatal("Caught SAX Exception trying to init SQL Data:" + ioe, ioe);
                        } catch (Exception ioe){
                            logger.fatal("Caught Exception trying to init SQL Data:" + ioe, ioe);
                        }

                    }
                    ConfigFrame.configFrame.reloadTree();
                }
            }
        });

        db.add(importXML);

        JMenuItem clean = new JMenuItem("Clean DB");

        clean.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {

                int choice = JOptionPane.showConfirmDialog(ConfigFrame.configFrame, "Are you sure you want to clean the DB?  It will ERASE all data!");

                if (choice == JOptionPane.OK_OPTION){
                    DBContext conn = Application.dbConnection();
                    for (int i = 0; i < DBContext.tableList.length; i++) {
                        String tableName = DBContext.tableList[i];
                        try {
                            conn.execute("DELETE FROM " + tableName);
                        } catch (SQLException e1) {
                            logger.warn("SQLException trying to clean " + tableName + ":" + e1, e1);
                        }
                    }
                    ConfigFrame.configFrame.reloadTree();
                }
            }
        });

        db.add(clean);

		JMenuItem create = new JMenuItem("Create Schema...");

		create.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ConfigFrame.loadSchema();
				ConfigFrame.configFrame.reloadTree();
			}
		});

		db.add(create);

		JMenuItem drop = new JMenuItem("Drop and Recreate Schema");

		drop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {

				int choice = JOptionPane.showConfirmDialog(ConfigFrame.configFrame, "Are you sure you want to drop the DB Schema?  It will ERASE all data and table definitions!");

				if (choice == JOptionPane.OK_OPTION){
					ConfigFrame.dropSchema();
					ConfigFrame.loadSchema();
					ConfigFrame.configFrame.reloadTree();
				}
			}
		});

		db.add(drop);

        this.add(db);

        JMenu employee = new JMenu("Employees");

        JMenuItem add = new JMenuItem("Add...");

        add.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.show(new EmployeeAdd());
            }
        });

        employee.add(add);

        JMenuItem timesheet = new JMenuItem("Timesheet Report");

        timesheet.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.show(new GlobalTimesheetReport(false));
            }
        });

        employee.add(timesheet);

        this.add(employee);

		JMenu hw = new JMenu("Hardware");

		JMenuItem hwconf = new JMenuItem("Configure...");

		hwconf.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				hwconfig();
			}
		});

		hw.add(hwconf);

		this.add(hw);
    }

	/**
	 * Called when the "Hardware/Configure..." menu item is selected
	 */
	void hwconfig()
	{
		Cursor currentCursor = getCursor();

		setCursor( new Cursor( Cursor.WAIT_CURSOR ) );

		JposEntryEditor.setDefaultFrameCloseOperation( JposEntryEditor.HIDE_ON_CLOSE );
		JposEntryEditor.setFrameVisible( true );

		setCursor( currentCursor );
	}
}
