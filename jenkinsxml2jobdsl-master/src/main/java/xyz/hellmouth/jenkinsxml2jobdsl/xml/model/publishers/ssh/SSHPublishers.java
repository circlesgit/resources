package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.ssh;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class SSHPublishers implements Buildable{
	@XmlElements({
		@XmlElement(name="jenkins.plugins.publish__over__ssh.BapSshPublisher", type=SSHPublisher.class)
	    })
	public List<Buildable> elements;

	@Override
	public void build(Builder builder) {
		for (Buildable o : elements){
			o.build(builder);
		}
		
	}
}
