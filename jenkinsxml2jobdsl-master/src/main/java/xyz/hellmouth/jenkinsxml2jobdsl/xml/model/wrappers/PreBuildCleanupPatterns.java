package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.wrappers;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class PreBuildCleanupPatterns implements Buildable{
	
	@XmlElements({
		@XmlElement(name="hudson.plugins.ws__cleanup.Pattern", type=PreBuildCleanupPattern.class)
	    })
	public List<Buildable> elements;

	@Override
	public void build(Builder builder) {
		for (Buildable b : elements){
			b.build(builder);
		}
	}

}
