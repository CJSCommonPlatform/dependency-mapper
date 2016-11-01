#!/bin/bash

if [ "$#" -ne 2 ]; then
  echo "Unexpected number of arguments"
  echo "     usage: ramlGen [sourcePath] [targetPath]"
  exit 1;
fi

function GenerateFile() {
  dirName=$(dirname $2)
  fileName=$(basename $2)
  newFileName=${fileName/".raml"/".html"}

  if grep -q "message:\/\/" $2 ; then
    #if the raml uses the message protocol it will be parsed as invalid by raml2html. we should replace this and add a warning
    mkdir "$1/$fileName.temp/"
    cp -a "$dirName/." "$1/$fileName.temp/"
    sed -i.bak s/"message:\/\/"/"http:\/\/"/g "$1/$fileName.temp/$fileName"
    raml2html "$1/$fileName.temp/$fileName" > "$1/$newFileName" 2> "$1/$newFileName.error"
    sed -i.bak s/"<\/h1>"/"<\/h1><div class=\"alert alert-warning\">Due to technical limitations, this is listed as a HTTP endpoint\. Please note that this is as an asynchronous endpoint that uses the 'message:\/\/' protocol.<\/div>"/g "$1/$newFileName"
    rm -rf "$1/$fileName.temp/"
  else
    raml2html $2 > "$1/$newFileName" 2> "$1/$newFileName.error"
  fi
}

export -f GenerateFile
mkdir -p "$2"
find $1 -name 'cpp.context.*' | xargs -I {} find {} -name '*.raml' | xargs -L1 bash -c 'GenerateFile "$@"' _ $2
find $2 -size  0 -print0 | xargs -0 rm
rm $2/*.bak