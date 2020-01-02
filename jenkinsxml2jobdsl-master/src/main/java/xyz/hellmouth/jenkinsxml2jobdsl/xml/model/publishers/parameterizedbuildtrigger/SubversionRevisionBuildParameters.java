package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class SubversionRevisionBuildParameters implements Buildable{
	public boolean includeUpstreamParameters;

	@Override
	public void build(Builder builder) {
		builder.createMethod("subversionRevision").withBooleanParameter(includeUpstreamParameters).endMethod();
	}
}
