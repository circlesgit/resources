package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.gitpublisher;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class BranchToPush implements Buildable{
	public String targetRepoName;
	public String branchName;
	@Override
	public void build(Builder builder) {
		builder.createMethod("branch").withStringParameter(targetRepoName).withStringParameter(branchName).endMethod();
		
	}

}
