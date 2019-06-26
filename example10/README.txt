example10
=============

Dependencies, Unittests, Integrationtests, Code coverage ... and modules.
Oh my!
Don't be too afraid by the apparent complexity, this is just example9 split up into pieces.

example10 is an aggregate module.
This means when you build it you also build the modules it contains.

example10 is also the parent module for each of tis submodules.
This means the submodules inherit its properties, dependencies and plugins.

The main module is "example10-cli", which contains the application.
It uses the assembly plugin just like example4 to create an application zip.

The dependencies go as follows:
example10-cli == compile ==> example10-api
example10-cli == runtime ==> example10-json
example10-cli == runtime(transitive) ==> example10-core
example10-cli == runtime(transitive) ==> json

example10-json provides an ExpressionParser as a service
example10-cli uses an ExpressionParser service at runtime

|-- README.txt
|-- pom.xml
|-- example10-api
|   |-- pom.xml
|   `-- src
|       `-- main
|           `-- java
|               `-- com
|                   `-- mycompany
|                       `-- example10
|                           `-- api
|                               |-- Expression.java
|                               |-- ExpressionParser.java
|                               `-- ParsingFailedException.java
|-- example10-cli
|   |-- pom.xml
|   `-- src
|       |-- assembly
|       |   `-- application.xml
|       `-- main
|           |-- bin
|           |   |-- example10.bat
|           |   `-- example10.sh
|           `-- java
|               `-- com
|                   `-- mycompany
|                       `-- example10
|                           `-- cli
|                               `-- App.java
|-- example10-core
|   |-- pom.xml
|   `-- src
|       |-- main
|       |   `-- java
|       |       `-- com
|       |           `-- mycompany
|       |               `-- example10
|       |                   `-- core
|       |                       |-- ConstantExpression.java
|       |                       |-- Operation.java
|       |                       `-- OperationExpression.java
|       `-- test
|           `-- java
|               `-- com
|                   `-- mycompany
|                       `-- example10
|                           `-- core
|                               `-- OperationTest.java
|-- example10-json
|   |-- pom.xml
|   `-- src
|       |-- main
|       |   |-- java
|       |   |   `-- com
|       |   |       `-- mycompany
|       |   |           `-- example10
|       |   |               `-- json
|       |   |                   `-- JsonExpressionParser.java
|       |   `-- resources
|       |       `-- META-INF
|       |           `-- services
|       |               `-- com.mycompany.example10.api.ExpressionParser
|       `-- test
|           `-- java
|               `-- com
|                   `-- mycompany
|                       `-- example10
|                           `-- json
|                               `-- ExpressionParserIT.java


mvn package -pl :example10-cli -am -Djacoco.skip

Options:
 -am,--also-make                        If project list is specified, also
                                        build projects required by the
                                        list
 -pl,--projects <arg>                   Comma-delimited list of specified
                                        reactor projects to build instead
                                        of all projects. A project can be
                                        specified by [groupId]:artifactId
                                        or by its relative path

jar xf example10\example10-cli\target\example10-cli-1.0.0-application.zip
example10-cli-1.0.0\example10.bat
rmdir /q /s example10-cli-1.0.0

jar -tf example10\example10-cli\target\example10-cli-1.0.0-application.zip

jar xf example10/example10-cli/target/example10-cli-1.0.0-application.zip
cd example10-cli-1.0.0
chmod +x example10.sh
./example10.sh
cd -
rm -rf example10-cli-1.0.0

jar -tf example10/example10-cli/target/example10-cli-1.0.0-application.zip

$ ./example10.sh  "{\"a\":1,\"operation\":\"ADD\",\"b\":1}"
Hello World!
args.length = 1
input = {"a":1,"operation":"ADD","b":1}
output = 2.0
