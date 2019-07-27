package com.github.foskel.cmdsys.syntax;

import com.github.foskel.cmdsys.CommandBranch;
import com.github.foskel.cmdsys.CommandModel;
import com.github.foskel.cmdsys.CommandRegistry;
import com.github.foskel.cmdsys.Sender;

import java.util.ArrayList;
import java.util.List;

import static com.github.foskel.cmdsys.syntax.ParsedCommand.Type.*;

public final class CommandParser {
    private static final String SOURCE_DELIMITER = "\\s+";

    private final StringBuilder quotedParamBuilder = new StringBuilder();

    public ParsedCommand parse(Sender sender, CommandRegistry registry, String source) {
        String[] sourceParts = source.trim().split(SOURCE_DELIMITER);
        List<String> parameters = new ArrayList<>();
        boolean startQuote = false;
        CommandModel command = null;
        CommandBranch currentBranch = null;
        int branchIndex = 0;

        parts:
        for (int i = 0, l = sourceParts.length; i < l; i++) {
            String part = sourceParts[i];

            if (i == 0) {
                command = registry.find(part);
                continue;
            }

            char startChar = part.charAt(0);
            char endChar = part.charAt(part.length() - 1);

            if (startChar == '"' || startChar == '\'') {
                if (endChar == '"' || endChar == '\'') {
                    part = part.substring(1, part.length() - 1);
                } else {
                    startQuote = true;
                    this.quotedParamBuilder.append(part, 1, part.length()).append(' ');
                    continue;
                }
            }

            if (startQuote && (endChar == '"' || endChar == '\'')) {
                this.quotedParamBuilder.append(part, 0, part.length() - 1);
                parameters.add(this.quotedParamBuilder.toString());
                this.quotedParamBuilder.setLength(0);
                startQuote = false;

                continue;
            }

            if (startQuote) {
                this.quotedParamBuilder.append(part).append(' ');
            } else {
                if (branchIndex == 0) {
                    for (CommandBranch branch : command.getBranches()) {
                        if (branch.getHandles() == null) {
                            currentBranch = branch;
                            branchIndex = -1;

                            continue parts;
                        }

                        for (String[] aliases : branch.getHandles()) {
                            for (String alias : aliases) {
                                if (alias.equals(part)) {
                                    currentBranch = branch;
                                    branchIndex++;

                                    continue parts;
                                }
                            }
                        }
                    }
                } else if (branchIndex != -1) {
                    for (CommandBranch branch : command.getBranches()) {
                        String[][] handles = branch.getHandles();

                        if (branchIndex < handles.length) {
                            String[] aliases = handles[branchIndex];

                            for (String alias : aliases) {
                                if (alias.equals(part)) {
                                    currentBranch = branch;
                                    branchIndex++;

                                    continue parts;
                                }
                            }

                            currentBranch = null;
                        }
                    }
                }

                parameters.add(part);
            }
        }

        if (command == null) {
            return new FailureResult(INVALID_COMMAND, "Unknown command.");
        }

        if (currentBranch == null) {
            CommandBranch unhandledBranch = command.getUnhandledBranch();

            if (unhandledBranch == null) {
                return new FailureResult(MISSING_BRANCH, "A command branch must be present to run this command.");
            } else {
                currentBranch = unhandledBranch;
            }
        }

        if (command.hasPermission() && !sender.hasPermission(command.getPermission())) {
            return new FailureResult(INSUFFICIENT_PERMISSIONS, "Insufficient permissions.");
        }

        return new SuccessfulResult(new SyntaxScheme(command, parameters, currentBranch));
    }
}
