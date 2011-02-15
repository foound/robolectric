package com.xtremelabs.robolectric.res;

import org.w3c.dom.*;

public class StringResourceLoader extends XpathResourceXmlLoader implements ResourceValueConverter {
    private ResourceReferenceResolver<String> stringResolver = new ResourceReferenceResolver<String>("string");

    public StringResourceLoader(ResourceExtractor resourceExtractor) {
        super(resourceExtractor, "/resources/string");
    }

    public String getValue(int resourceId) {
        String escaped = stringResolver.getValue(resourceExtractor.getResourceName(resourceId));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < escaped.length(); i++) {
        	char c = escaped.charAt(i);
        	if (c == '\\') {
        		i++;
        		sb.append(escaped.charAt(i));
        	} else {
        		sb.append(c);
        	}
        }
        return sb.toString();
    }

    public String getValue(String resourceName, boolean isSystem) {
        return getValue(resourceExtractor.getResourceId(resourceName, isSystem));
    }

    @Override protected void processNode(Node node, String name, boolean ignored) {
        stringResolver.processResource(name, node.getTextContent(), this);
    }

    @Override public Object convertRawValue(String rawValue) {
        return rawValue;
    }
}
