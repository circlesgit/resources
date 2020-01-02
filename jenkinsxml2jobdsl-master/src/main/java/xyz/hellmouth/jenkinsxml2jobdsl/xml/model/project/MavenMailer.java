package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.project;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class MavenMailer implements Buildable{
	
	public String recipients;
	public boolean dontNotifyEveryUnstableBuild;
	public boolean sendToIndividuals;
	public boolean perModuleEmail;

	@Override
	public void build(Builder builder) {
		// TODO Auto-generated method stub
		
	}

}
