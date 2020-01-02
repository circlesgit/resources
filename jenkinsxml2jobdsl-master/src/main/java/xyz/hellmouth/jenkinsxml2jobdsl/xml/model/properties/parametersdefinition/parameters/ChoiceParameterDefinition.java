package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters.choices.Choices;

public class ChoiceParameterDefinition implements Buildable{

	public String name;
	public String description;
	public Choices choices;
	
	@Override
	public void build(Builder builder) {
		builder.createMethod("choiceParam").withStringParameter(name).withListParameter(choices.a.elements).withStringParameter(description).endMethod();
	}
}
