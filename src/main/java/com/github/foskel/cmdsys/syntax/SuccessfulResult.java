package com.github.foskel.cmdsys.syntax;

public final class SuccessfulResult implements ParsedCommand {
    private final SyntaxScheme syntaxScheme;

    public SuccessfulResult(SyntaxScheme syntaxScheme) {
        this.syntaxScheme = syntaxScheme;
    }

    @Override
    public SyntaxScheme getSyntaxScheme() {
        return syntaxScheme;
    }

    @Override
    public Type getType() {
        return Type.SUCCESS;
    }
}