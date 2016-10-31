# Dependency Mapper [![Build Status](https://travis-ci.org/CJSCommonPlatform/dependency-mapper.svg?branch=master)](https://github.com/CJSCommonPlatform/dependency-mapper)

 Set system properties:
 
 * rootDirectory: Directory location where pom files will be read from default is `/opt`
 * outputFilePath: Directory location AND file name where generated file will be published, default is `/opt/contexts.json`
 
 e.g.: `java -DrootDirectory=/opt/dmx -DoutputFilePath=/opt/dmx/contexts1.json -jar target/dependency-mapper.jar
`