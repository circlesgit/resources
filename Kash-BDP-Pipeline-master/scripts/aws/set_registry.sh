#!/usr/bin/env bash
# set_registry.sh
# Helper script that will set the source registry along with its tag
#
set -o errexit
set -o nounset

if [ "${VERBOSE}" == "true" ] ; then
set -o xtrace
fi

if [ -z "${REGISTRY+x}" ] && [ ! -z "${AWS_ACCOUNT_ID+x}" ] && [ ! -z "${AWS_REGION+x}" ]
then
export REGISTRY="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
fi
if [ -z "${SRC_REGISTRY+x}" ] && [ ! -z "${SRC_AWS_ACCOUNT_ID+x}" ] && [ ! -z "${SRC_AWS_REGION+x}" ]
then
export SRC_REGISTRY="${SRC_AWS_ACCOUNT_ID}.dkr.ecr.${SRC_AWS_REGION}.amazonaws.com"
fi
if [ -z "${DST_REGISTRY+x}" ] && [ ! -z "${DST_AWS_ACCOUNT_ID+x}" ] && [ ! -z "${DST_AWS_REGION+x}" ]
then
export DST_REGISTRY="${DST_AWS_ACCOUNT_ID}.dkr.ecr.${DST_AWS_REGION}.amazonaws.com"
fi
