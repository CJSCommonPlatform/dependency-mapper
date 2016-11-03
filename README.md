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
 * ramlReportDir: RAML report dir, default is `/opt/raml-reports/`

 e.g.: `java -DramlReportDir=/opt/any/ -DrootDirectory=/opt/dmx -DoutputFilePath=/opt/dmx/contexts1.json -jar target/dependency-mapper.jar
`
