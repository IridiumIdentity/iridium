#!/bin/bash
set -e

registry=$1

image_name=$2

echo "building ${registry}/${image_name}"

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

mv iridium-core-server/target/iridium-core-server-*.jar ./

revision=$(iridium_git_rev)

docker build -t ghr.io/$registry/$image_name:$revision -f tools/images/Dockerfile.core .

docker push ghcr.io/iridiumidentity/iridium-core-server:$revision


