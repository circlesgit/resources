package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.Publisher;

public class ParameterizedBuildTriggerConfigs implements Buildable{

	@XmlElements({
		@XmlElement(name="hudson.plugins.parameterizedtrigger.BuildTriggerConfig", type=ParameterizedBuildTriggerConfig.class)
	    })
	public List<Buildable> elements;

	@Override
	public void build(Builder builder) {
		for (Buildable o : elements){
			o.build(builder);
		}
		
	}
}
