package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.git;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class GitExtensions implements Buildable{

	@XmlElements({
		@XmlElement(name="hudson.plugins.git.extensions.impl.RelativeTargetDirectory", type=RelativeTargetDirectory.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.PreBuildMerge", type=PreBuildMerge.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.CloneOption", type=CloneOption.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.SubmoduleOption", type=SubmoduleOption.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.LocalBranch", type=LocalBranch.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.CleanCheckout", type=CleanCheckout.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.CleanBeforeCheckout", type=CleanBeforeCheckout.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.PerBuildTag", type=PerBuildTag.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.IgnoreNotifyCommit", type=IgnoreNotifyCommit.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.DisableRemotePoll", type=DisableRemotePoll.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.UserExclusion", type=UserExclusion.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.PathRestriction", type=PathRestriction.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.MessageExclusion", type=MessageExclusion.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.PruneStaleBranch", type=PruneStaleBranch.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.BuildChooserSetting", type=BuildChooserSetting.class),
		@XmlElement(name="hudson.plugins.git.extensions.impl.WipeWorkspace", type=WipeWorkspace.class)
		
    })
	public List<Buildable> elements;

	@Override
	public void build(Builder builder) {
		if (elements != null){
			builder.create("extensions").openClosure();
			
			for (Buildable ext : elements){
				ext.build(builder);
			}
			
			builder.closeClosure();
		}
		
	}
	
	
}
