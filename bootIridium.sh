#!/bin/sh 

# Set iridium home directory if one does not exist 
if [[ ! -d $IRIDIUM_HOME ]]; then 
  export IRIDIUM_HOME=`pwd`

  echo "set IRIDIUM_HOME = $IRIDIUM_HOME"
fi

# Get Iridium Project Version 
export VERSION=`mvn help:evaluate -Dexpression=project.version -q -DforceStdout`
echo "Booting Iridium Version = $VERSION"

# Check to see if iridium cli bin is expanded 
if [[ ! -d $IRIDIUM_HOME/iridium-cli/target/iridium-$VERSION-bin ]]; then 
  if [[ -e $IRIDIUM_HOME/iridium-cli/target/iridium-$VERSION-bin.tar.gz ]]; then 
    gzip -d $IRIDIUM_HOME/iridium-cli/target/iridium-$VERSION-bin.tar.gz
  else 
    echo "Build from source following installation docs: mvn clean install"
  fi 

  cd $IRIDIUM_HOME/iridium-cli/target

  tar -xvf iridium-$VERSION-bin.tar 

  cd $IRIDIUM_HOME
fi 

# Check cli init directory / expand if necessary
clientArray=( $(cat $IRIDIUM_HOME/iridium-cli/target/iridium-$VERSION-bin/conf/external-providers.yaml| grep clientId | awk '{print $2}' | sed 's/\"//g') )

for clientId in ${clientArray[@]}
do
  if [[ -n $clientId ]]; then 
    echo "Found client id $cliendId"
    export CLIENTID_FOUND="true" 
  fi  
done

if [[ -z $CLIENTID_FOUND ]]; then 
  echo "###############################################################"
  echo "Please add a client id and secret in the following file: "
  echo " $IRIDIUM_HOME/iridium-cli/target/iridium-$VERSION-bin/conf/external-providers.yaml"
  echo " "
  echo " This can be one of github or google providers"
  echo " for github: "
  echo " https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authenticating-to-the-rest-api-with-an-oauth-app " 
  echo " " 
  echo " for google: " 
  echo " https://developers.google.com/identity/oauth2/web/guides/get-google-api-clientid " 
  echo " " 
  echo " Supply the clientId and secret in the external-providers.yaml above and then rerun the script"
  exit 1;
fi 

# Check to see if data directory exists if not create it.
export DATADIR="data" 
if [[ ! -d $DATADIR ]]; then 
  mkdir $DATADIR
  
  echo " Looks like this is the first time you have booted the iridium project."
  echo " You will need to run the init script (provided you have filled out the external providers file"
  echo " with the client id and secret from your provider(s) of choice." 
  echo " " 
  echo " The external providers file can be found here: " 
  echo "  $IRIDIUM_HOME/iridium-cli/target/iridium-$VERSION-bin/conf/external-providers.yaml"
  echo " "
  echo " Once the file is filled out run the following command:"
  echo " $IRIDIUM_HOME/iridium-cli/target/iridium-$VERSION-bin/bin/iridium init"
  echo " " 
  echo "sleeping 10"
  sleep 10 
else
  echo "data directory is present"
fi

# The intent of this is to find your default gateway or next hop address 
# that will provide internet access to docker 
#
# TODO: We have to figure out if sidaddr is appropriate for all environments that we might encounter.
#       I don't believe it will be. 
if [[ -z $HOST_INTERNAL ]]; then 
  export HOST_INTERNAL=`ipconfig getpacket en0 | grep siaddr | awk '{print $3}'`

  echo "set HOST_INTERNAL = $HOST_INTERNAL"  
fi

# run the docker command 
docker compose -f tools/schedulers/compose/local-iridium-compose.yml up
