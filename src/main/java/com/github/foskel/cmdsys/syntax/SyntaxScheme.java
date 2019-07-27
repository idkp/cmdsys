package com.github.foskel.cmdsys.syntax;

import com.github.foskel.cmdsys.CommandBranch;
import com.github.foskel.cmdsys.CommandModel;

import java.util.List;

public final class SyntaxScheme {
    private final CommandModel commandModel;
    private final List<String> parameters;
    private final CommandBranch selectedBranch;

    public SyntaxScheme(CommandModel commandModel, List<String> parameters, CommandBranch selectedBranch) {
        this.commandModel = commandModel;
        this.parameters = parameters;
        this.selectedBranch = selectedBranch;
    }

    public CommandModel getCommand() {
        return commandModel;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public CommandBranch getSelectedBranch() {
        return selectedBranch;
    }
}
