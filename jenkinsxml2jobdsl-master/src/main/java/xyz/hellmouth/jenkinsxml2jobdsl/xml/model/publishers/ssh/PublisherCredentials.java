package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.ssh;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class PublisherCredentials implements Buildable{
	@XmlAttribute(name="class")
	public String clazz;
	
	public String secretPassphrase;
	public String key;
	public String keyPath;
	public String username;
	@Override
	public void build(Builder builder) {
		if (username.trim().length() > 0){
			builder.createMethod("credentials").withStringParameter(username).endMethodAndOpenClosure();
			builder.createMethod("key").withStringParameter(key).endMethod();
			builder.createMethod("pathToKey").withStringParameter(keyPath).endMethod();
			builder.closeClosure();
		}
		
	}

}
