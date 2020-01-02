

env.APP_NAME = "jsb" // APPLICATION NAME
//env.GIT_CREDENTIALS = "3e3df65e-c7a1-4895-b6ee-ed61f314adae"
//env.APP_GIT_URL = "https://git.rakuten-it.com/scm/gcsopa/sample_application_java_springboot.git"
env.GIT_CREDENTIALS = "306a2628-508e-4b52-8839-b22178a7a657"
env.APP_GIT_URL = "ssh://git@git.dev.db.rakuten.co.jp:7999/gcsopa/sample_application_java_springboot.git"
env.BUILD = "false"
env.JENKINSAGENT_NAME = "kslave"
//gcp config
env.CLOUD = "gcp" // GCP or AWS
env.PROJECTID = "rid-fcb" // GCP project id
env.SERVICE_ACCOUNT = "qeuser"
env.CREDENTIALSID = "rid-fcb-qeuser"
//aws config
//env.CLOUD = "aws"
//env.AWS_ACCOUNT_ID = "941643445078" // https://console.aws.amazon.com/support/home?# check you account number under "Account number:"
//env.AWS_REGION = "us-east-1"
//env.CREDENTIALSID = "3d57c240-7fa0-49b2-93da-ef92777721c8"
