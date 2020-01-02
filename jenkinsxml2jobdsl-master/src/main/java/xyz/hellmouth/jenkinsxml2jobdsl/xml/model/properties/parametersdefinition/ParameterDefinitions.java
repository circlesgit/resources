package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters.BooleanParameterDefinition;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters.CVSTagsParameterDefinition;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters.ChoiceParameterDefinition;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters.CredentialsParameterDefinition;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters.FileParameterDefinition;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters.ListSubversionTagsParameterDefinition;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters.PasswordParameterDefinition;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters.RunParameterDefinition;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters.StringParameterDefinition;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters.TextParameterDefinition;

public class ParameterDefinitions implements Buildable{

	@XmlElements({
		@XmlElement(name="hudson.model.StringParameterDefinition", type=StringParameterDefinition.class),
		@XmlElement(name="hudson.model.FileParameterDefinition", type=FileParameterDefinition.class),
		@XmlElement(name="hudson.model.BooleanParameterDefinition", type=BooleanParameterDefinition.class),
		@XmlElement(name="hudson.scm.CvsTagsParamDefinition", type=CVSTagsParameterDefinition.class),
		@XmlElement(name="hudson.model.ChoiceParameterDefinition", type=ChoiceParameterDefinition.class),
		@XmlElement(name="hudson.model.TextParameterDefinition", type=TextParameterDefinition.class),
		@XmlElement(name="hudson.model.RunParameterDefinition", type=RunParameterDefinition.class),
		@XmlElement(name="hudson.model.PasswordParameterDefinition", type=PasswordParameterDefinition.class),
		@XmlElement(name="com.cloudbees.plugins.credentials.CredentialsParameterDefinition", type=CredentialsParameterDefinition.class),
		@XmlElement(name="hudson.scm.listtagsparameter.ListSubversionTagsParameterDefinition", type=ListSubversionTagsParameterDefinition.class)
	    })
	public List<Buildable> elements;

	@Override
	public void build(Builder builder) {
		builder.create("parameters").openClosure();

		if (elements != null) {
			for (Buildable parameter : elements) {
				parameter.build(builder);
			}
		}
		
		builder.closeClosure();
	}
}
