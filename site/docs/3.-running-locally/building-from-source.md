# Building from source

### Prerequisites

You will need.  
 -  [Node 18](https://nodejs.org/en)
 -  [Java 17](https://adoptium.net/)  
 -  [Maven 3.8.4](https://maven.apache.org/) or greater  
 
### Compiling from source

Iridium is with Maven.  From the top level directory you can execute a `mvn clean package` to build all the submodules.
You should see the following if you are successful.
```
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary for iridium {iridium-version}:
[INFO] 
[INFO] iridium ............................................ SUCCESS [  0.979 s]
[INFO] iridium-java-client ................................ SUCCESS [  7.152 s]
[INFO] iridium-core-entity ................................ SUCCESS [  2.365 s]
[INFO] iridium-server-base ................................ SUCCESS [  5.724 s]
[INFO] iridium-core-server ................................ SUCCESS [ 15.957 s]
[INFO] iridium-cli ........................................ SUCCESS [ 11.330 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  44.164 s
[INFO] Finished at: 2023-06-24T20:08:32-05:00
[INFO] ------------------------------------------------------------------------
```

After compiling, the distribution that contains the code needed to run iridium can be found at
```
{ProjectRoot}/iridium-cli/target/iridium-{iridium-version}-bin.tar.gz
```

Currently, the management UI is managed by separate build process.  In future revisions will work to consolidate the 
separate build processes into easier to manage commands.  You can build the management UI by following the commands below.
All commands are assuming you are starting at the top level directory of the Iridium project.

```shell
$ cd iridium-angular-client
$ npm install
$ ng build ngx-iridium-client
$ ng serve
```



