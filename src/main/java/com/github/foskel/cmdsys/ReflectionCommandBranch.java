package com.github.foskel.cmdsys;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public final class ReflectionCommandBranch implements CommandBranch {
    private static final MethodHandles.Lookup METHOD_LOOKUP = MethodHandles.lookup();

    private final Object command;
    private final MethodHandle methodHandle;
    private final String[][] handles;
    private final String id;

    public ReflectionCommandBranch(Object command, Method method,
                                   String id, String[] handles,
                                   String handleAliasDelimiter) throws IllegalAccessException {
        this.command = command;
        this.methodHandle = METHOD_LOOKUP.unreflect(method);
        this.id = id;
        this.handles = new String[handles.length][];

        for (int i = 0, l = handles.length; i < l; i++) {
            this.handles[i] = handles[i].split(handleAliasDelimiter);
        }
    }

    @Override
    public String[][] getHandles() {
        return this.handles;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void execute() {
        try {
            this.methodHandle.invoke(this.command);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
