package com.github.idkp.cmdsys.syntax.parameter;

public final class BooleanParameterParser implements ParameterParser {

    @Override
    public Object parse(String parameter, Class<?> type) throws ParameterParsingException {
        switch (parameter) {
            case "true":
            case "yes":
            case "y":
            case "1":
                return true;
            case "false":
            case "no":
            case "n":
            case "0":
                return false;
            default:
                throw new ParameterParsingException("Invalid boolean parameter: " + parameter);
        }
    }

    @Override
    public boolean canParse(Class<?> type) {
        return type == Boolean.TYPE || type == Boolean.class;
    }
}
