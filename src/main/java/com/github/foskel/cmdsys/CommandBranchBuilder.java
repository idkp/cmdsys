package com.github.foskel.cmdsys;

import java.util.ArrayList;
import java.util.List;

public final class CommandBranchBuilder {
    private final List<String[]> handles = new ArrayList<>();
    private final String id;
    private final Command command;

    public CommandBranchBuilder(String id, Command command) {
        this.id = id;
        this.command = command;
    }

    public CommandBranchBuilder addHandle(String... handleAliases) {
        this.handles.add(handleAliases);

        return this;
    }

    public void attach(Runnable task) {
        this.command.addBranch(this.id, this.handles.toArray(new String[this.handles.size()][]), task);
    }
}
