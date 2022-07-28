package de.wazilla.utils.config;

@FunctionalInterface
public interface PropertyConverter<T> {

    T convert(String value) throws Exception;

}
