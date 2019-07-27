package com.github.foskel.cmdsys;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

public final class Sender {
    private static final String WILDCARD_PERMISSION = "*";
    public static final Sender ROOT = new Sender("root", Collections.singleton(WILDCARD_PERMISSION));

    private final String id;
    private final Set<String> permissions;
    private final boolean hasAllPermissions;

    public Sender(String id, Set<String> permissions) {
        this.id = id;
        this.permissions = permissions;
        this.hasAllPermissions = permissions.contains(WILDCARD_PERMISSION);
    }

    public String getId() {
        return this.id;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(String permission) {
        if (this.hasAllPermissions || this.permissions.contains(permission) || permission.equals(WILDCARD_PERMISSION)) {
            return true;
        }

        String[] permissionParts = permission.split(Pattern.quote("."));

        for (String localPermission : this.permissions) {
            String[] localPermissionParts = localPermission.split(Pattern.quote("."));

            for (int i = 0; i < permissionParts.length && i < localPermissionParts.length; i++) {
                String permissionPart = permissionParts[i];
                String localPermissionPart = localPermissionParts[i];

                if (!permissionPart.equals(localPermissionPart) && !localPermissionPart.equals(WILDCARD_PERMISSION)) {
                    return false;
                }
            }
        }

        return true;
    }
}
