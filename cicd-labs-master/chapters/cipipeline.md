# Using Docker to Simplify CI Pipelines

In this Lab, you will learn how to prepare jenkins for docker agent based build, refactoring maven jobs with docker agent and how to build nodejs app with docker. How to write jenkinsfile for python based vote app and how to troubleshoot docker agent jobs, pull requests, code review and merge to master branch and you have assignment as well.

### Preparing jenkins for docker agent based builds

Here you will learn, how to prepare your jenkins server for docker based build, refer [docker-with-pipeline](https://jenkins.io/doc/book/pipeline/docker/) document.

Create `docker-pipe-01` pipeline job in your jenkins. In configuration page, mention the following test code in jenkins script and save the configuration and build it.
```
pipeline {
    agent {
        docker { image 'node:7-alpine' }
    }
    stages {
        stage('Test') {
            steps {
                sh 'node --version'
            }
        }
    }
}
```   
If you run the build, it shows build will be failed because docker is not installed on your jenkins container, so you need to install docker inside the jenkis container and use the following command to install docker inside jenkins container.
```
* docker ps
* docker exec -u root -it jenkins bash
* apt-get update
*  apt-get install apt-transport-https ca-certificates curl gnupg2 software-properties-common
* curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
* apt-key fingerprint 0EBFCD88
* add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"
* apt-get update
* apt-get install docker-ce docker-ce-cli containerd.io
```
Refer the docker install documentation for  [docker-install](https://docs.docker.com/install/linux/docker-ce/debian/) debian.

After installing docke, run the following commands to check the docker version.
```
* which docker
* docker version
```
Run the hello world container inside the jenkins container to validate docker.
```
docker run hello-world
```
Now run the build, agin you will get permission denied error, because jenkins user not a part of docker group. use following command inside jenkins container to add jenkins user into docker group and validate.
```
* usermod -a -G docker jenkins
* docker ps
```
After adding jenkins user to docker group, exit from jenkins container and restart the container by using following command.
```
docker restart jenkins
```
Goto jenkins page and build the job, the pipeline will be successfull and your jenkisn serever is ready to run docker based build.

### Refactoring maven jobs with docker agent
