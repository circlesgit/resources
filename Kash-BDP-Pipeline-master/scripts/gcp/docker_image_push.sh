#!/usr/bin/env bash
# docker_image_push.sh
# script will push the docker container to the destination container registry with its new name.
# If static, option is requested,corresponding STATIC docker images also will be pushed
# to the destination container registry.
#
set -o errexit
set -o nounset

if [ "${VERBOSE}" == "true" ] ; then
set -o xtrace
fi

## set the source registry based on the where you are pulling the docker image from
## based on the source environment cloud and src environment registry details
SOURCE="${BASH_SOURCE[0]}"
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
SRC_CLOUD=$(echo ${SRC_CLOUD}| tr '[:upper:]' '[:lower:]')
source ${DIR}/../${SRC_CLOUD}/set_registry.sh

DST_REGISTRY="gcr.io/${DST_PROJECTID}"

DST_DOCKER_TAG=${DOCKER_TAG/-STATIC/}
DST_DOCKER_TAG="${DST_DOCKER_TAG}-${DST_ENVIRONMENT}"

docker tag ${SRC_REGISTRY}/${APP_NAME}:${DOCKER_TAG} ${DST_REGISTRY}/${APP_NAME}:${DST_DOCKER_TAG}
if [ "${STATIC}" == "true" ] ; then
  docker tag "${SRC_REGISTRY}/${APP_NAME}:${DOCKER_TAG}" "${DST_REGISTRY}/${APP_NAME}:${DST_DOCKER_TAG}-STATIC"
fi
gcloud auth activate-service-account --key-file=${SA_FILE} --project=${DST_PROJECTID}
gcloud config set account ${DST_SERVICE_ACCOUNT}@${DST_PROJECTID}.iam.gserviceaccount.com
gcloud auth print-access-token | docker login -u oauth2accesstoken --password-stdin https://gcr.io
docker push ${DST_REGISTRY}/${APP_NAME}:${DST_DOCKER_TAG}
if [ $? !=  0 ] ; then
  echo "Docker Push failed"
  exit 1
fi
if [ "${STATIC}" == "true" ] ; then
  docker push ${DST_REGISTRY}/${APP_NAME}:${DST_DOCKER_TAG}-STATIC
  if [ $? !=  0 ] ; then
    echo "Docker Push failed"
    exit 1
  fi
  docker rmi ${DST_REGISTRY}/${APP_NAME}:${DST_DOCKER_TAG}-STATIC
  if [ $? !=  0 ] ; then
    echo "Docker rmi failed"
    exit 1
  fi
fi
docker rmi ${SRC_REGISTRY}/${APP_NAME}:${DOCKER_TAG} ${DST_REGISTRY}/${APP_NAME}:${DST_DOCKER_TAG}
if [ $? !=  0 ] ; then
  echo "Docker rmi failed"
  exit 1
fi
echo "=============================================================================="
echo "Docker image promoted as : "
echo "________________________________________"
echo "Source      Image : ${SRC_REGISTRY}/${APP_NAME}:${DOCKER_TAG}"
echo "Destination Image : ${DST_REGISTRY}/${APP_NAME}:${DST_DOCKER_TAG}"
if [ "${STATIC}" == "true" ] ; then
  echo "Destination  Image - Static : ${DST_REGISTRY}/${APP_NAME}:${DST_DOCKER_TAG}-STATIC"
fi
echo "=============================================================================="
