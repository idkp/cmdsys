package com.github.idkp.cmdsys;

import com.github.idkp.cmdsys.syntax.parameter.ParameterParser;

import java.util.List;

public interface CommandRegistry {
    boolean register(Object command);

    boolean unregister(Object command);

    boolean has(String handle);

    <T extends CommandModel> T find(String handle);

    <T extends CommandModel> T find(Class<?> commandClass);

    boolean registerParser(ParameterParser parser, boolean updateAllTypes);

    boolean registerParser(ParameterParser parser);

    boolean unregisterParser(ParameterParser parser);

    ParameterParser findParser(Class<?> type);

    List<ParameterParser> getParameterParsers();

    void clear();
}
