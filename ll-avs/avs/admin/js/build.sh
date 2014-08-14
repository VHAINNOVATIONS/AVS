#!/bin/bash

### USER-SUPPLIED PARAMETERS ##################################################

# Template file for the JSBuilder configuration.
JSB_TEMPLATE=build-template.jsb3
JSB_FILE=build.jsb3

# Local URL to the application entry-point URL
APP_ENTRY_URL=http://localhost/checkout/admin/index-dev.html

# Document root of the local web server referenced in APP_ENTRY_URL, above
DOCUMENT_ROOT=~/Sites/VA

###############################################################################
### NO USER-EDITABLE PARAMETERS BEYOND THIS POINT #############################
###############################################################################


### VALIDATE THE USER-SUPPLIED PARAMETERS #####################################

cd $(dirname $0)

if [ ! -e "$JSB_TEMPLATE" ]; then
    echo "Missing JSB_TEMPLATE file $JSB_TEMPLATE. Unable to continue." > /dev/stderr
    exit 1
fi

if [ ! -e "$DOCUMENT_ROOT" ]; then
    echo "DOCUMENT_ROOT $DOCUMENT_ROOT does not exist. Unable to continue." > /dev/stderr
    exit 1
fi

SENCHA=$(which sencha)
if [ ! -e "$SENCHA" ]; then
    echo "Sencha SDK tools do not appear to be installed on this system. Unable to continue." > /dev/stderr
    exit 1
fi

# If we have Curl, try loading the URL first
CURL=$(which curl)
if [ "$CURL" != "" ]; then
	STATUS_CODE=$(curl -sI -w "%{http_code}" $APP_ENTRY_URL -o /dev/null)
	if [ "$STATUS_CODE" != "200" ]; then
		echo "Unable to load APP_ENTRY_URL $APP_ENTRY_URL. Unable to continue." > /dev/stderr
		echo "Is your web server running?  Is the URL correct?" > /dev/stderr
		exit 1
	fi
fi

PERL=$(which perl)
if [ ! -e "$PERL" ]; then
    echo "Perl does not appear to be installed on this system. Unable to continue." > /dev/stderr
    exit 1
fi

### UPDATE THE JSB FILE #######################################################

# Prepare JSB file from template
cp $JSB_TEMPLATE $JSB_FILE || exit 1

# Update the JSB with Ext dynamic dependencies list
OUTPUT=$($SENCHA create jsb -a ${APP_ENTRY_URL//\&/\\\&} -p $JSB_FILE)
if [[ "$OUTPUT" != "" ]]; then
	echo $OUTPUT > /dev/stderr
	exit 1
fi

# Function for determining the relative path from one directory to another.
# Source: https://gist.github.com/1573996
relpath() {
    local from=$1; shift
    from=${from//\/\//\/}
    from=${from%/}
    IFS=/ dirs=(${from#/})
    local to=
    for to; do
        to=${to//\/\//\/}
        to=${to%/}
        local commonPrefix=/ d=
        for d in "${dirs[@]}"; do
            case "$to/" in "$commonPrefix$d/"*) ;;
                *)
                    break
                    ;;
            esac
            commonPrefix+="$d/"
        done
        local ancestor="${from#${commonPrefix%/}}"
        ancestor=${ancestor//[^\/]/}
        ancestor=${ancestor//\//..\/}
        echo "$ancestor${to#$commonPrefix}"
    done
}

# Sencha's tool seems to think it will never be run from anywhere
# else than the folder that the HTML file resides in.  So we have to
# fix relative paths so they are relative to the JSB file itself.
$PERL -pi -e 's/\"path\"\: \"js\//\"path\"\: \"/g' $JSB_FILE || exit 1

# Sencha's tool also seems to assume that the document root of the
# HTTP server is the folder that the HTML file resides in.  So we have
# to fix absolute paths so they are relative to the JSB file.
FROM_DIR=$(pwd)
TO_DIR=$(cd $DOCUMENT_ROOT && pwd)
RELATIVE_PATH=$(relpath $FROM_DIR $TO_DIR);
$PERL -pi -e 's/\"path\"\: \"\//\"path\"\: \"'${RELATIVE_PATH//\//\\/}'\//g' $JSB_FILE || exit 1


### COMPILE JS SCRIPTS ########################################################

# Process the JSB file to compile the various JavaScript includes it specifies
$SENCHA build -p $JSB_FILE -d . -v || exit 1


### CLEAN UP ##################################################################

echo "Removing temp files:"
ls -1 .tmp.*
rm .tmp.* || exit 1
