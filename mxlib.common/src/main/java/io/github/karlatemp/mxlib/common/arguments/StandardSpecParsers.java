/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/StandardSpecParsers.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.common.arguments;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class StandardSpecParsers<V> implements
        BiFunction<Iterator<String>, SParser.ParseResult, V>,
        BiConsumer<Iterator<String>, Consumer<String>>,
        Supplier<V> {
    public static final StandardSpecParsers<String> STRING = new StandardSpecParsers<String>() {
        @Override
        public void accept(Iterator<String> iterator, Consumer<String> stringConsumer) {
            if (iterator.hasNext()) iterator.next();
        }

        @Override
        public String apply(Iterator<String> iterator, SParser.ParseResult parseResult) {
            if (iterator.hasNext()) return iterator.next();
            parseResult.success = false;
            parseResult.trans = SParser.ParseResult.NO_VALUE_FOUND;
            return null;
        }
    };
    public static final StandardSpecParsers<Integer> INTEGER = new StandardSpecParsers<Integer>() {
        @Override
        public Integer apply(Iterator<String> iterator, SParser.ParseResult parseResult) {
            if (iterator.hasNext()) {
                String val = iterator.next();
                try {
                    return Integer.parseInt(val);
                } catch (NumberFormatException nfe) {
                    parseResult.success = false;
                    parseResult.trans = SParser.ParseResult.FAILED_TO_FORMAT_NUMBER;
                    parseResult.param = new Object[]{val};
                    return null;
                }
            }
            parseResult.success = false;
            parseResult.trans = SParser.ParseResult.NO_VALUE_FOUND;
            return null;
        }

        @Override
        public Integer get() {
            return 0;
        }

        @Override
        public void accept(Iterator<String> iterator, Consumer<String> stringConsumer) {
            if (iterator.hasNext()) iterator.next();
        }
    };
    public static final StandardSpecParsers<Double> DOUBLE = new StandardSpecParsers<Double>() {
        @Override
        public Double apply(Iterator<String> iterator, SParser.ParseResult parseResult) {
            if (iterator.hasNext()) {
                String val = iterator.next();
                try {
                    return Double.parseDouble(val);
                } catch (NumberFormatException nfe) {
                    parseResult.success = false;
                    parseResult.trans = SParser.ParseResult.FAILED_TO_FORMAT_NUMBER;
                    parseResult.param = new Object[]{val};
                    return null;
                }
            }
            parseResult.success = false;
            parseResult.trans = SParser.ParseResult.NO_VALUE_FOUND;
            return null;
        }

        @Override
        public Double get() {
            return 0d;
        }

        @Override
        public void accept(Iterator<String> iterator, Consumer<String> stringConsumer) {
            if (iterator.hasNext()) iterator.next();
        }
    };
    public static final StandardSpecParsers<Float> FLOAT = new StandardSpecParsers<Float>() {
        @Override
        public Float apply(Iterator<String> iterator, SParser.ParseResult parseResult) {
            if (iterator.hasNext()) {
                String val = iterator.next();
                try {
                    return Float.parseFloat(val);
                } catch (NumberFormatException nfe) {
                    parseResult.success = false;
                    parseResult.trans = SParser.ParseResult.FAILED_TO_FORMAT_NUMBER;
                    parseResult.param = new Object[]{val};
                    return null;
                }
            }
            parseResult.success = false;
            parseResult.trans = SParser.ParseResult.NO_VALUE_FOUND;
            return null;
        }

        @Override
        public Float get() {
            return 0f;
        }

        @Override
        public void accept(Iterator<String> iterator, Consumer<String> stringConsumer) {
            if (iterator.hasNext()) iterator.next();
        }
    };
    public static final StandardSpecParsers<Short> SHORT = new StandardSpecParsers<Short>() {
        @Override
        public Short apply(Iterator<String> iterator, SParser.ParseResult parseResult) {
            if (iterator.hasNext()) {
                String val = iterator.next();
                try {
                    return Short.parseShort(val);
                } catch (NumberFormatException nfe) {
                    parseResult.success = false;
                    parseResult.trans = SParser.ParseResult.FAILED_TO_FORMAT_NUMBER;
                    parseResult.param = new Object[]{val};
                    return null;
                }
            }
            parseResult.success = false;
            parseResult.trans = SParser.ParseResult.NO_VALUE_FOUND;
            return null;
        }

        @Override
        public Short get() {
            return 0;
        }

        @Override
        public void accept(Iterator<String> iterator, Consumer<String> stringConsumer) {
            if (iterator.hasNext()) iterator.next();
        }
    };
    public static final StandardSpecParsers<Byte> BYTE = new StandardSpecParsers<Byte>() {
        @Override
        public Byte apply(Iterator<String> iterator, SParser.ParseResult parseResult) {
            if (iterator.hasNext()) {
                String val = iterator.next();
                try {
                    return Byte.parseByte(val);
                } catch (NumberFormatException nfe) {
                    parseResult.success = false;
                    parseResult.trans = SParser.ParseResult.FAILED_TO_FORMAT_NUMBER;
                    parseResult.param = new Object[]{val};
                    return null;
                }
            }
            parseResult.success = false;
            parseResult.trans = SParser.ParseResult.NO_VALUE_FOUND;
            return null;
        }

        @Override
        public Byte get() {
            return (byte) 0;
        }

        @Override
        public void accept(Iterator<String> iterator, Consumer<String> stringConsumer) {
            if (iterator.hasNext()) iterator.next();
        }
    };
    public static final StandardSpecParsers<Long> LONG = new StandardSpecParsers<Long>() {
        @Override
        public Long apply(Iterator<String> iterator, SParser.ParseResult parseResult) {
            if (iterator.hasNext()) {
                String val = iterator.next();
                try {
                    return Long.parseLong(val);
                } catch (NumberFormatException nfe) {
                    parseResult.success = false;
                    parseResult.trans = SParser.ParseResult.FAILED_TO_FORMAT_NUMBER;
                    parseResult.param = new Object[]{val};
                    return null;
                }
            }
            parseResult.success = false;
            parseResult.trans = SParser.ParseResult.NO_VALUE_FOUND;
            return null;
        }

        @Override
        public Long get() {
            return 0L;
        }

        @Override
        public void accept(Iterator<String> iterator, Consumer<String> stringConsumer) {
            if (iterator.hasNext()) iterator.next();
        }
    };
    public static final StandardSpecParsers<Boolean> BOOLEAN = new StandardSpecParsers<Boolean>() {
        @Override
        public Boolean apply(Iterator<String> iterator, SParser.ParseResult parseResult) {
            if (iterator.hasNext()) {
                String val = iterator.next();
                return Boolean.parseBoolean(val);
            }
            parseResult.success = false;
            parseResult.trans = SParser.ParseResult.NO_VALUE_FOUND;
            return null;
        }

        @Override
        public void accept(Iterator<String> iterator, Consumer<String> stringConsumer) {
            if (iterator.hasNext()) iterator.next();
        }
    };
    public static final StandardSpecParsers<Boolean> BOOLEAN_KEY_EXIST = new StandardSpecParsers<Boolean>() {
        @Override
        public void accept(Iterator<String> iterator, Consumer<String> stringConsumer) {
        }

        @Override
        public Boolean apply(Iterator<String> iterator, SParser.ParseResult parseResult) {
            return true;
        }

        @Override
        public Boolean get() {
            return false;
        }
    };
    public static final StandardSpecParsers<Object> BAKED = new StandardSpecParsers<Object>() {
        @Override
        public void accept(Iterator<String> iterator, Consumer<String> stringConsumer) {
        }

        @Override
        public Object apply(Iterator<String> iterator, SParser.ParseResult parseResult) {
            return null;
        }
    };

    public static StandardSpecParsers<?> from(Class<?> type) {
        if (type == int.class || type == Integer.class) return INTEGER;
        if (type == double.class || type == Double.class) return DOUBLE;
        if (type == float.class || type == Float.class) return FLOAT;
        if (type == short.class || type == Short.class) return SHORT;
        if (type == byte.class || type == Byte.class) return BYTE;
        if (type == long.class || type == Long.class) return LONG;
        if (type == boolean.class || type == Boolean.class) return BOOLEAN;
        if (type == String.class) return STRING;
        return BAKED;
    }

    @Override
    public V get() {
        return null;
    }
}
