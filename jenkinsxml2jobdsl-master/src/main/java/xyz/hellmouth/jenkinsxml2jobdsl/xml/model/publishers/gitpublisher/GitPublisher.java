package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.gitpublisher;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class GitPublisher implements Buildable{

	@XmlAttribute
	public String plugin;
	
	public String configVersion;
	
	public boolean pushMerge;
	public boolean pushOnlyIfSuccess;
	public boolean forcePush;
	public TagsToPush tagsToPush;
	public BranchesToPush branchesToPush;
	public NotesToPush notesToPush;
	
	@Override
	public void build(Builder builder) {
		builder.create("git").openClosure();
		branchesToPush.build(builder);
		
		builder.createMethod("forcePush").withBooleanParameter(forcePush).endMethod();
		builder.createMethod("pushMerge").withBooleanParameter(pushMerge).endMethod();
		builder.createMethod("pushOnlyIfSuccess").withBooleanParameter(pushOnlyIfSuccess).endMethod();
		
		tagsToPush.build(builder);
		
		builder.closeClosure();
		
	}
	
}
