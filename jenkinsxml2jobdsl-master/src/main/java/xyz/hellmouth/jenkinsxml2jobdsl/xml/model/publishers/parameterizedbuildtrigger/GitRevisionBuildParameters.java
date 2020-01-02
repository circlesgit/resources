package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class GitRevisionBuildParameters implements Buildable{
	@XmlAttribute(name="plugin")
	public String plugin;
	
	public boolean combineQueuedCommits;

	@Override
	public void build(Builder builder) {
		builder.createMethod("gitRevision").withBooleanParameter(combineQueuedCommits).endMethod();
		
	}

}
