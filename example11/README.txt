example11
=============

[insert Spinal tap reference here]

Dependencies, Unittests, Integrationtests, Code coverage ... and modules with java 9!
This is just example10, but with java 9 module descriptors.

Disable running tests (and integration tests) with -DskipTests
Disable running integration tests only with -DskipITs
Disable running and compiling tests with -Dmaven.test.skip=true
Disable jacoco with -Djacoco.skip
Use as many build threads as you have cpus with -T 1C

So if you want to skip everything:
mvn clean package -Dmaven.test.skip -Djacoco.skip -T 1C


$ mvn clean verify
[INFO] Total time: 42.514 s

$ mvn clean verify -o -T 1C
[INFO] Total time: 16.798 s (Wall Clock)

$ mvn clean verify -o -T 1C -Djacoco.skip
[INFO] Total time: 5.906 s (Wall Clock)

$ mvn clean package -Dmaven.test.skip -Djacoco.skip -o -T 1C -pl :example11-cli -am
[INFO] Total time: 2.996 s (Wall Clock)

$ MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1 -noverify" mvn package -Djacoco.skip -Dmaven.test.skip -o -T 1C -pl :example11-cli -am
[INFO] Total time: 1.657 s (Wall Clock)

./example11-with-modules.sh "{\"a\":1,\"operation\":\"ADD\",\"b\":1}"
