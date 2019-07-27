package com.github.foskel.cmdsys.syntax;

public final class FailureResult implements ParsedCommand {
    private final Type type;
    private final String message;

    public FailureResult(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public SyntaxScheme getSyntaxScheme() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
