package com.github.foskel.cmdsys.syntax;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class ArgumentMapper {
    private final Map<TypeKeyData, Supplier<?>> typeToInstances = new HashMap<>();

    public <T> void registerMapping(Class<? extends T> type, Supplier<T> instanceSupplier) {
        registerMapping(type, null, instanceSupplier);
    }

    public <T> void registerMapping(Class<? extends T> type, String id, Supplier<T> instanceSupplier) {
        this.typeToInstances.put(new TypeKeyData(type, id), instanceSupplier);
    }

    public Object getMapping(Class<?> type) {
        return getMapping(type, null);
    }

    public Object getMapping(Class<?> type, String id) {
        Supplier<?> instanceSupplier = this.typeToInstances.get(new TypeKeyData(type, id));

        if (instanceSupplier == null) {
            return null;
        }

        return instanceSupplier.get();
    }

    private static class TypeKeyData {
        private final Class<?> type;
        private final String id;

        private TypeKeyData(Class<?> type, String id) {
            this.type = type;
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TypeKeyData)) {
                return false;
            }

            if (obj == this) {
                return true;
            }

            TypeKeyData other = (TypeKeyData) obj;

            return other.type == type && Objects.equals(other.id, id);
        }
    }
}
