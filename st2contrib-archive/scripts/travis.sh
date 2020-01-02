#!/usr/bin/env bash

GIT_BRANCH=$(git for-each-ref --format='%(objectname) %(refname:short)' refs/heads | awk "/^$(git rev-parse HEAD)/ {print \$2}")

if [ -z ${TASK} ]; then
  echo "No task provided"
  exit 2
fi

# When running on master branch we want to run checks on all the files / packs
# not only on changes ones.
echo "Running on branch: ${GIT_BRANCH}"
if [ "${GIT_BRANCH}" = "master" ]; then
    echo "Running on master branch, forcing check of all files"
    export FORCE_CHECK_ALL_FILES="true"
fi

# Note: Travis performs a shallow clone of a single branch which means we end up
# in a detached head state where git-changes-* scripts won't work since master
# branch is not available.
# We fix this by pulling down master branch.
git config remote.origin.fetch refs/heads/*:refs/remotes/origin/*
git fetch origin master

if [ ${TASK} == "flake8" ]; then
  make flake8
elif [ ${TASK} == "pylint" ]; then
  make pylint
elif [ ${TASK} == "configs-check" ]; then
  make configs-check
elif [ ${TASK} == "metadata-check" ]; then
  make metadata-check
elif [ ${TASK} == "packs-resource-register" ]; then
  make packs-resource-register
elif [ ${TASK} == "packs-tests" ]; then
  make packs-tests
elif [ ${TASK} == "packs-missing-tests-check" ]; then
  make packs-missing-tests
else
  echo "Invalid task: ${TASK}"
  exit 2
fi

exit $?
