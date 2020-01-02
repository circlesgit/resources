#!/usr/bin/env bash
# get_docker_tags.sh
# script will the fetch the docker tag from the container registry over http and
# print them in console.
#

set -o errexit
set -o nounset

if [ "${VERBOSE}" == "true" ] ; then
set -o xtrace
fi

if [ -z "${ENVIRONMENT+x}" ] && [ ! -z "${SRC_ENVIRONMENT+x}" ]
then
ENVIRONMENT=${SRC_ENVIRONMENT}
fi

if [ -z "${SERVICE_ACCOUNT+x}" ] && [ ! -z "${SRC_SERVICE_ACCOUNT+x}" ]
then
SERVICE_ACCOUNT=${SRC_SERVICE_ACCOUNT}
fi

if [ -z "${PROJECTID+x}" ] && [ ! -z "${SRC_PROJECTID+x}" ]
then
PROJECTID=${SRC_PROJECTID}
fi


gcloud auth activate-service-account --key-file=${SA_FILE} --project=${PROJECTID}
gcloud config set account ${SERVICE_ACCOUNT}@${PROJECTID}.iam.gserviceaccount.com
OAUTH_TOKEN=$(gcloud auth print-access-token --project=${PROJECTID})

if [ -z "${ENVIRONMENT+x}" ]
then
  curl --silent -u oauth2accesstoken:${OAUTH_TOKEN} https://gcr.io/v2/${PROJECTID}/${APP_NAME}/tags/list | jq ".tags[]" | sed 's/"//g' | grep -E "^[0-9]{4}-[0-9]{2}-[0-9]{2}-[0-9]{10}-[0-9]{2}-[a-z0-9]{15}$" | sort -rn
else
  curl --silent -u oauth2accesstoken:${OAUTH_TOKEN} https://gcr.io/v2/${PROJECTID}/${APP_NAME}/tags/list | jq ".tags[]" | sed 's/"//g' | grep -E "(\-${ENVIRONMENT}|\-${ENVIRONMENT}-STATIC)$" | sort -rn
fi
