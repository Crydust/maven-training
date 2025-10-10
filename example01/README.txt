example01
=============

A minimal project.
No dependencies.
No config for output jar

|   pom.xml
|   README.txt
|
+---src
|   \---main
|       \---java
|           \---com
|               \---mycompany
|                   \---example01
|                           App.java
|
\---target
        example01-1.0.0.jar

mvn clean package

java -cp target\example01-1.0.0.jar com.mycompany.example01.App

jar xf target\example01-1.0.0.jar META-INF/MANIFEST.MF
type META-INF\MANIFEST.MF
rmdir /q /s META-INF

jar xf target/example01-1.0.0.jar META-INF/MANIFEST.MF
cat META-INF/MANIFEST.MF
rm -rf META-INF
