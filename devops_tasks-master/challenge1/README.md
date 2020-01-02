# Task
 - Write an application/script that accepts a list of public Github repositories and prints out the name, clone URL, date of latest commit and name of latest author for each one
Input
 - Read plain text list of repositories from  stdin
 - Onerepoperinputline,format: $orgname/$repo, e.g. kubernetes/charts
 - Other parameters/env vars as needed, should be documented
Output
 - One line per input repo in CSV or TSV format plus one header line to  stdout
Notes
 - Solutions can be done in Python, Golang, NodeJS or Bash
 - Solution should include dependency management for the language chosen
 - Please provide a Dockerfile


# Solution
### Step to run locally
1. `go mod init`
2. `go build -o githubStats`
3. `./githubStats`

### Step to run from a docker
1. build the docker images by running the following command
`docker build -t githubstats:1 -f Dockerfile .`
2. execute the docker image by running the following command
`docker run -it githubstats:1`
