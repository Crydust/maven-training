example9
=============

Unittests which are run by the surefire plugin.
Integrationtest which are run by the failsafe plugin.
Code coverage is determined by the jacoco plugin.
Jacoco slows the build down even more.

|-- README.txt
|-- pom.xml
|-- src
|   |-- main
|   |   `-- java
|   |       `-- com
|   |           `-- mycompany
|   |               `-- example9
|   |                   |-- App.java
|   |                   `-- calculator
|   |                       |-- ConstantExpression.java
|   |                       |-- Expression.java
|   |                       |-- ExpressionParser.java
|   |                       |-- Operation.java
|   |                       `-- OperationExpression.java
|   `-- test
|       `-- java
|           `-- com
|               `-- mycompany
|                   `-- example9
|                       `-- calculator
|                           |-- ExpressionParserIT.java
|                           `-- OperationTest.java

mvn clean verify site
open target/site/index.html

Disable running tests (and integration tests) with -DskipTests
Disable running integration tests only with -DskipITs
Disable running and compiling tests with -Dmaven.test.skip=true
Disable jacoco with -Djacoco.skip
Use as many build threads as you have cpus with -T 1C

So if you want to skip everything:
mvn clean package -Dmaven.test.skip=true -Djacoco.skip -T 1C -o
