package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.ssh;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class SSHBuilder implements Buildable{

	public String configName;
	public boolean verbose;
	public BuilderTransfers transfers;
	public boolean useWorkspaceInPromotion;
	public boolean usePromotionTimestamp;
	public BuilderRetry retry;
	public BuilderLabel label;
	public BuilderCredentials credentials;
	@Override
	
	public void build(Builder builder) {
		builder.createMethod("server").withStringParameter(configName).endMethodAndOpenClosure();
		builder.createMethod("credentials").withStringParameter(credentials.username).endMethodAndOpenClosure();
		builder.createMethod("key").withStringParameter(credentials.key).endMethod();
		builder.createMethod("pathToKey").withStringParameter(credentials.keyPath).endMethod();
		builder.closeClosure();
		
		builder.createMethod("label").withStringParameter(label.label).endMethod();
		builder.createMethod("retry").withLongParameter(retry.retries).withLongParameter(retry.retryDelay).endMethod();
	
		transfers.build(builder);
		
		builder.createMethod("verbose").withBooleanParameter(verbose).endMethod();
		builder.closeClosure();
		
	}
	
}
