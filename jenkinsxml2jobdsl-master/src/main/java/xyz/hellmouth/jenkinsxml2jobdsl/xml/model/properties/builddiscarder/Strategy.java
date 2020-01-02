package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.builddiscarder;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class Strategy implements Buildable{

	@XmlAttribute(name="class")
	public String clazz;
	
	public long daysToKeep;
	public long numToKeep;
	public long artifactDaysToKeep;
	public long artifactNumToKeep;
	@Override
	public void build(Builder builder) {
		builder.create("logRotator").openClosure();
		builder.createMethod("artifactDaysToKeep").withLongParameter(artifactDaysToKeep).endMethod();
		builder.createMethod("artifactNumToKeep").withLongParameter(artifactNumToKeep).endMethod();
		builder.createMethod("daysToKeep").withLongParameter(daysToKeep).endMethod();
		builder.createMethod("numToKeep").withLongParameter(numToKeep).endMethod();
		builder.closeClosure();
	}
}
