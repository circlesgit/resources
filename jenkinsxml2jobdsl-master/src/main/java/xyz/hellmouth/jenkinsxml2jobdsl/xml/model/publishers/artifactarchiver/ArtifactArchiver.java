package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.artifactarchiver;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class ArtifactArchiver implements Buildable{

	public String artifacts;
	public String excludes;
	public boolean allowEmptyArchive;
	public boolean onlyIfSuccessful;
	public boolean fingerprint;
	public boolean defaultExcludes;
	public boolean caseSensitive;
	@Override
	public void build(Builder builder) {
		builder.create("archiveArtifacts").openClosure();
		builder.createMethod("allowEmpty").withBooleanParameter(allowEmptyArchive).endMethod();
		builder.createMethod("defaultExcludes").withBooleanParameter(defaultExcludes).endMethod();
		builder.createMethod("exclude").withStringParameter(excludes).endMethod();
		builder.createMethod("fingerprint").withBooleanParameter(fingerprint).endMethod();
		builder.createMethod("onlyIfSuccessful").withBooleanParameter(onlyIfSuccessful).endMethod();
		builder.createMethod("pattern").withStringParameter(artifacts).endMethod();
		builder.closeClosure();
	}
}
