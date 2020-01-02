package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.wrappers;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class PreBuildCleanupPattern implements Buildable{
	public String pattern;
	public String type;
	
	@Override
	public void build(Builder builder) {
		builder.createMethod(type.toLowerCase()+"Pattern").withStringParameter(pattern).endMethod();
	}
}
