package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class ParameterizedBuildTriggerConfigFileBuildParameters implements Buildable{

	public String propertiesFile;
	public boolean failTriggerOnMissing;
	public boolean useMatrixChild;
	public boolean onlyExactRuns;
	@Override
	public void build(Builder builder) {
		builder.createMethod("propertiesFile").withStringParameter(propertiesFile).withBooleanParameter(failTriggerOnMissing).endMethod();
		
	}
}
