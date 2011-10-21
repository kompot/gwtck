#!/bin/bash

# This script updates CKEditor sources to the required version.
# 
# It checks out version CKEDITOR_VERSION to an empty directory, applies patch
# by comparing fresh checkout to CKEDITOR_MODIFIED directory contents
# (in case you want to make modifications to CKEditor sources)
# and packages/minifies CKEditor for production use.
#
# Feel free to put modified `ckeditor.pack` to appropriate place
# in CKEDITOR_MODIFIED to get rid of unwanted plugins/languages/skins
# in the minified version.
#
# See
# http://docs.cksource.com/CKEditor_3.x/Developers_Guide/CKPackager
# for more info.

JAVA_HOME="/usr/lib/jvm/java-6-sun"
CKEDITOR_TRUNK="ckeditor-trunk"
CKEDITOR_RELEASE="ckeditor-release"
CKEDITOR_MODIFIED="ckeditor-modified"
GWT_CKEDITOR_PUBLIC="src/main/resources/com/gmail/kompotik/gwtck/public/ckeditor/"
CKEDITOR_VERSION="3.6.2"

rm -rf $CKEDITOR_TRUNK
rm -rf $CKEDITOR_RELEASE
rm -rf $GWT_CKEDITOR_PUBLIC
mkdir -p $GWT_CKEDITOR_PUBLIC
svn export http://svn.fckeditor.net/CKEditor/tags/$CKEDITOR_VERSION/ $CKEDITOR_TRUNK
diff -cr $CKEDITOR_TRUNK $CKEDITOR_MODIFIED > modifications.patch
patch -p0 -i modifications.patch
rm modifications.patch
#$JAVA_HOME/bin/java -jar $CKEDITOR_TRUNK/_dev/packager/ckpackager/ckpackager.jar $CKEDITOR_TRUNK/ckeditor.pack
$JAVA_HOME/bin/java -jar $CKEDITOR_TRUNK/_dev/releaser/ckreleaser/ckreleaser.jar $CKEDITOR_TRUNK/_dev/releaser/ckreleaser.release $CKEDITOR_TRUNK/ ./$CKEDITOR_RELEASE/ "trunk" ckeditor_trunk --run-before-release=$LANGTOOL
#cp $CKEDITOR_RELEASE/release/ckeditor_source.js $CKEDITOR_RELEASE/release/ckeditor.js $CKEDITOR_RELEASE/release/ckeditor_basic.js $CKEDITOR_RELEASE/release/config.js $GWT_CKEDITOR_PUBLIC/
cp $CKEDITOR_RELEASE/release/ckeditor.js $CKEDITOR_RELEASE/release/ckeditor_basic.js $CKEDITOR_RELEASE/release/config.js  $GWT_CKEDITOR_PUBLIC/
#cp -r $CKEDITOR_RELEASE/release/source $CKEDITOR_RELEASE/release/plugins  $CKEDITOR_RELEASE/release/skins $CKEDITOR_RELEASE/release/lang $GWT_CKEDITOR_PUBLIC/
cp -r $CKEDITOR_RELEASE/release/plugins  $CKEDITOR_RELEASE/release/skins $CKEDITOR_RELEASE/release/lang $GWT_CKEDITOR_PUBLIC/
