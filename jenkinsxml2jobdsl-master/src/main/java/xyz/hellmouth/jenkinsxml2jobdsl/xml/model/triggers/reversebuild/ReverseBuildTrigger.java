package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.triggers.reversebuild;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class ReverseBuildTrigger implements Buildable {

	public String spec;
	public String upstreamProjects;
	public BuildThreshold threshold;
	@Override
	public void build(Builder builder) {
		builder.createMethod("upstream").withStringParameter(upstreamProjects).withStringParameter(threshold.name).endMethod();
		
	}
}
