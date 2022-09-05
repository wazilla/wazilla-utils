package de.wazilla.utils.xml.fluent;

import java.util.List;

import javax.xml.namespace.QName;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLElement {
    
    private final Element element;

    protected XMLElement(Element element) {
        this.element = element;
    }

    public XMLElement append(String tagName) {
        Document document = this.element.getOwnerDocument();
        Element child;
        if (this.element.getNamespaceURI() == null) {
            child = document.createElement(tagName);
        } else {
            child = document.createElementNS(this.element.getNamespaceURI(), tagName);
        }
        this.element.appendChild(child);
        return this;
    }

    public String attribute(String name) {
        return this.element.getAttribute(name);
    }

    public List<XMLElement> childs() {
        return childs((String) null);
    }
        
    public List<XMLElement> childs(String tagName) {
        return childs(new QName(this.element.getNamespaceURI(), tagName));
    }
        
    public List<XMLElement> childs(String namespaceURI, String tagName) {
        return childs(new QName(namespaceURI, tagName));
    }
        
    public List<XMLElement> childs(QName qualifiedName) {
        return null;
    }

    public String namespace() {
        return this.element.getNamespaceURI();
    }

    public XMLElement parent() {
        Element parentElement = (Element) this.element.getParentNode();
        if (parentElement != null) {
            return new XMLElement(parentElement);
        } else {
            return null;
        }
    }

}
