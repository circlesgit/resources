package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.git.branches;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class BranchSpec implements Buildable{
	public String name;

	@Override
	public void build(Builder builder) {
		builder.withStringParameter(name);
		
	}

}
