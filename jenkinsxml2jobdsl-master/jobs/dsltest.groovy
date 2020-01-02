freeStyleJob('dsltest_converted') {
  
  description('')
  
  displayName('dsltest_converted')
  
  keepDependencies(false)
  
  scm {
    
  }
  
  quietPeriod(0)
  
  checkoutRetryCount(0)
  
  disabled(false)
  
  concurrentBuild(false)
  
  configure { project ->
    
    project / 'builders' << 'javaposse.jobdsl.plugin.ExecuteDslScripts'(plugin:'job-dsl@1.70') {
      
      'scriptText'('''mavenJob(\'jkretsch_converted\') {
          
          goals(\'--update-snapshots clean verify -Pperformance -Djmeter.test.plan=eligibility-web-average.jmx\')
          
          mavenOpts(\'\')
          
          incrementalBuild(false)
          
          incrementalBuild(false)
          
          archivingDisabled(false)
          
          siteArchivingDisabled(false)
          
          fingerprintingDisabled(false)
          
          resolveDependencies(false)
          
          runHeadless(false)
          
          disableDownstreamTrigger(false)
          
          description(\'Performance test job for PBEC average test.\')
          
          displayName(\'jkretsch_converted\')
          
          keepDependencies(false)
          
          logRotator {
              
              artifactDaysToKeep(-1)
              
              artifactNumToKeep(-1)
              
              daysToKeep(-1)
              
              numToKeep(20)
              
          }
          
          parameters {
              
              booleanParam(\'DEPLOY_FIRST\', false, \'Perform a deployment first\')
              
              stringParam(\'DEPLOY_HOST\', \'ea-1904\', \'Deploy to VSP Performance Dashboard\')
              
              stringParam(\'BUILD_VERSION_NUMBER\', \'18.8.*\', \'Build number\')
              
          }
          
          properties {
              
          }
          
          scm {
              
              git {
                  
                  branches(\'origin/master\')
                  
                  remote {
                      
                      url(\'ssh://git@git.vspglobal.com:7999/test/api-test.git\')
                      
                      credentials(\'\')
                      
                      name(\'\')
                      
                      refspec(\'\')
                      
                  }
                  
              }
              
          }
          
          quietPeriod(0)
          
          checkoutRetryCount(0)
          
          disabled(false)
          
          concurrentBuild(false)
          
          triggers {
              
              cron(\'@midnight\')
              
          }
          
          wrappers {
              
              timestamps()
              
          }
          
          configure { maven2moduleset ->
              
              maven2moduleset / \'properties\' << \'hudson.plugins.buildblocker.BuildBlockerProperty\'(plugin:\'build-blocker-plugin@1.7.3\') {
                  
                  \'useBuildBlocker\'(false)
                  
                  \'blockLevel\'(\'GLOBAL\')
                  
                  \'scanQueueFor\'(\'DISABLED\')
                  
                  \'blockingJobs\'()
                  
              }
              
              maven2moduleset / \'properties\' << \'com.sonyericsson.rebuild.RebuildSettings\'(plugin:\'rebuild@1.27\') {
                  
                  \'autoRebuild\'(false)
                  
                  \'rebuildDisabled\'(false)
                  
              }
              
              maven2moduleset / \'properties\' << \'hudson.plugins.throttleconcurrents.ThrottleJobProperty\'(plugin:\'throttle-concurrents@2.0.1\') {
                  
                  \'categories\'(class:\'java.util.concurrent.CopyOnWriteArrayList\')
                  
                  \'throttleEnabled\'(false)
                  
                  \'throttleOption\'(\'project\')
                  
                  \'limitOneJobWithMatchingParams\'(false)
                  
                  \'paramsToUseForLimit\'()
                  
              }
              
              maven2moduleset / \'properties\' << \'de.pellepelster.jenkins.walldisplay.WallDisplayJobProperty\'(plugin:\'jenkinswalldisplay@0.6.34\')
              
              maven2moduleset / \'scm\' << \'gitTool\'(\'Default\')
              
              maven2moduleset / \'scm\' << \'submoduleCfg\'(class:\'list\')
              
              maven2moduleset << delegate.\'assignedNode\'(\'linux\')
              
              maven2moduleset << delegate.\'jdk\'(\'Java 7\')
              
              maven2moduleset << delegate.\'rootModule\' {
                  
                  \'groupId\'(\'com.vsp.api\')
                  
                  \'artifactId\'(\'api-test\')
                  
              }
              
              maven2moduleset << delegate.\'rootPOM\'(\'api-test/pom.xml\')
              
              maven2moduleset << delegate.\'mavenName\'(\'Maven 3.3.x\')
              
              maven2moduleset << delegate.\'aggregatorStyleBuild\'(true)
              
              maven2moduleset << delegate.\'ignoreUpstremChanges\'(true)
              
              maven2moduleset << delegate.\'ignoreUnsuccessfulUpstreams\'(false)
              
              maven2moduleset << delegate.\'processPlugins\'(false)
              
              maven2moduleset << delegate.\'mavenValidationLevel\'(-1)
              
              maven2moduleset << delegate.\'blockTriggerWhenBuilding\'(true)
              
              maven2moduleset << delegate.\'settings\'(class:\'org.jenkinsci.plugins.configfiles.maven.job.MvnSettingsProvider\',plugin:\'config-file-provider@2.16.4\') {
                  
                  \'settingsConfigId\'(\'maven-settings\')
                  
              }
              
              maven2moduleset << delegate.\'globalSettings\'(class:\'org.jenkinsci.plugins.configfiles.maven.job.MvnGlobalSettingsProvider\',plugin:\'config-file-provider@2.16.4\') {
                  
                  \'settingsConfigId\'(\'maven-global-settings\')
                  
              }
              
              maven2moduleset << delegate.\'reporters\'()
              
              maven2moduleset / \'publishers\' << \'be.certipost.hudson.plugin.SCPRepositoryPublisher\'(plugin:\'scp@1.8\') {
                  
                  \'siteName\'(\'ea-tc0672\')
                  
                  \'entries\' {
                      
                      \'be.certipost.hudson.plugin.Entry\' {
                          
                          \'filePath\'()
                          
                          \'sourceFile\'(\'api-test/target/jmeter/results/*.jtl\')
                          
                          \'keepHierarchy\'(true)
                          
                      }
                      
                  }
                  
              }
              
              maven2moduleset / \'publishers\' << \'hudson.plugins.performance.PerformancePublisher\'(plugin:\'performance@3.3\') {
                  
                  \'errorFailedThreshold\'(0)
                  
                  \'errorUnstableThreshold\'(0)
                  
                  \'errorUnstableResponseTimeThreshold\'()
                  
                  \'relativeFailedThresholdPositive\'(0.0)
                  
                  \'relativeFailedThresholdNegative\'(0.0)
                  
                  \'relativeUnstableThresholdPositive\'(0.0)
                  
                  \'relativeUnstableThresholdNegative\'(0.0)
                  
                  \'nthBuildNumber\'(21)
                  
                  \'configType\'(\'ART\')
                  
                  \'graphType\'(\'ART\')
                  
                  \'modeOfThreshold\'(false)
                  
                  \'failBuildIfNoResultFile\'(false)
                  
                  \'compareBuildPrevious\'(false)
                  
                  \'optionType\'(\'ART\')
                  
                  \'xml\'()
                  
                  \'modePerformancePerTestCase\'(true)
                  
                  \'excludeResponseTime\'(false)
                  
                  \'modeThroughput\'(false)
                  
                  \'modeEvaluation\'(false)
                  
                  \'ignoreFailedBuilds\'(false)
                  
                  \'ignoreUnstableBuilds\'(false)
                  
                  \'persistConstraintLog\'(false)
                  
                  \'sourceDataFiles\'(\'**/*.jtl\')
                  
              }
              
              maven2moduleset / \'publishers\' << \'hudson.plugins.emailext.ExtendedEmailPublisher\'(plugin:\'email-ext@2.61\') {
                  
                  \'recipientList\'(\'samaar@vsp.com,dustdi@vsp.com,niki.nguyen@vsp.com\')
                  
                  \'configuredTriggers\' {
                      
                      \'hudson.plugins.emailext.plugins.trigger.PreBuildTrigger\' {
                          
                          \'email\' {
                              
                              \'subject\'(\'$PROJECT_DEFAULT_SUBJECT\')
                              
                              \'body\'(\'$PROJECT_DEFAULT_CONTENT\')
                              
                              \'recipientProviders\' {
                                  
                                  \'hudson.plugins.emailext.plugins.recipients.ListRecipientProvider\'()
                                  
                              }
                              
                              \'attachmentsPattern\'()
                              
                              \'attachBuildLog\'(false)
                              
                              \'compressBuildLog\'(false)
                              
                              \'replyTo\'(\'$PROJECT_DEFAULT_REPLYTO\')
                              
                              \'contentType\'(\'project\')
                              
                          }
                          
                      }
                      
                      \'hudson.plugins.emailext.plugins.trigger.SuccessTrigger\' {
                          
                          \'email\' {
                              
                              \'subject\'(\'eligibility-web-performance-average build successful\')
                              
                              \'body\'(\'$PROJECT_DEFAULT_CONTENT\')
                              
                              \'recipientProviders\' {
                                  
                                  \'hudson.plugins.emailext.plugins.recipients.DevelopersRecipientProvider\'()
                                  
                                  \'hudson.plugins.emailext.plugins.recipients.RequesterRecipientProvider\'()
                                  
                                  \'hudson.plugins.emailext.plugins.recipients.ListRecipientProvider\'()
                                  
                              }
                              
                              \'attachmentsPattern\'()
                              
                              \'attachBuildLog\'(false)
                              
                              \'compressBuildLog\'(false)
                              
                              \'replyTo\'(\'$PROJECT_DEFAULT_REPLYTO\')
                              
                              \'contentType\'(\'project\')
                              
                          }
                          
                      }
                      
                      \'hudson.plugins.emailext.plugins.trigger.FailureTrigger\' {
                          
                          \'email\' {
                              
                              \'subject\'(\'eligibility-web-performance-average build failed!\')
                              
                              \'body\'(\'$PROJECT_DEFAULT_CONTENT\')
                              
                              \'recipientProviders\' {
                                  
                                  \'hudson.plugins.emailext.plugins.recipients.DevelopersRecipientProvider\'()
                                  
                                  \'hudson.plugins.emailext.plugins.recipients.RequesterRecipientProvider\'()
                                  
                                  \'hudson.plugins.emailext.plugins.recipients.CulpritsRecipientProvider\'()
                                  
                              }
                              
                              \'attachmentsPattern\'()
                              
                              \'attachBuildLog\'(true)
                              
                              \'compressBuildLog\'(false)
                              
                              \'replyTo\'(\'$PROJECT_DEFAULT_REPLYTO\')
                              
                              \'contentType\'(\'project\')
                              
                          }
                          
                      }
                      
                  }
                  
                  \'contentType\'(\'text/html\')
                  
                  \'defaultSubject\'(\'$DEFAULT_SUBJECT\')
                  
                  \'defaultContent\'(\'<p>Build Reason: ${CAUSE}</p><p>${CHANGES_SINCE_LAST_SUCCESS}</p><p><a href=\\\'https://git.vspglobal.com/projects/TEST/repos/api-test/commits/${GIT_REVISION}\\\'>View Git commit information</a></p><p><b><a href=\\\'http://jenkinsmm.vspglobal.com/eligibility/job/eligibility-web-performance-average/${BUILD_NUMBER}/\\\'>Build info page in Jenkins</a></b></p>\')
                  
                  \'attachmentsPattern\'()
                  
                  \'presendScript\'(\'$DEFAULT_PRESEND_SCRIPT\')
                  
                  \'postsendScript\'()
                  
                  \'attachBuildLog\'(false)
                  
                  \'compressBuildLog\'(false)
                  
                  \'replyTo\'(\'$DEFAULT_REPLYTO\')
                  
                  \'saveOutput\'(false)
                  
                  \'disabled\'(false)
                  
              }
              
              maven2moduleset / \'buildWrappers\' << \'com.michelin.cio.hudson.plugins.maskpasswords.MaskPasswordsBuildWrapper\'()
              
              maven2moduleset / \'buildWrappers\' << \'hudson.plugins.build__timeout.BuildTimeoutWrapper\'(plugin:\'build-timeout@1.19\') {
                  
                  \'strategy\'(class:\'hudson.plugins.build_timeout.impl.NoActivityTimeOutStrategy\') {
                      
                      \'timeoutSecondsString\'(3600)
                      
                  }
                  
                  \'operationList\'()
                  
              }
              
              maven2moduleset << delegate.\'prebuilders\' {
                  
                  \'org.jenkinsci.plugins.conditionalbuildstep.ConditionalBuilder\'(plugin:\'conditional-buildstep@1.3.6\') {
                      
                      \'runner\'(class:\'org.jenkins_ci.plugins.run_condition.BuildStepRunner$Fail\',plugin:\'run-condition@1.0\')
                      
                      \'runCondition\'(class:\'org.jenkins_ci.plugins.run_condition.core.BooleanCondition\',plugin:\'run-condition@1.0\') {
                          
                          \'token\'(\'${DEPLOY_FIRST}\')
                          
                      }
                      
                      \'conditionalbuilders\' {
                          
                          \'hudson.tasks.Shell\' {
                              
                              \'command\'(\'ssh -oStrictHostKeyChecking=no -l root ea-app1904 JOB_NAME=eligibility-web-performance-average chef-client --no-color\')
                              
                          }
                          
                      }
                      
                  }
                  
              }
              
              maven2moduleset << delegate.\'postbuilders\'()
              
              maven2moduleset << delegate.\'runPostStepsIfResult\' {
                  
                  \'name\'(\'FAILURE\')
                  
                  \'ordinal\'(2)
                  
                  \'color\'(\'RED\')
                  
                  \'completeBuild\'(true)
                  
              }
              
          }
          
      }''')
      
      'usingScriptText'(true)
      
      'sandbox'(false)
      
      'ignoreExisting'(false)
      
      'ignoreMissingFiles'(false)
      
      'failOnMissingPlugin'(false)
      
      'unstableOnDeprecation'(false)
      
      'removedJobAction'('IGNORE')
      
      'removedViewAction'('IGNORE')
      
      'removedConfigFilesAction'('IGNORE')
      
      'lookupStrategy'('JENKINS_ROOT')
      
    }
    
  }
  
}

