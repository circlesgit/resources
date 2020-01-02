package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.project;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.Parser;

@XmlRootElement(name=Parser.MULTI_JOB_PROJECT_TYPE) 
public class MultiJobProject extends BaseProject{
	
	@XmlAttribute
	public String plugin; 

	@Override
	public void build(Builder builder) {
		builder.createMethod("multiJob").withStringParameter(name).endMethodAndOpenClosure();
		
		super.build(builder);
		
		builder.closeClosure();
		
	}

	@Override
	public Set<String> getDownstreamJobs() {
		// TODO Auto-generated method stub
		return null;
	}

}
