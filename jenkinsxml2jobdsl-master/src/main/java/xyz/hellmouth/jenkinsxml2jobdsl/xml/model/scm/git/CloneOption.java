package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.git;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class CloneOption implements Buildable{
	public boolean shallow;
	public boolean noTags;
	public String reference;
	public long timeout;
	public long depth;
	public boolean honorRefspec;
	
	@Override
	public void build(Builder builder) {
		builder.create("cloneOptions").openClosure();
		
		builder.createMethod("honorRefspec").withBooleanParameter(honorRefspec).endMethod();
		builder.createMethod("reference").withStringParameter(reference).endMethod();
		builder.createMethod("shallow").withBooleanParameter(shallow).endMethod();
		builder.createMethod("timeout").withLongParameter(timeout).endMethod();
		builder.closeClosure();
		
	}
}
