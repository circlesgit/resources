package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class RunParameterDefinition implements Buildable{

	public String name;
	public String description;
	public String projectName;
	public String filter;
	
	@Override
	public void build(Builder builder) {
		builder.createMethod("runParam").withStringParameter(name).withStringParameter(projectName).withStringParameter(description).withStringParameter(filter).endMethod();
		
	}
}
