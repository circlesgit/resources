package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class ParameterizedBuildTriggerConfig implements Buildable{
	
	public ParameterizedBuildTriggerConfigConfigs configs;
	
	public String projects;
	public String condition;
	public boolean triggerWithNoParameters;
	public boolean triggerFromChildProjects;
	
	@Override
	public void build(Builder builder) {
		
		builder.createMethod("trigger").withStringParameter(projects).endMethodAndOpenClosure();
		
		builder.createMethod("condition").withStringParameter(condition).endMethod();
		builder.createMethod("triggerWithNoParameters").withBooleanParameter(triggerWithNoParameters).endMethod();
		
		configs.build(builder);
		
		builder.closeClosure();
		
	}

}
