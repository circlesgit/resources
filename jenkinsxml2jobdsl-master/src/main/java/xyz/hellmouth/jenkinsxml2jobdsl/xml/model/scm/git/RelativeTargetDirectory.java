package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.git;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class RelativeTargetDirectory implements Buildable{
	
	public String relativeTargetDir;

	@Override
	public void build(Builder builder) {
		builder.createMethod("relativeTargetDirectory").withStringParameter(relativeTargetDir).endMethod();
	}

}
