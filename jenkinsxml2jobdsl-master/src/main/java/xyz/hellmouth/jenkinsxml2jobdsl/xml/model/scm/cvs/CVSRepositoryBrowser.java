package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.cvs;

import javax.xml.bind.annotation.XmlAttribute;

public class CVSRepositoryBrowser {

	@XmlAttribute(name="class")
	public String clazz;
	
	public String url;
}
