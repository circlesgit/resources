# Setting up Continuous Integration with Jenkins
In this lab, you will learn how to run jenkins by using docker container and configure it, launching jobs, adding unit test and packaging Jobs. configuring build triggers to auto launch jenkins, defining downstream/upstream and a pipeline view. how to integrate github with jenkins to setup jenkins, configuring job status with commit messages and setting up a CI pipeline for a NodeJS app.

### Setup jenkins with docker :-
Here you are going learn, how to setup jenkins using docker. prerequisite for this you need docker installed locally.

You could run a jenkins container on your docker host by using official jenkins image with the version `2.178-Slim`. Use below command to run a jenkins container,
```
docker container run -idt --name jenkins -P -p 8080:8080 -v jenkins_home:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock jenkins/jenkins:2.178-slim
```
![](./images/jenkins1.png)
You could use `localhost:8080` on your browser to visit jenkins.
![](./images/jenkins2.png)
Once you visit jenkins, run `docker logs jenkins` to find out the initialAdminPassword and unlock it.
```
docker logs jenkins
```
Next step , choose install suggested plugins to configure automatically or else you could choose plugin which you want.

Once plugin got installed, you will get create first admin user page, fill your detail and continue the procedure to finish the configuration and finally you will get jenkins page where you could create jobs.
![](./images/jenkins3.png)
### Jenkins configurations walkthrough :-
Previously you configured jenkins using Docker, now you are going to know about what are all the user and job related configurations in jenkins.

You could use new items for creating jobs, view build history, create views for jobs, manage jenkins, etc.

Manage Jenkins is the most important one in jenkins, there you could manage plugins, configure sytem, global tool configuration , configure credentials and more.

* configure system :- here you could mentions number of executors, labels, email notifications, global properties and etc
* Configure Global Security :- here you could mention authentication and authorization properties,  CSRF protection and etc,
* Global Tool Configuration :- here you could define tools whatever you use to bulid or compile jobs such as docker, Nodejs, Maven, Git, JDk, etc.
* Manage pluins :- here you could search and install or update plugins whatever you need to build or compile the jobs.

### Launching your first jenkins job :-
Here you will learn, how to create your first jenkins job, run it and check the status of the job.

You could create jenkins job for build your application. You need application for this job, so visit github [example-voting-app](https://github.com/lfs261/example-voting-app) and fork the repository for creating your job.

Once you forked the repository, goto jenkins dashboard and click create new Jobs. You could create any type of job like freestyle, pipeline, multibranch pipeline, etc.

Now you are going to create a `freestyle` job with the name of `job-01` and click ok to configure your job. You will get job configuration page there you could configure your job.
Steps :-
* Goto `job-01` cofiguration page, add description of your job.
* Under source code management choose git and provide your project repository url. If it a private repository you need to provide password for that, but now it is a public repository, so no need to worry about password.
![](./images/jenkins4.png)
* Next goto build and choose execute shell to run below commands, once you complete the commands, save & apply to build your job.

    ```
    * ls -ltr
    * sleep 10
    ```
![](./images/jenkins5.png)
* Goto your project page , choose build option to build your job. Once your build finished, click on your build and check build execute status.
![](./images/jenkins6.jpg)
* Every build you could see build status, it will store logs of your build.
* if your job is succesful it will show on blue, if it's fail you could see red over there.
* you could add some commands or shell script you want to run and save it, again build your job to get result.

### Configuring a maven build job :-
Previous lab you have created simple job-01 and tested, now you are going to learn how to configure jenkins to build maven based application, use maven integration plugin and compile the worker application.

You could follow the below steps to configure maven build job.
Steps :-
* first you need to install `Maven integration` plugin, for that goto `manage jenkins -> Available`,search maven integration plugin and install with out restart.
![](./images/jenkins7.png)
* You need to create `instavote` folder for your project, once you create folder it is easy to create all your service jobs separately and run.
* Once you create folder, inside the folder create your first maven job with the name of `worker-build` and mention your description anything you would understand.
* Next goto souce code management, provide your git repository url and this is public repository, so you no need to provide password.
* Goto build step, it will automatically inject configurations related to maven. after that you need to setup maven.
* Goto `manage jenkins -> global tools configuration` and under maven section, provide name as `maven 3.6.1` and select the maven version `3.6.1`, save those changes.
![](./images/jenkins8.png)
* Again goto job configuration under build step mention the goal and option as `compile`.
![](./images/jenkins9.png)
* save the job and build, the result job will be failed because you are not provided exact path of pom.xml, so provide the exact path in build step and save it, again run your build.
![](./images/jenkins10.png)
Finally you have succesfully build maven job.
### Adding unit test and packaging jobs :-
Previous lab you build maven job, now you will learn how to test and package the application.

You need to create one more job on your same folder and name it as `worker-test`, while creating `worker-test` copy `worker-build` job. Follow below steps to complete the configuration.

Steps :-

* In `worker-test` job , change your description as `test worker java app`. Source code management,repository is same .
* under build step, change the goals and option as `clean test` and remaining will be same, so save the job and build.
![](./images/jenkins11.png)
* your all unit test will be completed succesfully.you are going to run unit test after every commit to the master branch.
* Next job is `worker-package`, this will compile the application and then generate the jar file. Create a job on same folder with the name of `worker-package`, while creating
job copy `worker-test` or `worker- build` job.
* Update the description as `package worker java app, create jar` and only change in the configuration is buld step.
* In your buld step change and options as `package`, save the changes and build.
* After build succesful, you could see the jar file created on your workspace. Example is given below , you can verify your workspace by using it.
![](./images/jenkins12.png)
* You could see the `worker package` job doing test and packaging it, now you are going to skip that test by editing build step.
* In your `worker-package` build step , change the goals and options as `package -Dskip Tests` and the next one is archive the artifacts by using post build action.
* In your post build action, choose archive artifacts and provide path `**/target/*.jar` to store the jar file.
![](./images/jenkins13.png)
* Save the changes and build the job. Once build succesful, check your workspace to find out your artifacts.

###  Configuring build triggers to auto launch  jenkins jobs :-
Here you will learn, how to trigger the job automatically using schedule or remote script.

You could use anything under build triggers for automatic build, but now you are going to use `poll scm` under build triggers.
* goto your `worker-build` job configuration page and choose `poll SCM` under build triggers. In that poll scm mention below time interval for periodically poll the git repository, if there is any change in the git repository it will build your job.
```
H/2 * * * *
```
* Once you made changes, save the job. goto your job page there you will find `Git polling log`, check your polling logs by using Git polling log.
* polling scm is the very flexible, even if you are running jenkins behind the firewall.
![](./images/jenkins14.png)

Now you are going to use `Trigger builds remotely` in build trigger. provide your authentication token randomly and save it. Use the below example to trigger the build using browser.
![](./images/jenkins15.png)
Example url :-
```
localhost:8080/job/instavote/job/worker-build/build?token=yourauthenticationtoken
```
* Once you trigger the job from browser it will run the buld.
* You could use CLI to trigger the build, that time you need to provide username & password with the url else api token.
* To create the api token, goto `jenkins -> people` and choose admin user, on your left side bar configure will be there, select the configure option and there you will create new api token.
 ![](./images/jenkins16.png)
* Copy the api token some where and save the configuration. You could run below command to trigger the job from CLI.
```
curl http://admin:yourapitoken@localhost:8080/job/instavote/job/worker-build/build?token=yourauthenticationtoken
```
This is how you could automatically trigger the builds.

### Defining downstreams/upstreams and a Pipeline view :-
Here you will learn, how to links your job and run it in a sequence, setup pipeline view. Before that you need to know about upstreams & downstreams.

Follow the below steps to setup upstream and downstream :-
* goto your `woker-build job` configure page, add `projects to build`  in `post build actions`.
* provide `worker-test` job in projects to build and save the job configuration.
![](./images/jenkins17.png)
* Once you complete downstream, you could see `worker-test` as downstream job in `worker-build`.
* Now you are going to setup upstream for `worker-package`. goto `worker-package` configuration page, choose `build after other project build` under `build trigger`.
* provide `woker-test`job in `build after other project`.
![](./images/jenkins18.png)

Once you complete upsteam & downstream, run `worker-build` and it will automatically run `worker-test` & `worker-package`. this is how upsteam & downstream works.

Now you are going to setup pipeline view for this build jobs, for that you need to install `build pipeline` plugin from manage jenkins page. follow the below steps to setup pipeline.
* goto manage `jenkins -> manage plugin`,search `build pipeline` and install it.
* goto the project page, there you can see `build pipeline view`. select pipeline view and provide the view name and click ok.
* after that you will redirect to the configuration page , there you can see pipeline flow. In that pipeline flow select the first job in the pipeline and select number of builds as 5, save it.
![](./images/jenkins19.png)
* Once you complete, you could see build pipeline of your job. Green is succesful, red is failed and blue is on the process.

This is how you could setup pipeline view of your project or jobs which you have.

### Integrating github with jenkins to setup webhook based triggers :-
Here you will learn, how to integrate github with jenkins and trigger build automatically using github webhooks.

You could trigger your job when there is a commit changes in master branch. webhook will be trigger your job from github side and even you could send status back to github, so you don't need polling.

Follow the below steps to setting up the webhook :-
* first you need github tocken, so goto github user page select `settings -> choose developer settings -> select personal access tocken`. In the personal access tocken page select `generate new tocken`, mention your note and generate it. once you generate your tocken save it anywhere, because  you can't view it again.
![](./images/jenkins20.png)
* Now goto jenkins page, `manage jenkins -> configure system`, choose github to add github server. provide name of your github account and add your credentials like below image,
![](./images/jenkins21.png)
here secret is your github secret id and you need to provide your id description, save it.
* Now choose credentials as `secret tocken github` and check the manage hooks. save the configuration once you complete .
![](./images/jenkins22.png)
* Goto your `worker-build` configure page, choose `github project` and provide your repository url over there. Remove poll scm and choose github hook trigger for gitscm polling, save the changes.
* Again goto your github repository and choose `settings -> webhooks`, add payload url and content type as application/json, save it(refer example image given below).
![](./images/jenkins23.png)
* Now goto git repository, add some file in your repository and commit the changes.
* Once you make commit on your repository, it will automatically trigger your build. You could see it by using build pipeline which you have created.

This is how you could integrate github with jenkins.

### Adding jenkins status badges to github :-
In this lab, you are going to setup  two way communication. you could send the status of the build to the github and show the status right from the repository.

You need to install `Embeddable Build Status` plugin from `manage jenkins -> manage plugin -> Available`. Once installation done, you could see Embeddable build status on your every job page.

Follow the below steps to add jenkins status badge to github :-
* Goto your `worker-build` job page, there select embeddable build status. In that embeddable status page, copy `markdown unprotected link` under `links` and paste it on your github repository README.md file which you have created last lab and commit the changes.   
* Once you make commit changes, you could see `build status  passing` on your same README.md file and if you see jenkins job, it will automatically triggers the build which you have created.
![](./images/jenkins24.png)
* goto `worker-build` configuration page, under build step change the root file as `pom.xml` insted of `worker/pom.xml` and save the changes.
* Build your `worker-build` job, it will fail your build. Now goto github repository page there you could see build failed status.
* Correct your `worker-build` and add the embeddable build status `markdown unprotect link` of worker-test & worker-package to your github repository README.md file and test it by your self.

This how you could add jenkins status badge to github.
### Configuring  job status with commit messages :-
Here you will learn, how to configure build & test job status with github commit messages.

Follow the below steps to configure job status :-
  * Goto your `worker-build` job configuration page, under post build action choose `Set github communication status (universal)` and make status result as `One of default messages and statuses`, save the changes.
  ![](./images/jenkins25.png)
  * Copy your `github project url` form worker-build and paste it on `worker-test` and add the post build action `Set github communication status(universal)` for `worker-test` as well. refer previous step to add post build action.
  * Now goto your `worker-test` job page, there select embeddable build status. In that embeddable status page, copy `markdown unprotected link` under `links` and paste it on your github repository README.md file, add `subject=Unittest` with your `worker-test` link. Refer example image given below.
  ![](./images/jenkins26.png)
  * Once you commit the changes, it will automatically build the triggers. you could see the builds in pipeline view and the status will be updated on the github.
  * If you check your commit messages it will be check mark. even you could see all the current build status with build number on git commit message. If you click details in commit message, it will take you to jenkins page.
  * You could change the `worker-build` git status color by adding below subject in your git repository `README.md`file.
  ```
  [![Build Status](http://localhost:8080/buildStatus/icon?job=instavote%2Fworker-build&subject=Build&color=blue)](http://localhost:8080/job/instavote/job/worker-build/)
  ```
  * After updating the changes in `README.md` commit the file . It will triggers the pipeline. This time build will fail and you could see build color has changed in git status message.
  * If the build is failed, git commit message will be in red mark.
  * Fix your `worker-test` job and save the changes, by providing correct path and add `pre steps` from configuration, choose `set build status "pending" on GitHub commit` and save the configuration.

  * Make one more commit in your git repository `README.md` file by using below code and commit the file.
  ```
  [![Build Status](http://localhost:8080/buildStatus/icon?job=instavote%2Fworker-test&subject=UnitTest&color=pink)](http://localhost:8080/job/instavote/job/worker-test/)
  ```
  * Once you commit the file it will triggers the build but in commit message you could see pending state till the build get finish and whenever it get finished the status will be change with the check mark.

This is how you could configure job status to git commit message.

### Assignment - Create a pipeline for nodejs app :-
Here you need to create pipeline for result application and use build tool as npm.

Create tow jobs, first one`result-build` job will run npm install after git chekin with downstream of second job.`result-test`is second job, it will run npm test. These two will be trigger automatically via webhook.
