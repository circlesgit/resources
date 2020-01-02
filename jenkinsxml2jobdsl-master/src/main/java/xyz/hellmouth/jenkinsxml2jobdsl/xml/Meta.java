package xyz.hellmouth.jenkinsxml2jobdsl.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Meta {
	
	public static Set<String> unsupportedTags;
	public static Map<String, List<String>> allLongTagNamesForShortTagNames;
	
	public static void init(){
		unsupportedTags = new HashSet<String>();
		allLongTagNamesForShortTagNames = new HashMap<String, List<String>>();
	}
	
	/**
	 * Recursively concatenate the names of the parent nodes for a node 
	 * to build up a fully qualified node name
	 * 
	 * @param node the node to build the fully qualified name for
	 * @return the fully qualified node name
	 */
	public static String buildLongNameFromParentsOfThisNode(final Node node){
		if (node.getParentNode() != null){
			return node.getNodeName() + buildLongNameFromParentsOfThisNode(node.getParentNode());
		}
		else{
			return node.getNodeName();
		}
	}
	
	/**
	 * Parse a validation event message for an unsupported tag name, to extract
	 * the name of the unsupported tag, and then build and store the fully qualified tag name
	 * of the tag that caused the validation event by comparing the line number on which
	 * the tag name occurs, with the line number on which the validation event occurred
	 * 
	 * @param validationEventMessage
	 * @param validationEventLineNumber
	 */
	public static void extractUnsupportedTagNameAndStoreLongName(final String validationEventMessage, final int validationEventLineNumber){
		String unsupportedTagName = extractUnsupportedTagName(validationEventMessage);
		
		unsupportedTags.add(unsupportedTagName);
		
		storeLongNameForTagOnThisLine(validationEventLineNumber, unsupportedTagName);
	}

	/**
	 * Iterate through all known instances of this tag name, and upon finding the correct instance
	 * of this tag name (based on the line number on which it occurs), build and store the 
	 * long name of this tag in a list of all long names known for this tag name
	 * 
	 * @param validationEventLineNumber
	 * @param unsupportedTagName
	 * @throws NumberFormatException
	 */
	private static void storeLongNameForTagOnThisLine(final int validationEventLineNumber, final String unsupportedTagName)
			throws NumberFormatException {
		NodeList nodeList = getAllInstancesOfTagName(unsupportedTagName);
		
		for (int currentNodeIndex = 0; currentNodeIndex < nodeList.getLength(); currentNodeIndex++){
			Node currentTag = nodeList.item(currentNodeIndex);
			
			Integer lineNumberOfCurrentTag = Integer.parseInt((String) currentTag.getUserData("lineNumber"));
			
			if (lineNumberOfCurrentTag == validationEventLineNumber){
				storeLongNameForThisTag(unsupportedTagName, currentTag);
				break;
			}
		}
	}

	/**
	 * Store the fully qualified name for the current tag 
	 * 
	 * @param unsupportedTagName
	 * @param currentTag
	 */
	private static void storeLongNameForThisTag(final String unsupportedTagName, final Node currentTag) {
		if (allLongTagNamesForShortTagNames.get(unsupportedTagName) == null){
			allLongTagNamesForShortTagNames.put(unsupportedTagName, new ArrayList<String>());
		}
		
		List<String> longNames = allLongTagNamesForShortTagNames.get(unsupportedTagName);
		longNames.add(Meta.buildLongNameFromParentsOfThisNode(currentTag));
	}

	/**
	 * Get a list of all instances of a specific tag name
	 * 
	 * @param unsupportedTag
	 * @return a list of all instances of a specific tag name
	 */
	private static NodeList getAllInstancesOfTagName(final String unsupportedTag) {
		return DocumentFactory.getXmlAsDocument().getElementsByTagName(unsupportedTag);
	}

	/**
	 * Extract the name of an unsupported tag from the ValidationEvent message
	 * 
	 * @param validationEventMessage
	 * @return the name of an unsupported tag
	 */
	private static String extractUnsupportedTagName(final String validationEventMessage) {
		String unsupportedTagIsWithin = validationEventMessage.split(Pattern.quote("\"). "))[0];
		
		String unsupportedTag = unsupportedTagIsWithin.split(Pattern.quote(", local:\""))[1];
		unsupportedTag = unsupportedTag.replace(" ", "");
		
		return unsupportedTag;
	}
	
}
