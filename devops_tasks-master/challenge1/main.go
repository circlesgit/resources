/*
Package main that accepts a list of public Github repositories and prints out the name,
clone URL, date of latest commit and name of latest author for each one.

Input
	1. Read plain text list of repositories from  stdin
  2. One repo per inputline,format: $orgname/$repo, e.g. kubernetes/charts
  3. Other parameters/env vars as needed, should be documented
  4. Type 'QUIT' to exit the program.

Validation
  1. If the github repos name is not of the format $orgname/$repo, will print ("Invalid Input")
	2. If github API RateLimit is hit,will print "API rate limit Exceeded.Please try after some minutes"
	and exit.
	3. If the program fails to fetch the detail from github api will appropiate messages.

Output
  1. One line per input repo in CSV or TSV format plus one header line to  stdout

*/
package main

import (
	"bufio"
	"context"
	"fmt"
	"os"
	"regexp"
	"strings"

	"github.com/google/go-github/v24/github"
)

func main() {
	var inputLine string
	var repoStrings []string
	var successfullResult, firstTime bool
	firstTime = true
	var err error
	var gitrepo *github.Repository
	var commitInfo []*github.RepositoryCommit
	var latestCommit *github.RepositoryCommit
	// regex to validate  github repo name input
	r, _ := regexp.Compile("^[-_a-zA-Z0-9]+/[-_a-zA-Z0-9]+$")
	context := context.Background()
	client := github.NewClient(nil)
	fmt.Println("Enter github repo(s) or Type 'QUIT' to exit.")
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		inputLine = scanner.Text()
		successfullResult = true
		if strings.EqualFold("QUIT", inputLine) { //refer Input 4
			fmt.Println("Thank you")
			os.Exit(0)
		}
		if firstTime {
			fmt.Print("\nGitRepoName,GitCloneUrl,CommitDate,CommitBy\n")
			firstTime = false
		}
		if r.MatchString(inputLine) { //refer Validation 1
			_, _, err = client.Repositories.List(context, "", nil) //refer Validation 2
			if _, ok := err.(*github.RateLimitError); ok {
				fmt.Println("API rate limit Exceeded.Please try after some minutes")
				os.Exit(0)
			}
			repoStrings = strings.Split(inputLine, "/")
			// getting the repo details from github API
			gitrepo, _, err = client.Repositories.Get(context, repoStrings[0], repoStrings[1])
			if err != nil { //refer Validation 3
				fmt.Println("Problem in getting repository information")
				successfullResult = false
			}
			// getting the list of commit made to the repo from github API
			commitInfo, _, err = client.Repositories.ListCommits(context, repoStrings[0], repoStrings[1], nil)
			if err != nil { //refer Validation 3
				fmt.Println("Problem in getting commitInfo information")
				successfullResult = false
			}
			// getting the commit info of the last commit made in the repo from from github API
			latestCommit, _, err = client.Repositories.GetCommit(context, repoStrings[0], repoStrings[1], commitInfo[0].GetSHA())
			if err != nil { //refer Validation 3
				fmt.Println("Problem in getting Commit information")
				successfullResult = false
			}
			if successfullResult {
				fmt.Printf("'%s','%s','%s','%s'\n", gitrepo.GetName(), gitrepo.GetCloneURL(), latestCommit.Commit.Author.GetDate(), latestCommit.Commit.Author.GetName())
			}
		} else {
			fmt.Println("Invalid Input")
		}
	}
	if err := scanner.Err(); err != nil {
		fmt.Fprintln(os.Stderr, "error: ", err)
		os.Exit(1)
	}
}
