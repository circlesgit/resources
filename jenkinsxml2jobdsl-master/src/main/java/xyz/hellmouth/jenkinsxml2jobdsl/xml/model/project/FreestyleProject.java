package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.project;

import javax.xml.bind.annotation.XmlRootElement;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.Parser;

@XmlRootElement(name=Parser.FREESTYLE_PROJECT_TYPE) 
public class FreestyleProject extends BaseProject implements Buildable {

	@Override
	public void build(Builder builder) {
		builder.createMethod("freeStyleJob").withStringParameter(name).endMethodAndOpenClosure();
		
		super.build(builder);
		
		builder.closeClosure();
		
	}
	
}
