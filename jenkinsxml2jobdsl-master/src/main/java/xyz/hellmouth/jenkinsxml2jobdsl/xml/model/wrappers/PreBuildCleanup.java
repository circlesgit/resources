package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.wrappers;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class PreBuildCleanup implements Buildable{

	public PreBuildCleanupPatterns patterns;
	public boolean deleteDirs;
	public String cleanupParameter;
	public String externalDelete;
	
	@Override
	public void build(Builder builder) {
		builder.create("preBuildCleanup").openClosure();
		builder.createMethod("cleanupParameter").withStringParameter(cleanupParameter).endMethod();
		builder.createMethod("deleteCommand").withStringParameter(externalDelete).endMethod();
		builder.createMethod("deleteDirectories").withBooleanParameter(deleteDirs).endMethod();
		if (patterns != null){
			patterns.build(builder);
		}
		builder.closeClosure();
	}

}
