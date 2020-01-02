package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.mailer;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class Mailer implements Buildable{
	
	@XmlAttribute
	public String plugin;
	
	public String recipients;
	public boolean dontNotifyEveryUnstableBuild;
	public boolean sendToIndividuals;
	@Override
	public void build(Builder builder) {
		builder.createMethod("mailer").withStringParameter(recipients).withBooleanParameter(dontNotifyEveryUnstableBuild).withBooleanParameter(sendToIndividuals).endMethod();
		
	}

}
