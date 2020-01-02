package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class ParametersDefinition implements Buildable{

	public ParameterDefinitions parameterDefinitions;

	@Override
	public void build(Builder builder) {
		parameterDefinitions.build(builder);
		
	}
}
