package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class BooleanParameterConfig implements Buildable{

	public String name;
	public boolean value;
	@Override
	public void build(Builder builder) {
		builder.createMethod("booleanParam").withStringParameter(name).withBooleanParameter(value).endMethod();
		
	}
}
