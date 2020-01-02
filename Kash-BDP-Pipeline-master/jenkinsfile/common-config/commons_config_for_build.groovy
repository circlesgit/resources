//************ COMMON PARAMETERS ************//

env.APPLICATION_GIT_REPO = "application_git_repo"
env.DOCKER_REGISTRY = ""
env.BUILD_APP = "TRUE"
env.APP_VERSION = sh(script: 'echo -n $(date "+0.0.%Y%m%d".${BUILD_NUMBER}."%s")', returnStdout: true)
