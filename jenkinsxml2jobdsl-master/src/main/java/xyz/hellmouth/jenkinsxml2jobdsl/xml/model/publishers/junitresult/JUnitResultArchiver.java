package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.junitresult;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class JUnitResultArchiver implements Buildable{

	@XmlAttribute
	public String  plugin;
	
	public String testResults;
	public boolean allowEmptyResults;
	public boolean keepLongStdio;
	public double healthScaleFactor;
	@Override
	public void build(Builder builder) {
		builder.createMethod("archiveJunit").withStringParameter(testResults).endMethodAndOpenClosure();
		builder.createMethod("healthScaleFactor").withDoubleParameter(healthScaleFactor).endMethod();
		builder.createMethod("allowEmptyResults").withBooleanParameter(allowEmptyResults).endMethod();
		
		
		if (keepLongStdio){
			builder.createMethod("retainLongStdout").endMethod();
		}
		builder.closeClosure();
		
	}
}
