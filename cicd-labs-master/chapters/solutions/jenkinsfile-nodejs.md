### Writing jenkinsfile for a NodeJS app :-

Here you will verify your declarative pipeline for result and if you have any doubts to create Jenkinsfile for result, follow the below steps to complete the pipeline.

#### Steps

  * Create a branch `feature/resultpipe` using git checkout command.
```
git checkout -b feature/resultpipe
```
  * Create a Jenkinsfile inside result directory or copy the previous Jenkinsfile file from worker to result.

  * In Jenkinsfile, add nodejs tools with exact version that configured in `manage jenkins -> global tool configuration -> Nodejs`.


`file: result/Jenkinsfile`

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
  }
}
```
commit the changes into `feature/resultpipe` branch by using below commands and it will automatically
```
 git status
 git add feature/resultpipe
 git commit -am "adding Jenkinsfile for result app"
 git push origin feature/resultpipe
```
  * In your jenkins, create new multibranch pipeline as `result-pipe` and copy form `worker-pipe`.

  * go to your `result-pipe` configuration page add description as `instavote result multi branch pipeline` and under build configuration change the script path as `result/Jenkinsfile` and save the configiuration, it will start scaning your repository and start your build as well.

  * go to `result/test/mock.test.js` file, add the one more more mocktest form exiting mocktest. commit the changes to `feature/resultpipe` branch using  below commands.
```
git status
git add feature/resultpipe
git commit -am "adding mock test"
git push origin feature/resultpipe
```  
Once you  commit changes into the branch, it will automatically build the pipeline and gives the result. Now create a pull request from main github account and have it reviewed,  merge with master branch. Once merged, jenkins will run pipeline job for master branch  as well.
