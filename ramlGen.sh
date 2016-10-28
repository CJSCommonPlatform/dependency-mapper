#!/bin/sh

function MungeFile() {

  dirName=$(dirname $1)
  fileName=$(basename $1)
  newFileName=${fileName/".raml"/".html"}

  if grep -q "message:\/\/" $1 ; then
    mkdir "ramlDocs/$fileName.temp/"
    cp -a "$dirName/." "ramlDocs/$fileName.temp/"
    sed -i.bak s/"message:\/\/"/"http:\/\/"/g "ramlDocs/$fileName.temp/$fileName"
    GenerateFile "ramlDocs/$fileName.temp/$fileName" "ramlDocs/$newFileName"
    sed -i.bak s/"<\/h1>"/"<\/h1><div class=\"alert alert-warning\">Due to technical limitations, this is listed as a HTTP endpoint\. Please note that this is as an asynchronous endpoint that uses the 'message:\/\/' protocol.<\/div>"/g "ramlDocs/$newFileName"
    rm -rf "ramlDocs/$fileName.temp/"
  else
    GenerateFile $1 "ramlDocs/$newFileName"
  fi

}

function GenerateFile() {
  raml2html $1 > $2 2> "$2.error"
}

export -f MungeFile
export -f GenerateFile

mkdir ramlDocs

find . -name *.raml | xargs -L1 bash -c 'MungeFile "$@"' _
find ramlDocs/ -size  0 -print0 |xargs -0 rm
rm ramlDocs/*.bak
