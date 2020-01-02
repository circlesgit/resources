### Kash BDP pipeline for GCP cloud.

We will using the [Google Container Registry](https://cloud.google.com/container-registry/) to store the docker images.  
We will using the [Google Kubernetes Engine (GKE)](https://cloud.google.com/kubernetes-engine/) for deploying the Application.  


## We see the steps required to setup the Kash BDP pipeline for Google cloud.

1. Create a new [Service Account](https://cloud.google.com/iam/docs/service-accounts) for doing the docker push and deployment.
2. Following IAM policy is required.  

| IAM Role                                | Purpose                             |
| ---------------------------------       | ------------------------------------ |
| `roles/storage.admin`   | [Required for docker push and pull](https://cloud.google.com/container-registry/docs/access-control) |
| `roles/storage.objectViewer`                   | [Required for deployment](https://cloud.google.com/kubernetes-engine/docs/how-to/iam) |
| `roles/container.developer`                      | [Required for deployment](https://cloud.google.com/kubernetes-engine/docs/how-to/iam) |

3. Download the service account keys as json file which is newly created users as file.Example the json will look like this.
```
{
"type": "service_account",
"project_id": "[PROJECT-ID]",
"private_key_id": "[KEY-ID]",
"private_key": "-----BEGIN PRIVATE KEY-----\n[PRIVATE-KEY]\n-----END PRIVATE KEY-----\n",
"client_email": "[SERVICE-ACCOUNT-EMAIL]",
"client_id": "[CLIENT-ID]",
"auth_uri": "https://accounts.google.com/o/oauth2/auth",
"token_uri": "https://accounts.google.com/o/oauth2/token",
"auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
"client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/[SERVICE-ACCOUNT-EMAIL]"
}
```
4. Create jenkins [**Secret file**](https://jenkins.io/doc/book/using/using-credentials/) with the service account keys credentials created above.  
`In the ID field, specify a meaningful credential ID value - for example, jenkins-user-for-xyz-artifact-repository. You can use upper- or lower-case letters for the credential ID, as well as any valid separator character. However, for the benefit of all users on your Jenkins instance, it is best to use a single and consistent convention for specifying credential IDs.
Note: This field is optional. If you do not specify its value, Jenkins assigns a globally unique ID (GUID) value for the credential ID. Bear in mind that once a credential ID is set, it can no longer be changed.`
5. Make a note of the jenkins secrets, which we will be using going forward.
6. Install [Helms](https://helm.sh) in Google Kubernetes Engine (GKE) cluster.

### FAQ
