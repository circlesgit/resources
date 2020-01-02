package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.ssh;

import javax.xml.bind.annotation.XmlAttribute;

public class BuilderLabel {
	
	@XmlAttribute(name="class")
	public String clazz;
	
	public String label;

}
