package com.github.foskel.cmdsys;

import java.util.List;

public interface CommandModel {
    Object getCommandReference();

    String[] getHandles();

    List<CommandBranch> getBranches();

    String getPermission();

    boolean hasPermission();

    CommandBranch getUnhandledBranch();

    default boolean isUnhandled() {
        return getUnhandledBranch() != null;
    }
}