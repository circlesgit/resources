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
export AWS_SHARED_CREDENTIALS_FILE=${SA_FILE}
CLOUD=$(echo ${CLOUD}| tr '[:upper:]' '[:lower:]')

cd ${WORKSPACE}/${APPLICATION_GIT_REPO}/
GITCOMMITID=$(git rev-parse --short=15 HEAD)

DOCKER_VERSION="$(date "+%Y-%m-%d-%s"-${BUILD_NUMBER})-${GITCOMMITID}"
DOCKER_REGISTRY="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

echo "===================================="
echo "DOCKER_REGISTRY : ${DOCKER_REGISTRY}"
echo "===================================="

cd ${WORKSPACE}/${APPLICATION_GIT_REPO}/docker

docker build --no-cache -t ${DOCKER_REGISTRY}/${APP_NAME}:${DOCKER_VERSION} .

if [ $? !=  0 ] ; then
  echo "Docker Build failed"
  exit 1
fi

eval $(aws ecr get-login --no-include-email --region ${AWS_REGION})
docker push ${DOCKER_REGISTRY}/${APP_NAME}:${DOCKER_VERSION}
if [ $? !=  0 ] ; then
  echo "Docker Push failed"
  exit 1
fi
echo "===================================="
echo "DOCKER IMAGE : ${DOCKER_REGISTRY}/${APP_NAME}:${DOCKER_VERSION}"
echo "===================================="
