package com.globalretailtech.admin;

import com.globalretailtech.util.ShareProperties;
import com.globalretailtech.util.Application;
import com.globalretailtech.util.xmlservices.*;
import com.globalretailtech.admin.nav.NavTree;
import com.globalretailtech.admin.settings.SettingsContainer;
import com.globalretailtech.data.DBContext;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

public class ConfigFrame extends JFrame {

    static Logger logger = Logger.getLogger(ConfigFrame.class);

    NavTree navTree;
    SettingsContainer settingsContainer;
    JSplitPane splitPane;

    static ConfigFrame configFrame;

    public ConfigFrame() throws HeadlessException {
        super("Config Utility");

        configFrame = this;

        //before we go getting everything setup, let's test the DB to see if the schema is created
        checkSchema();

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1,1));
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        reloadTree();
        splitPane.setRightComponent(settingsContainer = new SettingsContainer());

        p.add(splitPane, BorderLayout.WEST);

        this.setJMenuBar(new ConfigMenuBar());

        ShareProperties prop = new ShareProperties(this.getClass().getName());

        int xoff;
        int yoff;
        int width;
        int height;

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        }
        );
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().add(p);

        xoff = new Integer(prop.getProperty("XOffset", "0")).intValue();
        yoff = new Integer(prop.getProperty("YOffset", "0")).intValue();
        width = new Integer(prop.getProperty("Width", "800")).intValue();
        height = new Integer(prop.getProperty("Height", "600")).intValue();


        setSize(width, height);
        setLocation(xoff, yoff);
        p.validate();
    }

    public static void checkSchema(){
        try {
            Statement s = Application.dbConnection().getConn().createStatement();
            ResultSet rs = s.executeQuery("SELECT * from pos_key");
        } catch (SQLException e){
            if (e.getMessage().indexOf("does not exist") > -1 || e.getMessage().indexOf("Table not found") > -1){
                //The DB has not been created yet.  Prompt to create it, exit otherwise
                int option = JOptionPane.showConfirmDialog(configFrame, "It looks like the DB is not created yet or was deleted, the Admin Utilty cannot run without the DB configured, create it?" );
                if (option == JOptionPane.OK_OPTION){
                	dropSchema();
                    loadSchema();
                } else {
                    configFrame.quit();
                }
            }
        }
    }

	public static void dropSchema() {
		DBContext conn = Application.dbConnection();
		for (int i = 0; i < DBContext.tableList.length; i++) {
			String tableName = DBContext.tableList[i];
			try {
				conn.execute("drop table " + tableName);
			} catch (SQLException e1) {
				logger.warn("SQLException trying to clean " + tableName + ":" + e1, e1);
			}
		}
	}
	
	public static void loadSchema() {
        JFileChooser chooser = new JFileChooser("../src/sql");//(System.getProperty("user.dir"));
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);

        int returnVal = chooser.showDialog(ConfigFrame.configFrame, "Import Schema XML");

        if(returnVal == JFileChooser.APPROVE_OPTION) {
            //old schema should have been deleted before this

            File[] files = chooser.getSelectedFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                try {
                    if (file.getName().indexOf(".xml") > 0){
                        SQLConverter conv  = new SQLConverter(new FileReader(file));

                        CommonImplSQLConverter gen = null;

                        if (Application.dbConnection().getDriver().indexOf("postgres") != -1) {
                            gen = new PostgresSQLConverter(conv);
                        } else if (Application.dbConnection().getDriver().indexOf("interbase") != -1) {
                            gen = new InterbaseSQLConverter(conv);
                        } else if (Application.dbConnection().getDriver().indexOf("hsqldb") != -1) {
                            gen = new HsqldbSQLConverter(conv);
                        }

                        if (gen != null){
                        	try {
								Application.dbConnection().execute(gen.generateCreationSQL());
                        	}catch (SQLException e){
                        		logger.warn ("Can't execute sql statement",e);
                        	}
                        } else{
                            logger.fatal("Unrecognised DB type, quitting.");
                            configFrame.quit();
                        }
                    }

//                } catch (SQLException se){
//                    logger.fatal("Caught SQL Exception trying to init SQL Schema:" + se, se);
//                    configFrame.quit();
                } catch (IOException ioe){
                    logger.fatal("Caught IO Exception trying to init SQL Schema:" + ioe, ioe);
                    configFrame.quit();
                } catch (SAXException ioe){
                    logger.fatal("Caught SAX Exception trying to init SQL Schema:" + ioe, ioe);
                    configFrame.quit();
                } catch (Exception ioe){
                    logger.fatal("Caught Exception trying to init SQL Schema:" + ioe, ioe);
                    configFrame.quit();
                }
            }
        } else {
            configFrame.quit();
        }
    }

    public void reloadTree(){
        splitPane.setLeftComponent(new JScrollPane(navTree = new NavTree(new DefaultMutableTreeNode("Root"))));
        splitPane.setDividerLocation(200);
        SettingsContainer.closeAll();
    }

    public void quit() {
        try {
            try {
                Application.dbConnection().getConn().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finalize();
        } catch (java.lang.Throwable t) {
        }
        System.exit(0);
    }

    public static ConfigFrame getConfigFrame() {
        return configFrame;
    }
}
