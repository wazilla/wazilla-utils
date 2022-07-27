package de.wazilla.utils.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConfigurationBuilder {

    private List<PropertySource> propertySources;

    public ConfigurationBuilder() {
        this.propertySources = new ArrayList<>();
    }

    public ConfigurationBuilder withPropertySource(PropertySource propertySource) {
        this.propertySources.add(propertySource);
        return this;
    }

    public ConfigurationBuilder withPropertySources(Collection<PropertySource> propertySources) {
        this.propertySources.addAll(propertySources);
        return this;
    }

    public <T> T build(Class<T> configurationInterface) {
        if (!configurationInterface.isInterface()) throw new IllegalStateException("TODO"); // TODO
        ClassLoader classLoader = configurationInterface.getClassLoader();
        Class<?>[] interfaces = {configurationInterface};
        InvocationHandler handler = new ConfigurationInvocationHandler(this.propertySources);
        return (T) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }

}
