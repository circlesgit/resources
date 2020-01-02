package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.git;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class WipeWorkspace implements Buildable{

	@Override
	public void build(Builder builder) {
		builder.createMethod("wipeOutWorkspace").endMethod();
		
	}

}
