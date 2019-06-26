example7
=============

Unittests which are run by the surefire plugin.
We add junit and hamcrest as test scoped dependencies.

|-- README.txt
|-- pom.xml
|-- src
    |-- main
    |   `-- java
    |       `-- com
    |           `-- mycompany
    |               `-- example7
    |                   |-- App.java
    |                   `-- calculator
    |                       |-- ConstantExpression.java
    |                       |-- Expression.java
    |                       |-- ExpressionParser.java
    |                       |-- Operation.java
    |                       `-- OperationExpression.java
    `-- test
        `-- java
            `-- com
                `-- mycompany
                    `-- example7
                        `-- calculator
                            `-- OperationTest.java

mvn clean test

Disable running tests with -DskipTests
Disable running and compiling tests with -Dmaven.test.skip=true
