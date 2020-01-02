# Continuous Testing

 This section is dedicated to automated testing and here are your learning objectives,

   * You are going to begin by analysing code coverage with Jococo
   * Learn how to improve the code coverage
   * Setup sonarqube to automatically scan your repository
   * Create a  project gating system  to automatically decide whether the code is safe to deploy
   * Add integration and acceptance tests  with docker compose
   and finally,  incorporate all of these into the Jenkins pipeline

### Code coverage with Jacoco

STEPS:

  * Fork this repo:  https://github.com/lfs261/maven-examples
  * Add a maven job to run  test on it
  * From post build action, publish Jacoco reports
  * Merge ut1, and ut2 branches into master and run the jobs again

### Adding Static Code Analysis with Sonarqube

Steps:

  * Setup sonarqube
  * Install sonarqube scanner plugin for Jenkins
  * Create token on sonarqube
  * Add Jenkins system/global tools  configuration for sonarqube
  * Add build breaker plugin to sonarqube:  https://github.com/adnovum/sonar-build-breaker/releases/download/v2.3/sonar-build-breaker-plugin-2.3.jar




#### Sonarqube stage code snippet for Jenkinsfile [options for the classroom course]

```
stage('Sonarqube') {
    agent any
    when{
      branch 'master'
    }
    environment{
      sonarpath = tool 'SonarScanner'
    }
    steps {
        echo 'Running Sonarqube Analysis..'
          withSonarQubeEnv('sonar') {
            sh "${sonarpath}/bin/sonar-scanner -Dproject.settings=sonar-project.properties"
          }
    }
}

```


## Adding Integration Tests

STEPS:

  * vote/integration_test.sh
  * run as part of vote pipeline

```
stage('vote integration'){
    agent any
    when{
      changeset "**/vote/**"
      branch 'master'
    }
    steps{
      echo 'Running Integration Tests on vote app'
      dir('vote'){
        sh 'integration_test.sh'
      }
    }
}

```

## Adding e2e tests

  * ./e2e.sh
  * run as independent job
