package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import io.github.karlatemp.mxlib.network.cs.serialize.PkgSerializable;
import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.unsafeaccessor.Unsafe;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"Convert2MethodRef", "unchecked"})
public class ReflectSerializer<T> implements PacketSerializer<T> {
    private static final Unsafe usf = Unsafe.getUnsafe();
    private final Class<T> type;
    private final List<FieldSerializer> fields;

    public ReflectSerializer(Class<T> type) {
        this.type = type;
        if (type.getDeclaredAnnotation(PkgSerializable.class) == null) {
            throw new IllegalArgumentException(type + " is not marked @PkgSerializable");
        }
        if (type.getSuperclass() != Object.class) {
            throw new IllegalArgumentException(type + " is not a raw model type");
        }
        if (Modifier.isAbstract(type.getModifiers())) {
            throw new IllegalArgumentException("Abstract class is not support: " + type);
        }
        this.fields = Stream.of(type.getDeclaredFields())
                .filter(Reflections.ModifierFilter.NON_STATIC)
                .peek(Reflections.openAccess())
                .map(field -> new FieldSerializer(field))
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull Class<T[]> arrayType() {
        return (Class<T[]>) Array.newInstance(type, 0).getClass();
    }

    @Override
    public void serialize(T value, SerializeContext context) {
        try {
            for (FieldSerializer serializer : fields) {
                serializer.serialize(
                        serializer.field,
                        value,
                        context
                );
            }
        } catch (IllegalAccessException exception) {
            throw new EncoderException(exception);
        }
    }

    @Override
    public @NotNull T deserialize(SerializeContext context) {
        try {
            T obj = (T) usf.allocateInstance(type);
            for (FieldSerializer serializer : fields) {
                serializer.deserialize(
                        serializer.field,
                        obj,
                        context
                );
            }
            return obj;
        } catch (Exception exception) {
            throw new DecoderException(exception);
        }
    }

    protected interface FieldSerializerI {
        void serialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException;

        void deserialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException;
    }

    protected static enum PrimitiveFieldSerializer implements FieldSerializerI {
        BOOLEAN {
            @Override
            public void serialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                context.getBuf().writeBoolean(field.getBoolean(self));
            }

            @Override
            public void deserialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                field.setBoolean(self, context.getBuf().readBoolean());
            }
        },
        BYTE {
            @Override
            public void serialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                context.getBuf().writeByte(field.getByte(self));
            }

            @Override
            public void deserialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                field.setByte(self, context.getBuf().readByte());
            }
        },
        CHAR {
            @Override
            public void serialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                context.getBuf().writeChar(field.getChar(self));
            }

            @Override
            public void deserialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                field.setChar(self, context.getBuf().readChar());
            }
        },
        DOUBLE {
            @Override
            public void serialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                context.getBuf().writeDouble(field.getDouble(self));
            }

            @Override
            public void deserialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                field.setDouble(self, context.getBuf().readDouble());
            }
        },
        INT {
            @Override
            public void serialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                context.getBuf().writeInt(field.getInt(self));
            }

            @Override
            public void deserialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                field.set(self, context.getBuf().readInt());
            }
        },
        LONG {
            @Override
            public void serialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                context.getBuf().writeLong(field.getLong(self));
            }

            @Override
            public void deserialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                field.setLong(self, context.getBuf().readLong());
            }
        },
        SHORT {
            @Override
            public void serialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                context.getBuf().writeShort(field.getShort(self));
            }

            @Override
            public void deserialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                field.setShort(self, context.getBuf().readShort());
            }
        },
        ;
    }

    protected static class FieldSerializer implements FieldSerializerI {
        private final Field field;
        protected FieldSerializerI delegate;

        private static FieldSerializerI findD(Class<?> type) {
            if (type == int.class) return PrimitiveFieldSerializer.INT;
            if (type == boolean.class) return PrimitiveFieldSerializer.BOOLEAN;
            if (type == byte.class) return PrimitiveFieldSerializer.BYTE;
            if (type == char.class) return PrimitiveFieldSerializer.CHAR;
            if (type == double.class) return PrimitiveFieldSerializer.DOUBLE;
            if (type == long.class) return PrimitiveFieldSerializer.LONG;
            if (type == short.class) return PrimitiveFieldSerializer.SHORT;
            return null;
        }

        public FieldSerializer(Field field) {
            this.field = field;
            delegate = findD(field.getType());
        }

        protected void patchS(PacketSerializer.SerializeContext context) {
            if (delegate == null) {
                PacketSerializer<Object> serializer = context.findSerializer(field.getGenericType());
                delegate = new FieldSerializerI() {
                    @Override
                    public void serialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                        serializer.serialize(field.get(self), context);
                    }

                    @Override
                    public void deserialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
                        field.set(self, serializer.deserialize(context));
                    }
                };
            }
        }

        @Override
        public void serialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
            patchS(context);
            delegate.serialize(field, self, context);
        }

        @Override
        public void deserialize(Field field, Object self, PacketSerializer.SerializeContext context) throws IllegalAccessException {
            patchS(context);
            delegate.deserialize(field, self, context);
        }

        protected static enum PS {}
    }
}
