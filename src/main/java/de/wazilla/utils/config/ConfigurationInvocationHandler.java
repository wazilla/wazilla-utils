package de.wazilla.utils.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class ConfigurationInvocationHandler extends AbstractConfiguration implements InvocationHandler {

    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";

    protected ConfigurationInvocationHandler(List<PropertySource> propertySources) {
        super(propertySources);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isPublicAbstractConfigurationMethod(method)) {
            Method ownPublicMethod = getClass().getMethod(method.getName(), method.getParameterTypes());
            return ownPublicMethod.invoke(this, args);
        } else {
            String key = getKey(method);
            Class<?> returnType = method.getReturnType();
            PropertyConverter propertyConverter = getPropertyConverter(method);
            return getValue(key, propertyConverter, returnType);
        }
    }

    private String getKey(Method method) {
        ConfigurationKey annotation = method.getAnnotation(ConfigurationKey.class);
        if (annotation != null) {
            return annotation.value();
        } else {
            String key = method.getName();
            if (key.startsWith(GET_PREFIX) && key.length() > GET_PREFIX.length() + 1) {
                key = key.substring(3, 4).toLowerCase() + key.substring(4);
            } else if (key.startsWith(IS_PREFIX) && key.length() > IS_PREFIX.length() + 1) {
                key = key.substring(2, 3).toLowerCase() + key.substring(3);
            }
            return key;
        }
    }

    private PropertyConverter getPropertyConverter(Method method) {
        ConfigurationConverter annotation = method.getAnnotation(ConfigurationConverter.class);
        if (annotation != null) {
            Class<? extends PropertyConverter> converterClass = annotation.value();
            try {
                return converterClass.getConstructor().newInstance();
            } catch (ReflectiveOperationException ex) {
                throw new ConfigurationRuntimeException("Error instantiating " + converterClass, ex);
            }
        } else {
            Class<?> returnType = method.getReturnType();
            return propertyConverterMap.get(returnType);
        }
    }

    private boolean isPublicAbstractConfigurationMethod(Method method) {
        return Arrays.stream(AbstractConfiguration.class.getMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .anyMatch(m -> isSameMethod(m, method));
    }

    private boolean isSameMethod(Method m1, Method m2) {
        if (!m1.getName().equals(m2.getName())) return false;
        if (!m1.getReturnType().equals(m2.getReturnType())) return false;
        if (m1.getParameterCount() != m2.getParameterCount()) return false;
        for(int i = 0; i < m1.getParameterCount(); i++) {
            if (m1.getParameterTypes()[i] == m2.getParameterTypes()[i]) return false;
        }
        return true;
    }

}
