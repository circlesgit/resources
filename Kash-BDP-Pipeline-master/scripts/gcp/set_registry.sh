#!/usr/bin/env bash
# set_registry.sh
# Helper script that will set the source registry along with its tag
#

set -o errexit
set -o nounset

if [ "${VERBOSE}" == "true" ] ; then
set -o xtrace
fi
if [ -z "${REGISTRY+x}" ] && [ ! -z "${PROJECTID+x}" ]
then
export REGISTRY="gcr.io/${PROJECTID}"
fi
if [ -z "${SRC_REGISTRY+x}" ] && [ ! -z "${SRC_PROJECTID+x}" ]
then
export SRC_REGISTRY="gcr.io/${SRC_PROJECTID}"
fi
if [ -z "${DST_REGISTRY+x}" ] && [ ! -z "${DST_PROJECTID+x}" ]
then
export DST_REGISTRY="gcr.io/${DST_PROJECTID}"
fi
