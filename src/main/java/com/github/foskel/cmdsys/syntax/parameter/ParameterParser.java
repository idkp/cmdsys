package com.github.foskel.cmdsys.syntax.parameter;

public interface ParameterParser {
    Object parse(String parameter, Class<?> type) throws ParameterParsingException;

    Class[] getParameterTypes();
}
