#
# lcp.sh	
#		concatenates jar file names in the argument directory
#		to $LOCALCLASSPATH prepending $CLASSPATH to it, if any
#		
# Version:	@(#) freemercator/bin/lcp.sh 1.00 18-Apr-2003
#
# Author:	Piero de Salvia, <piergi@yahoo.com>
#

jarfind()
{

local path=$1

for i in $path/*.jar ; do
  if [ -n "$LOCALCLASSPATH" ] 
  then
   LOCALCLASSPATH=$LOCALCLASSPATH:$i
  else
   LOCALCLASSPATH=$i
  fi
done
}