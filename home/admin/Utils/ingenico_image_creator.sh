#!/bin/bash
#
# zenity wrapper for imagemagick (convert)
# to create PCX-formatted images for Ingenico
# en-Touch 1000 PIN Pad.
#
# W.J. Kennedy
# <wjkennedy@openlpos.org>
#############################################

zenity --info --text "You are about to create a PCX image for use on\n Ingenico (TM) en-Touch hardware."


# file selection routine
select_file(){
zenity --file-selection --text "Select a file to convert" --title "Select a file to convert" > file_selection.$$
if [ $? = "1" ]
  then
  zenity --warning --text "Quitting."
  exit 1
fi

SELECTED_FILE=`cat file_selection.$$`
# file conversion routine
convert_file(){
convert -scale 320x240 -monochrome $SELECTED_FILE $SELECTED_FILE.pcx
        zenity --info --text "Your image is converted.  You can find it as $SELECTED_FILE.pcx in the original directory."
        display $SELECTED_FILE.pcx &
        zenity --question --text "Convert another image?"
            if [ $? = "0" ]
            then
            select_file
	    else
        rm -rf file_selection.*
	    exit 0
	    fi
}
    zenity --info --text "You selected $SELECTED_FILE.  Is this correct?"
    if [ $? = "0" ]
        then
        convert_file
            else
            rm -rf file_selection.*
            exit 0
            fi
}

select_file
