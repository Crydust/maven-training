example3
=============

Executable jar with a dependency.
One dependency 
We add maven-dependency-plugin to copy all dependencies in the lib folder.
We add the maven-jar-plugin to our pom to add Main-Class and Class-Path in MANIFEST.MF.

|   pom.xml
|   README.txt
+---src
|   \---main
|       \---java
|           \---com
|               \---mycompany
|                   \---example3
|                           App.java
\---target
    |   example3-1.0.0.jar
    \---lib
            json-20170516.jar

mvn clean package

mvn dependency:list
... org.json:json:jar:20170516:compile

java -jar target\example3-1.0.0.jar

jar xf target\example3-1.0.0.jar META-INF/MANIFEST.MF
type META-INF\MANIFEST.MF
rmdir /q /s META-INF
