example8
=============

Unittests which are run by the surefire plugin.
We add junit and hamcrest as test scoped dependencies.
Integrationtest which are run by the failsafe plugin.
These are not run unless we specifically tell maven to.
We can do this by:
* mvn verify (and add failsafe plugin with executions to pom and this makes all builds slow)
* mvn failsafe:integration-test
* mvn org.apache.maven.plugins:maven-failsafe-plugin:2.20.1:integration-test

|-- README.txt
|-- pom.xml
|-- src
|   |-- main
|   |   `-- java
|   |       `-- com
|   |           `-- mycompany
|   |               `-- example8
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
|                   `-- example8
|                       `-- calculator
|                           |-- ExpressionParserIT.java
|                           `-- OperationTest.java


mvn failsafe:integration-test
mvn org.apache.maven.plugins:maven-failsafe-plugin:2.20.1:integration-test
mvn verify

Disable running tests (and integration tests) with -DskipTests
Disable running integration tests only with -DskipITs
Disable running and compiling tests with -Dmaven.test.skip=true
Disable jacoco with -Djacoco.skip
