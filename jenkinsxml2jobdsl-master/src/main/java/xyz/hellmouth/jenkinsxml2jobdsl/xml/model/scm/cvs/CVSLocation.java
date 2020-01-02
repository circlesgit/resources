package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.cvs;

import javax.xml.bind.annotation.XmlAttribute;

public class CVSLocation {

	@XmlAttribute(name="class")
	public String clazz;
	
	public String locationType;
	public String locationName;
	public boolean useHeadIfNotFound; 
}
