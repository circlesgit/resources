package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.publishers.ccmpublisher;

import javax.xml.bind.annotation.XmlAttribute;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class CCMThresholds implements Buildable{

	@XmlAttribute
	public String plugin;
	
	public String unstableTotalAll;
	public String unstableTotalHigh;
	public String unstableTotalNormal;
	public String unstableTotalLow;
	public String unstableNewAll;
	public String unstableNewHigh;
	public String unstableNewNormal;
	public String unstableNewLow;
	public String failedTotalAll;
	public String failedTotalHigh;
	public String failedTotalNormal;
	public String failedTotalLow;
	public String failedNewAll;
	public String failedNewHigh;
	public String failedNewNormal;
	public String failedNewLow;
	@Override
	public void build(Builder builder) {
		builder.createMethod("thresholds");
		builder.with("\n\tunstableTotal: [").with("all: "+unstableTotalAll+", ").with("high: "+unstableTotalHigh+", ").with("normal: "+unstableTotalNormal+", ").with("low: "+unstableTotalLow+"],");
		builder.with("\tfailedTotal: [").with("all: "+failedTotalAll+", ").with("high: "+failedTotalHigh+", ").with("normal: "+failedTotalNormal+", ").with("low: "+failedTotalLow+"],");
		builder.with("\tunstableNew: [").with("all: "+unstableNewAll+", ").with("high: "+unstableNewHigh+", ").with("normal: "+unstableNewNormal+", ").with("low: "+unstableNewLow+"],");
		builder.with("\tfailedNew: [").with("all: "+failedNewAll+", ").with("high: "+failedNewHigh+", ").with("normal: "+failedNewNormal+", ").with("low: "+failedNewLow+"]");
		builder.endMethod();
	}
	
}
