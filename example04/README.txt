example04
=============

Executable jar with a dependency.
One dependency.

We add maven-assembly-plugin to create an application zip.
This contains our jar and all dependencies.
It is relatively easy for a user to replace a jar with a newer version.
Exactly what most open source projects want you to do.

|   pom.xml
|   README.txt
|
+---src
|   +---assembly
|   |       application.xml
|   |
|   \---main
|       +---bin
|       |       example04.bat
|       |       example04.sh
|       |
|       \---java
|           \---com
|               \---mycompany
|                   \---example04
|                           App.java
|
\---target
        example04-1.0.0-application.zip
        example04-1.0.0.jar

mvn clean package

mvn dependency:list
... org.json:json:jar:20170516:compile

jar xf target\example04-1.0.0-application.zip
example04-1.0.0\example04.bat
rmdir /q /s example04-1.0.0
jar -tf target\example04-1.0.0-application.zip
