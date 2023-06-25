#!/bin/bash
set -e

if [[ "$OSTYPE" == "darwin"* ]]; then
  IDEA=`ls -1d /Applications/IntelliJ\ * 2> /dev/null| tail -n1`
  open -a "$IDEA" .
else
  echo "distribution not known"
fi
