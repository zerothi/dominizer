#!/bin/bash

# This file will make the relase for the Dominizer Application.

if [ "$#" -eq "0" ]; then
	echo "You have not supplied the version number, please do."
	exit 1
fi
export JAVA_HOME=~/.jdk1.6.0_23

ant -f dominizer.xml clean
ant -f dominizer.xml compileAllNonAndroid
echo "Done compiling Non-Android devices"
zip -9 DominizerJVM$1.zip dist/*.jad dist/*.jar
echo "Done zipping Non-Android devices"
ant -f dominizer.xml clean
ant -f dominizer.xml compileAndroid
echo "Done compiling Android devices"
zip -9 DominizerAndroid$1.zip dist/*.apk dist/*.jad dist/*.ap_
echo "Done zipping Android devices"

echo "Done with releasestep"

