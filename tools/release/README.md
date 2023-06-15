# Release process

## Create a release branch
```shell
$ git co -b release/v${my-version-number}
```

## Update the build versions
* Update the parent pom.xml file 
  * https://github.com/IridiumIdentity/iridium/blob/main/pom.xml
  * And all sub-modules with the appropriate update parent pom version. 
* Update the Angular Iridium Client Library
  * https://github.com/IridiumIdentity/iridium/tree/main/iridium-angular-client/projects/ngx-iridium-client


## Create a tag
```shell
$ git tag -a v${my-version-number} -m "some message"
```

## Push up the tag
```shell
$ git push origin v${my-version-number} 
```

## Create Tar file for distribution
```shell
$ tar -czvf iridium-${my-version-number}-bin.tar.gz ./iridium-${my-version-number}-bin
```

## Check that artifacts were built and uploaded correctly to GitHub
If they are continue on to publishing the release in GitHub

## Mark the tag as a release in GitHub
Find the tag and mark is as the latest release.  Upload the generated artifacts to the release page. 


