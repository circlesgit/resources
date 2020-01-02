package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class CredentialsParameterDefinition implements Buildable{

	@XmlAttribute
	public String plugin;
	
	public String name;
	public String description;
	public String defaultValue;
	public String credentialType;
	public boolean required;
	@Override
	public void build(Builder builder) {
//		builder.createMethod("credentialsParam").withStringParameter(name).withListParameter(choices.a.elements).withStringParameter(description).endMethod();
		
		
		builder.createMethod("credentialsParam").withStringParameter(name).endMethodAndOpenClosure();
		builder.createMethod("defaultValue").withStringParameter(defaultValue).endMethod();
		builder.createMethod("description").withStringParameter(description).endMethod();
		builder.createMethod("required").withBooleanParameter(required).endMethod();
		builder.createMethod("type").withStringParameter(credentialType).endMethod();
		builder.closeClosure();
		
	}
}
