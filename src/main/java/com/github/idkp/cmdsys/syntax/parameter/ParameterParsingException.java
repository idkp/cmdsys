package com.github.idkp.cmdsys.syntax.parameter;

public final class ParameterParsingException extends Exception {
    public ParameterParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterParsingException(String message) {
        super(message);
    }
}
