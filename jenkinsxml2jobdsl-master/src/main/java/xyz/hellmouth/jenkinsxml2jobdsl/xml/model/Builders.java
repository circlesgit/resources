package xyz.hellmouth.jenkinsxml2jobdsl.xml.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.ant.Ant;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.batchfile.BatchFile;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.maven.Maven;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.shell.Shell;
import xyz.hellmouth.jenkinsxml2jobdsl.xml.model.builders.ssh.SSHBuilderPlugin;

@XmlRootElement(name="builders") 
public class Builders implements Buildable{

	@XmlElements({
	       @XmlElement(name="hudson.tasks.Shell", type=Shell.class),
	       @XmlElement(name="jenkins.plugins.publish__over__ssh.BapSshBuilderPlugin", type=SSHBuilderPlugin.class),
	       @XmlElement(name="hudson.tasks.Maven", type=Maven.class),
	       @XmlElement(name="hudson.tasks.BatchFile", type=BatchFile.class),
	       @XmlElement(name="hudson.tasks.Ant", type=Ant.class)
	    })
	public List<Buildable> elements;

	@Override
	public void build(xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder builder) {
		if (elements !=null){
			
			builder.create("steps").openClosure();
			
			for (Buildable bo : elements){
				bo.build(builder);
				
			}
			
			builder.closeClosure();
		}
		
	}
}
