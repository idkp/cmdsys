package com.github.foskel.cmdsys;

import java.util.ArrayList;
import java.util.List;

public abstract class Command implements CommandModel {
    private final String[] handles;
    private final List<CommandBranch> branches = new ArrayList<>();
    private final String permission;
    private final boolean hasPermission;
    private CommandBranch unhandledBranch;

    protected Command(String[] handles, String permission) {
        this.handles = handles;
        this.permission = permission;
        this.hasPermission = permission != null;

        if (this instanceof Runnable) {
            this.unhandledBranch = CommandBranch.unhandled((Runnable) this);
        }
    }

    protected Command(String[] handles) {
        this(handles, null);
    }

    protected boolean addBranch(String id, String[][] handles, Runnable task) {
        if (this.isUnhandled()) {
            return false;
        }

        return this.branches.add(new DirectCommandBranch(handles, id, task));
    }

    @Override
    public Object getCommandReference() {
        return this;
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
