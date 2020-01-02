package xyz.hellmouth.jenkinsxml2jobdsl.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DocumentFactory {

	public static Document getXmlAsDocument(){
	    Document res = null;
		try {
			InputStream stream = new ByteArrayInputStream(ConfigXML.getXml().getBytes(StandardCharsets.UTF_8));
		    res = PositionalXMLReader.readXML(stream);
		    
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
}
