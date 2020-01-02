package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class ParameterizedBuildTriggerConfigMatrixSubsetBuildParameters implements Buildable{

	public String filter;

	@Override
	public void build(Builder builder) {
		builder.createMethod("matrixSubset").withStringParameter(filter).endMethod();
		
	}
}
