example5
=============

Executable jar with a dependency.
One dependency.

We add maven-assembly-plugin to our pom to create an uber-jar with Main-Class.
The jar-with-dependencies is quite large.
Some licenses don't allow us to do this.

|   pom.xml
|   README.txt
|
+---src
|   \---main
|       \---java
|           \---com
|               \---mycompany
|                   \---example5
|                           App.java
|
\---target
        example5-1.0.0-jar-with-dependencies.jar
        example5-1.0.0.jar

mvn clean package

mvn dependency:list
... org.json:json:jar:20170516:compile

java -jar target\example5-1.0.0-jar-with-dependencies.jar
jar xf target\example5-1.0.0-jar-with-dependencies.jar META-INF/MANIFEST.MF
type META-INF\MANIFEST.MF
rmdir /q /s META-INF
jar -tf target\example5-1.0.0-jar-with-dependencies.jar

