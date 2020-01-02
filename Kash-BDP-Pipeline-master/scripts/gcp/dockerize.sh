#!/usr/bin/env bash
# dockerize.sh
# This scripts will checkout the source code.Dockerized the application with Dockerfile
# available with the source code.Push the docker images to the container registry.
#
set -o errexit
set -o nounset

if [ "${VERBOSE}" == "true" ] ; then
set -o xtrace
fi

PWD=$(pwd)
cd ${WORKSPACE}/${APPLICATION_GIT_REPO}/
GITCOMMITID=$(git rev-parse --short=15 HEAD)

#mkdir -p "${WORKSPACE}/${ARTIFACT_DIR}"
#cp -r ${WORKSPACE}/${MANIFEST_GIT_DIR}/${APP_NAME}/docker/* ${WORKSPACE}/${ARTIFACT_DIR}/

DOCKER_VERSION="$(date "+%Y-%m-%d-%s"-${BUILD_NUMBER})-${GITCOMMITID}"
DOCKER_REGISTRY="gcr.io/${PROJECTID}"

echo "===================================="
echo "DOCKER_REGISTRY : ${DOCKER_REGISTRY}"
echo "===================================="
CLOUD=$(echo ${CLOUD}| tr '[:upper:]' '[:lower:]')
#cd ${WORKSPACE}/${ARTIFACT_DIR}/
cd ${WORKSPACE}/${APPLICATION_GIT_REPO}/docker/

docker build --no-cache -t ${DOCKER_REGISTRY}/${APP_NAME}:${DOCKER_VERSION} .

if [ $? !=  0 ] ; then
  echo "Docker Build failed"
  exit 1
fi

gcloud auth activate-service-account --key-file=${SA_FILE} --project=${PROJECTID}
gcloud config set account ${SERVICE_ACCOUNT}@${PROJECTID}.iam.gserviceaccount.com
gcloud auth print-access-token | docker login -u oauth2accesstoken --password-stdin https://gcr.io
docker push ${DOCKER_REGISTRY}/${APP_NAME}:${DOCKER_VERSION}
if [ $? !=  0 ] ; then
  echo "Docker Push failed"
  exit 1
fi
echo "===================================="
echo "DOCKER IMAGE : ${DOCKER_REGISTRY}/${APP_NAME}:${DOCKER_VERSION}"
echo "===================================="
