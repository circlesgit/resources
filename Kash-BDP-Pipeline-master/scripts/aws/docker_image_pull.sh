#!/usr/bin/env bash
# docker_image_pull.sh
# script that will pull the docker images from google container registry.

set -o errexit
set -o nounset

if [ "${VERBOSE}" == "true" ] ; then
set -o xtrace
fi

export AWS_SHARED_CREDENTIALS_FILE=${SA_FILE}
export AWS_DEFAULT_REGION=${SRC_AWS_REGION}
export SRC_REGISTRY="${SRC_AWS_ACCOUNT_ID}.dkr.ecr.${SRC_AWS_REGION}.amazonaws.com"

eval $(aws ecr get-login --no-include-email --region ${SRC_AWS_REGION})

docker pull ${SRC_REGISTRY}/${APP_NAME}:${DOCKER_TAG}
