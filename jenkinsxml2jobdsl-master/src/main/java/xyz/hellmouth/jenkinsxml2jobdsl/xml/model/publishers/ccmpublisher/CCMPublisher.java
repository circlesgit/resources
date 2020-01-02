package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.ccmpublisher;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class CCMPublisher implements Buildable{

	@XmlAttribute
	public String plugin;
	
	
	public long healthy;
	
	public long unHealthy;
	
	public String thresholdLimit;
	
	public String pluginName;
	
	public String defaultEncoding;
	
	public boolean canRunOnFailed;
	
	public boolean usePreviousBuildAsReference;
	
	public boolean useStableBuildAsReference;
	
	public boolean useDeltaValues;
	
	public CCMThresholds thresholds;
	
	public boolean shouldDetectModules;
	
	public boolean dontComputeNew;
	
	public boolean doNotResolveRelativePaths;
	
	public String pattern;

	@Override
	public void build(Builder builder) {
		builder.createMethod("ccm").withStringParameter(pattern).endMethodAndOpenClosure();
		
		builder.createMethod("canRunOnFailed").withBooleanParameter(canRunOnFailed).endMethod();
		builder.createMethod("computeNew").withBooleanParameter(!dontComputeNew).endMethod();
		builder.createMethod("defaultEncoding").withStringParameter(defaultEncoding).endMethod();
		builder.createMethod("healthLimits").withLongParameter(healthy).withLongParameter(unHealthy).endMethod();
		builder.createMethod("shouldDetectModules").withBooleanParameter(shouldDetectModules).endMethod();
		builder.createMethod("thresholdLimit").withStringParameter(thresholdLimit).endMethod();
		builder.createMethod("useDeltaValues").withBooleanParameter(useDeltaValues).endMethod();
		builder.createMethod("useStableBuildAsReference").withBooleanParameter(useStableBuildAsReference).endMethod();
		thresholds.build(builder);
		builder.closeClosure();
		
	}
	
}
