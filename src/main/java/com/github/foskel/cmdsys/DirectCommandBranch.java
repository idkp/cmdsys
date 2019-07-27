package com.github.foskel.cmdsys;

public final class DirectCommandBranch implements CommandBranch {
    private final String[][] handles;
    private final String id;
    private final Runnable task;

    public DirectCommandBranch(String[][] handles, String id, Runnable task) {
        this.handles = handles;
        this.id = id;
        this.task = task;
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
        this.task.run();
    }
}
