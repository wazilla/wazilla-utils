package de.wazilla.utils.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.wazilla.utils.Strings;

public final class XPaths {

    private static final Map<Class<?>, QName> typeMapping = new HashMap<>();
    private static final Map<Class<?>, Supplier<Collection<Node>>> collectionSupplierMapping = new HashMap<>();
    
    static {
        typeMapping.put(String.class, XPathConstants.STRING);
        typeMapping.put(Boolean.class, XPathConstants.BOOLEAN);
        typeMapping.put(Long.class, XPathConstants.NUMBER);
        typeMapping.put(Integer.class, XPathConstants.NUMBER);
        typeMapping.put(Float.class, XPathConstants.NUMBER);
        typeMapping.put(Double.class, XPathConstants.NUMBER);
        typeMapping.put(Short.class, XPathConstants.NUMBER);
        typeMapping.put(Node.class, XPathConstants.NODE);
        typeMapping.put(Element.class, XPathConstants.NODE);
        typeMapping.put(NodeList.class, XPathConstants.NODESET);
        typeMapping.put(List.class, XPathConstants.NODESET);
        typeMapping.put(Set.class, XPathConstants.NODESET);
        typeMapping.put(Collection.class, XPathConstants.NODESET);
        collectionSupplierMapping.put(Set.class, LinkedHashSet::new);
        collectionSupplierMapping.put(Collection.class, LinkedHashSet::new);
        collectionSupplierMapping.put(List.class, ArrayList::new);
    }

    private XPaths() {
        // Utility class
    }

    @SuppressWarnings("unchecked")
    public static <T> T evaluate(Node node, NamespaceContext namespaceContext, String expression, Class<T> returnType) throws XPathException {
        if (node == null) return null;
        if (Strings.isNullOrBlank(expression)) throw new IllegalArgumentException("expression is null or blank: '" + expression + "'!");
        if (returnType == null) throw new IllegalArgumentException("returnType is null!");
        XPathFactory factory = XPathFactory.newInstance(); // The XPathFactory class is not thread-safe
        XPath xPath = factory.newXPath(); // An XPath object is not thread-safe and not reentrant
        if (namespaceContext != null) xPath.setNamespaceContext(namespaceContext);
        QName returnTypeName = typeMapping.get(returnType);
        if (returnTypeName == null) throw new IllegalArgumentException("No mapping for type: " + returnType);
        try {
            Object result = xPath.evaluate(expression, node, returnTypeName);
            if (Collection.class.isAssignableFrom(returnType) && result instanceof NodeList) {
                NodeList nodes = (NodeList) result;
                Supplier<Collection<Node>> collectionSupplier = collectionSupplierMapping.get(returnType);
                return (T) toCollection(nodes, collectionSupplier);
            } else {
                return (T) result;
            }
        } catch (XPathExpressionException ex) {
            String message = "Error evaluating " + expression;
            XPathExpressionException exWithMessage = new XPathExpressionException(message);
            exWithMessage.initCause(ex);
            throw exWithMessage;
        }
    }

    private static <T extends Collection<Node>> T toCollection(NodeList nodes, Supplier<T> collectionSupplier) {
        T collection = collectionSupplier.get();
        for(int index = 0; index < nodes.getLength(); index++) {
            Node node = nodes.item(index);
            collection.add(node);
        }
        return collection;
    }

}
