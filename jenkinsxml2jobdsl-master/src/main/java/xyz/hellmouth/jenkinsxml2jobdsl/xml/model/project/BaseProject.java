package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.project;

import java.util.HashSet;
import java.util.Set;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.Actions;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.BuildWrappers;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.Builders;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.Project;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.Properties;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.Publishers;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.SCM;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.Triggers;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.buildtrigger.BuildTrigger;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger.ParameterizedBuildTrigger;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger.ParameterizedBuildTriggerConfig;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger.ParameterizedBuildTriggerConfigs;

public abstract class BaseProject implements Project {
	
	public Actions actions;

	public String description; 
	public String displayName;  
	public boolean keepDependencies; 
	
	public Properties properties; 
	public SCM scm; 
	
	public long quietPeriod;  
	public long scmCheckoutRetryCount; 
	public boolean canRoam;
	public boolean disabled;  
	public boolean blockBuildWhenDownstreamBuilding;  
	public boolean blockBuildWhenUpstreamBuilding;  
	public String authToken; 
	
	public boolean concurrentBuild; 
	public String customWorkspace; 
	
	public Triggers triggers; 
	
	public Builders builders; 
	public Publishers publishers;  
	public BuildWrappers buildWrappers; 
	
	public void build(Builder builder){
		builder.createMethod("description").withStringParameter(description).endMethod();
		builder.createMethod("displayName").withStringParameter(name).endMethod();
		builder.createMethod("keepDependencies").withBooleanParameter(keepDependencies).endMethod();
		
		if (properties != null){
			properties.build(builder);
		}
		
		if (scm != null){
			scm.build(builder);
		}
		
		builder.createMethod("quietPeriod").withLongParameter(quietPeriod).endMethod();
		builder.createMethod("checkoutRetryCount").withLongParameter(scmCheckoutRetryCount).endMethod();
		builder.createMethod("disabled").withBooleanParameter(disabled).endMethod();
		
		if (blockBuildWhenDownstreamBuilding){
			builder.createMethod("blockOnDownstreamProjects").endMethod();
		}
		if (blockBuildWhenUpstreamBuilding){
			builder.createMethod("blockOnUpstreamProjects").endMethod();
		}
		if(authToken != null){
			builder.createMethod("authenticationToken").withStringParameter(authToken).endMethod();
		}
		builder.createMethod("concurrentBuild").withBooleanParameter(concurrentBuild).endMethod();
		
		if (customWorkspace != null){
			builder.createMethod("customWorkspace").withStringParameter(customWorkspace).endMethod();
		}
		
		if (triggers != null){
			triggers.build(builder);
		}
		
		if (builders != null){
			builders.build(builder);
		}
		
		if (publishers != null){
			publishers.build(builder);
		}
		
		if (buildWrappers != null){
			buildWrappers.build(builder);
		}
		
		builder.createConfigureBlock();
	}
	
	public Set<String> getDownstreamJobs(){
		Set<String> jobs = new HashSet<String>();
		if (publishers.elements != null){
			for (Object o : publishers.elements){
				if (o instanceof BuildTrigger){
					BuildTrigger bt = (BuildTrigger) o;
					String childProjects = bt.childProjects;
					if (!childProjects.trim().isEmpty()){
						String [] projects = childProjects.split(",");
						for (String project : projects){
							String trimmed = project.trim();
							if (!trimmed.isEmpty()){
								jobs.add(trimmed);
							}
						}
					}
				}
				else if (o instanceof ParameterizedBuildTrigger){
					ParameterizedBuildTrigger pbt = (ParameterizedBuildTrigger) o;
					ParameterizedBuildTriggerConfigs configs = pbt.configs;
					if (configs != null){
						for (Object br : configs.elements){
							if (br instanceof ParameterizedBuildTriggerConfig){
								ParameterizedBuildTriggerConfig pbtc = (ParameterizedBuildTriggerConfig) br;
								String [] projects = pbtc.projects.split(",");
								for (String project : projects){
									String trimmed = project.trim();
									if (!trimmed.isEmpty()){
										jobs.add(trimmed);
									}
								}
							}
						}
					}
				}
			}
		}
		return jobs;
	}
	
	
	public String name;
	
	public void setName(String name){
		this.name = name;
	}
	
}
