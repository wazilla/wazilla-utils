package de.hzd.commons.xml;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * Eine Implementierung von {@link NamespaceContext} mit Hilfe einer einfachen
 * <code>Map&lt;String, List&lt;String&gt;&gt;</code>. Der Key innerhalb der Map ist dabei die URI des Namespace, der Value eine
 * Liste an Prefixen.
 * 
 * @author Ralf Lang
 *
 */
public class MapNamespaceContext implements NamespaceContext {

	private Map<String, Set<String>> namespaceMap;

	/**
	 * Erzeugt einen neuen, zunächst leeren {@link MapNamespaceContext}. Anschließend sollen Namespaces über die
	 * {@link #add(String, String)} Methode hinzugefügt werden.
	 */
	public MapNamespaceContext() {
		this(new LinkedHashMap<String, Set<String>>());
	}

	/**
	 * Erzeugt einen neuen {@link MapNamespaceContext} mit dem übergebenen Namespaces.
	 * 
	 * @param namespaceMap eine {@link Map} mit Namespaces. Der Key ist die Namespace-URI, der Value eine Liste an Prefixen.
	 */
	public MapNamespaceContext(Map<String, Set<String>> namespaceMap) {
		setNamespaceMap(Objects.requireNonNull(namespaceMap));
	}

	public void add(String namespaceURI, String prefix) {
		add(namespaceURI, createSingleEntrySet(prefix));
	}

	/**
	 * Lädt ein Namespace-Mapping aus {@link Properties}. Der Key ist die URI, der Value der Prefix. Mehrere Prefixe
	 * zur selben URI werden hier somit nicht unterstützt.
	 *
	 * @param properties das {@link Properties}-Objekt
	 */
	public void add(Properties properties) {
		if (properties == null) return;
		Enumeration<?> propertyNames = properties.propertyNames();
		while (propertyNames.hasMoreElements()) {
			String namespaceURI = propertyNames.toString();
			String prefix = properties.getProperty(namespaceURI);
			add(namespaceURI, prefix);
		}
	}

	/**
	 * Fügt ein Namespace-Mapping hinzu. Ein ggf. bereits zuvor bereits bestehendes Mapping zu dieser URI
	 * wird dabei überschrieben.
	 *
	 * @param namespaceURI die URI des Namespace
	 * @param prefixes die Prefixe
	 */
	public void add(String namespaceURI, Set<String> prefixes) {
		if (!this.namespaceMap.containsKey(namespaceURI)) {
			this.namespaceMap.put(namespaceURI, new HashSet<>());
		}
		this.namespaceMap.get(namespaceURI).addAll(prefixes);
	}

	@Override
	public String getNamespaceURI(String prefix) {
		if (prefix == null) return null;
		String result = XMLConstants.NULL_NS_URI;
		if (XMLConstants.XML_NS_PREFIX.equals(prefix)) {
			result = XMLConstants.XML_NS_URI;
		} else if (XMLConstants.XMLNS_ATTRIBUTE.equals(prefix)) {
			result = XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
		} else {
			for (String namespaceURI : this.namespaceMap.keySet()) {
				for (String namespacePrefix : this.namespaceMap.get(namespaceURI)) {
					if (namespacePrefix.equals(prefix)) {
						result = namespaceURI;
					}
				}
			}
		}
		return result;
	}

	@Override
	public String getPrefix(String namespaceURI) {
		Iterator<String> prefixIterator = getPrefixes(namespaceURI);
		if (prefixIterator.hasNext()) {
			return prefixIterator.next();
		} else {
			return null;
		}
	}

	@Override
	public Iterator<String> getPrefixes(String namespaceURI) {
		if (namespaceURI == null) throw new IllegalArgumentException("Es wurde keine namespaceURI angegeben!");
		Set<String> prefixes = null;
		if (XMLConstants.XML_NS_URI.equals(namespaceURI)) {
			prefixes = createSingleEntrySet(XMLConstants.XML_NS_PREFIX);
		} else if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
			prefixes = createSingleEntrySet(XMLConstants.XMLNS_ATTRIBUTE);
		} else if (namespaceMap.containsKey(namespaceURI)) {
			prefixes = namespaceMap.get(namespaceURI);
		} else {
			prefixes = new HashSet<String>();
		}
		return Collections.unmodifiableCollection(prefixes).iterator();
	}

	public Map<String, Set<String>> getNamespaceMap() {
		return namespaceMap;
	}

	public void setNamespaceMap(Map<String, Set<String>> namespaceMap) {
		this.namespaceMap = Objects.requireNonNull(namespaceMap);
	}
	
	private Set<String> createSingleEntrySet(String value) {
		Set<String> singleEntrySet = new HashSet<>();
		singleEntrySet.add(value);
		return singleEntrySet;
	}

}
