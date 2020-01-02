package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.git;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class BuildChooserSetting implements Buildable{

	public GitBuildChooser buildChooser;

	@Override
	public void build(Builder builder) {
		if (buildChooser != null){
			builder.create("choosingStrategy").openClosure();
			if (buildChooser.clazz != null && buildChooser.clazz.contains("Ancestry")){
				builder.createMethod("ancestry").withLongParameter(buildChooser.maximumAgeInDays).withStringParameter(buildChooser.ancestorCommitSha1).endMethod();
			}
			else if (buildChooser.clazz != null && buildChooser.clazz.contains("Inverse")){
				builder.createMethod("inverse").endMethod();
			}
			builder.closeClosure();
		}
		
	}
}
