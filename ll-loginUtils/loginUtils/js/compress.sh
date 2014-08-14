#!/bin/bash

# Compresses all JavaScript files found in the current directory

cd $(dirname $0)

SENCHA=$(which sencha)

if [ ! -e "$SENCHA" ]; then
	echo "Sencha SDK tools do not appear to be installed.  Unable to continue." > /dev/stderr
	exit 1
fi

for FILE in $(ls *.js); do
	if [[ "${FILE:${#FILE}-9}" != "-debug.js" ]]; then
		continue
	fi

	TARGET=$(basename $FILE -debug.js).js

	echo "$FILE --> $TARGET"

	TMPFILE=".$FILE.jsb3"
	cat > $TMPFILE <<End-Of-Message
	{
		"builds": [{
			"target": "$TARGET",
			"compress": true,
			"options": {"debug":false},
			"files": [{"path":"","name":"$FILE"}]
		}]
	}
End-Of-Message

	# add "-v" for verbose output
	$SENCHA build -p $TMPFILE -d . >/dev/null

	rm $TMPFILE

done
