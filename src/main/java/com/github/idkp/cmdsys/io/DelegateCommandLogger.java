package com.github.idkp.cmdsys.io;

import java.util.function.BiConsumer;

public final class DelegateCommandLogger implements CommandLogger {
    private final BiConsumer<String, Object[]> backingInfoLog;
    private final BiConsumer<String, Object[]> backingErrorLog;
    private final BiConsumer<String, Object[]> backingRawLog;

    public DelegateCommandLogger(BiConsumer<String, Object[]> backingInfoLog, BiConsumer<String, Object[]> backingErrorLog, BiConsumer<String, Object[]> backingRawLog) {
        this.backingInfoLog = backingInfoLog;
        this.backingErrorLog = backingErrorLog;
        this.backingRawLog = backingRawLog;
    }

    @Override
    public void info(String msg, Object... args) {
        backingInfoLog.accept(msg, args);
    }

    @Override
    public void error(String msg, Object... args) {
        backingErrorLog.accept(msg, args);
    }

    @Override
    public void raw(String msg, Object... args) {
        backingRawLog.accept(msg, args);
    }
}
