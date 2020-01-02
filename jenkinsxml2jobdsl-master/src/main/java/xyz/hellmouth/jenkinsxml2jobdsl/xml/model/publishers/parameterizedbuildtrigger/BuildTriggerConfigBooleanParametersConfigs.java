package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class BuildTriggerConfigBooleanParametersConfigs implements Buildable{

	@XmlElements({
		@XmlElement(name="hudson.plugins.parameterizedtrigger.BooleanParameterConfig", type=BooleanParameterConfig.class)
	    })
	public List<Buildable> elements;

	@Override
	public void build(Builder builder) {
		if (elements != null){
			for (Buildable opbtbool : elements){
				opbtbool.build(builder);
			}
		}
		
	}
}
