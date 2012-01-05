#!/bin/bash
# script to interactively generate a SPEC file
# for building an RPM
#
# William Kennedy
# Tue May 15 15:29:50 CDT 2007
##############################################

if [ -z $1 ]
then
	echo "Usage: $0 package_name"
	echo "This utility will create an RPM spec file used for building a package."
	exit 1
else
    clear
    SPECFILE=$1.spec
    if [ -f $SPECFILE ]
        then rm $SPECFILE
        echo "removed old specfile."
    fi
    echo "Using $1.spec as the spec file for this package."
    touch $1.spec
    echo
fi

tput setf 4 
echo "Enter a statement summarizing the purpose and content of this package."
tput sgr0
echo -en "Summary? "
	read summary
	echo "Summary:	$summary" >> $SPECFILE


tput setf 4 
echo "Enter a name for this package."
tput sgr0
echo -en "Name? "
	read name
	echo "Name:	$name" >> $SPECFILE


tput setf 4 
echo "Please enter a version number of the software in this package." 
tput sgr0
echo -en "Version? "
	read version
	echo "Version:	$version" >> $SPECFILE


tput setf 4 
echo "Please enter a release number for this package. (`date +%Y%m%d`)"
tput sgr0
echo -en "Release? "
	read release
	echo "Release:	$release" >> $SPECFILE

## Legacy Syntax - unsupported.
#tput setf 4 
#echo "Please enter a copyright type for this package. Suggested types are 'Commercial', 'Proprietary'"
#tput sgr0
#echo -en "Copyright? "
#	read copyright
#	echo "Copyright:	$copyright" >> $SPECFILE


tput setf 3 
echo "Enter a program group for this applcation"
echo "Select one of the following:"
echo "-------------------------------------------------"
tput setf 8
echo -e "Amusements/Games\n\
Amusements/Graphics\n\
Applications/Archiving\n\
Applications/Communications\n\
Applications/Databases\n\
Applications/Editors\n\
Applications/Emulators\n\
Applications/Engineering\n\
Applications/File\n\
Applications/Internet\n\
Applications/Multimedia\n\
Applications/Productivity\n\
Applications/Publishing\n\
Applications/System\n\
Applications/Text\n\
Development/Debuggers\n\
Development/Languages\n\
Development/Libraries\n\
Development/System\n\
Development/Tools\n\
Documentation\n\
System Environment/Base\n\
System Environment/Daemons\n\
System Environment/Kernel\n\
System Environment/Libraries\n\
System Environment/Shells\n\
User Interface/Desktops\n\
User Interface/X\n\
User Interface/X Hardware Support\n"
echo
tput sgr0
echo -en "Group? "
	read group
	echo "Group:	$group" >> $SPECFILE


# Select source code location 
# Disabled - openSUSE 11.1 grumbles..
#dialog --title "Please select the tar ball containing the source code." --fselect / 14 48 
#echo "%source" >> $SPECFILE
#echo "$2" >> $SPECFILE
## command line version
#echo "Enter a statement summarizing the purpose and content of this package."
#tput sgr0
#echo -en "Source? "
#	read source
#	echo "Source:	$source" >> $SPECFILE

select_patch(){
# Select patch file location
num = 0
dialog --title "Please select a file containing a patch for this source release." --fselect / 14 48 
echo "%patch-$num" >> $SPECFILE
echo "$2" >> $SPECFILE
dialog --yesno "Select another patch file?" 0 0
if [ "$2" = "1" ]
    then
    num = $num + 1
    select_patch
fi
}
# call patch selection loop
#Disabled - we're not building from source right now..
#select_patch

echo -e "BuildRoot: /var/tmp/%{name}-buildroot" >> $SPECFILE

GetMacros(){
# Not called
# Description macro section
dialog --title "Select a macro for the package description." --fselect / 14 48 
echo "%description" >> $SPECFILE
echo "$2" >> $SPECFILE

# Preparation macro section
dialog --title "Select a Preparation macro for the package." --fselect / 14 48 
echo "%prep" >> $SPECFILE
echo "$2" >> $SPECFILE
echo "%setup -q" >> $SPECFILE
dialog --title "Select an Application Setup macro for the package." --fselect / 14 48 
echo "$2" >> $SPECFILE
dialog --title "Select a Patch macro for the package." --fselect / 14 48 
echo "%patch -p1 -b .buildroot" >> $SPECFILE
echo "$2" >> $SPECFILE
}
# Build macro section
echo "%build" >> $SPECFILE
echo "make RPM_OPT_FLAGS=\"$RPM_OPT_FLAGS\"" >> $SPECFILE

# Installation macro section
echo "Generating Macros."
echo "%install" >> $SPECFILE
echo "rm -rf $RPM_BUILD_ROOT" >> $SPECFILE
echo "mkdir -p $RPM_BUILD_ROOT/usr/bin" >> $SPECFILE
echo "mkdir -p $RPM_BUILD_ROOT/usr/man/man1" >> $SPECFILE

# Cleanup macro section
echo "%clean" >> $SPECFILE
echo "rm -rf $RPM_BUILD_ROOT" >> $SPECFILE

# Actual files macro section
echo "%files" >> $SPECFILE
echo -e "%defattr(-,root,root)" >> $SPECFILE
echo "%doc README TODO COPYING ChangeLog" >> $SPECFILE

echo "/usr/bin/$1" >> $SPECFILE
tput setf 4
echo "Are you including man pages with this package? (Y,y/N,n)"
tput sgr0
read manpages
    case $manpages in
        [y,Y])
        echo "/usr/man/man1/$1.1" >> $SPECFILE
        echo "install -s -m 755 $1 $RPM_BUILD_ROOT/usr/bin/$1" >> $SPECFILE
        echo "install -m 644 $1.1 $RPM_BUILD_ROOT/usr/man/man1/$1.1" >> $SPECFILE
        ;;
    esac       

# Changelog
echo "%changelog" >> $SPECFILE

####################################
echo "Spec file for $1 is generated."
tput setf 4
echo "Ready to build RPM file? (Y,y/N,n)"
read build
    case $build in
        [y,Y])
        tput setf 4
        echo "Building binary-only RPM of $1..."
        echo "Checking spec:"
        rpmbuild --nobuild $1.spec &2>&1
            if [ $? = "0" ]
            then
            echo "Checking file list:"
            rpmbuild -bl $1.spec &2>&1
                if [ $? = "1" ]
                then
                    tput setf 2
                    echo "Your file list checks out OK."
                    echo "Ready to build RPM."
                    tput sgr0
                else
                    tput setf 4
                    echo "Your file list contains errors."
                    echo "Please verify the accuracy of your files and re-run this utility."
                    tput sgr0
                fi
            else
            tput setf 4
            echo "There is a problem with your spec file.  Please check and re-generate."
            fi
        tput sgr0
        ;;
        [n,N])
        echo "Run rpmbuild at a later time to build your package."
        tput sgr0
        ;;
    esac
exit 0
