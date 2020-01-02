package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.properties.parametersdefinition.parameters.choices;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class A {
	
	@XmlAttribute(name="class")
	public String clazz;

	@XmlElements({
		@XmlElement(name="string", type=String.class)
	})
	public List elements;
}
