package com.github.foskel.cmdsys;

public interface CommandBranch {
    String[][] getHandles();

    String getId();

    void execute();

    static DirectCommandBranch unhandled(Runnable task) {
        return new DirectCommandBranch(new String[0][], null, task);
    }
}
