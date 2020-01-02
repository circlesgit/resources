package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.batchfile;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class BatchFile implements Buildable{
	public String command;

	@Override
	public void build(Builder builder) {
		builder.createMethod("batchFile").withStringParameter(command).endMethod();
		
	}
}
