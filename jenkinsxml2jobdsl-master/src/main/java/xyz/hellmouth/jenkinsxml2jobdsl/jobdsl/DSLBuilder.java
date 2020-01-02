package xyz.hellmouth.jenkinsxml2jobdsl.jobdsl;

import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.Project;

public class DSLBuilder {

	/**
	 * Build the JobDSL definitions for all objects in the DOM
	 * 
	 * @param project
	 * @param name
	 * 
	 * @return the job dsl script
	 */
	public static String build(final Project project, final String name){
		final Builder builder = new Builder();
		project.setName(name+"_converted");
		project.build(builder);
		final String dslScript = StringUtils.print(builder);
		return dslScript;
	}
}
