package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.ssh;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class SSHBuilderTransfer implements Buildable{
	
	public String remoteDirectory;
	public String sourceFiles;
	public String excludes;
	public String removePrefix;
	public boolean remoteDirectorySDF;
	public boolean flatten;
	public boolean cleanRemote;
	public boolean noDefaultExcludes;
	public boolean makeEmptyDirs;
	public String patternSeparator;
	public String execCommand;
	public long execTimeout;
	public boolean usePty;
	
	@Override
	public void build(Builder builder) {
		builder.create("transferSet").openClosure();
		
		builder.createMethod("excludeFiles").withStringParameter(excludes).endMethod();
		builder.createMethod("execCommand").withStringParameter(execCommand).endMethod();
		builder.createMethod("execInPty").withBooleanParameter(usePty).endMethod();
		builder.createMethod("execTimeout").withLongParameter(execTimeout).endMethod();
		builder.createMethod("flattenFiles").withBooleanParameter(flatten).endMethod();
		builder.createMethod("makeEmptyDirs").withBooleanParameter(makeEmptyDirs).endMethod();
		builder.createMethod("noDefaultExcludes").withBooleanParameter(noDefaultExcludes).endMethod();
		builder.createMethod("patternSeparator").withStringParameter(patternSeparator).endMethod();
		builder.createMethod("remoteDirIsDateFormat").withBooleanParameter(remoteDirectorySDF).endMethod();
		builder.createMethod("remoteDirectory").withStringParameter(remoteDirectory).endMethod();
		builder.createMethod("removePrefix").withStringParameter(removePrefix).endMethod();
		builder.createMethod("sourceFiles").withStringParameter(sourceFiles).endMethod();
		
		builder.closeClosure();
		
	}

}
