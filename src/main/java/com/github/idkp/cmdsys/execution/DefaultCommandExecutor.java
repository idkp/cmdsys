package com.github.idkp.cmdsys.execution;

import com.github.idkp.cmdsys.CommandBranch;
import com.github.idkp.cmdsys.CommandModel;
import com.github.idkp.cmdsys.annotations.Flag;
import com.github.idkp.cmdsys.annotations.Parameter;
import com.github.idkp.cmdsys.annotations.Supply;
import com.github.idkp.cmdsys.syntax.ArgumentMapper;
import com.github.idkp.cmdsys.syntax.parameter.ParameterParser;
import com.github.idkp.cmdsys.syntax.parameter.ParameterParsingException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class DefaultCommandExecutor implements CommandExecutor {
    private final Collection<ParameterParser> parameterParsers;
    private final ArgumentMapper argumentMapper;

    public DefaultCommandExecutor(Collection<ParameterParser> parameterParsers, ArgumentMapper argumentMapper) {
        this.parameterParsers = parameterParsers;
        this.argumentMapper = argumentMapper;
    }

    @Override
    public ExecutionResult execute(CommandModel commandModel, CommandBranch branch, List<String> parameters) {
        Object commandRef = commandModel.getCommandReference();
        Class<?> commandType = commandRef.getClass();
        Field[] fields = commandType.getDeclaredFields();
        List<FieldData> parameterFields = new ArrayList<>();
        List<FlagData> flagFields = new ArrayList<>();

        for (Field field : fields) {
            Parameter paramAnnotation = field.getAnnotation(Parameter.class);

            if (paramAnnotation != null) {
                String[] paramBranches = paramAnnotation.branch();

                if (!commandModel.isUnhandled() && paramBranches[0].length() != 0) {
                    for (String paramBranch : paramAnnotation.branch()) {
                        if (paramBranch.equals(branch.getId())) {
                            parameterFields.add(new FieldData(field));
                        }
                    }
                } else {
                    parameterFields.add(new FieldData(field));
                }

                continue;
            }

            Supply supplyAnnotation = field.getAnnotation(Supply.class);

            if (supplyAnnotation != null) {
                Object mappedValue = supplyAnnotation.id().equals(Supply.NO_ID)
                        ? argumentMapper.getMapping(field.getType())
                        : argumentMapper.getMapping(field.getType(), supplyAnnotation.id());

                if (mappedValue != null) {
                    field.setAccessible(true);

                    try {
                        field.set(commandRef, mappedValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                continue;
            }

            Flag flagAnnotation = field.getAnnotation(Flag.class);

            if (flagAnnotation != null) {
                String[] flagBranches = flagAnnotation.branch();

                if (flagBranches[0].length() != 0) {
                    boolean found = false;

                    for (String flagBranch : flagBranches) {
                        if (flagBranch.equals(branch.getId())) {
                            found = true;
                        }
                    }

                    if (!found) {
                        continue;
                    }
                }

                flagFields.add(new FlagData(flagAnnotation.shortOpt(), flagAnnotation.longOpt(), field));
            }
        }

        if (parameters.size() < parameterFields.size()) {
            return new ExecutionResult("Missing parameters.");
        }

        paramsLoop:
        for (int i = 0, pI = 0, l = parameters.size(); i < l; i++, pI++) {
            String param = parameters.get(i);

            for (FlagData flagData : flagFields) {
                if (!isFlag(param, flagData)) {
                    continue;
                }

                pI--;
                Field field = flagData.field;

                if (field.getType() == Boolean.TYPE || field.getType() == Boolean.class) {
                    field.setAccessible(true);

                    try {
                        flagData.oldValue = field.get(commandRef);
                        flagData.present = true;
                        field.setBoolean(commandRef, !(boolean) flagData.oldValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else if (i < l - 1) { // check if the flag can have an argument attached to it
                    String nextParam = parameters.get(++i);

                    field.setAccessible(true);

                    try {
                        flagData.oldValue = field.get(commandRef);
                        flagData.present = true;
                        updateParam(flagData, nextParam, commandRef);
                    } catch (IllegalAccessException | ParameterParsingException e) {
                        e.printStackTrace();
                        return new ExecutionResult("Illegal flag: " + param + '.');
                    }
                } else {
                    return new ExecutionResult("Missing flag argument for '" + param + "'.");
                }

                continue paramsLoop;
            }

            if (pI < parameterFields.size()) {
                FieldData paramData = parameterFields.get(pI);

                //for (FieldData paramData : parameterFields) {
                paramData.field.setAccessible(true);

                try {
                    updateParam(paramData, param, commandRef);
                } catch (IllegalAccessException | ParameterParsingException e) {
                    e.printStackTrace();
                    return new ExecutionResult("Illegal parameter: " + param + '.');
                }
                //}
            }
        }

        branch.execute();

        for (FlagData flagData : flagFields) {
            if (flagData.present) {
                try {
                    flagData.field.set(commandRef, flagData.oldValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return new ExecutionResult();
    }

    private void updateParam(FieldData fieldData, String param, Object commandRef) throws IllegalAccessException, ParameterParsingException {
        Field field = fieldData.field;

        for (ParameterParser parameterParser : parameterParsers) {
            Class<?> type = field.getType();

            if (parameterParser.canParse(type)) {
                Object paramValue = parameterParser.parse(param, type);

                fieldData.oldValue = field.get(commandRef);
                field.set(commandRef, paramValue);
            }
        }
    }

    private static boolean isFlag(String param, FlagData flagData) {
        return (param.equals(flagData.shortOpt)) || (param.equals(flagData.longOpt));
    }

    private static class FlagData extends FieldData {
        private static final char SHORT_OPT_PREFIX = '-';
        private static final String LONG_OPT_PREFIX = "--";

        private final String shortOpt;
        private final String longOpt;
        private boolean present;

        private FlagData(String shortOpt, String longOpt, Field field) {
            super(field);
            this.shortOpt = shortOpt.isEmpty() ? null : SHORT_OPT_PREFIX + shortOpt;
            this.longOpt = longOpt.isEmpty() ? null : LONG_OPT_PREFIX + longOpt;
        }
    }

    private static class FieldData {
        protected Object oldValue;
        protected final Field field;

        protected FieldData(Field field) {
            this.field = field;
        }
    }
}
