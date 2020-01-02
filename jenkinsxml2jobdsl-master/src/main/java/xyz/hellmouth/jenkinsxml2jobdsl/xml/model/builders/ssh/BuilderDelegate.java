package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.ssh;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class BuilderDelegate implements Buildable{
	
	public SSHBuilders publishers;
	
	public boolean continueOnError;
	public boolean failOnError;
	public boolean alwaysPublishFromMaster;
	public BuilderHostConfigurationAccess hostConfigurationAccess;
	@Override
	public void build(Builder builder) {
		builder.create("publishOverSsh").openClosure();
		builder.createMethod("alwaysPublishFromMaster").withBooleanParameter(alwaysPublishFromMaster).endMethod();
		builder.createMethod("continueOnError").withBooleanParameter(continueOnError).endMethod();
		builder.createMethod("failOnError").withBooleanParameter(failOnError).endMethod();
		publishers.build(builder);
		builder.closeClosure();
		
	}

}
