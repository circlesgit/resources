#!/usr/bin/env bash
# deployer.sh will deploy the helm chart in the kubernetes cluster
# If the application is new, it will be installed.
# If the application is already released, it will get upgraded.
#
set -o errexit
set -o nounset

if [ "${VERBOSE}" == "true" ] ; then
set -o xtrace
fi
if [ ${#HELMS_EXTRA_OPS} -eq 0 ] ; then
HELMS_EXTRA_OPS="  "
fi
export REGISTRY="gcr.io/${PROJECTID}"

echo "=============================================================================="
echo "Following docker image is being deployed"
echo "________________________________________"
echo "${REGISTRY}/${APP_NAME}:${DOCKER_TAG}"
echo "=============================================================================="
export KUBECONFIG="${WORKSPACE}/${APP_NAME}_${CLOUD}_${ENVIRONMENT}_kubeconfig"
gcloud auth activate-service-account --key-file=${SA_FILE} --project=${PROJECTID}
gcloud config set account ${SERVICE_ACCOUNT}@${PROJECTID}.iam.gserviceaccount.com
gcloud container clusters get-credentials ${KUBERNETES_CLUSTER} --zone ${CLOUD_ZONE}
helm ls
if [ $(helm ls --namespace ${KUBERNETES_NAMESPACE} -q "^${APP_NAME}-${ENVIRONMENT}$" | grep -c "${APP_NAME}-${ENVIRONMENT}") -eq 1 ]; then
  echo "Upgrading chart"
  helm upgrade ${APP_NAME}-${ENVIRONMENT} \
  -f ${WORKSPACE}/deployment_config_git_repo/helm/values/${CLOUD}/${ENVIRONMENT}/values.yaml \
  ${HELMS_EXTRA_OPS} \
  --set "Applicationimage=${REGISTRY}/${APP_NAME}:${DOCKER_TAG}" \
  --namespace ${KUBERNETES_NAMESPACE} \
  ${WORKSPACE}/deployment_config_git_repo/helm/
elif [ $(helm ls --namespace ${KUBERNETES_NAMESPACE} -q "^${APP_NAME}-${ENVIRONMENT}$" | grep -c "${APP_NAME}-${ENVIRONMENT}") -eq 0 ]; then
  echo "Installing chart"
  helm install -n ${APP_NAME}-${ENVIRONMENT} \
  -f ${WORKSPACE}/deployment_config_git_repo/helm/values/${CLOUD}/${ENVIRONMENT}/values.yaml \
  ${HELMS_EXTRA_OPS} \
  --set "Applicationimage=${REGISTRY}/${APP_NAME}:${DOCKER_TAG}" \
  --namespace ${KUBERNETES_NAMESPACE} \
  ${WORKSPACE}/deployment_config_git_repo/helm/
else
  echo "$0 : Error : Something is wrong with helm deployment script : $0"
  exit 1
fi
helm ls
