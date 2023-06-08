#!/bin/bash
set -e

namespace=$1

image_name=$2

version=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)
echo "#############"
echo "building iridium version ${version}"
echo "#############"

mvn --version

mvn test

mvn package -Dmaven.test.skip=true

mv iridium-core-server/target/iridium-core-server-*.jar ./

docker build -t $namespace/$image_name:$version -f tools/images/Dockerfile.core .

docker tag $namespace/$image_name:$version $namespace/$image_name:latest

docker push $namespace/iridium-core-server:$version

docker push $namespace/$image_name:latest


