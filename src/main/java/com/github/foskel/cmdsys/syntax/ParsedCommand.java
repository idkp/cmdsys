package com.github.foskel.cmdsys.syntax;

public interface ParsedCommand {
    Type getType();

    SyntaxScheme getSyntaxScheme();

    default String getErrorMessage() {
        return null;
    }

    enum Type {
        SUCCESS,
        MISSING_BRANCH,
        INSUFFICIENT_PERMISSIONS,
        INVALID_COMMAND
    }
}
