package com.github.foskel.cmdsys.syntax.parameter;

public final class CharSequenceParameterParser implements ParameterParser {

    @Override
    public Object parse(String parameter, Class<?> type) {
        return parameter;
    }

    @Override
    public boolean canParse(Class<?> type) {
        return type == Character.TYPE || type == Character.class || type == String.class;
    }
}