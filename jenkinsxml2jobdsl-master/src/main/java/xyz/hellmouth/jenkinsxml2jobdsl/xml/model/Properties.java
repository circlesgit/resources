package xyz.hellmouth.jenkinsxml2jobdsl.xml.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.builddiscarder.BuildDiscarder;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.builddiscarder.GitHubProject;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.ParametersDefinition;

@XmlRootElement(name="properties") 
public class Properties implements Buildable{
	
	private Builder propertiesBlock = new Builder();
	
	@XmlElements({
		@XmlElement(name="jenkins.model.BuildDiscarderProperty", type=BuildDiscarder.class),
		@XmlElement(name="hudson.model.ParametersDefinitionProperty", type=ParametersDefinition.class),
		@XmlElement(name="com.coravy.hudson.plugins.github.GithubProjectProperty", type=GitHubProject.class),
		@XmlElement(name="org.jenkinsci.plugins.runmemaybe.RunMeMaybeJobProperty", type=RunMeMaybeProperty.class),
	    })
	public List<Buildable> elements;

	@Override
	public void build(Builder builder) {
		if (elements != null){
			for (Buildable p : elements){
				if (p instanceof GitHubProject || p instanceof RunMeMaybeProperty){ // some properties are at the root level, others are expected to be within a properties block
					p.build(propertiesBlock);
				}
				else{
					p.build(builder);
				}
			}
			
			builder.create("properties").openClosure();
			builder.append(propertiesBlock.toString());
			builder.closeClosure();
			
		}
	}

}
