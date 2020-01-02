#!/usr/bin/env bash

set -o errexit
set -o nounset

TERRAFORM=$(which terraform)
if [ -z ${TERRAFORM} ];then
    echo -e "terraform not found\nplease install terraform"
    open "https://learn.hashicorp.com/terraform/getting-started/install.html"
    exit 1
fi
GCLOUD=$(which gcloud)
if [ -z ${GCLOUD} ];then
    echo -e "gcloud not found\nplease install gcloud"
    open "https://cloud.google.com/sdk/install"
    exit 1
fi
GIT=$(which git)
if [ -z ${GIT} ];then
    echo -e "git not found\nplease install git"
    open "https://git-scm.com/book/en/v2/Getting-Started-Installing-Git"
    exit 1
fi
HELM=$(which helm)
if [ -z ${HELM} ];then
    echo -e "helm not found\nplease install helm"
    open "https://github.com/helm/helm#install"
    exit 1
fi
echo "=========== RUNNING GCLOUD INIT( info : https://cloud.google.com/sdk/gcloud/reference/init )=================="
gcloud init

echo "=========== GCLOUD LOGIN(info : https://cloud.google.com/sdk/gcloud/reference/auth/login)=================="
gcloud auth login

cd terraform

echo "===================TERRAFORM INIT=================="
echo  "terrafrom init: Input 'y' to proceed"
read confirm
if [ $confirm == 'y' ];then
    echo  "RUNNING: terrafrom init"
    terraform init
else
    echo  "SKIPPING: terrafrom init"
fi
echo "===================TERRAFORM REFRESH=================="
echo  "terrafrom refresh: Input 'y' to proceed"
read confirm
if [ $confirm == 'y' ];then
    echo  "RUNNING: terrafrom refresh"
    terraform refresh
else
    echo  "SKIPPING: terrafrom refresh"
fi
echo "===================TERRAFORM PLAN=================="
echo  "terrafrom plan: Input 'y' to proceed"
read confirm
if [ $confirm == 'y' ];then
    echo  "RUNNING: terrafrom plan"
    terraform plan
else
    echo  "SKIPPING: terrafrom plan"
fi
echo "===================TERRAFORM APPLY=================="
echo  "terrafrom apply: Input 'y' to proceed"
read confirm
if [ $confirm == 'y' ];then
    echo  "RUNNING: terrafrom apply"
    terraform apply
else
    echo  "SKIPPING: terrafrom apply"
fi
cd ..
echo "= please make a note of the Kubernetes cluster name ="

if [ -z ${ACCOUNT_NAME} ];then
    echo -e "please set the google account name to ACCOUNT_NAME"
    echo -e "export ACCOUNT_NAME=<replace this with account name"
    exit 1
fi
if [ -z ${GCP_PROJECT} ];then
    echo -e "please set the google account name to GCP_PROJECT"
    echo -e "export GCP_PROJECT=<replace this with gcp_project name"
    exit 1
fi
if [ -z ${GCP_ZONE} ];then
    echo -e "please set the google zone to GCP_ZONE"
    echo -e "export GCP_ZONE=<replace this with gcp_zone name"
    exit 1
fi
if [ -z ${K8S_CLUSTER_NAME} ];then
    echo -e "please set the k8s cluster name to K8S_CLUSTER_NAME"
    echo -e "export K8S_CLUSTER_NAME=<replace this with k8s_cluster_name name"
    exit 1
fi
gcloud config set account ${ACCOUNT_NAME}
gcloud config set project ${GCP_PROJECT}
gcloud container clusters get-credentials ${K8S_CLUSTER_NAME} --zone ${GCP_ZONE} --project ${GCP_PROJECT}
echo -e "creating namespace ingress"
kubectl create ns ingress
echo -e "creating namespace staging"
kubectl create ns staging
echo -e "creating namespace production"
kubectl create ns production
echo -e "creating helm tiller serviceaccount and clusterrolebinding "
kubectl apply -f ./tiller.yaml
echo -e "Installing Helm"
helm init --history-max 200
echo -e "Installing nginx Ingress"
helm install --name nginx-ingress stable/nginx-ingress --namespace ingress
echo -e "Installing guest-book application in staging namespace"
kubectl --namespace staging apply -f https://raw.githubusercontent.com/kubernetes/examples/master/guestbook/frontend-deployment.yaml
kubectl --namespace staging apply -f https://raw.githubusercontent.com/kubernetes/examples/master/guestbook/frontend-service.yaml
kubectl --namespace staging apply -f https://raw.githubusercontent.com/kubernetes/examples/master/guestbook/redis-master-deployment.yaml
kubectl --namespace staging apply -f https://raw.githubusercontent.com/kubernetes/examples/master/guestbook/redis-master-service.yaml
kubectl --namespace staging apply -f https://raw.githubusercontent.com/kubernetes/examples/master/guestbook/redis-slave-deployment.yaml
kubectl --namespace staging apply -f https://raw.githubusercontent.com/kubernetes/examples/master/guestbook/redis-slave-service.yaml
echo -e "setting ingress in staging namespace"
kubectl --namespace staging apply -f ./staging-ingress.yaml
echo -e "Installing guest-book application in production namespace"
kubectl --namespace production apply -f https://raw.githubusercontent.com/kubernetes/examples/master/guestbook/frontend-deployment.yaml
kubectl --namespace production apply -f https://raw.githubusercontent.com/kubernetes/examples/master/guestbook/frontend-service.yaml
kubectl --namespace production apply -f https://raw.githubusercontent.com/kubernetes/examples/master/guestbook/redis-master-deployment.yaml
kubectl --namespace production apply -f https://raw.githubusercontent.com/kubernetes/examples/master/guestbook/redis-master-service.yaml
kubectl --namespace production apply -f https://raw.githubusercontent.com/kubernetes/examples/master/guestbook/redis-slave-deployment.yaml
kubectl --namespace production apply -f https://raw.githubusercontent.com/kubernetes/examples/master/guestbook/redis-slave-service.yaml
echo -e "setting ingress in production namespace"
kubectl --namespace staging apply -f ./production-ingress.yaml
echo -e "setting autoscale for CPU 70%"
kubectl  --namespace staging autoscale deployment frontend --cpu-percent=70 --min=1 --max=10
kubectl  --namespace production autoscale deployment frontend --cpu-percent=70 --min=1 --max=10

echo -e "increasing load on frontend pods in staging namespace for 1 minutes"
kubectl --namespace staging exec $(kubectl -n staging get pods -l app=guestbook,tier=frontend) "apt-get update && apt-get install stress && stress --cpu 1 --timeout 60 "
echo -e "watching the change in scaling.Manually  kill to exit the following command"
kubectl  --namespace staging get hpa --watch


echo -e "increasing load on frontend pods in production namespace"
kubectl --namespace production exec $(kubectl -n production get pods -l app=guestbook,tier=frontend) "apt-get update && apt-get install stress && stress --cpu 1 --timeout 60 "
echo -e "watching the change in scaling.Manually  kill to exit the following command"
kubectl  --namespace production get hpa --watch
