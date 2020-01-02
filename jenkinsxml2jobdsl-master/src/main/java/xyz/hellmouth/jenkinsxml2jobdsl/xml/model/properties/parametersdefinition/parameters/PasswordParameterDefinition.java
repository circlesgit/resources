package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class PasswordParameterDefinition implements Buildable{

	public String name;
	public String description;
	public String defaultValue;
	@Override
	public void build(Builder builder) {
		builder.createMethod("nonStoredPasswordParam").withStringParameter(name).withStringParameter(defaultValue).withStringParameter(description).endMethod();
		
	}
}
