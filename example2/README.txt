example2
=============

Executable jar.
No dependencies.
We add the maven-jar-plugin to our pom to add Main-Class in MANIFEST.MF.

|   pom.xml
|   README.txt
|
+---src
|   \---main
|       \---java
|           \---com
|               \---mycompany
|                   \---example2
|                           App.java
|
\---target
        example2-1.0.0.jar


mvn clean package

java -jar target\example2-1.0.0.jar

jar xf target\example2-1.0.0.jar META-INF/MANIFEST.MF
type META-INF\MANIFEST.MF
rmdir /q /s META-INF
