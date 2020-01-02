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
export ENVIRONMENT=${SRC_ENVIRONMENT}
fi

if   [ -z "${AWS_ACCOUNT_ID+x}" ] && [ ! -z "${SRC_AWS_ACCOUNT_ID+x}" ]
then
export AWS_ACCOUNT_ID=${SRC_AWS_ACCOUNT_ID}
elif [ -z "${AWS_ACCOUNT_ID+x}" ] && [ ! -z "${ECR_AWS_ACCOUNT_ID+x}" ]
then
export AWS_ACCOUNT_ID=${ECR_AWS_ACCOUNT_ID}
fi

if   [ -z "${AWS_DEFAULT_REGION+x}" ] && [ ! -z "${SRC_AWS_REGION+x}" ]
then
export AWS_DEFAULT_REGION=${SRC_AWS_REGION}
elif [ -z "${AWS_DEFAULT_REGION+x}" ] && [ ! -z "${ECR_AWS_REGION+x}" ]
then
export AWS_DEFAULT_REGION=${ECR_AWS_REGION}
fi

export AWS_SHARED_CREDENTIALS_FILE=${SA_FILE}
TOKEN=$(aws ecr get-authorization-token --output text --query 'authorizationData[].authorizationToken')
if [ -z "${ENVIRONMENT+x}" ]
then
  curl --silent -H "Authorization: Basic ${TOKEN}" https://${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/v2/${APP_NAME}/tags/list | jq ".tags[]" | sed 's/"//g' | grep -E "^[0-9]{4}-[0-9]{2}-[0-9]{2}-[0-9]{10}-[0-9]{2}-[a-z0-9]{15}$" | sort -rn
else
  curl --silent -H "Authorization: Basic ${TOKEN}" https://${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/v2/${APP_NAME}/tags/list | jq ".tags[]" | sed 's/"//g' | grep -E "(\-${ENVIRONMENT}|\-${ENVIRONMENT}-STATIC)$" | sort -rn
fi
