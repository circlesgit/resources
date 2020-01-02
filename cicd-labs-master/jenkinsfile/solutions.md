### Writing jenkinsfile for a NodeJS app :-
Here you will verify your declarative pipeline for result and if you have any doubts to create Jenkinsfile for result, follow the below steps to complete the pipeline.

Steps :-
Create a branch `feature/resultpipe` using git checkout command.
```
git checkout -b feature/resultpipe
```
Create a Jenkinsfile inside result directory or copy the previous Jenkinsfile file from worker to result.
In Jenkinsfile, add nodejs tools with exact version that configured in `manage jenkins -> global tool configuration -> Nodejs`.
Refer your Jenkinsfile by using below code.
```
pipeline {
  agent any

  tools{
    nodejs 'NodeJS 8.9.0'

  }

  stages{
      stage(build){
        when{
            changeset "**/result/**"
          }

        steps{
          echo 'Compiling result app..'
          dir('worker'){
            sh 'npm install'
          }
        }
      }
      stage(test){
        when{
          changeset "**/result/**"
        }
        steps{
          echo 'Running Unit Tets on result app..'
          dir('result'){
            sh 'npm test'
           }

          }
      }
  }

  post{
    always{
        echo 'Building multibranch pipeline for worker is completed..'
    }
    failiure{
      slackSend (channel: "instavote-cd", message: "Build Failed - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)")
    }
    success{
      slackSend (channel: "instavote-cd", message: "Build Succeeded - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)")
    }
  }
}
```
Now you have a Jenkisfile which install & test npm and it will send slack notification as well.
commit the changes into `feature/resultpipe` branch by using below commands and it will automatically
```
 git status
 git add feature/resultpipe
 git commit -am "adding Jenkinsfile for result app"
 git push origin feature/resultpipe
```
In your jenkins, create new multibranch pipeline as `result-pipe` and copy form `worker-pipe`.
goto your `result-pipe` configuration page add description as `instavote result multi branch pipeline` and under build configuration change the script path as `result/Jenkinsfile` and save the configiuration, it will start scaning your repository and start your build as well.
goto `result/test/mock.test.js` file, add the one more more mocktest form exiting mocktest. commit the changes to `feature/resultpipe` branch using  below commands.
```
git status
git add feature/resultpipe
git commit -am "adding mock test"
git push origin feature/resultpipe
```  
Once you make commit change into the branch, it will automatically build the pipeline and gives the result. Now create a pull request from main github account and review from another account and merge with master branch. Once you merge with master branch, jenkins will run pipeline job for master branch and you could check slack notification as well. Disable our previous jobs workerbuild, resultbuild.

Now you have learned how to write a Jenkinsfile for nodejs application.
