package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.maven;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class Maven implements Buildable{
	public String targets;
	public String jvmOptions;
	public String pom;
	public String properties;
	public boolean usePrivateRepository;
	public Settings settings;
	public GlobalSettings globalSettings;
	@Override
	public void build(Builder builder) {
		builder.create("maven").openClosure();
		
		for (String g : targets.split(" ")){
			if (g.trim().length() > 0){
				builder.createMethod("goals").withStringParameter(g).endMethod();
			}
		}
		builder.createMethod("mavenOpts").withStringParameter(jvmOptions).endMethod();
		builder.createMethod("rootPOM").withStringParameter(pom).endMethod();
		
		if (properties.trim().length() > 0){
			String[] keyValuePairs = properties.split("\n");
			for (String kv : keyValuePairs){
				String [] kvp = kv.split("=");
				builder.createMethod("property").withStringParameter(kvp[0].trim()).withStringParameter(kvp[1].trim()).endMethod(); // TODO this needs to be a map
			}
		}
		
		builder.createMethod("providedSettings").withStringParameter(settings.path).endMethod();
		builder.createMethod("providedGlobalSettings").withStringParameter(globalSettings.path).endMethod();
		
		if (usePrivateRepository){
			builder.createMethod("localRepository").withConstantParameter("LocalRepositoryLocation.LOCAL_TO_WORKSPACE").endMethod();
		}
		builder.closeClosure();
	}
}
