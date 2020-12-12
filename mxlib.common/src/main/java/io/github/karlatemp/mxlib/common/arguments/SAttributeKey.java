/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 17:58:49
 *
 * MXLib/mxlib.arguments/SAttributeKey.java
 */

package io.github.karlatemp.mxlib.common.arguments;

import java.util.Objects;
import java.util.UUID;

public class SAttributeKey<T> {
    private final UUID namespace;
    private final String id;

    public SAttributeKey(UUID namespace, String id) {
        this.namespace = namespace;
        this.id = id;
    }

    public UUID getNamespace() {
        return namespace;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SAttributeKey<?> that = (SAttributeKey<?>) o;

        if (!Objects.equals(namespace, that.namespace)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = namespace != null ? namespace.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
