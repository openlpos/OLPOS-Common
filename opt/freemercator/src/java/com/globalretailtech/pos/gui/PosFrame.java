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
import java.awt.event.*;

import javax.swing.*;
import jpos.JposException;

import org.apache.log4j.Logger;


import com.globalretailtech.util.ShareProperties;
import com.globalretailtech.pos.context.PosContext;
import com.globalretailtech.pos.services.factories.PCKeyboardServiceInstanceFactory;


/**
 * The GUI beginning of a pos. This is created from an
 * Application shell. The shell passes the pos configuration
 * used in further configuration.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class PosFrame extends JFrame implements ContainerListener, KeyListener{
	
	Logger logger = Logger.getLogger (PosFrame.class.getName());
	KeyListener keyListener;

    private static PosTabs tabpane;
    private static PosFrame posFrame;
    private PosContext basecontext;
	private static int xoff = 0;
	private static int yoff = 0;
	private static int width = 1024;
	private static int height = 768;

    public PosContext baseContext() {
        return basecontext;
    }

    public PosTabs tabPane() {
        return tabpane;
    }

    public PosFrame getPosFrame() {
        return posFrame;
    }

    public void setBaseContext(PosContext value) {
        basecontext = value;
    }

    public PosFrame() {

        super("POS");
        posFrame = this;
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());

        p.add(tabpane = new PosTabs(), BorderLayout.CENTER);

        ShareProperties prop = new ShareProperties(this.getClass().getName());


        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        }
        );
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().add(p);
		
		boolean undecorated;
        if (prop.Found()) {
            xoff = new Integer(prop.getProperty("XOffset", "0")).intValue();
            yoff = new Integer(prop.getProperty("YOffset", "0")).intValue();
            width = new Integer(prop.getProperty("Width", "800")).intValue();
			height = new Integer(prop.getProperty("Height", "600")).intValue();
			undecorated = new Boolean(prop.getProperty("Undecorated", "false")).booleanValue();
        } else {
            xoff = 0;
            yoff = 0;
            width = 800;
            height = 600;
            undecorated=false;
        }
		setUndecorated(undecorated);
        setSize(width, height);
        setLocation(xoff, yoff);
        p.validate();


		// load PCKeyboard service 
		PCKeyboardServiceInstanceFactory factory = new PCKeyboardServiceInstanceFactory();
		try {
			keyListener = (KeyListener)factory.createInstance(null, null);
		} catch (JposException e) {
			logger.warn("Can't attach PCKeyboard: "+e.getMessage());
		}
		addKeyAndContainerListenerRecursively(this);

    }

    /**
     * Create a startup pos context and add it to the initial tabpane.
     */
    public void add(int site, int pos, int posno, int config) {

        tabpane.logon(site, pos, posno, config);
    }

    public void sizes() {
        tabpane.sizes();
    }

    public void quit() {
        try {
            finalize();
        } catch (java.lang.Throwable t) {
        }
    }
    
	private void addKeyAndContainerListenerRecursively(Component c)
		 {
//	  To be on the safe side, try to remove KeyListener first just in case it has been added before.
//	  If not, it won't do any harm
			  c.removeKeyListener(keyListener);
//	  Add KeyListener to the Component passed as an argument
			  c.addKeyListener(keyListener);

			  if(c instanceof Container){

//	  Component c is a Container. The following cast is safe.
				   Container cont = (Container)c;

//	  To be on the safe side, try to remove ContainerListener first just in case it has been added before.
//	  If not, it won't do any harm
				   cont.removeContainerListener(this);
//	  Add ContainerListener to the Container.
				   cont.addContainerListener(this);

//	  Get the Container's array of children Components.
				   Component[] children = cont.getComponents();

//	  For every child repeat the above operation.
				   for(int i = 0; i < children.length; i++){
						addKeyAndContainerListenerRecursively(children[i]);
				   }
			  }
		 }
	private void removeKeyAndContainerListenerRecursively(Component c)
	{
		  c.removeKeyListener(keyListener);

		  if(c instanceof Container){

			   Container cont = (Container)c;

			   cont.removeContainerListener(this);

			   Component[] children = cont.getComponents();

			   for(int i = 0; i < children.length; i++){
					removeKeyAndContainerListenerRecursively(children[i]);
			   }
		  }
	 }

		 
//	ContainerListener interface
  /**********************************************************/

//	This function is called whenever a Component or a Container is added to another Container belonging to this Dialog
	   public void componentAdded(ContainerEvent e)
	   {
			addKeyAndContainerListenerRecursively(e.getChild());
	   }

//	This function is called whenever a Component or a Container is removed from another Container belonging to this Dialog
	   public void componentRemoved(ContainerEvent e)
	   {
			removeKeyAndContainerListenerRecursively(e.getChild());
	   }
    
	/**********************************************************/


//	  KeyListener interface
	/**********************************************************/
//	  This function is called whenever a Component belonging to this Dialog (or the Dialog itself) gets the KEY_PRESSED event
		 public void keyPressed(KeyEvent e)
		 {
			  int code = e.getKeyCode();
			  
			   logger.debug ("KeyPressed: "+code+" "+e.getKeyChar());
//	  Insert code to process other keys here
		 }

//	  We need the following 2 functions to complete imlementation of KeyListener
		 public void keyReleased(KeyEvent e)
		 {
		 }

		 public void keyTyped(KeyEvent e)
		 {
		 }


	/**********************************************************/
	public static double xScale (){
		return (double)width/(double)1024;
	}
	
	public static double yScale (){
		return (double)height/(double)768;
	}
}



