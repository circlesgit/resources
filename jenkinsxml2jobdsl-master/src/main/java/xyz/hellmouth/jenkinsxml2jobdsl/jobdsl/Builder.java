package xyz.hellmouth.jenkinsxml2jobdsl.jobdsl;

import java.util.List;

import xyz.hellmouth.jenkinsxml2jobdsl.xml.ConfigureBlock;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.Parser;

public class Builder {
	
	private StringBuffer builderStringBuffer;
	
	private static final String METHOD_OPENING_PARENS = "(";
	private static final String METHOD_CLOSING_PARENS = ")";
	
	private static final String CLOSURE_OPENING_PARENS = " {";
	private static final String CLOSURE_CLOSING_PARENS = "}";
	
	private static final String LIST_OPENING_PARENS = "[";
	private static final String LIST_CLOSING_PARENS = "]";
	
	private static final String METHOD_PARAMETER_SEPARATOR = ", ";
	private static final String CHARACTER_SPACING = " ";
	public static final String METHOD_PARAMETER_STRING_WRAPPER = "'";
	public static final String ESCAPE_CHARACTER = "\\\\\\";
	private static final String LINE_ENDING = "\n\n";
	private static final String ROCKET = "->";
	public static final String NODE_IDENTIFIER = "it";
	private static final String CHEVRONS = "<<";
	private static final String COLON = ":";
	
	public static boolean isWithinDelegateBlock;
	
	public Builder(){
		builderStringBuffer = new StringBuffer();
	}
	
	public Builder createMethod(final String methodName){
		builderStringBuffer.append(methodName).append(METHOD_OPENING_PARENS);
		return this;
	}
	
	public Builder createUnsupportedMethod(final String methodName){
		if (methodName.equals("label")){
			builderStringBuffer.append("delegate.");
		}
		createMethod(methodName);
		return this;
	}
	
	public Builder createNodePath(String ... elements){
		for (String x : elements){
			builderStringBuffer.append(x).append(CHARACTER_SPACING).append("/").append(CHARACTER_SPACING);
		}
		trimParams();
		
		return this;
	}
	
	public Builder createConfigureBlock(){
		String configureBlockContents = ConfigureBlock.getConfigureBlock();

		if (!configureBlockContents.trim().isEmpty()){
			builderStringBuffer.append("configure").append(CLOSURE_OPENING_PARENS).append(CHARACTER_SPACING).append(StringUtils.cleanupParentName(Parser.getProjectType())).append(CHARACTER_SPACING).append(ROCKET).append(LINE_ENDING);
			
			builderStringBuffer.append(configureBlockContents);
			
			closeClosure();
		}

		return this;
	}
	
	
	public Builder create(final String methodName){
		builderStringBuffer.append(methodName);
		return this;
	}
	
	public Builder openClosure(){
		builderStringBuffer.append(CLOSURE_OPENING_PARENS).append(LINE_ENDING);
		return this;
	}
	
	public Builder openNamedClosure(String name){
//		sb.append(CLOSURE_OPENING_PARENS).append(CHARACTER_SPACING).append(name).append(CHARACTER_SPACING).append(ROCKET).append(LINE_ENDING);
		builderStringBuffer.append(CLOSURE_OPENING_PARENS).append(CHARACTER_SPACING).append(LINE_ENDING);
		return this;
	}
	
	public Builder createString(String name){
		builderStringBuffer.append(METHOD_PARAMETER_STRING_WRAPPER);
		create(name);
		builderStringBuffer.append(METHOD_PARAMETER_STRING_WRAPPER);
		return this;
	}
	
	public Builder append(String value){
		builderStringBuffer.append(value);
		return this;
	}
	
	public Builder openConfigureClosure(String name){
		builderStringBuffer.append(CHARACTER_SPACING).append(CHEVRONS).append(CHARACTER_SPACING);
		createString(name);
		openClosure();
		return this;
	}
	
	public Builder openConfigureClosureWithAttributes(String name, String ... attributes){
		builderStringBuffer.append(CHARACTER_SPACING).append(CHEVRONS).append(CHARACTER_SPACING);
		createString(name);
		builderStringBuffer.append(METHOD_OPENING_PARENS);
		for (int i = 0; i < attributes.length; i+=2){
			withAttribute(attributes[i], attributes[i+1]);
		}
		trimParams();
		builderStringBuffer.append(METHOD_CLOSING_PARENS);
		
		openClosure();
		return this;
	}

	
	public Builder closeClosure(){
		builderStringBuffer.append(CLOSURE_CLOSING_PARENS).append(LINE_ENDING);
		return this;
	}
	
	public Builder endMethod(){
		trimParams();
		builderStringBuffer.append(METHOD_CLOSING_PARENS).append(LINE_ENDING);
		return this;
	}
	
	public Builder endMethodAndOpenClosure(){
		trimParams();
		builderStringBuffer.append(METHOD_CLOSING_PARENS).append(CLOSURE_OPENING_PARENS).append(LINE_ENDING);
		return this;
	}
	
	public Builder withStringParameter(final String parameterName){
		builderStringBuffer.append(METHOD_PARAMETER_STRING_WRAPPER).append(StringUtils.escapeString(parameterName).trim()).append(METHOD_PARAMETER_STRING_WRAPPER).append(METHOD_PARAMETER_SEPARATOR);
		return this;
	}
	
	public Builder withAttribute(final String attributeName, final String attributeValue){
		builderStringBuffer.append(attributeName).append(COLON).append(METHOD_PARAMETER_STRING_WRAPPER).append(StringUtils.escapeString(attributeValue)).append(METHOD_PARAMETER_STRING_WRAPPER).append(METHOD_PARAMETER_SEPARATOR);
		return this;
	}
	
	public Builder with(final String content){
		builderStringBuffer.append(content);
		return this;
	}
	
	public Builder withConstantParameter(final String parameterName){
		builderStringBuffer.append(parameterName).append(METHOD_PARAMETER_SEPARATOR);
		return this;
	}
	
	public Builder withBooleanParameter(final boolean parameterName){
		builderStringBuffer.append(parameterName).append(METHOD_PARAMETER_SEPARATOR);
		return this;
	}
	
	public Builder withLongParameter(final long parameterName){
		builderStringBuffer.append(parameterName).append(METHOD_PARAMETER_SEPARATOR);
		return this;
	}
	
	public Builder withDoubleParameter(final double parameterName){
		builderStringBuffer.append(parameterName).append(METHOD_PARAMETER_SEPARATOR);
		return this;
	}
	
	public Builder withListParameter(final List parameterName){
		
		builderStringBuffer.append(LIST_OPENING_PARENS);
		
		for (Object x : parameterName){
			withStringParameter((String)x);
		}
		
		trimParams();
		
		
		builderStringBuffer.append(LIST_CLOSING_PARENS).append(METHOD_PARAMETER_SEPARATOR);
		
		return this;
		
	}
	
	public Builder trimParams(){
		
		if (lastCharIs(builderStringBuffer,'(') || lastCharIs(builderStringBuffer, ']')){
			return this;
		}
		
		for (int i = 0; i < METHOD_PARAMETER_SEPARATOR.length(); i++){
			builderStringBuffer.deleteCharAt(builderStringBuffer.length()-1);
		}
		return this;
	}
	
	private boolean lastCharIs(StringBuffer s, char last){
		return s.charAt(s.length()-1) == last;
	}
	
	public String toString(){
		return builderStringBuffer.toString();
	}

}
