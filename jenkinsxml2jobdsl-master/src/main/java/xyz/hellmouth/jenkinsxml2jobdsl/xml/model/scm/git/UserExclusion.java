package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.git;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class UserExclusion implements Buildable{
	public String excludedUsers;

	@Override
	public void build(Builder builder) {
		// TODO Unsipported
		
	}
}
