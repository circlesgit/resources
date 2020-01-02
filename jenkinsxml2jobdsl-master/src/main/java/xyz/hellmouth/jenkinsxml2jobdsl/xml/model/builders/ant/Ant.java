package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.ant;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class Ant implements Buildable{
	@XmlAttribute
	public String plugin;
	
	public String targets;
	public String antOpts;
	public String buildFile;
	public String properties;
	
	@Override
	public void build(Builder builder) {
		builder.create("ant").openClosure();

		for (String g : targets.split(" ")){
			if (g.trim().length() > 0){
				builder.createMethod("target").withStringParameter(g).endMethod();
			}
		}
		
		for (String g : antOpts.split(" ")){
			if (g.trim().length() > 0){
				builder.createMethod("javaOpt").withStringParameter(g).endMethod();
			}
		}
		
		if (properties.trim().length() > 0){
			if (properties.contains("=")){
				String[] keyValuePairs = properties.split("\n");
				for (String kv : keyValuePairs){
					String [] kvp = kv.split("=");
					builder.createMethod("prop").withStringParameter(kvp[0].trim()).withStringParameter(kvp[1].trim()).endMethod(); // TODO this needs to be a map
				}
			}
			else{
				builder.createMethod("prop").withStringParameter(properties).withStringParameter("NONE").endMethod();
			}
		}
		
		builder.createMethod("buildFile").withStringParameter(buildFile).endMethod();
		builder.createMethod("antInstallation").withStringParameter(plugin).endMethod();
		
		builder.closeClosure();
		
	}
}
