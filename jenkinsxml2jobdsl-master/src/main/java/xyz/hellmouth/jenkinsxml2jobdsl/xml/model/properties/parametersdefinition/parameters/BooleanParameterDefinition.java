package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class BooleanParameterDefinition implements Buildable{

	public String name;
	public String description;
	public boolean defaultValue;
	
	@Override
	public void build(Builder builder) {
		builder.createMethod("booleanParam").withStringParameter(name).withBooleanParameter(defaultValue).withStringParameter(description).endMethod();
		
	}
}
