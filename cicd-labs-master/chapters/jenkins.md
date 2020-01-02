# Setting up Continuous Integration with Jenkins

Following are the learning objectives with this lab

  * you will begin by setting up Jenkins using docker
  * take a walkthrough of Jenkins web interface and essential configurations
  * start creating freestyle and maven jobs
  * integrate with tools such as nodeJS  and maven
  * integrate with GitHub and setup build triggers  and.
  * setup a pipelines which run  automated builds  and unit tests


## Setup Jenkins with Docker

Here you are going learn, how to setup jenkins using docker. prerequisite for this is docker installation.

You could run a jenkins container on your docker host by using official jenkins image with the version `2.178-Slim`. Use following command to launch the jenkins container,

```
docker container run -idt --name jenkins -P -p 8080:8080 -v jenkins_home:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock jenkins/jenkins:2.204-slim
```

![](./images/jenkins1.png)

You could access  jenkins UI by browsing to  `http://IPADDRESS:8080`  

![](./images/jenkins2.png)

To fetch the initialAdminPassword  use the following command

```
docker logs jenkins
```
and paste it on the Jenkins UI to unlock.


In the next step , choose **install suggested plugins** to configure the default plugins automatically.

Once plugins are  installed, you will  create the  admin user using the form presented.
![](./images/jenkins3.png)

completing this process will get you to jenkins main page where you could get started creating jobs from.



## Creating first Jenkins Job

After setting up jenkins, its now time to create your first jenkins job and run it.

You could create jenkins job to build one of the applications with the instavote sample app you have learnt about.  You need to create your version of this app by forking it. Visit [example-voting-app on Github](https://github.com/lfs261/example-voting-app) and fork the repository onto your git account.

Once you forked the repository, goto jenkins dashboard and clickon **create new Jobs**.

Now you are going to create a `freestyle` job with the name of `job-01` and click ok to configure your job. You will get job configuration page where you could choose the following options.

**Steps:**

  * Goto `job-01` cofiguration page, add description of your job.  
  * Under source code management choose git and provide your project repository url copied from github repo.  
![](./images/jenkins4.png)  
  * Next select  **build** tab  and choose **execute shell** to run following commands

    ```
     ls -ltr
     sleep 10
    ```
    ![](./images/jenkins5.png)

  * once you fill in the build step,  save the job configuration page.

  * Goto your project page , choose build option to build your job. Once your build finished, click on your build and check build execute status.
  ![](./images/jenkins6.jpg)

  * Every build you could see build status, it will store logs of your build.

  * Observe the color coding in the build status. If your job is successful it will show as blue, if it fails you could see it turn red



### Configuring a maven build job


You have already  created a simple, freestyle project with name  *job-01* and tested it. Now is the time to build the **worker** application as part of the instavote project, which is a  java application that uses  maven as a build tool.  

You could follow the following steps to configure maven build job.

**Steps:**

  * Goto `manage jenkins -> global tools configuration` and under maven section, provide name as `Maven 3.6.2` and select the maven version `3.6.2`, save those changes.
![](./images/jenkins8.png)


  * Install `Maven integration` plugin.  Goto `Manage Jenkins => Manage Plugins =>  Available`,search for **Maven Integration** plugin and install it without restart.
![](./images/jenkins7.png)

  * Create `instavote` folder for your project which serves as a namespace. Do so by creating a new job with type **folder** with name *instavote*

  * Switch to instavote folder, create a new project and select **Maven** as the type of project. Name the job as **worker-build**

  * Next goto source code management, provide your git repository url

  * Go to  build step and provide the  goal and option as `compile`.
![](./images/jenkins9.png)

  * At the build step, you would also need to provide the  path to pom.xml, which in the worker subdirectory of the repo.  e.g. worker/pom.xml as
![](./images/jenkins10.png)


Save the job and build. Observe the job status, console output etc.   


### Adding unit test and packaging jobs

After creating the build job, its now time to add test and package jobs for the worker application.

You need to create one more job on your same folder and name it as `worker-test`, while creating `worker-test` copy `worker-build` job. Follow following steps to complete the configuration.

Steps

  * In `worker-test` job , change your description as `test worker java app`. Source code management,repository is same .

  * under build step, change the goals and option as `clean test` and remaining will be same, so save the job and build.
![](./images/jenkins11.png)


  * Next job is `worker-package`, this will compile the application and then generate the jar file. Create a job on same folder with the name of `worker-package`, while creating
job copy `worker-test` or `worker- build` job.


  * Update the description as `package worker java app, create jar` and only change in the configuration is build step.

  * In the build step for the package job,  change the goal  to `package -DskipTests`, save the changes and build.


  * After build succesful, you could see the jar file created in the  workspace. Example is given following , you can verify your workspace by using it.
![](./images/jenkins12.png)

  * From the  post build actions, choose archive artifacts and provide path `**/target/*.jar` to store the jar file.
![](./images/jenkins13.png)

  * Save the changes and build the job. Once build succesful, check the project page  to find out your artifacts right out there.


##  Configuring Build Triggers


You could use anything under build triggers for automatic build, but now you are going to use `poll scm` under build triggers.

  * goto your `worker-build` job configuration page and choose `poll SCM` under build triggers. In that poll scm mention schedule for  periodically polling the git repository.

```
H/2 * * * *
```

  * Once you made changes, save the job. goto your job page there you will find `Git polling log`, check your polling logs by using Git polling log.
  ![](./images/jenkins14.png)

  *  Now you are going to use `Trigger builds remotely` in build trigger. provide your authentication token randomly and save it. Use the following example to trigger the build using browser.
  ![](./images/jenkins15.png)


  * You should get a trigger URL mentioned just below where you defined the token on Jenkins job configuration page. Generate your custom  trigger URL similar to following  

  ```
  http://localhost:8080/job/instavote/job/worker-build/build?token=yourauthenticationtoken
  ```

  * If you paste this URL on the browser, since you are already authenticated, you should see the job launched automatically.  

  * You could also trigger the build using a command line interface or programtically from external code by providing the api token.  

  * To create the api token by browsing to  `jenkins -> people -> admin -> configure` as shown in the following image.  
   ![](./images/jenkins16.png)

  * Copy the api token to generate a remote trigger URL similar to following,

  ```
   http://admin:TOKEN@localhost:8080/job/instavote/job/worker-build/build?token=yourauthenticationtoken
  ```

  * Test trigger the build using a curl command or equivalent
  ```
   curl http://admin:yourapitoken@localhost:8080/job/instavote/job/worker-build/build?token=yourauthenticationtoken
  ```

The above instructions  demonstrate  how to trigger builds remotely.

## Creating a Job pipeline

Here you will learn, how to links the  jobs by defining upstreams and downstreams. You would also go on to create a pipeline view using a plugin.

Follow the following steps to setup upstream and downstream

  * From `woker-build` job configuration page, scroll all the way to **post build actions**, this is where you could define the downstream job. Provide `worker-test` as the job to build and save.
  ![](./images/jenkins17.png)

  * Now you are going to setup upstream for `worker-package`. goto `worker-package` configuration page. From **build triggers**, check the box for  `build after other projects are built`  Provide upstream project name as `woker-test`job
  ![](./images/jenkins18.png) this defines the upstream for worker-package.

Once upstreams and  downstreams are defined, run `worker-build` and it will automatically run `worker-test` & `worker-package`.

### Setup Pipeline View

Now you are going to setup pipeline view for this build jobs,

  * Begin by installing  `**Build Pipeline**` plugin from manage jenkins page.

  * From jenkins console, create a new view. Select type as pipeline view and provide name for it.

  * From the  configuration page,  select the first job in the pipeline and select number of builds as 5, save it.
![](./images/jenkins19.png)

  * Once you complete, you could see **Build Pipeline** of your job. Green is successful, red is failed and blue is to do, yellow is in progress.

You have just completed creating a pipeline for a java project.


## Assignment - Create a pipeline for the Nodejs app

Here you need to create pipeline for result application and use build tool as npm.

  * Create two jobs, first by name `result-build` which will will run **npm install** and define the second job as downstream.  
  * `result-test` is the second job, which  will run **npm test** as the command.

  These two should  be triggered automatically via a PollSCM trigger scheduled to run every two minutes .
