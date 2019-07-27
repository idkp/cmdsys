package com.github.foskel.cmdsys.syntax.parameter;

import com.github.foskel.cmdsys.CommandRegistry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public final class ArrayParameterParser implements ParameterParser {
    private static final String ELEMENT_DELIMITER = ";";
    private final CommandRegistry registry;

    public ArrayParameterParser(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Object parse(String parameter, Class<?> type) throws ParameterParsingException {
        ParameterParser elementParser = registry.findParser(type.getComponentType());

        if (elementParser == null) {
            return null;
        }

        String[] rawArray = parameter.split(ELEMENT_DELIMITER);
        List<Object> elements = new ArrayList<>();

        for (String rawElement : rawArray) {
            Object element = elementParser.parse(rawElement, type.getComponentType());

            elements.add(element);
        }

        Object result = Array.newInstance(type.getComponentType(), elements.size());

        for (int i = 0; i < elements.size(); i++) {
            Array.set(result, i, elements.get(i));
        }

        return result;
    }

    @Override
    public boolean canParse(Class<?> type) {
        Class<?> componentType = type.getComponentType();

        return componentType != null && registry.findParser(componentType) != null;

        /*
        return new Class[]{
                String[].class,
                int[].class, Integer[].class,
                double[].class, Double[].class,
                float[].class, Float[].class,
                long[].class, Long[].class,
                byte[].class, Byte[].class,
                short[].class, Short[].class
        };
        */
    }
}
