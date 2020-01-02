package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.ssh;

import javax.xml.bind.annotation.XmlAttribute;

public class PublisherLabel {
	
	@XmlAttribute(name="class")
	public String clazz;
	
	public String label;

}
