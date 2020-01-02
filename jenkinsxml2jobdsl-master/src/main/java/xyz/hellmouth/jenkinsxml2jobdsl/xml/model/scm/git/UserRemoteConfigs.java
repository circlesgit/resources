package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.git;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.userremoteconfigs.UserRemoteConfig;

public class UserRemoteConfigs implements Buildable{

	@XmlElements({
       @XmlElement(name="hudson.plugins.git.UserRemoteConfig", type=UserRemoteConfig.class)
    })
	public List<Buildable> elements;

	@Override
	public void build(Builder builder) {
		for (Buildable urc : elements){
			urc.build(builder);
		}
	}
}
