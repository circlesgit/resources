package xyz.hellmouth.jenkinsxml2jobdsl.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.StringUtils;

public class ConfigureBlock {
	
	private static List<String> DSL_KEYWORDS = Arrays.asList("label", "description", "project");
	
	private static StringBuffer configureBlockStringBuffer;
	
	/**
	 * Generate the configure block by parsing the XML document, starting from the root node
	 */
	public static void generate(){
		configureBlockStringBuffer = new StringBuffer();
		parseXMLNode(DocumentFactory.getXmlAsDocument().getDocumentElement());
	}
	
	/**
	 * For an unsupported node, declare a new entry in the configure block, and build the configure DSL
	 * For a supported node, parse its child nodes
	 * 
	 * @param node
	 */
	public static void parseXMLNode(final Node node) {
	    if (thisNodeIsUnsupported(node)){
	    	buildDSLForFullyQualifiedNodeName(node);
	    }
	    else {
		    parseChildNodesOfSupportedNode(node);
	    }
	}

	/**
	 * For a given node, declare a new entry in configure block and generate the configure DSL
	 * 
	 * @param node
	 */
	private static void buildDSLForFullyQualifiedNodeName(final Node node) {
		String longTagNameForCurrentNode = Meta.buildLongNameFromParentsOfThisNode(node);
		List<String> allLongTagNamesForShortTagName = Meta.allLongTagNamesForShortTagNames.get(node.getNodeName());
		
		for (String longTagName : allLongTagNamesForShortTagName){
			if (longTagName.equals(longTagNameForCurrentNode)){
				declareNodeAndBuildConfigureBlockDSL(node);
			}
		}
	}

	/**
	 * Declare a new node in the configure block, and generate the child DSL
	 * 
	 * @param node
	 */
	private static void declareNodeAndBuildConfigureBlockDSL(final Node node) {
		declareConfigureBlockNode(node);
		nodeToDSL(node);
	}

	/**
	 * Check if a node name has been registered as unsupported
	 * 
	 * @param node
	 * @return true if the tag unsupported, false otherwise
	 */
	private static boolean thisNodeIsUnsupported(final Node node) {
		return Meta.unsupportedTags.contains(node.getNodeName());
	}

	/**
	 * For a supported node, iterate over all child nodes, and parse
	 * that node to determine if it should be into the configure block,
	 * i.e. if the child node is an unsupported node
	 * 
	 * @param node the supported node whose child nodes we want to parse
	 */
	private static void parseChildNodesOfSupportedNode(final Node node) {
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
		    Node currentNode = nodeList.item(i);
		    if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
		    	parseXMLNode(currentNode);
		    }
		}
	}
	
	/**
	 * For a given node, generate a configure block node declaration
	 * 
	 * @param node
	 */
	private static void declareConfigureBlockNode(final Node node){
		List<String> parentNames = getReverseListOfParentNames(node);
		createConfigureBlockNodeDeclaration(parentNames);
		endConfigureBlockNodeDeclaration();
		markTopLevelElementAsDelegate(parentNames.size());
	}

	/**
	 * For all parent nodes except the root node, wrap the node name in quotes, and 
	 * split with a forward slash. For root nodes, neglect to wrap the node name in 
	 * quotes, and remove hyphens from the tag name
	 * 
	 * @param parentNames
	 */
	private static void createConfigureBlockNodeDeclaration(final List<String> parentNames) {
		for (String parent : parentNames){
			if (Parser.PROJECT_TYPES.contains(parent)){
				configureBlockStringBuffer.append(StringUtils.cleanupParentName(parent)).append(" / ");
			}
			else {
				configureBlockStringBuffer.append("'").append(parent).append("'").append(" / ");
			}
		}
	}

	/**
	 * If a node's immediate parent is the root node, prepend ".delegate" to the node
	 * declaration. Failure to do this can result in DSL errors regarding operations 
	 * on null objects
	 * 
	 * @param numberOfParentNodes
	 */
	private static void markTopLevelElementAsDelegate(final int numberOfParentNodes) {
		if (numberOfParentNodes == 1){
			configureBlockStringBuffer.append("delegate.");
		}
	}

	/**
	 * Strip the trailing forward slash from the string buffer, and append chevrons 
	 * to end the node declaration
	 */
	private static void endConfigureBlockNodeDeclaration() {
		configureBlockStringBuffer.deleteCharAt(configureBlockStringBuffer.length()-1);
		configureBlockStringBuffer.deleteCharAt(configureBlockStringBuffer.length()-1);
		configureBlockStringBuffer.deleteCharAt(configureBlockStringBuffer.length()-1);
		
		configureBlockStringBuffer.append(" << ");
	}

	/**
	 * For a given node, build up a list of parent names, then remove the root parent node,
	 * and then reverse the list to given an order of parent node names that matches the
	 * order expected by the configure block
	 * 
	 * @param node
	 * @return
	 */
	private static List<String> getReverseListOfParentNames(final Node node) {
		List<String> parentNames = new ArrayList<String>();
		
		Node parentNode = node.getParentNode();
		
		while(parentNode != null){
			parentNames.add(parentNode.getNodeName());
			parentNode = parentNode.getParentNode();
		}
		
		parentNames.remove(parentNames.size()-1);
		
		Collections.reverse(parentNames);
		
		return parentNames;
	}
	
	/**
	 * Determine if we should prefix a method declaration with ".delegate"
	 * 
	 * @param nodeName
	 * @return true if the method name is a reserved keyword in JobDSL, false otherwise
	 */
	private static boolean isDelegate(final String nodeName){
		return DSL_KEYWORDS.contains(nodeName);
	}
	
	/**
	 * Process a node into a valid configure block
	 * 
	 * @param node
	 */
	private static void nodeToDSL(final Node node){
		if (node.hasChildNodes()){
			createNodeWithContent(node);
		}
		else{
			createNodeWithAttributesOnly(node);
		}
	}

	/**
	 * Either create a node with simple content, e.g. <mynode>value</mynode> or
	 * create a node with complex content, e.g. <node><childNode>value</childNode></node>
	 * 
	 * @param node
	 */
	private static void createNodeWithContent(final Node node) {
		NodeList nodeList = node.getChildNodes();

		if (nodeList.getLength() > 1){
			createNodeWithComplexContent(node, nodeList);
		}
		else{
			createNodeWithSimpleContent(node);
		}
	}

	/**
	 * Nodes with complex content are declared with mynode('value') but then contain
	 * a block of the form { moreContent('myvalue') }
	 * 
	 * @param node
	 * @param nodeList
	 */
	private static void createNodeWithComplexContent(final Node node, final NodeList nodeList) {
		markMethodWithDelegate(node);
		createMethodDeclarationWithAttributes(node); 
		createChildBlock(nodeList);
	}

	/**
	 * The child block declaration of a complex node
	 * 
	 * @param nodeList
	 */
	private static void createChildBlock(final NodeList nodeList) {
		configureBlockStringBuffer.append(" {").append("\n\n");
		declareChildNodes(nodeList);
		configureBlockStringBuffer.append("}").append("\n\n");
	}

	/**
	 * Declare the child nodes of a given node
	 * 
	 * @param nodeList
	 */
	private static void declareChildNodes(final NodeList nodeList) {
		for (int i = 0; i < nodeList.getLength(); i++) {
		    Node currentNode = nodeList.item(i);
		    if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
		    	nodeToDSL(currentNode);
		    }
		}
	}

	/**
	 * Create a method declaration of the form mynode(plugin : '1.2.3')
	 * 
	 * @param node
	 */
	private static void createMethodDeclarationWithAttributes(final Node node) {
		configureBlockStringBuffer.append("'").append(node.getNodeName()).append("'");
		
		if (node.hasAttributes()){
			configureBlockStringBuffer.append("(");
			createAttributes(node);
			configureBlockStringBuffer.append(")");
		}
	}

	/**
	 * Create a method declaration of the form mynode(123)
	 * 
	 * @param node
	 */
	private static void createNodeWithSimpleContent(final Node node) {
		markMethodWithDelegate(node);
		createMethodDeclaration(node);
		
		if (node.hasAttributes()){
			createAttributes(node); // TODO THIS MAY NOT BE NEEDED, SINCE ATTRIBUTES MAY NOT BE CONSIDERED AS CHILDREN, SO THIS WOULD NEVER BE HIT
		}
		else{
			createNodeContent(node);
		}
		
		endMethodDeclarationWithNewLine();
	}

	/**
	 * Create the content of a method declaration. Numeric and boolean values
	 * are written as-is, string values are wrapped in quotes and escaped
	 * 
	 * @param node
	 */
	private static void createNodeContent(final Node node) {
		String content = node.getTextContent();
		
		if (StringUtils.isNumeric(content) || StringUtils.isBoolean(content)){
			configureBlockStringBuffer.append(content);
		}
		else {
			configureBlockStringBuffer.append("'").append(StringUtils.escapeString(content)).append("'");
		}
	}

	/**
	 * Create a node that only contains attributes, no value, e.g.
	 * <mynode plugin="1.2.3"/>
	 * 
	 * @param node
	 */
	private static void createNodeWithAttributesOnly(final Node node) {
		markMethodWithDelegate(node);
		createMethodDeclaration(node);
		if (node.hasAttributes()){
			createAttributes(node);
		}
		endMethodDeclarationWithNewLine();
	}

	/**
	 * Create a closing parenthesis with a new line
	 */
	private static void endMethodDeclarationWithNewLine() {
		configureBlockStringBuffer.append(")").append("\n\n");
	}

	/**
	 * Write a node's attributes to the buffer
	 * 
	 * @param node
	 */
	private static void createAttributes(final Node node) {
		NamedNodeMap nmap = node.getAttributes();
		for (int i = 0; i < nmap.getLength(); i++){
			Node mappedNode = nmap.item(i);
			configureBlockStringBuffer.append(mappedNode.getNodeName()).append(":").append("'").append(mappedNode.getNodeValue()).append("'").append(",");
		}
		
		configureBlockStringBuffer.deleteCharAt(configureBlockStringBuffer.length()-1);
	}

	/**
	 * Create a method declaration of the form "'<node name>'("
	 * 
	 * @param node
	 */
	private static void createMethodDeclaration(final Node node) {
		configureBlockStringBuffer.append("'").append(node.getNodeName()).append("'").append("(");
	}

	/**
	 * If a method declaration requires to be marked with delegate, make it so
	 * 
	 * @param node
	 */
	private static void markMethodWithDelegate(final Node node) {
		if (isDelegate(node.getNodeName())){
			configureBlockStringBuffer.append("delegate.");
		}
	}
	
	public static String getConfigureBlock(){
		return configureBlockStringBuffer.toString();
	}

}
