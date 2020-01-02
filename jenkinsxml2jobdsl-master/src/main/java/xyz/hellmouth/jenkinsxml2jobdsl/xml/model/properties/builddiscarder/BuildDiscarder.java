package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.builddiscarder;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class BuildDiscarder implements Buildable{

	public Strategy strategy;

	@Override
	public void build(Builder builder) {
		strategy.build(builder);
		
	}
	
	
}
