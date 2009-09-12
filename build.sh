#!/bin/sh

mkdir -p build
cd build

CP="."
for f in ../library/*.jar; do
    if [ "$f" != "../library/java_anymeta.jar" ]; then
	CP="$f:$CP";
    fi
done

FILES=`find ../src -name "*.java"`

javac -d . -cp $CP $FILES
jar -cf java_anymeta.jar org net
cp java_anymeta.jar ../library
echo
echo "Build done; .jar file put in library/"

if [ "`which hg`" != "" ]; then
  F=java-anymeta-`date +%Y%m%d`.zip
  hg archive -t zip -p java_anymeta $F
  echo "Created $F."
fi

