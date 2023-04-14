# Building Iridium from source

### Prerequisites

You will need.  
 -  [Java 17](https://adoptium.net/)  
 -  [Maven 3.8.4](https://maven.apache.org/) or greater  
 - You will also need to set an `application.properties` file.  Details can be found [here](/for-developers/setting-properties)
 
### Compiling from source

Iridium is with Maven.  From the top level directory you can execute a `mvn clean package` to build all the submodules.
You should see the following if you are successful.
```
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary for iridium 0.0.1:
[INFO] 
[INFO] iridium ............................................ SUCCESS [  0.955 s]
[INFO] iridium-client ..................................... SUCCESS [  6.192 s]
[INFO] iridium-server-base ................................ SUCCESS [  1.577 s]
[INFO] iridium-core-server ................................ SUCCESS [ 15.668 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  29.421 s
[INFO] Finished at: 2023-04-10T15:39:00-05:00
[INFO] ------------------------------------------------------------------------
```

After compiling, the jar that contains the code needed to run iridium can be found at
```
{ProjectRoot}/iridium-core-server/target/iridium-core-server-{version}.jar
```



