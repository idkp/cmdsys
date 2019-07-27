package com.github.foskel.cmdsys.syntax;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class ArgumentMapper {
    private final Map<Class<?>, Supplier<?>> typeToInstances = new HashMap<>();

    public <T> void registerMapping(Class<? extends T> type, Supplier<T> instanceSupplier) {
        this.typeToInstances.put(type, instanceSupplier);
    }

    public Object getMapping(Class<?> type) {
        Supplier<?> instanceSupplier = this.typeToInstances.get(type);

        if (instanceSupplier == null) {
            return null;
        }

        return instanceSupplier.get();
    }
}
