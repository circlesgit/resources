# Task
 - Deploy a Dockerized application serving a static website via (e.g. via Nginx) that displays a custom welcome page (but not the default page for the web server used)
 - Use fluentd to ship Nginx request logs to an output of your choice (e.g. S3, ElasticSearch)
 - Provide a standalone solution including both webapp and fluentd using docker-compose and/or a kubernetes deployment (plain manifest or helm chart)
Notes
 - Avoid running multiple services in single container
 - You can use any 3rd party Docker image (you might have to explain your choice)
 - Bonus: use an IAC tool of your choice to create cloud resources you may need (e.g. S3
buckets)

# Solution
## Docker image details `docker-images/`:
#### nginx image
 - build from nginx:1.15.10 base image.
 - copies content from the folder `static-html-directory` to `/usr/share/nginx/html`
 - copies custom nginx.conf file to `/etc/nginx/nginx.conf`
#### fluent base
 - build from alpine:3.7 base image.
 - fluent is installed to the base image.
 - custom entrypoint script is copied.
 - entrypoint.sh check for the fluent configuration and stop the container on configuration error.
 - fluent starts if there is no issue with the configuration.

## Helm chart details `nginx-helm-chart/`
helm charts consists of two container.
#### Application container
 - mount empty dir `log-storage` to the path ` /var/log/nginx`
#### sidecar fluentd container
 - mounts empty dir `log-storage` to the path ` /mnt/log` as readOnly.
 - mounts configuration from configmap `sidecar-configuration` to the path `/etc/fluentd/files`
 - populates the following environment variable `AWS_KEY_ID,AWS_SECRET_KEY,S3_BUCKET_NAME,S3_REGION` from secret.
## provisioning s3 bucket.
 - run `apply.sh` script inside `create_bucket/` folder to create s3 bucket.
