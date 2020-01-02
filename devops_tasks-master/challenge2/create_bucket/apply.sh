#!/bin/bash
TERRAFORM=$(which terraform)
if [ -z ${TERRAFORM} ];then
    echo -e "terraform not found\nplease install terraform"
    exit 1
fi
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
