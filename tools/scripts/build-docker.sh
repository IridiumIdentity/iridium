#!/bin/bash

iridium_git_rev() {
  local git_rev=$(git rev-parse HEAD)
  if [[ $? != 0 ]];
  then
    exit 1
  fi
  echo $git_rev
}

mvn --version

mvn test

mvn package -Dmaven.test.skip=true

mv iridium-core-server/target/iridium-core-server-*.jar tools/images

revision=iridium_git_rev

docker build -t iridiumidentity/iridium:$revision -f tools/images/Dockerfile.core .


