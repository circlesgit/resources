# eks-alb-ingress	

This document walks you through Amazon EKS with [AWS ALB Ingress](https://github.com/kubernetes-sigs/aws-alb-ingress-controller).



# Getting Started

#### Attaching extra IAM inline policies

Download the `iam-policy.json` 

```
wget https://raw.githubusercontent.com/kubernetes-sigs/aws-alb-ingress-controller/master/examples/iam-policy.json
```

Manual attach it to your EC2 Node Instance Role.

```
aws iam put-role-policy --role-name <EC2_NODE_INSTANCE_ROLE> --policy-name alb-ingress-extra --policy-document file://iam-policy.json
```



modify the `alb-ingress-controller.yaml` file:

- `AWS_REGION`: Region of your Amazon EKS cluster.

  ```yaml
  - name: AWS_REGION
    value: us-west-2
  ```

  ​

- `CLUSTER_NAME`: name of the cluster

  ```yaml
  - name: CLUSTER_NAME
    value: mycluster
  ```



Create the `ClusterRole`, `ClusterRoleBinding` and `ServiceAccount`

```bash
$ kubectl apply -f albrbac.yaml
```



Deploy the ingress-controller

```bash
$ kubectl apply -f alb-ingress-controller.yaml
```

Verify the deployment was successful and the controller started.

```bash
$ kubectl logs -n kube-system \
    $(kubectl get po -n kube-system | \
    egrep -o alb-ingress[a-zA-Z0-9-]+) | \
    egrep -o '\[ALB-INGRESS.*$'
```

Create the sample application

```bash
$ kubectl apply -f app.yaml
```

Update the ingress resource

```bash
$ vim ingress-resource.yaml
```



```yaml
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: "webapp-alb-ingress"
  annotations:
    alb.ingress.kubernetes.io/scheme: internet-facing
    alb.ingress.kubernetes.io/listen-ports: '[{"HTTP":80,"HTTPS": 443}]'
    alb.ingress.kubernetes.io/subnets: 'subnet-xxxxxxxx,subnet-xxxxxxxx'
    alb.ingress.kubernetes.io/security-groups: 'sg-xxxxxxxx,sg-xxxxxxxx'
    alb.ingress.kubernetes.io/certificate-arn: <ACM_CERT_ARN>
  labels:
    app: webapp-service
spec:
  rules:
  - http:
      paths:
      - path: /greeting
        backend:
          serviceName: "webapp-service"
          servicePort: 80
      - path: /
        backend:
          serviceName: "caddy-service"
          servicePort: 80
```

1. make sure to modify the `subnet` `security-groups` and `certificate-arn` if required.
2. make sure public internet can access ALB TCP 80 and 443 and ALB can access any TCP port on node group - double check your security groups setting. You would usually need two security groups - one for TCP 80 and 443 public from all and the other for the NodeSecurityGroup.



find your EKS NodeSecurityGroup

```bash
$ aws ec2 describe-security-groups --query "SecurityGroups[?VpcId=='vpc-666d9a1e']|[?contains(GroupName, 'NodeSecurityGroup')].GroupId"
[
    "sg-2f25645f"
]
```

create another SecurityGroup for your ALB with HTTP/HTTPS wide open:

```
$ aws ec2 create-security-group --vpc-id vpc-666d9a1e --group-name alb-sg --description "ALB 80 443 open"
{
    "GroupId": "sg-54391324"
}

$ aws ec2 authorize-security-group-ingress --group-id sg-54391324 --port 80 --protocol tcp --cidr 0.0.0.0/0
$ aws ec2 authorize-security-group-ingress --group-id sg-54391324 --port 443 --protocol tcp --cidr 0.0.0.0/0

```

Finally, input `sg-2f25645f,sg-54391324` as the value of `alb.ingress.kubernetes.io/security-groups:`



find your EKS subnets with `aws cli` :

```bash
$ aws ec2 describe-subnets --query "join(',', Subnets[?VpcId=='vpc-e692c79f'].SubnetId)" --output text
subnet-eb16cba0,subnet-7ef24007
```



Deploy the ingress resource

```Bash
$ kubectl apply -f ingress-resource.yaml
ingress "webapp-alb-ingress" created
```

Describe your ingress resource

```bash
$ kubectl describe ing/webapp-alb-ingress
Name:             webapp-alb-ingress
Namespace:        default
Address:          b0ae30d6-default-webappalb-9895-847304586.us-west-2.elb.amazonaws.com
Default backend:  default-http-backend:80 (192.168.236.106:8080)
Rules:
  Host  Path  Backends
  ----  ----  --------
  *
        /greeting   webapp-service:80 (<none>)
        /           caddy-service:80 (<none>)
Annotations:
  alb.ingress.kubernetes.io/security-groups:         sg-2f25645f
  alb.ingress.kubernetes.io/subnets:                 subnet-c21954bb, subnet-1bb7d850, subnet-ce481794
  kubectl.kubernetes.io/last-applied-configuration:  {"apiVersion":"extensions/v1beta1","kind":"Ingress","metadata":{"annotations":{"alb.ingress.kubernetes.io/listen-ports":"[{\"HTTP\":80,\"HTTPS\": 443}]","alb.ingress.kubernetes.io/scheme":"internet-facing","alb.ingress.kubernetes.io/security-groups":"sg-2f25645f","alb.ingress.kubernetes.io/subnets":"subnet-c21954bb, subnet-1bb7d850, subnet-ce481794"},"labels":{"app":"webapp-service"},"name":"webapp-alb-ingress","namespace":"default"},"spec":{"rules":[{"http":{"paths":[{"backend":{"serviceName":"webapp-service","servicePort":80},"path":"/greeting"},{"backend":{"serviceName":"caddy-service","servicePort":80},"path":"/"}]}}]}}

  alb.ingress.kubernetes.io/listen-ports:  [{"HTTP":80,"HTTPS": 443}]
  alb.ingress.kubernetes.io/scheme:        internet-facing
Events:
  Type    Reason  Age              From                Message
  ----    ------  ----             ----                -------
  Normal  CREATE  1m               ingress-controller  Ingress default/webapp-alb-ingress
  Normal  CREATE  1m               ingress-controller  b0ae30d6-default-webappalb-9895 created
  Normal  CREATE  1m               ingress-controller  b0ae30d6-32157-HTTP-e65397d target group created
  Normal  CREATE  1m               ingress-controller  b0ae30d6-32064-HTTP-e65397d target group created
  Normal  CREATE  1m               ingress-controller  80 listener created
  Normal  CREATE  1m (x2 over 1m)  ingress-controller  1 rule created
  Normal  CREATE  1m (x2 over 1m)  ingress-controller  2 rule created
  Normal  CREATE  1m               ingress-controller  443 listener created
  Normal  UPDATE  1m               ingress-controller  Ingress default/webapp-alb-ingress
  Normal  MODIFY  24s              ingress-controller  b0ae30d6-default-webappalb-9895 tags modified
```



After a few minutes of DNS propagation of your ALB, you should be able to test it like this:



```bash
$ curl "http://<YOUR_ALB_DNS_NAME>/greeting?name=pahud"
Hello pahud
```



and the root path will go to [Caddy](https://caddyserver.com/) web server document root:



```bash
$ curl http://<YOUR_ALB_DNS_NAME>
```



