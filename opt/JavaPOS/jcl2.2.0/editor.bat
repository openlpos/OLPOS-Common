@echo off

rem Assumes that install dir is current directory
set JCL_INSTALL_DIR=.

rem Setup JCL_CLASSPATH with jpos15.jar (this includes the content of jcl.jar) and jcl_editor.jar files
set JCL_CLASSPATH=%JCL_INSTALL_DIR%\lib\jpos15.jar;%JCL_INSTALL_DIR%\lib\jcl_editor.jar;%CLASSPATH%

rem Run the JCL editor
java -cp %JCL_CLASSPATH% jpos.config.simple.editor.JposEntryEditor
