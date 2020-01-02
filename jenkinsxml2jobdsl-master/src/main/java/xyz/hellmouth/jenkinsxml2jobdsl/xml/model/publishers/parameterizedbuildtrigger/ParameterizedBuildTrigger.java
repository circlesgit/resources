package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class ParameterizedBuildTrigger implements Buildable{
	
	@XmlAttribute(name="plugin")
	public String plugin;
	
	public ParameterizedBuildTriggerConfigs configs;

	@Override
	public void build(Builder builder) {
		builder.create("downstreamParameterized").openClosure();
		configs.build(builder);
		
		builder.closeClosure();
		
	}

}
