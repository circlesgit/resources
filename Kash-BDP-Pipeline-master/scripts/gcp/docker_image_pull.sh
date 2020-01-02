#!/usr/bin/env bash
# docker_image_pull.sh
# script that will pull the docker images from google container registry.
#

set -o errexit
set -o nounset

if [ "${VERBOSE}" == "true" ] ; then
set -o xtrace
fi

SRC_REGISTRY="gcr.io/${SRC_PROJECTID}"
gcloud auth activate-service-account --key-file=${SA_FILE} --project=${SRC_PROJECTID}
gcloud config set account ${SRC_SERVICE_ACCOUNT}@${SRC_PROJECTID}.iam.gserviceaccount.com
gcloud auth print-access-token | docker login -u oauth2accesstoken --password-stdin https://gcr.io
docker pull ${SRC_REGISTRY}/${APP_NAME}:${DOCKER_TAG}
