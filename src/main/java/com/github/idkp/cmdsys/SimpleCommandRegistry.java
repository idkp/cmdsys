package com.github.idkp.cmdsys;

import com.github.idkp.cmdsys.syntax.parameter.ParameterParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SimpleCommandRegistry implements CommandRegistry {
    private final Map<Class<?>, CommandModel> commands = new HashMap<>();
    private final List<ParameterParser> parameterParsers = new ArrayList<>();
    private static List<Class> allParamTypes = new ArrayList<>();
    private static Class[] arrayTypes;

    @Override
    public boolean register(Object command) {
        if (command instanceof Command) {
            this.commands.put(command.getClass(), (Command) command);
        } else {
            this.commands.put(command.getClass(), new AnnotatedCommandModel(command));
        }

        return true;
    }

    @Override
    public boolean unregister(Object command) {
        this.commands.remove(command.getClass());

        return true;
    }

    @Override
    public boolean has(String handle) {
        return this.find(handle) != null;
    }

    @Override
    public <T extends CommandModel> T find(String handle) {
        for (CommandModel model : this.commands.values()) {
            for (String modelHandle : model.getHandles()) {
                if (modelHandle.equals(handle)) {
                    return (T) model;
                }
            }
        }

        return null;
    }

    @Override
    public <T extends CommandModel> T find(Class<?> commandClass) {
        return (T) this.commands.get(commandClass);
    }

    @Override
    public boolean registerParser(ParameterParser parser, boolean updateAllTypes) {
        /*if (updateAllTypes) {
            updateAllParamTypes();
        }*/

        return this.parameterParsers.add(parser);
    }

    @Override
    public boolean registerParser(ParameterParser parser) {
        return registerParser(parser, false);
    }

    @Override
    public boolean unregisterParser(ParameterParser parser) {
        return this.parameterParsers.remove(parser);
    }

    @Override
    public ParameterParser findParser(Class<?> type) {
        for (ParameterParser parser : parameterParsers) {
            if (parser.canParse(type)) {
                return parser;
            }
        }

        return null;
    }

    /*
    @Override
    public List<Class> getAllSupportedParameterTypes() {
        if (allParamTypes.isEmpty()) {
            updateAllParamTypes();
        }

        return allParamTypes;
    }

    @Override
    public Class[] getSupportedArrayTypes() {
        return arrayTypes;
    }

    private void updateAllParamTypes() {
        allParamTypes.clear();

        for (ParameterParser parameterParser : parameterParsers) {
            allParamTypes.addAll(Arrays.asList(parameterParser.getParameterTypes()));
        }

        arrayTypes = new Class[allParamTypes.size()];

        for (int i = 0, l = allParamTypes.size(); i < l; i++) {
            arrayTypes[i] = Array.newInstance(allParamTypes.get(i), 0).getClass();
        }
    }*/

    @Override
    public List<ParameterParser> getParameterParsers() {
        return this.parameterParsers;
    }

    @Override
    public void clear() {
        this.commands.clear();
    }
}
