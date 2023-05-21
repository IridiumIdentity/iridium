#!/bin/bash
set -e

namespace=$1

image_name=$2

echo "building..."
echo $namespace
echo $image_name


iridium_git_rev() {
  local git_rev=$(git rev-parse --short HEAD)
  if [[ $? != 0 ]];
  then
    exit 1
  fi
  echo $git_rev
}

mvn --version

mvn test

mvn package -Dmaven.test.skip=true

mv iridium-core-server/target/iridium-core-server-*.jar tools/images/

revision=$(iridium_git_rev)

docker build -t ghcr.io/$namespace/$image_name:$revision -f tools/images/Dockerfile.core .

docker push ghcr.io/$namespace/iridium-core-server:$revision


