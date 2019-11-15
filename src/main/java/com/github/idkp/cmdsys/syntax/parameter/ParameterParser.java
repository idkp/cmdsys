package com.github.idkp.cmdsys.syntax.parameter;

public interface ParameterParser {
    Object parse(String parameter, Class<?> type) throws ParameterParsingException;

    boolean canParse(Class<?> type);
}