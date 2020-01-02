package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.ssh;

import javax.xml.bind.annotation.XmlAttribute;

public class BuilderRetry {

	@XmlAttribute(name="class")
	public String clazz;
	
	public long retries;
	public long retryDelay;
}
