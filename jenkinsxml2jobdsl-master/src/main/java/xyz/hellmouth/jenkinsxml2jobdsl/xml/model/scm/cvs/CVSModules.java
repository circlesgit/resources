package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.cvs;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class CVSModules {
	@XmlElements({
	       @XmlElement(name="hudson.scm.CvsModule", type=CVSModule.class)
	    })
	List elements;
}
