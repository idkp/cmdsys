package com.github.idkp.cmdsys.io;

public interface CommandLogger {
    void info(String msg, Object... args);

    void error(String msg, Object... args);

    void raw(String msg, Object... args);
}