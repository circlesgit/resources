package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.ssh;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class SSHBuilderPlugin implements Buildable{
	
	public SSHBuilderWrapperDelegate delegate;

	@Override
	public void build(Builder builder) {
		delegate.build(builder);
	}
	
	
	

}
