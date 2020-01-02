package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.project;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.Parser;

@XmlRootElement(name=Parser.MATRIX_PROJECT_TYPE) 
public class MatrixProject extends BaseProject{
	
	@XmlAttribute
	public String plugin; 

	public String combinationFilter;
	public String childCustomWorkspace;
	
	@Override
	public void build(Builder builder) {
		builder.createMethod("matrixJob").withStringParameter(name).endMethodAndOpenClosure();
		
		builder.createMethod("combinationFilter").withStringParameter(combinationFilter).endMethod();
		builder.createMethod("childCustomWorkspace").withStringParameter(childCustomWorkspace).endMethod();
		
		super.build(builder);
		
		builder.closeClosure();
		
	}
}
