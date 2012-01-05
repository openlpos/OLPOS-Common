/*
* Copyright (C) 2003 Igor Semenko
* igorsemenko@yahoo.com
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
*/
package net.sf.mercator.daemons;

import java.io.*;
import org.mortbay.util.*;
import org.mortbay.http.*;
import org.mortbay.jetty.Server;

import com.globalretailtech.util.ShareProperties;
 /**
  * Launches embedded Jetty web server as a daemon thread
  *  
  * @author Igor Semenko (igorsemenko@yahoo.com)
  *
  */
public class EmbeddedJetty implements Runnable {

	org.apache.log4j.Logger logger =
		org.apache.log4j.Logger.getLogger(EmbeddedJetty.class.getName());

	private Server server;
	private int port;
	private int securePort;
	
	String keystore;
	String keystoreKeyPassword;
	String keystorePassword;
	String webapp;

	public EmbeddedJetty() {
		ShareProperties props =	new ShareProperties(EmbeddedJetty.class.getName());
		port = Integer.parseInt(props.getProperty("Port", "-1"));
		securePort = Integer.parseInt(props.getProperty("SecurePort", "-1"));
		keystore = props.getProperty("keystore","keystore");
		keystorePassword = props.getProperty("keystorePassword","changeit");
		keystoreKeyPassword = props.getProperty("keystoreKeyPassword","changeit");
		webapp = props.getProperty("webapp","../web");
	}

	/**
		* This method Starts the Jetty server.
		*/
	public void run() {
		//	Create the server
		server = new Server();

		// Create a port listener
		if (port > 0) {
			SocketListener listener = new SocketListener();
			listener.setPort(port);
			server.addListener(listener);
		}
		if (securePort > 0) {
			SunJsseListener listener = new SunJsseListener();
			listener.setPort(securePort);
			listener.setKeystore(keystore);
			listener.setKeyPassword(keystorePassword);
			listener.setPassword(keystoreKeyPassword);
			server.addListener(listener);
		}

		try {
			server.addWebApplication("localhost","/",webapp);
		} catch (IOException e) {
			logger.error("", e);
		}
		
		// Start the http server
		try {
			server.start();
		} catch (MultiException e) {
			logger.error("Can't start jetty", e);
		}
	}


	/**
		* This method Stops the Jetty server.
		*/
	public void stopServer() throws Exception {
		// Stop the embedded server
		server.stop();
	}

}
