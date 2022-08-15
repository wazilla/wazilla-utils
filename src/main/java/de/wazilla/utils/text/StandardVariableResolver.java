package de.wazilla.utils.text;

import de.wazilla.utils.Strings;

import java.util.ArrayList;
import java.util.List;

public class StandardVariableResolver implements VariableResolver {

    private static final String START_TAG = "${";
    private static final String END_TAG = "}";

    @Override
    public String resolve(String template, Lookup lookup) {
        if (template == null) return null;
        List<String> parts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int index = 0;
        while(index < template.length()) {
            if (isStartTag(template, index)) {
                if (sb.length() > 0) {
                    String before = sb.toString();
                    parts.add(before);
                    sb = new StringBuilder();
                }
                int start = index;
                int end = -1;
                int startTagCount = 0;
                while(index < template.length() && end < 0) {
                    index++;
                    if (isStartTag(template, index)) {
                        startTagCount++;
                    } else if (isMatchingEndTag(template, index, startTagCount)) {
                        end = index + 1;
                    } else if (isEndTag(template, index)) {
                        startTagCount--;
                    }
                }
                String key = template.substring(start + 2, end - 1);
                if (key.contains(START_TAG)) key = resolve(key, lookup);
                String value = lookup != null ? lookup.lookup(key) : START_TAG + key + END_TAG;
                if (value == null) value = START_TAG + key + END_TAG;
                parts.add(value);
                index = end;
            } else {
                sb.append(template.charAt(index));
                index++;
            }
        }
        if (sb.length() > 0) {
            String after = sb.toString();
            parts.add(after);
        }
        return Strings.join(parts, "");
    }

    private boolean isStartTag(String template, int index) {
        if (index + START_TAG.length() > template.length()) return false;
        return template.startsWith(START_TAG, index);
    }

    private boolean isEndTag(String template, int index) {
        return isMatchingEndTag(template, index, 0);
    }

    private boolean isMatchingEndTag(String template, int index, int startTagCount) {
        return startTagCount == 0 && template.startsWith(END_TAG, index);
    }

}
