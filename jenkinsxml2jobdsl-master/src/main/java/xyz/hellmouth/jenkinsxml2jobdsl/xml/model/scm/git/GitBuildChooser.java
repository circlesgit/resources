package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.git;

import javax.xml.bind.annotation.XmlAttribute;

public class GitBuildChooser {

	@XmlAttribute(name="class")
	public String clazz;
	
	public long maximumAgeInDays;
	public String ancestorCommitSha1;
}
