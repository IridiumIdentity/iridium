#!/bin/bash
set -e

namespace=$1

image_name=$2

version=$3

echo "#############"
echo "building namespace: ${namespace} image: ${image_name}  version: ${version}"
echo "#############"

mvn --version

mvn test

mvn package -Dmaven.test.skip=true

mv iridium-core-server/target/iridium-core-server-*.jar ./

docker build -t $namespace/$image_name:$version -f tools/images/Dockerfile.core .

docker tag $namespace/$image_name:$version $namespace/$image_name:latest

docker push $namespace/$image_name:$version

docker push $namespace/$image_name:latest


