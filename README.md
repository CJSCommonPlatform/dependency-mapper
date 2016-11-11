# Dependency Mapper [![Build Status](https://travis-ci.org/CJSCommonPlatform/dependency-mapper.svg?branch=master)](https://github.com/CJSCommonPlatform/dependency-mapper)

The Dependency Mapper project is made up of two separate parts:

 * The Java back-end to generate the json
 * The Java back-end to generate the RAML documents as an HTML.

(These technologies were chosen based on the current needs of the system)

## Java back-end (JSON Build Map)
TBA

## Java back-end command line override configuration

| Property | Default | Description | Example Override |
| --- | --- | --- | --- |
| dmx.contexts.dir | `/opt/contexts` | Directory location where pom files will be read from | -Ddmx.contexts.dir=/opt/dmx/ |
| dmx.contexts.map.file | `/opt/contexts.json` | Directory location AND file name where generated file will be published | -Ddmx.contexts.map.file=/opt/dmx/contexts1.json |
| dmx.raml.reports.dir | `/opt/raml-reports/` | RAML report dir | -Ddmx.raml.reports.dir=/opt/any/ |

### How to use the Java back-end

`java -jar dependency-mapper-latest.jar`

Run using overrides:
`java -Ddmx.contexts.dir=/opt/dmx/ -Ddmx.raml.reports.dir=/opt/any/ -Ddmx.contexts.map.file=/opt/dmx/contexts1.json -jar target/dependency-mapper.jar`
