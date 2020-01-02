package xyz.hellmouth.jenkinsxml2jobdsl;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import xyz.hellmouth.jenkinsxml2jobdsl.file.Writer;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.DSLBuilder;
import xyz.hellmouth.jenkinsxml2jobdsl.net.Request;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.ConfigureBlock;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.Meta;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.Parser;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.Project;

public class ProjectConvertor {
	
	private static final Set<String> convertedProjects = new HashSet<String>();
	
	/**
	 * Perform an authenticated request to the target Jenkins, and pull
	 * back the config.xml for a specific job. Parse the config.xml using
	 * JAXB to construct supported JobDSL calls. Next, generate the configure
	 * block using a DocumentFactory, and then write the complete DSL to a file.
	 *  
	 * @param jenkinsServer the Jenkins URL to target
	 * @param username the username to log into the Jenkins instance with
	 * @param apiToken the API token associated with the user
	 * @param jenkinsPort the port to connect to the server on. Defaults to 8080
	 * @param jobName the name of the Jenkins job to convert to JobDSL
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	public static void convert(final String jenkinsServer, final String username, final String apiToken, final String jenkinsPort, final String projectName, final String jobName) throws FileNotFoundException, JAXBException{
		if (convertedProjects.contains(jobName)){
			return;
		}
		
		Request.fetchConfigXML(jenkinsServer, username, apiToken, jenkinsPort, projectName, jobName);
		Meta.init();
		Project project = Parser.configXMLToProject();
		ConfigureBlock.generate();
		String dslScript = DSLBuilder.build(project, jobName);
		Writer.writeToFile(jobName, dslScript);
		convertedProjects.add(jobName);
		
		if (project.getDownstreamJobs() != null){
			for (String p : project.getDownstreamJobs()){
				convert(jenkinsServer, username, apiToken, jenkinsPort, projectName, p);
			}
		}
	}
}
