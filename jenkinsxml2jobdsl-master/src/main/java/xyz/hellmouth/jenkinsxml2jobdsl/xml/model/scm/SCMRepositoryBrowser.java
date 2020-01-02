package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm;

import javax.xml.bind.annotation.XmlAttribute;

public class SCMRepositoryBrowser {
	
	@XmlAttribute(name="class")
	public String clazz;
	
	public String url;
	public String repositoryInstance;
	public String rootModule;
	public String spaceName;
}
