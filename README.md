#Automated testing tool
## Description
https://github.com/neueda/homework/tree/master/mindmap
## Requirements
* Java 1.7
* Maven 3.0

## Building project

To build project run `mvn clean package`

### Running default integration tests

To run default integration tests run `mvn clean verify`

### Running  custom integration tests 

To run custom  integration tests with provided mind map file or different host 
<br> run `mvn clean verify -Dmind.map.file=<File> -Dtest.host=<host>`


## Running tool via command line
To run tool via command line
* 1. build project `mvn clean package`
* 2. call `java -jar %projectdir%\target\ testtool-1.0-SNAPSHOT-jar-with-dependencies.jar -f <mindmap file>`
