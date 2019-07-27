package com.github.foskel.cmdsys.syntax.parameter;

public final class CharSequenceParameterParser implements ParameterParser {

    @Override
    public Object parse(String parameter, Class<?> type) {
        return parameter;
    }

    @Override
    public Class[] getParameterTypes() {
        return new Class[]{Character.TYPE, Character.class, String.class};
    }
}