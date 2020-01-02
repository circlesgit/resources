package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.gitpublisher;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class BranchesToPush implements Buildable{

	@XmlElements({
		@XmlElement(name="hudson.plugins.git.GitPublisher_-BranchToPush", type=BranchToPush.class)
	})
	public List<Buildable> elements;

	@Override
	public void build(Builder builder) {
		for (Buildable b : elements){
			b.build(builder);
		}
		
	}
	
	
}
