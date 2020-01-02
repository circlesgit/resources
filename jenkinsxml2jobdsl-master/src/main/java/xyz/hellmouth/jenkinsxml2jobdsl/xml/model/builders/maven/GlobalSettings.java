package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.maven;

import javax.xml.bind.annotation.XmlAttribute;

public class GlobalSettings{
	
	@XmlAttribute(name="class")
	public String clazz;
	
	public String path;
}
