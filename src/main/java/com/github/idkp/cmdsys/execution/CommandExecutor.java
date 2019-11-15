package com.github.idkp.cmdsys.execution;

import com.github.idkp.cmdsys.CommandBranch;
import com.github.idkp.cmdsys.CommandModel;

import java.util.List;

public interface CommandExecutor {
    ExecutionResult execute(CommandModel command, CommandBranch branch, List<String> parameters);
}
