package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.builddiscarder;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class GitHubProject implements Buildable{

	@XmlAttribute
	public String plugin;
	
	public String projectUrl;
	public String displayName;
	
	@Override
	public void build(Builder builder) {
		builder.createMethod("githubProjectUrl").withStringParameter(projectUrl).endMethod();
		
	}
	
	
}
