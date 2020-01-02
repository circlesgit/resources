package xyz.hellmouth.jenkinsxml2jobdsl.xml;

public class ConfigXML {
	
	/**
	 * Config XML store
	 */
	
	private static String xml;

	public static String getXml() {
		return xml;
	}

	public static void setXml(final String xml) {
		ConfigXML.xml = xml;
	}
}
