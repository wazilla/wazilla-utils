package de.wazilla.utils.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ConfigurationInvocationHandler extends AbstractConfiguration implements InvocationHandler {
    protected ConfigurationInvocationHandler(List<PropertySource> propertySources) {
        super(propertySources);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Proxy: " + proxy.getClass().getName());
        System.out.println("Method: " + method);
        System.out.println("Args: " + Arrays.toString(args));
        String key = getKey(method);
        System.out.println("Key: " + key);
        Class<?> type = method.getReturnType();
        System.out.println("Type: " + type);
        PropertyConverter propertyConverter = getPropertyConverter(method);
        return getValue(key, type);
    }

    private String getKey(Method method) {
        ConfigurationKey annotation = method.getAnnotation(ConfigurationKey.class);
        if (annotation != null) {
            return annotation.value();
        } else {
            String key = method.getName();
            if (key.startsWith("get") && key.length() > 4) {
                key = key.substring(3, 4).toLowerCase() + key.substring(4);
            } else if (key.startsWith("is") && key.length() > 3) {
                key = key.substring(2, 3).toLowerCase() + key.substring(3);
            }
            return key;
        }
    }

    private PropertyConverter getPropertyConverter(Method method) {
        ConfigurationConverter annotation = method.getAnnotation(ConfigurationConverter.class);
        if (annotation != null) {
            try {
                Class<? extends PropertyConverter> converterClass = annotation.value();
                return converterClass.getConstructor().newInstance();
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException(ex); // TODO
            }
        } else {
            Class<?> returnType = method.getReturnType();
            return propertyConverterMap.get(returnType);
        }
    }

}
