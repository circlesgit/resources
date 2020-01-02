package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.gitpublisher;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class TagToPush implements Buildable{
	public String targetRepoName;
	public String tagName;
	public String tagMessage;
	public boolean createTag;
	public boolean updateTag;
	@Override
	public void build(Builder builder) {
		builder.createMethod("tag").withStringParameter(targetRepoName).withStringParameter(tagName).endMethodAndOpenClosure();
		builder.createMethod("create").withBooleanParameter(createTag).endMethod();
		builder.createMethod("update").withBooleanParameter(updateTag).endMethod();
		builder.createMethod("message").withStringParameter(tagMessage).endMethod();
		builder.closeClosure();
		
	}

}
