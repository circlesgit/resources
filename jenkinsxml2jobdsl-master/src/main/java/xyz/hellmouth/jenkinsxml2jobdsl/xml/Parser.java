package xyz.hellmouth.jenkinsxml2jobdsl.xml;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.Project;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.project.FreestyleProject;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.project.MatrixProject;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.project.MavenProject;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.project.MultiJobProject;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.project.PipelineProject;

public class Parser {
	
	public static final String FREESTYLE_PROJECT_TYPE = "project";
	public static final String MATRIX_PROJECT_TYPE = "matrix-project";
	public static final String MAVEN_PROJECT_TYPE = "maven2-moduleset";
	public static final String MULTI_JOB_PROJECT_TYPE = "com.tikal.jenkins.plugins.multijob.MultiJobProject";
	public static final String PIPELINE_PROJECT_TYPE = "flow-definition";
	
	public static final List<String> PROJECT_TYPES = Arrays.asList(FREESTYLE_PROJECT_TYPE, MATRIX_PROJECT_TYPE, MAVEN_PROJECT_TYPE, MULTI_JOB_PROJECT_TYPE, PIPELINE_PROJECT_TYPE);
	
	private static String projectType;
	
	public static String getProjectType() {
		return projectType;
	}

	/**
	 * Get the name of the root tag of the config xml
	 * 
	 * @return the tag name of the root element
	 */
	private static String determineProjectType(){
		return DocumentFactory.getXmlAsDocument().getDocumentElement().getTagName();
	}
	
	/**
	 * Parse the config XML to a valid Project instance
	 * 
	 * @return a Project instance
	 * @throws JAXBException
	 */
	public static Project configXMLToProject() throws JAXBException{
		Logger.getLogger("com.sun.xml.internal.bind").setLevel(Level.FINEST);
	    Unmarshaller unmarshaller = createUnmarshaller();
	    configureUnmarshaller(unmarshaller);
	    return unmarshallConfig(unmarshaller);
	}

	/**
	 * Set event handlers on the unmarshaller
	 * 
	 * @param unmarshaller the unmarshaller to configure
	 * @throws JAXBException
	 */
	private static void configureUnmarshaller(final Unmarshaller unmarshaller) throws JAXBException {
		unmarshaller.setEventHandler(new ValidationEventHandler() {
	    	 @Override
	    	 public boolean handleEvent(ValidationEvent validationEvent) {
	    		 Meta.extractUnsupportedTagNameAndStoreLongName(validationEvent.getMessage(), validationEvent.getLocator().getLineNumber());
	    		 return true;
	    	 }
	    });
	}

	/**
	 * @param jaxbContext
	 * @return
	 * @throws JAXBException
	 */
	private static Unmarshaller createUnmarshaller() throws JAXBException {
		projectType = determineProjectType();
		
		JAXBContext jaxbContext = getJAXBContext(projectType);
		
		return jaxbContext.createUnmarshaller();
	}

	/**
	 * Unmarshall config XML to a Project instance
	 * 
	 * @param unmarshaller
	 * @param reader
	 * @return a Project instance
	 * @throws JAXBException
	 */
	private static Project unmarshallConfig(final Unmarshaller unmarshaller)
			throws JAXBException {
		
		StringReader reader = new StringReader(ConfigXML.getXml());
		
		if (projectType.equals(FREESTYLE_PROJECT_TYPE)){
	    	return (FreestyleProject) unmarshaller.unmarshal(reader);
	    }
	    else if (projectType.equals(MATRIX_PROJECT_TYPE)){
	    	return (MatrixProject) unmarshaller.unmarshal(reader);
	    }
	    else if (projectType.equals(MAVEN_PROJECT_TYPE)){
	    	return (MavenProject) unmarshaller.unmarshal(reader);
	    }
	    else if (projectType.equals(MULTI_JOB_PROJECT_TYPE)){
	    	return (MultiJobProject) unmarshaller.unmarshal(reader);
	    }
	    else if (projectType.equals(PIPELINE_PROJECT_TYPE)){
	    	return (PipelineProject) unmarshaller.unmarshal(reader);
	    }
		return null;
	}

	/**
	 * Get a JAXB Context for the type of project we are parsing
	 * 
	 * @param projectType the type of project we are parsing
	 * @return a JAXB context or null
	 * @throws JAXBException
	 */
	private static JAXBContext getJAXBContext(final String projectType) throws JAXBException {
		if (projectType.equals(FREESTYLE_PROJECT_TYPE)){
			return JAXBContext.newInstance(FreestyleProject.class);
	    }
	    else if (projectType.equals(MATRIX_PROJECT_TYPE)){
	    	return JAXBContext.newInstance(MatrixProject.class);
	    }
	    else if (projectType.equals(MAVEN_PROJECT_TYPE)){
	    	return JAXBContext.newInstance(MavenProject.class);
	    }
	    else if (projectType.equals(MULTI_JOB_PROJECT_TYPE)){
	    	return JAXBContext.newInstance(MultiJobProject.class);
	    }
	    else if (projectType.equals(PIPELINE_PROJECT_TYPE)){
	    	return JAXBContext.newInstance(PipelineProject.class);
	    }
		return null;
	}
}

