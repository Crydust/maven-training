example6
=============

Executable jar with a dependency.
One dependency.

We add maven-shade-plugin to our pom to create a minimal uber-jar with Main-Class.
The shaded is smaller than the jar-with-dependencies.
Some licenses don't allow us to do this.

|   pom.xml
|   README.txt
|
+---src
|   \---main
|       \---java
|           \---com
|               \---mycompany
|                   \---example6
|                           App.java
|
\---target
        example6-1.0.0-shaded.jar
        example6-1.0.0.jar

mvn clean package

mvn dependency:list
... org.json:json:jar:20170516:compile

java -jar target\example6-1.0.0-shaded.jar
jar xf target\example6-1.0.0-shaded.jar META-INF/MANIFEST.MF
type META-INF\MANIFEST.MF
rmdir /q /s META-INF
jar -tf target\example6-1.0.0-shaded.jar
