package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class ListSubversionTagsParameterDefinition implements Buildable{
	
	@XmlAttribute(name="plugin")
	public String plugin;
	
	public String name;
	public String description;
	public String tagsDir;
	public String credentialsId;
	public String tagsFilter;
	public boolean reverseByDate;
	public boolean reverseByName;
	public String defaultValue;
	public String maxTags;
	public String uuid;
	@Override
	public void build(Builder builder) {
		builder.createMethod("listTagsParam").withStringParameter(name).withStringParameter(tagsDir).endMethodAndOpenClosure();
		builder.createMethod("credentialsId").withStringParameter(credentialsId).endMethod();
		builder.createMethod("description").withStringParameter(description).endMethod();
		builder.createMethod("tagFilterRegex").withStringParameter(tagsFilter).endMethod();
		builder.createMethod("defaultValue").withStringParameter(defaultValue).endMethod();
		builder.createMethod("maxTagsToDisplay").withStringParameter(maxTags).endMethod();
		builder.createMethod("sortNewestFirst").withBooleanParameter(reverseByDate).endMethod();
		builder.createMethod("sortZtoA").withBooleanParameter(reverseByName).endMethod();
		builder.closeClosure();
	}

}
