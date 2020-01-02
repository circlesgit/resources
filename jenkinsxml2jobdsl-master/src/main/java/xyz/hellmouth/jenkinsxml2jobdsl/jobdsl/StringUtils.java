package xyz.hellmouth.jenkinsxml2jobdsl.jobdsl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StringUtils {
	
	public static String cleanupParentName(String parent){
		parent = parent.replaceAll("-", "");
		parent = parent.substring(parent.lastIndexOf(".")+1);
		return parent;
	}
	
	public static String print(Builder builder){
		return replaceTabsWithSpaces(pretty(builder.toString()));
	}
	
	public static String urlEncode(String stringToEncode){
		try {
			stringToEncode = URLEncoder.encode(stringToEncode, "UTF-8");
			stringToEncode = stringToEncode.replaceAll("%2F", "/");
			stringToEncode = stringToEncode.replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return stringToEncode;
	}
	
	private static String replaceTabsWithSpaces(String job){
		return job.replaceAll("\t", "  ");
	}
	
	public static boolean isNumeric(String value)  {  
	  try {  
	    Double.parseDouble(value);  
	  }  
	  catch(NumberFormatException nfe) {  
	    return false;  
	  }  
	  return true;  
	}
	
	public static boolean isBoolean(String value)  {  
		return "true".equals(value) || "false".equals(value);
	}
	
	public static String tab(int indent){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < indent; i++){
			sb.append("\t");
		}
		return sb.toString();
	}
	
	private static String pretty(final String dslContents){
		final BufferedReader bufReader = new BufferedReader(new StringReader(dslContents));
		
		final StringBuffer result = new StringBuffer();
		
		String line = null;
		int indent = 0;
		
		try {
			while( (line=bufReader.readLine()) != null ){
				if (shouldUnindent(line)){
					indent--;
				}
				result.append(tab(indent)).append(line).append("\n");
				if (shouldIndent(line)){
					indent++;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result.toString();
	}
	
	private static int instancesOf(String character, String line){
		return line.length() - line.replace(character, "").length();
	}
	
	private static boolean shouldUnindent(String line){
		return instancesOf("}", line) > instancesOf("{", line);
		
	}
	
	private static boolean shouldIndent(String line){
		return instancesOf("{", line) > instancesOf("}", line);
	}
	
	public static String escapeString(String stringToEscape){
		if (stringToEscape == null){
			return "";
		}
		stringToEscape = stringToEscape.replaceAll("\\\\", "\\\\\\\\");
		stringToEscape = stringToEscape.replaceAll(Builder.METHOD_PARAMETER_STRING_WRAPPER, Builder.ESCAPE_CHARACTER+Builder.METHOD_PARAMETER_STRING_WRAPPER);
		
		String newline = System.getProperty("line.separator");
		if (stringToEscape.contains(newline)){
			stringToEscape = "''"+stringToEscape+"''";
		}
		
		return stringToEscape;
	}
	
}
