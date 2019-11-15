package com.github.idkp.cmdsys.execution;

public final class ExecutionResult {
    private final String errorMessage;
    private final boolean succeeded;

    public ExecutionResult() {
        this.errorMessage = null;
        this.succeeded = true;
    }

    public ExecutionResult(String errorMessage) {
        this.errorMessage = errorMessage;
        this.succeeded = false;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean hasSucceeded() {
        return this.succeeded;
    }
}
