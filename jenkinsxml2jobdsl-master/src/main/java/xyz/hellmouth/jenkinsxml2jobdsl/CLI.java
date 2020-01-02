package xyz.hellmouth.jenkinsxml2jobdsl;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import static java.lang.System.out;

public class CLI {
	
	private static final Options options = new Options();
	private static final String DEFAULT_PORT = "8080";
	
	private CommandLine commandLine;
	
	public CLI() {
		options.addOption("h", "help", false, "Display the help dialogue");
		options.addOption("u", "username", true, "The username to login to the Jenkins instance with");
		options.addOption("a", "apitoken", true, "The api token for the user");
		options.addOption("j", "jenkins", true, "The Jenkins instance to log in to");
		options.addOption("p", "port", true, "The port the Jenkins instance is running on");
		options.addOption("r", "project", true, "The Jenkins project the job(s) exist under");

	}

	/**
	 * Parse the command line args specified by the user. If the args are invalid 
	 * the usage dialogue is printed. Otherwise, attempt to convert a project based 
	 * on the command line flags specified by the user
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	public void parse(final String[] args) throws FileNotFoundException, JAXBException{
		try{
			processCommandLineArgs(args);
		}
		catch(ParseException e){
			printUsage();
		}
	}

	/**
	 * If the help flag has been specified, print the usage dialogue. Otherwise,
	 * convert a project based on the command line arguments specified by the user
	 * 
	 * @param args the command line flags specified by the user
	 * @throws ParseException
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	private void processCommandLineArgs(final String[] args) throws ParseException, FileNotFoundException, JAXBException {
		commandLine = new BasicParser().parse(options, args);
		if (commandLine.hasOption("h")){
			printUsage();
		}
		else {
			convertProjectsUsingCommandLineArgs();
		}
	}

	/**
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	private void convertProjectsUsingCommandLineArgs() throws FileNotFoundException, JAXBException {
		String server = commandLine.getOptionValue("j");
		
		if (server == null){
			out.println("You must specify a Jenkins instance");
			printUsage();
		}
		
		String username = commandLine.getOptionValue("u");
		String apiToken = commandLine.getOptionValue("a");
		String port = commandLine.hasOption("p") ? commandLine.getOptionValue("p") : DEFAULT_PORT;

		String project = commandLine.getOptionValue("r");
		for (Object jobName : commandLine.getArgList()){
			ProjectConvertor.convert(server, username, apiToken, port, project, (String)jobName);
		}
	}
	
	/**
	 * Print usage of jenkins2dsl
	 */
	private void printUsage(){
		out.println("java -jar jenkins2dsl.jar [-u username -a api-token] -j instance [-p port] [-r project] job1 job2 ...");
		System.exit(0);
	}
	
	
}
