package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.ssh;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class SSHPublisher implements Buildable{

	public String configName;
	public boolean verbose;
	public PublisherTransfers transfers;
	public boolean useWorkspaceInPromotion;
	public boolean usePromotionTimestamp;
	public PublisherRetry retry;
	public PublisherLabel label;
	public PublisherCredentials credentials;
	@Override
	public void build(Builder builder) {
		builder.createMethod("server").withStringParameter(configName).endMethodAndOpenClosure();
		
		if (credentials != null){
			credentials.build(builder);
		}
		if (transfers != null){
			transfers.build(builder);
		}
		
		builder.createMethod("label").withStringParameter(label.label).endMethod();
		builder.createMethod("retry").withLongParameter(retry.retries).withLongParameter(retry.retryDelay).endMethod();
		builder.createMethod("verbose").withBooleanParameter(verbose).endMethod();
		
		builder.closeClosure();
		
	}
	
}
