package de.hzd.commons.xml;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

public class XMLTest {

	@Test
	public void createNamespaceContext() throws Exception {
		String xml = XML.fromResource("classpath:/xml/multiple-namespaces-in-whole-document.xml");
		Document document = XML.toDocument(xml);
		XML.createNamespaceContext(document);
	}
	
}
