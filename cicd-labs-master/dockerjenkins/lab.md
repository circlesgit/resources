# Using docker with Jenkins Pipelines

Objectives:

  * You will learn how to prepare Jenkins environment to build with docker agent
  * Refactor Jenkinsfile with   docker based agents
  * Build and test  nodejs, maven and python jobs with docker


Steps:

  * Read : [Using Docker with Pipeline](https://jenkins.io/doc/book/pipeline/docker/)
  * [Install docker inside jenkins container](https://gist.github.com/initcron/feb53b3b8b0e45225dcd1a438768ec81)
  * Restart Jenkins Container with Docker
  * Refactor the Jenkinsfile for worker app using the following reference


### Test docker agent

Create a test pipeline to run a job with docker agent


Create `docker-pipe-01` pipeline job in your jenkins. In configuration page, mention the below test code in jenkins script and save the configuration and build it.
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
Run the job, and check if that worked? If not, proceed with the next section to setup docker. 

### Install docker inside jenkins container


Exec into jenkins container as root,

```
docker exec -it -u root jenkins bash

```

Run the following set of commands to install docker client and prepare Jenkins to launch containers from,

```

apt-get update

apt-get install -yq \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg2 \
    software-properties-common

curl -fsSL https://download.docker.com/linux/debian/gpg |  apt-key add -


add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/debian \
   $(lsb_release -cs) \
   stable"

apt-get update

apt-get install docker-ce docker-ce-cli containerd.io

usermod -a -G docker jenkins

chown 777 /var/run/docker.sock

curl -L "https://github.com/docker/compose/releases/download/1.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

chmod +x /usr/local/bin/docker-compose

exit
```

Restart jenkins container

```
docker restart jenkins
```

Exec into jenkins container again and validate,

```
docker exec -it jenkins bash

docker ps

```

If you see the list of containers, docker client is been installed and configured ok.  



`file: worker/Jenkinsfile`


```
pipeline{

    agent{
       docker{
         image 'maven:3.6.1-jdk-8-slim'
         args '-v $HOME/.m2:/root/.m2'
       }

    }


    stages{
        stage('build'){
            steps{
                echo 'building worker app'
                dir('worker'){
                  sh 'mvn compile'
                }
            }
        }
        stage('test'){
            steps{
                echo 'running unit tests on worker app'
                dir('worker'){
                  sh 'mvn clean test'
                }
            }
        }
        stage('package'){
            steps{
                echo 'packaging worker app into a jarfile'
                dir('worker'){
                  sh 'mvn package -DskipTests'
                  archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                }
            }
        }
    }

    post{
        always{
            echo 'the job is complete'
        }

    }

}
```


#### Exercise

  * Refactor the Jenkinsfile for result app
  * Create a Jenkinsfile and a multi branch pipeline for vote python app
