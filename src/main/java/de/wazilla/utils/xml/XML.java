package de.wazilla.utils.xml;

import de.wazilla.utils.Streams;
import org.w3c.dom.*;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class XML {

    private static final String CLASSPATH_PROTOCOL = "classpath:";
    private static final String DEFAULT_XML_ENCODING = StandardCharsets.UTF_8.name();
    private static final Pattern ENCODING_PATTERN = Pattern.compile(" encoding=[\'|\"](.*?)[\'|\"]");

    private XML() {
        // Utility class
    }

    /**
     * <p>
     * Erzeugt einen {@link NamespaceContext} anhand der xmlns-Attribute im übergebenen XML-Dokument. Sofern im Dokument ein Prefix
     * angegeben wurde (xmlns:foo=...), so wird dieser verwendet. Bei Namespace-Angaben ohne Prefix (xmlns=...) wird ein Prefix
     * generiert. Dabei bekommt der erste (und ggf. einzige) Namespace den Prefix "default", alle weiteren (bei inneren Elementen
     * gefundenen) Namespaces bekommen einen Prefix der mit "ns" beginnt und dahinter (beginnend mit 1) fortlaufend durchnummeriert
     * wird. Also "ns1", "ns2",... usw.
     * </p>
     * <p>
     * Es wird auch ein {@link NamespaceContext} erzeugt, auch wenn keine Namespace-Angaben im Dokument enthalten sind! Der erzeugte
     * Context mappt dann immer noch die Prefixe für "xml" und "xmlns" (entsprechend der XML-Spezifikation).
     * </p>
     *
     * @param document das {@link Document}, aus welchem der {@link NamespaceContext} erzeugt werden soll.
     * @return den erzeugten {@link NamespaceContext} oder <code>null</code>, wenn kein XML-Dokument übergeben wurde.
     */
    public static NamespaceContext createNamespaceContext(Document document) {
        if (document == null) return null;
        Map<String, Set<String>> namespaceMap = new HashMap<>();
        int defaultPrefixCounter = 0;
        DocumentTraversal traversal = (DocumentTraversal) document;
        NodeIterator nodeIterator = traversal.createNodeIterator(document, NodeFilter.SHOW_ELEMENT, null, false);
        Node node = null;
        while ((node = nodeIterator.nextNode()) != null) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NamedNodeMap attributes = element.getAttributes();
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node attributeNode = attributes.item(i);
                    String name = attributeNode.getNodeName();
                    String value = attributeNode.getNodeValue();
                    if (name.startsWith(XMLConstants.XMLNS_ATTRIBUTE)) {
                        String prefix = null;
                        int index = name.indexOf(':');
                        if (index < 0) {
                            if (defaultPrefixCounter == 0) {
                                // der erste (und ggf. einzige) Default-Namespace
                                // bekommt "default" als Prefix
                                prefix = "default";
                            } else {
                                // Wenn weitere (innere) Namespaces ohne Prefix gefunden werden
                                // bekommen diese ns<lfdNr> als Prefix, also ns1, ns2,...
                                prefix = "ns" + defaultPrefixCounter;
                            }
                            defaultPrefixCounter++;
                        } else {
                            prefix = name.substring(index + 1);
                        }
                        namespaceMap.put(value, Collections.singleton(prefix));
                    }
                }
            }
        }
        return new MapNamespaceContext(namespaceMap);
    }

    /**
     * Liest XML aus dem Stream in einem String ein. Ermittelt dabei automatisch das Encoding. Der Stream wird anschließend
     * geschlossen.
     *
     * @param in der {@link InputStream} aus dem gelesen werden soll.
     * @return {@link String} mit dem XML oder <code>null</code> wenn übergebene InputStream null war.
     * @throws IOException wenn der Stream nicht gelesen werden konnte.
     */
    public static String fromStream(final InputStream in) throws IOException {
        if (in == null) return null;
        byte[] bytes = Streams.read(in);
        String encoding = XML.getEncoding(bytes);
        return new String(bytes, encoding);
    }

    /**
     * Liest XML von einer Resource (Datei) im Classpath in einen String ein. Das Encoding wird dabei automatisch ermittelt.
     *
     * @param location Ort der Resource (Datei) im Classpath, ggf. mit "classpath:" als Prefix
     * @return Eine {@link String} mit XML oder <code>null</code> wenn die location null war.
     * @throws IOException wenn die Resource nicht gelesen werden konnte, z.B. in Form einer {@link FileNotFoundException} wenn sie
     *                     nicht gefunden wurde.
     */
    public static String fromResource(final String location) throws IOException {
        if (location == null) return null;
        String name = location;
        if (name.toLowerCase().startsWith(CLASSPATH_PROTOCOL)) name = name.substring(CLASSPATH_PROTOCOL.length());
        InputStream in = XML.class.getResourceAsStream(name);
        if (in == null) throw new FileNotFoundException(CLASSPATH_PROTOCOL + name);
        return fromStream(in);
    }

    /**
     * Ermittelt das XML-Encoding anhand der Angaben in der XML-Deklaration des übergebenen XMLs. Wenn im XML keine Deklaration
     * <code>&lt;?xml .../&gt;</code> vorhanden ist oder diese keine Angabe zum Encoding macht, wird entsprechend der Spezifikation
     * UTF-8 als Default zurückgegebem.
     *
     * @param bytes das XML als byte[]
     * @return die Encoding-Angabe oder <code>null</code>, wenn nichts übergeben wurde.
     */
    public static String getEncoding(byte[] bytes) {
        if (bytes == null) return null;
        // Lt. XML Spec besteht die XML-Declaration nur aus ASCII-Zeichen
        // da zum ermitteln des Encoding alles au�erhalb der Declaration egal ist
        // wandelt wir das byte[] einfach (ggf. fehlerhaft) in einen ASCII-String um.
        // Daraus wird dann das Encoding herausgelesen.
        // Hinzu kommt, dass US-ASCII zu den Charsets geh�rt, die von jeder Java -
        // Implementierung unterst�tzt werden muss. Siehe
        // https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html
        // Und die Klasse java.nio.charset.StandardCharsets
        // Siehe auch:
        // Extensible Markup Language (XML) 1.0 (Fifth Edition) (https://www.w3.org/TR/xml/)
        // Appendix F Autodetection of Character Encodings (Non-Normative):
        // "Because the contents of the encoding declaration are restricted to characters from the ASCII repertoire[...]
        String xml = new String(bytes, StandardCharsets.US_ASCII);
        return getEncoding(xml);
    }

    /**
     * Ermittelt das XML-Encoding anhand der Angaben in der XML-Deklaration des übergebenen XMLs. Wenn im XML keine Deklaration
     * <code>&lt;?xml .../&gt;</code> vorhanden ist oder diese keine Angabe zum Encoding macht, wird entsprechend der Spezifikation
     * UTF-8 als Default zurückgegebem.
     *
     * @param xml das XML als {@link String}
     * @return die Encoding-Angabe oder <code>null</code>, wenn nichts übergeben wurde.
     */
    public static String getEncoding(String xml) {
        int beginIndex = xml.indexOf("<?");
        if (beginIndex == -1) return DEFAULT_XML_ENCODING;
        int endIndex = xml.indexOf("?>", beginIndex);
        if (endIndex == -1) throw new IllegalStateException("Kein Ende der XML-Deklaration gefunden! XML: " + xml + "!");
        String declaration = xml.substring(beginIndex, endIndex);
        Matcher matcher = ENCODING_PATTERN.matcher(declaration);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return DEFAULT_XML_ENCODING;
        }
    }


    public static Document toDocument(File file) throws IOException, ParserConfigurationException, SAXException {
        try (InputStream in = new FileInputStream(file)) {
            return toDocument(new InputSource(in));
        }
    }

    public static Document toDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
        String encoding = getEncoding(xml);
        byte[] bytes = xml.getBytes(encoding);
        return toDocument(bytes);
    }

    public static Document toDocument(byte[] bytes) throws ParserConfigurationException, SAXException, IOException {
        InputStream in = new ByteArrayInputStream(bytes);
        return toDocument(in);
    }

    public static Document toDocument(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        try {
            return toDocument(new InputSource(in));
        } finally {
            Streams.close(in);
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
