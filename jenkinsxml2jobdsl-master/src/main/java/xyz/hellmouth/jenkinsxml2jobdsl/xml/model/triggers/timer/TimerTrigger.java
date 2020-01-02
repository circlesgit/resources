package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.triggers.timer;

import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Buildable;
import xyz.hellmouth.jenkinsxml2jobdsl.jobdsl.Builder;

public class TimerTrigger implements Buildable {

	public String spec;

	@Override
	public void build(Builder builder) {
		builder.createMethod("cron").withStringParameter(spec).endMethod();
		
	}
}
