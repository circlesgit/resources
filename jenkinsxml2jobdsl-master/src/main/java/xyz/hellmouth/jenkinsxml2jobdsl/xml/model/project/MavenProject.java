package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.project;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.Parser;

@XmlRootElement(name=Parser.MAVEN_PROJECT_TYPE) 
public class MavenProject extends BaseProject{
	
	@XmlAttribute
	public String plugin; 
	
	public String goals;
	public String mavenOpts;
	public boolean incrementalBuild;
	public LocalRepository localRepository;
	public boolean archivingDisabled;
	public boolean siteArchivingDisabled;
	public boolean fingerprintingDisabled;
	public boolean resolveDependencies;
	public boolean runHeadless;
	public boolean disableTriggerDownstreamProjects;

	@Override
	public void build(Builder builder) {
		builder.createMethod("mavenJob").withStringParameter(name).endMethodAndOpenClosure();
		
		builder.createMethod("goals").withStringParameter(goals).endMethod();
		builder.createMethod("mavenOpts").withStringParameter(mavenOpts).endMethod();
		builder.createMethod("incrementalBuild").withBooleanParameter(incrementalBuild).endMethod();

		if (localRepository != null) {
			localRepository.build(builder);
		}
		
		builder.createMethod("incrementalBuild").withBooleanParameter(incrementalBuild).endMethod();
		builder.createMethod("archivingDisabled").withBooleanParameter(archivingDisabled).endMethod();
		builder.createMethod("siteArchivingDisabled").withBooleanParameter(siteArchivingDisabled).endMethod();
		builder.createMethod("fingerprintingDisabled").withBooleanParameter(fingerprintingDisabled).endMethod();
		builder.createMethod("resolveDependencies").withBooleanParameter(resolveDependencies).endMethod();
		builder.createMethod("runHeadless").withBooleanParameter(runHeadless).endMethod();
		builder.createMethod("disableDownstreamTrigger").withBooleanParameter(disableTriggerDownstreamProjects).endMethod();
		
		super.build(builder);
		
		builder.closeClosure();
		
	}

	@Override
	public Set<String> getDownstreamJobs() {
		// TODO Auto-generated method stub
		return null;
	}

}
