# Dependency Mapper [![Build Status](https://travis-ci.org/CJSCommonPlatform/dependency-mapper.svg?branch=master)](https://github.com/CJSCommonPlatform/dependency-mapper)

The Dependency Mapper project is made up of two separate parts:

 * The Java back-end to generate the json
 * The shell script to generate the RAML documents as an HTML.

(These technologies were chosen based on the current needs of the system - especially the shell script may mature to need something more robust.)

## Java back-end (JSON Build Map)

### How to use the Java back-end

 Set system properties:

 * rootDirectory: Directory location where pom files will be read from default is `/opt`
 * outputFilePath: Directory location AND file name where generated file will be published, default is `/opt/contexts.json`
 
 e.g.: `java -DrootDirectory=/opt/dmx/ -DoutputFilePath=/opt/dmx/contexts1.json -jar target/dependency-mapper.jar
`

## Shell Script (HTML documentation)

### How to use the Shell script

 The shell script is located in ./ramlGenScript and can be executed from any \*nix command line. It takes two parameters, which in order are:

 * sourcePath: Directory location where we will start our recursive search for \*.raml files
 * targetPath: Output location where we will output paths.

 Both these parameters are compulsory.

 e.g.: `./ramlGen.sh /opt/dmx /opt/ramlDocs`

### Common issues

 * The executing user does not have read access to the sourcePath
 * The executing user does not have write access to the targetPath
 * The dependency mapper project is in the sourcePath (This will cause the script to output an error due to the inclusion of invalid RAML as a negative unit case.)
 * There is a \*.html.error file in the targetPath. This is most often caused by a RAML schema violation in one of the contexts.