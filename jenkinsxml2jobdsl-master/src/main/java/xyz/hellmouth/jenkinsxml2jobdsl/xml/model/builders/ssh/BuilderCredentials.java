package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.ssh;

import javax.xml.bind.annotation.XmlAttribute;

public class BuilderCredentials {
	@XmlAttribute(name="class")
	public String clazz;
	
	public String secretPassphrase;
	public String key;
	public String keyPath;
	public String username;

}
