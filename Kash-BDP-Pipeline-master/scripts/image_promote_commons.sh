#!/usr/bin/env bash

set -o errexit
set -o nounset

if [ "${VERBOSE}" == "true" ] ; then
set -o xtrace
fi

SRC_CLOUD=$(echo ${SRC_CLOUD}| tr '[:upper:]' '[:lower:]')

case ${SRC_CLOUD} in
  aws)
     export SRC_REGISTRY="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
     ;;
  gcp)
     export SRC_REGISTRY="gcr.io/${SRC_PROJECTID}"
     ;;
  *)
    echo "${SRC_CLOUD} is supported"
    exit 1
    ;;
esac
