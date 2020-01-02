package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.git;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class PathRestriction implements Buildable{

	public String includedRegions;
	public String excludedRegions;
	
	@Override
	public void build(Builder builder) {
		// TODO Unsipported
		
	}
}
