#!/bin/sh

mkdir -p build
cd build

CP="."
for f in ../jar/*.jar; do
    CP="$f:$CP";
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

