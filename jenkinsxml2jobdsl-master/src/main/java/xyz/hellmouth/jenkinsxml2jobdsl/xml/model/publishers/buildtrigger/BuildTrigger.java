package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.buildtrigger;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class BuildTrigger implements Buildable{

	public String childProjects;
	public Threshold threshold;
	@Override
	public void build(Builder builder) {
		builder.createMethod("downstream").withStringParameter(childProjects).withStringParameter(threshold.name).endMethod();
		
	}
}
