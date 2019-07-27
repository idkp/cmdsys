package com.github.foskel.cmdsys;

import com.github.foskel.cmdsys.annotations.Branch;
import com.github.foskel.cmdsys.annotations.Command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class AnnotatedCommandModel implements CommandModel {
    private final String[] handles;
    private final List<CommandBranch> branches = new ArrayList<>();
    private final String permission;
    private final boolean hasPermission;
    private CommandBranch unhandledBranch;
    private final Object commandRef;

    public AnnotatedCommandModel(Object command) {
        commandRef = command;
        Class<?> commandType = command.getClass();
        Command commandAnno = commandType.getAnnotation(Command.class);

        if (commandAnno == null) {
            throw new IllegalStateException("The command annotation is absent for the target command object.");
        }

        this.handles = commandAnno.handles();
        this.permission = commandAnno.permission();
        this.hasPermission = !this.permission.isEmpty();

        if (command instanceof Runnable) {
            this.unhandledBranch = CommandBranch.unhandled((Runnable) command);
            return;
        }

        for (Method method : commandType.getDeclaredMethods()) {
            Branch branchAnno = method.getAnnotation(Branch.class);

            if (branchAnno != null) {
                try {
                    String[] handles = branchAnno.handles();

                    method.setAccessible(true);

                    this.branches.add(new ReflectionCommandBranch(
                            command,
                            method,
                            branchAnno.id(),
                            handles,
                            branchAnno.handleAliasDelimiter()
                    ));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object getCommandReference() {
        return this.commandRef;
    }

    @Override
    public String[] getHandles() {
        return this.handles;
    }

    @Override
    public List<CommandBranch> getBranches() {
        return this.branches;
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    @Override
    public boolean hasPermission() {
        return this.hasPermission;
    }

    @Override
    public CommandBranch getUnhandledBranch() {
        return this.unhandledBranch;
    }
}
