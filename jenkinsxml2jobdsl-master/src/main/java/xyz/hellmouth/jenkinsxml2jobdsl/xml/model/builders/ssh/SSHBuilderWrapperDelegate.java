package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.ssh;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class SSHBuilderWrapperDelegate implements Buildable{

	@XmlAttribute
	public String plugin;
	
	public String consolePrefix;
	
	public BuilderDelegate delegate;

	@Override
	public void build(Builder builder) {
		delegate.build(builder);
		
	}
	
	
}
