# z-sak

z-sak is a tool specific to [Hopsworks](http://hops.io) allowing editing of Zeppelin interpreter configuration stored in the database.

In Hopsworks we store in the database a big interpreter configuration file as TEXT. This configuration contains properties that are used by the configured Zeppelin interpreters. Periodically we need to change some of the properties for all the Projects, such as Hadoop version.

z-sak can be seen as a framework which provides you a connection to the database and methods to read and update these configurations. A developer can write its own Processor class, which will implement the logic for updating the properties. z-sak will take care of the rest. All you need to do is tell z-sak where to load your Processor class.

### User guide

z-sak provides two interfaces for processors. *Processor* is the basic interface which does not require a database connection. It is meant for dry runs, where your processor will read and write locally. *DbProcessor* on the other hand requires a connection object. *ProcessorAbstr* is an abstract class which provides utility methods for accessing the database and updating with new values.
See the examples at `src/main/java/gr/kzps/processors/impls` To create your own Processor follow the instructions.

#### Create your processor
1) First you need to download z-sak archive which contains the library from [here](https://github.com/kouzant/z-sak/releases/download/v1.0/z-sak-1.0-dist.tar)
2) Use your favorite IDE to create a new project
3) Extract z-sak archive and add z-sak jar as a dependency to your project
4) Write your processor implementing either the *Processor* or *DbProcessor* interface or extending the *ProcessorAbstr* class.
5) Build your project with your IDE or from command line, assuming that your source directory is `src` and z-sak extracted archive in your current working directory, for example
```sh
javac -sourcepath src -cp .:z-sak-1.0-SNAPSHOT/z-sak-1.0-SNAPSHOT.jar src/my/package/MyProcessor.java
```
6) If everything went fine you should have your class file

#### z-sak configuration
z-sak is configurable by a configuration file in `$ZSAK_ROOT/conf/zsak-conf.xml` It is an XML file containing properties for the connection to the database, table and column names and processor class.

| Property | Usage |
| ---------|-------|
|**database**.host| Connection string to the database|
|**database**.port| Port the database is listening to|
|**database**.ssl| If the connection should be over SSL/TLS|
|**database**.user| User of the database|
|**database**.password| Password for the user|
|**zeppelin_table**.name|Name of the Zeppelin interpreter confs table|
|**zeppelin_table**.conf_id_name|Name of the configuration ID column|
|**zeppelin_table**.project_id_name|Name of the project ID column|
|**zeppelin_table**.last_update_name|Name of the last update column|
|**zeppelin_table**.interpreter_conf_name|Name of interpreter configuration column|
|**project_table**.name|Name of the Projects table|
|**project_table**.id_name|Name of the projects ID column|
|**project_table**.project_name_name|Name of the project name column|
|**processors**.processor|The canonical name of the Processor class|

A typical configuration file would look like this
```xml
<?xml version="1.0" encoding="UTF-8"?>
<zsak>
  <database>
    <host>192.168.1.2</host>
    <port>3306</port>
    <ssl>false</ssl>
    <user>userA</user>
    <password>passwordA</password>
  </database>

  <zeppelin_table>
    <name>zeppelin_interpreter_confs</name>
    <conf_id_name>id</conf_id_name>
    <project_id_name>project_id</project_id_name>
    <last_update_name>last_update</last_update_name>
    <interpreter_conf_name>interpreter_conf</interpreter_conf_name>
  </zeppelin_table>

  <!-- Project table name and ID column name -->
  <project_table>
    <name>project</name>
    <id_name>id</id_name>
    <project_name_name>projectname</project_name_name>
  </project_table>

  <!-- Full name of processor class -->
  <processors>
    <processor config-class="my.package.MyProcessor"/>
  </processors>
</zsak>
```

#### Running z-sak with MyProcessor
Now that we have configured z-sak it is time to execute our custom processor. To do that z-sak should be able to find your class file. This can be done by two ways
* Assuming that your class file is at `src/my/package/MyPackage.class`, copy `src/my` directory in $ZSAK_ROOT
* Export the environment variable **ZSAK_CLASSPATH** with the path to your processor

For the purpose of this guide we will go with the second way.
```sh
export ZSAK_CLASSPATH=/home/user/MyUberProcessorProject/src
```
The last step is to execute the run script `$ZSAK_ROOT/scripts/run.sh` z-sak will make a connection to the database, fetch all the Zeppelin configurations, apply your processor, update the database with the new configurations transactionally and perform any necessary cleanup.

### Developer guide
* Project's dependency tree is the following:
```sh
[INFO] --- maven-dependency-plugin:3.0.2:tree (default-cli) @ z-sak ---
[INFO] gr.kzps:z-sak:jar:1.0-SNAPSHOT
[INFO] +- junit:junit:jar:4.12:test
[INFO] |  \- org.hamcrest:hamcrest-core:jar:1.3:test
[INFO] +- org.apache.logging.log4j:log4j-core:jar:2.10.0:compile
[INFO] |  \- org.apache.logging.log4j:log4j-api:jar:2.10.0:compile
[INFO] +- org.apache.commons:commons-configuration2:jar:2.1.1:compile
[INFO] |  +- org.apache.commons:commons-lang3:jar:3.3.2:compile
[INFO] |  \- commons-logging:commons-logging:jar:1.2:compile
[INFO] +- commons-beanutils:commons-beanutils:jar:1.9.3:compile
[INFO] |  \- commons-collections:commons-collections:jar:3.2.2:compile
[INFO] +- commons-io:commons-io:jar:2.6:compile
[INFO] \- mysql:mysql-connector-java:jar:6.0.6:compile
```

* JavaDoc should be available soon HERE (WHERE???)

* To build the distribution archive execute
```sh
mvn clean package assembly:single
```

## License
z-sak software is licensed under Apache version 2.0