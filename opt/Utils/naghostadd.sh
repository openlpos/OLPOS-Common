#!/bin/bash
# nagios host file maker
#########################


DATE=`date +%Y%m%d`


add_host(){
host_name=$1
alias=$2
address=$3
	echo "
	define host{
        	use                     generic-host            ; Name of host template to use
	        host_name               $1 
	        alias                   $2
	        address                 $3 
	        check_command           check-host-alive
	        max_check_attempts      20
	        notification_interval   60
	        notification_period     24x7
	        notification_options    d,u,r
	        }" >> host.$DATE.cfg


echo "Host: $1 aliased as $2 with IP $3 added to host.$DATE.cfg."
}



exit 0
