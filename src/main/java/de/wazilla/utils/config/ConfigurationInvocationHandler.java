package de.wazilla.utils.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class ConfigurationInvocationHandler extends AbstractConfiguration implements InvocationHandler {
    protected ConfigurationInvocationHandler(List<PropertySource> propertySources) {
        super(propertySources);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
