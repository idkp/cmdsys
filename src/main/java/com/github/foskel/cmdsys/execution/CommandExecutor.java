package com.github.foskel.cmdsys.execution;

import com.github.foskel.cmdsys.CommandBranch;
import com.github.foskel.cmdsys.CommandModel;

import java.util.List;

public interface CommandExecutor {
    ExecutionResult execute(CommandModel command, CommandBranch branch, List<String> parameters);
}
