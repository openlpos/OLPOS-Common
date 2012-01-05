#!/bin/sh

#Assumes that install dir is current directory
JCL_INSTALL_DIR=.

#Setup JCL_CLASSPATH with jpos15.jar (this includes the content of jcl.jar) and jcl_editor.jar files
JCL_CLASSPATH=$JCL_INSTALL_DIR/lib/jpos15.jar:$JCL_INSTALL_DIR/lib/jcl_editor.jar

#Extends the CLASSPATH with the JCL_CLASSPATH
export CLASSAPTH=$JCL_CLASSPATH:$CLASSPATH

#Run the JCL editor
/opt/jdk1.6.0_04/bin/java jpos.config.simple.editor.JposEntryEditor&
