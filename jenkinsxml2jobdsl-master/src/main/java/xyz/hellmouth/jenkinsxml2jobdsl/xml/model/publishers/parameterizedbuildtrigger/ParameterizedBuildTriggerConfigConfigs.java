package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.parameterizedbuildtrigger;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class ParameterizedBuildTriggerConfigConfigs implements Buildable{
	
	@XmlElements({
		@XmlElement(name="hudson.plugins.git.GitRevisionBuildParameters", type=GitRevisionBuildParameters.class),
		@XmlElement(name="hudson.plugins.parameterizedtrigger.matrix.MatrixSubsetBuildParameters", type=ParameterizedBuildTriggerConfigMatrixSubsetBuildParameters.class),
		@XmlElement(name="hudson.plugins.parameterizedtrigger.PredefinedBuildParameters", type=ParameterizedBuildTriggerConfigPredefinedBuildParameters.class),
		@XmlElement(name="hudson.plugins.parameterizedtrigger.FileBuildParameters", type=ParameterizedBuildTriggerConfigFileBuildParameters.class),
		@XmlElement(name="hudson.plugins.parameterizedtrigger.CurrentBuildParameters", type=ParameterizedBuildTriggerConfigCurrentBuildParameters.class),
		@XmlElement(name="hudson.plugins.parameterizedtrigger.NodeParameters", type=ParameterizedBuildTriggerConfigNodeParameters.class),
		@XmlElement(name="hudson.plugins.parameterizedtrigger.BooleanParameters", type=ParameterizedBuildTriggerConfigBooleanParameters.class),
		@XmlElement(name="hudson.plugins.parameterizedtrigger.SubversionRevisionBuildParameters", type=SubversionRevisionBuildParameters.class)
	    })
	public List<Buildable> elements;

	@Override
	public void build(Builder builder) {
		if (elements != null){
			if (!elements.isEmpty()){
				builder.create("parameters").openClosure();
				
				for (Buildable opbt :elements){
					opbt.build(builder);
				}
				
				builder.closeClosure();
			}
		}
		
	}

}
