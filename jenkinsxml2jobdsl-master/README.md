# Jenkins XML to Job DSL converter

![](https://s-media-cache-ak0.pinimg.com/564x/de/60/b0/de60b07fa39022595c4a197135ea1bda.jpg)

Converts a Jenkins job's config.xml to Job DSL

**Table of Contents**

- [What is it?](#what-is-it)
- [Prerequisites](#prerequisites)
- [Building](#building)
- [Running](#running)
- [Configuration](#configuration)
- [Publishing](#publishing)

### What is it?

This is a tool for converting a Jenkins job's config.xml to a Job DSL script

### Prerequisites

The only thing you should need to get up and running with this is a valid JDK installation

### Building

You can build the jar as follows:

```
git clone https://github.com/hellmouthengine/jenkinsxml2jobdsl
cd jenkinsxml2jobdsl
./gradlew build
```

### Running

Run the jar with the following command:

```
java -jar build/libs/jenkinsxml2jobdsl.jar [-u <jenkins username>] [-a <jenkins api token>] -j <jenkins server> [-p <jenkins port>] [-r <project name>] job1 job2 ... jobN
```

The converter will write the converted Job DSL scripts to the `jobs/` directory

### Configuration

The following command line flags are supported:

flag | description
---- | -----------
-u | The username to use when connecting to the Jenkins instance
-a | The API token to use when connecting to the Jenkins instance, accessible via `<jenkins url>/user/<username>/configure`
-j | The URL of the Jenkins instance to connect to, e.g. myjenkins.myhost.com
-p | The port on which the Jenkins instance is listening, defaults to 8080
-r | The project under which the job exists

The converter accepts a list of space-delimited job names as they are displayed under the "Project name" field of the job configuration page in Jenkins

### Publishing

On the Jenkins instance you want to deploy the Job DSL scripts against, you need to have the Job DSL plugin installed, and a seed job created

With a seed job created, configure the job, and navigate to the build actions section of the configuration. Add the "Process Job DSLs" build step, and either paste in a generated Job DSL script, or use the file browser to point to a file on disk

Save the configuration, and then run the seed job. The Job DSL plugin will create a new Jenkins job, which will appear on the Jenkins home page.

