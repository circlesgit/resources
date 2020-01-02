package xyz.hellmouth.jenkinsxml2jobdsl.net;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.StringUtils;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.ConfigXML;

public class Request {
	
	private static final String PROTOCOL = "http://";
	
	/**
	 * Fetch the XML configuration for a specific job on a Jenkins instance
	 * 
	 * @param server
	 * @param username
	 * @param apiToken
	 * @param port
	 * @param jobName
	 */
	public static void fetchConfigXML(final String server, final String username, final String apiToken, final String port, final String projectName, final String jobName){
		String configXmlLocation = buildLocation(server, port, projectName, jobName);
		GetMethod getMethod = buildHttpMethod(configXmlLocation);
		HttpClient client = buildHttpClient(username, apiToken);
		String xml = executeRequest(getMethod, client);
		ConfigXML.setXml(xml);
	}
	
	/**
	 * Build a HTTP client to execute an authenticated GET request
	 * 
	 * @param username the username to use for authentication
	 * @param apiToken the API token to use for authentication
	 * @return a HTTP client
	 */
	private static HttpClient buildHttpClient(final String username, final String apiToken) {
		setLoggingLevels();
		HttpClient client = new HttpClient();
		authenticateClient(username, apiToken, client);
		if (username != null && apiToken != null){
			client.getParams().setAuthenticationPreemptive(true);
		}
		return client;
	}
	
	/**
	 * Set the HTTP client logging levels
	 */
	private static void setLoggingLevels(){
		Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.SEVERE);
		Logger.getLogger("httpclient.wire.header").setLevel(Level.SEVERE);
		Logger.getLogger("httpclient.wire.content").setLevel(Level.SEVERE);
	}

	/**
	 * Authenticate a HTTP client with a username and API token
	 * 
	 * @param username the username to use for authentication
	 * @param apiToken the API token to use for authentication
	 * @param a HTTP client
	 */
	private static void authenticateClient(final String username, final String apiToken, final HttpClient client) {
		if (username != null && apiToken != null){
			AuthScope scope=new AuthScope(AuthScope.ANY_HOST,AuthScope.ANY_PORT,AuthScope.ANY_REALM,AuthScope.ANY_SCHEME);
			client.getState().setCredentials(
				scope,
				new UsernamePasswordCredentials(username, apiToken)
			);
		}
	}
	
	/**
	 * Build a GET request to target the config.xml location
	 * 
	 * @param configXmlLocation the location to perform a GET request against
	 * @return a GET request
	 */
	private static GetMethod buildHttpMethod(final String configXmlLocation) {
		GetMethod getMethod = new GetMethod(configXmlLocation);
		
		getMethod.setDoAuthentication(true);
		getMethod.setFollowRedirects(true);
		
		return getMethod;
	}

	/**
	 * Build the URL to pull the config.xml from
	 * 
	 * @param server the Jenkins server to target
	 * @param port the port to connect to the Jenkins instance on
	 * @param jobName the job to pull the config.xml for
	 * @return
	 */
	private static String buildLocation(final String server, final String port, final String projectName, final String jobName){
		StringBuilder jenkinsHost = new StringBuilder(PROTOCOL);
		if (projectName == null) {
			jenkinsHost.append(server).append(":").append(port).append("/job/");

		} else {
			jenkinsHost.append(server).append(":").append(port).append("/").append(projectName).append("/job/");

		}
		jenkinsHost.append(StringUtils.urlEncode(jobName));
		jenkinsHost.append("/config.xml");
		return jenkinsHost.toString();
	}
	
	/**
	 * Check for specific status codes and act accordingly
	 * 
	 * @param statusCode
	 */
	private static void checkStatusCode(final int statusCode){
		if (statusCode == 404){
			System.out.println("404 not found");
			System.exit(0);
		}
	}
	
	/**
	 * Execute the GET method using the HttpClient
	 * 
	 * @param getMethod the GET method to execute
	 * @param client the client to execute the method with
	 * 
	 * @return the job config xml
	 */
	private static String executeRequest(final GetMethod getMethod, final HttpClient client) {
		String xml = "";
		try {
			int statusCode = client.executeMethod(getMethod);
			checkStatusCode(statusCode);
			xml = getMethod.getResponseBodyAsString();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return xml;
	}
}
