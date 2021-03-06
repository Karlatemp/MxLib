/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/SOptions.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.common.arguments;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SOptions {
    final Collection<SSpec<?>> specs = new ConcurrentLinkedQueue<>();
    final Collection<String> values = new HashSet<>();
    static final String[] NO_SPEC = new String[]{"<no-spec>"};
    final SSpec<?> noSpec = new SSpec<>(NO_SPEC);
    boolean strictly;

    public SSpec<?> getNoSpec() {
        return noSpec;
    }

    public SOptions() {
    }

    public Collection<SSpec<?>> getSpecs() {
        return specs;
    }

    public Collection<String> getValues() {
        return values;
    }

    public SOptions strictly() {
        strictly = true;
        return this;
    }

    public SSpec<?> register(String... keys) {
        SSpec<?> spec = new SSpec<>(keys);
        for (String k : keys) {
            if (values.contains(k.toLowerCase())) {
                throw new IllegalArgumentException("Key " + k + " was registered.");
            }
        }
        specs.add(spec);
        for (String k : keys) values.add(k.toLowerCase());
        return spec;
    }

    public SParser newParser() {
        return new SParser(this);
    }
}
