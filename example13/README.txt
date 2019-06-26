example13
=============

System tests (aka acceptance tests) for example12 with selenium.

mvn verify
Will start tomcat on port 8080 and then use htmlunit to browse http://localhost:8080/example12/.

|-- README.txt
|-- pom.xml
`-- src
    `-- test
        |-- java
        |   `-- com
        |       `-- mycompany
        |           `-- example13
        |               |-- HelloServletIT.java
        |               |-- IndexPageIT.java
        |               |-- TodoJspServletIT.java
        |               |-- boilerplate
        |               |   `-- BrowserResource.java
        |               `-- model
        |                   |-- HelloServletPage.java
        |                   |-- IndexPage.java
        |                   `-- TodoJspServletPage.java
        `-- resources
            `-- example13.properties

mvn verify -pl :example13 -am