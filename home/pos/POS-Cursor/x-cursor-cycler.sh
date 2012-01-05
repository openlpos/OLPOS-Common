#!/bin/bash
#
# OpenLPOS POS Client Cursor installer
# Suitable for touch displays
#
#######################################
for i in *.xbm 
do
xmessage "Setting X cursor to $i"
xsetroot -cursor $i $i
sleep 3
done
exit 0
