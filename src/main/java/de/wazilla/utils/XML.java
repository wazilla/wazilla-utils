package de.wazilla.utils;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

public final class XML {

    private XML() {
        // Utility class
    }

    public static Document toDocument(File file) throws IOException, ParserConfigurationException, SAXException {
        try (InputStream in = new FileInputStream(file)) {
            return toDocument(new InputSource(in));
        }
    }

    @SuppressWarnings("java:S6373")
    public static Document toDocument(InputSource is) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        factory.setExpandEntityReferences(false);
        factory.setNamespaceAware(true);
        factory.setXIncludeAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(is);
    }

    public static String toXPath(Node node) {
        if (node == null) return null;
        Node parent = null;
        Stack<Node> stack = new Stack<>();
        if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            Attr attr = (Attr) node;
            parent = attr.getOwnerElement();
        } else if (node.getNodeType() == Node.ELEMENT_NODE || node.getNodeType() == Node.DOCUMENT_NODE) {
            parent = node.getParentNode();
        } else {
            throw new IllegalStateException("Unexpected Node type" + node.getNodeType());
        }
        while (parent != null && parent.getNodeType() != Node.DOCUMENT_NODE) {
            stack.push(parent);
            parent = parent.getParentNode(); // get parent of parent
        }
        StringBuilder sb = new StringBuilder();
        Node currentNode = null;
        while (!stack.isEmpty() && (currentNode = stack.pop()) != null) {
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                if (sb.length() == 1) {
                    // root element - simply append element name
                    sb.append(node.getNodeName());
                } else {
                    // child element - append slash and element name
                    sb.append('/').append(node.getNodeName());
                    // get number of sibling index
                    int prevSiblingsCount = 1;
                    Node prevSibling = node.getPreviousSibling();
                    while (prevSibling != null) {
                        if (prevSibling.getNodeType() == node.getNodeType()) {
                            if (prevSibling.getNodeName().equalsIgnoreCase(node.getNodeName())) prevSiblingsCount++;
                        }
                        prevSibling = prevSibling.getPreviousSibling();
                    }
                    sb.append('[').append(prevSiblingsCount).append(']');
                }
            } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                sb.append("/@");
                sb.append(node.getNodeName());
            } else {
                throw new IllegalStateException("Unexpected Node type" + node.getNodeType());
            }
        }
        return sb.toString();
    }

}
