### *Kash BDP pipeline for Amazon cloud*

We will using the [Amazon Elastic Container Registry](https://aws.amazon.com/ecr/) to store the docker images.  
We will using the [Amazon Elastic Kubernetes Service](https://aws.amazon.com/eks/) for deploying the Application.  

## We see the steps required to setup the Kash BDP pipeline for AWS cloud.

1. Create [new AWS IAM user](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html) for doing the docker push and deployment.
2. Following IAM policy is required.

| IAM Role                                | Purpose                             |
| ---------------------------------       | ------------------------------------ |
| `AmazonEC2ContainerRegistryPowerUser`   | [Required for docker push and pull](https://docs.aws.amazon.com/AmazonECR/latest/userguide/ecr_managed_policies.html#AmazonEC2ContainerRegistryPowerUser) |
| `eks:DescribeCluster`                   | [Required for deployment](https://docs.aws.amazon.com/eks/latest/userguide/security_iam_id-based-policy-examples.html#policy_example2) |
| `eks:ListClusters`                      | [Required for deployment](https://docs.aws.amazon.com/eks/latest/userguide/security_iam_id-based-policy-examples.html#policy_example2) |

3. Download the AWS credentials for the newly created users as file.Example the AWS_SHARED_CREDENTIALS_FILE will look like this.
```
[default]
aws_access_key_id = xXxXxXxXxXxXxXxXxXxX
aws_secret_access_key = xXxXxXxXxXxXxXx/xXxXxXxXxXxXxXxXxXxXxXxX
```
4. Create jenkins [**Secret file**](https://jenkins.io/doc/book/using/using-credentials/) with the AWS credentials created above.  
`In the ID field, specify a meaningful credential ID value - for example, jenkins-user-for-xyz-artifact-repository. You can use upper- or lower-case letters for the credential ID, as well as any valid separator character. However, for the benefit of all users on your Jenkins instance, it is best to use a single and consistent convention for specifying credential IDs.
Note: This field is optional. If you do not specify its value, Jenkins assigns a globally unique ID (GUID) value for the credential ID. Bear in mind that once a credential ID is set, it can no longer be changed.`  
5. Make a note of the jenkins secrets, which we will be using going forward.
6. [Install](https://docs.aws.amazon.com/eks/latest/userguide/helm.html) [Helms](https://helm.sh) in Amazon Elastic Kubernetes cluster.

### FAQ
