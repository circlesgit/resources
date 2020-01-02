package xyz.hellmouth.jenkinsxml2jobdsl.xml.model;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class RunMeMaybeProperty implements Buildable{
	
	public boolean enabled;
	public String resourceRequestsName;
	public String resourceTargetParameter;
	public boolean preserveResources;
	public String combinationFilter;

	@XmlAttribute
	public String plugin;

	@Override
	public void build(Builder builder) {
		builder.create("runMeMaybeJobProperty").openClosure();
		builder.createMethod("enabled").withBooleanParameter(enabled).endMethod();
		builder.createMethod("preserveResources").withBooleanParameter(preserveResources).endMethod();
		builder.createMethod("resourceRequestsName").withStringParameter(resourceRequestsName).endMethod();
		builder.createMethod("resourceTargetParameter").withStringParameter(resourceTargetParameter).endMethod();
		builder.createMethod("combinationFilter").withStringParameter(combinationFilter).endMethod();
		builder.closeClosure();
		
	}
}
