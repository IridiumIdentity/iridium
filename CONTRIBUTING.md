# Contributing to iridium

Iridium is 100% free and open source. We encourage and support an active, healthy community that accepts contributions from the public – including you!

## <a name="questions"></a> It is okay to raise an issue to ask a question
If you have a question about iridium, feel free to raise an issue for it.

## <a name="helpful"></a> Helpful problem reports look like this
A helpful issue (problem report) is one that saves the maintainer time.
Try to include these elements:

* How to reproduce the problem
* What is the expected behaviour
* What do you actually see

If you can provide code that reproduces the problem, great!
If this code is in the form of a failing unit test, even better!

## <a name="issue_or_pr"></a> Issues or pull requests?
As a rule of thumb, it may be good to raise an issue first before providing a pull request.

It is a good idea to check we all have the same understanding that there actually is a problem to solve, 
and for complex pull requests it may save time when we discuss in advance what shape the solution should take.

That said, for bugfixes and documentation fixes, reporting the issue and providing a pull request to fix it in one PR is perfectly fine.
When in doubt, raise an issue first.


## <a name="pr_conventions"></a> Conventions for pull requests
If there is a corresponding GitHub issue, please mention the issue number in the pull request title.

Ideally prefix commit comments with either the pull request number, or the associated GitHub issue number.


## <a name="java_version"></a> Java version
The project is built with Java 17


## <a name="build"></a> Building

```
git clone git@github.com:IridiumIdentity/iridium.git
cd iridium
mvn clean package
```

Please note if you have trouble with building the project you can use the command 
```
mvn -version

Apache Maven 3.6.0 (97c98ec64a1fdfee7767ce5ffb20918da4f719f3; 2018-10-24T13:41:47-05:00)
Maven home: /usr/local/Cellar/maven/3.6.0/libexec
Java version: 17.0.7, vendor: Homebrew, runtime: /usr/local/Cellar/openjdk@17/17.0.7/libexec/openjdk.jdk/Contents/Home
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "11.4", arch: "x86_64", family: "mac"
```

It should be apparent from the output above that the version command will tell you where maven is picking up your java environment from.  Please remember if you have to upgrade or align your java version you will need to set the JAVA_HOME variable and put JAVA_HOME/bin in your path. 

We are actively updating the documentation as quickly as we can, we'd appreciate your help and/or feedback. https://docs.iridium.software/

## <a name="Running"></a> Running 
Running iridium locally is as simple as running the following command and following the prompts.  it will tell you exactly what to do (if you are on macos).  We need a similar script for windows and linux.  

** Please Note: ** This presumes that you have already [built the project](#building)

```
chmod +x bootIridium.sh
./bootIridium.sh 
```

This will do the following for you 
* Set the IRIDIUM_HOME directory to the root of the bootIridium.sh script. 
* Calculate the project version
* Unpack the the cli bin target
* Check to see if a client id needs to be added to the external providers yaml file, it will exit if a client id does not exist.  
* Check to see if a data directory exists and if not it will create one. If it does not exist it assumes that this is the first time you have run iridium and it provides links to setting up the social providers we support. 
* It calculates the host internal for routing outside from the containers. 
* It launches docker
