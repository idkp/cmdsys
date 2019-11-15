package com.github.idkp.cmdsys.syntax.parameter;

import java.util.Arrays;

public final class EnumParameterParser implements ParameterParser {

    @Override
    public Object parse(String parameter, Class<?> type) throws ParameterParsingException {
        for (Object constant : type.getEnumConstants()) {
            if (parameter.equalsIgnoreCase(constant.toString())) {
                return constant;
            }
        }

        throw new ParameterParsingException("Invalid option: " + parameter + ". Must be one of " + Arrays.toString(type.getEnumConstants()));
    }

    @Override
    public boolean canParse(Class<?> type) {
        return type.isEnum();
    }
}
