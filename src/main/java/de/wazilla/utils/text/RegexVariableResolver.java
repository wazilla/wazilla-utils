package de.wazilla.utils.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Eine {@link VariableResolver} - Implementierung basierend auf regulaeren Ausdruecken.
 * 
 * @see <a href="http://stackoverflow.com/questions/959731/how-to-replace-a-set-of-tokens-in-a-java-string">Stack Ovwerflow Question</a>
 * @author Ralf Lang
 * 
 */
public class RegexVariableResolver implements VariableResolver {

	@Override
	public String resolve(String template, Lookup lookup) {
		if (template == null) return null;
		Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
		Matcher matcher = pattern.matcher(template);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			String key = matcher.group(1);
			String replacement = lookup != null ? lookup.lookup(key) : null;
			if (replacement != null) {
				replacement = resolve(replacement, lookup);
				matcher.appendReplacement(buffer, "");
				buffer.append(replacement);
			}
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

}
