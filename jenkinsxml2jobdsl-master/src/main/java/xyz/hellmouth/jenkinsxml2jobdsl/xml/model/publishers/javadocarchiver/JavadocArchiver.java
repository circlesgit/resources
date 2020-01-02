package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.javadocarchiver;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class JavadocArchiver implements Buildable{
	@XmlAttribute
	public String plugin;
	
	public String javadocDir;
	public boolean keepAll;
	@Override
	public void build(Builder builder) {
		builder.create("archiveJavadoc").openClosure();
		builder.createMethod("javadocDir").withStringParameter(javadocDir).endMethod();
		builder.createMethod("keepAll").withBooleanParameter(keepAll).endMethod();
		builder.closeClosure();
		
	}
}
