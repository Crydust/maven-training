example1
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
|                   \---example1
|                           App.java
|
\---target
        example1-1.0.0.jar

mvn clean package

java -cp target\example1-1.0.0.jar com.mycompany.example1.App

jar xf target\example1-1.0.0.jar META-INF/MANIFEST.MF
type META-INF\MANIFEST.MF
rmdir /q /s META-INF

jar xf target/example1-1.0.0.jar META-INF/MANIFEST.MF
cat META-INF/MANIFEST.MF
rm -rf META-INF
