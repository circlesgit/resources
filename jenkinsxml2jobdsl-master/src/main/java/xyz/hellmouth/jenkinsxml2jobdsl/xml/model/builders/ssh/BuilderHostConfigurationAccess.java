package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.ssh;

import javax.xml.bind.annotation.XmlAttribute;

public class BuilderHostConfigurationAccess {

	@XmlAttribute(name="class")
	public String clazz;
	
	@XmlAttribute
	public String reference;
}
