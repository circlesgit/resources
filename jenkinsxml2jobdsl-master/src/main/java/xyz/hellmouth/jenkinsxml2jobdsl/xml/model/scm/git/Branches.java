package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.git;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.git.branches.BranchSpec;

public class Branches implements Buildable{
	
	@XmlElements({
       @XmlElement(name="hudson.plugins.git.BranchSpec", type=BranchSpec.class)
    })
	public List<Buildable> elements;

	@Override
	public void build(Builder builder) {
		if (!elements.isEmpty()){
			builder.createMethod("branches");
			for (Buildable b : elements){
				b.build(builder);
			}
			builder.endMethod();
		}
		
	}
}
