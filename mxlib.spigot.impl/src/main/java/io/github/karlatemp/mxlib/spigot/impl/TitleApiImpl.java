package io.github.karlatemp.mxlib.spigot.impl;

import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.mxlib.spigot.PlayerHelper;
import io.github.karlatemp.mxlib.spigot.TitleApi;
import io.github.karlatemp.unsafeaccessor.Root;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.UUID;
import java.util.stream.Stream;

import static io.github.karlatemp.mxlib.spigot.NmsHelper.getHandle;
import static io.github.karlatemp.mxlib.spigot.NmsHelper.getNmsClass;
import static io.github.karlatemp.mxlib.spigot.PlayerHelper.getPlayerConnection;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TitleApiImpl implements TitleApi {
    private static final Class<?> ChatSerializer = getNmsClass("IChatBaseComponent$ChatSerializer", "ChatSerializer");
    private static final Class<?> IChatBaseComponent = getNmsClass("IChatBaseComponent");
    private static final Class<?> PacketPlayOutChat = getNmsClass("PacketPlayOutChat");
    private static final Class<?> PacketPlayOutPlayerListHeaderFooter = getNmsClass("PacketPlayOutPlayerListHeaderFooter");
    private static final boolean PacketPlayOutPlayerListHeaderFooter$hf_array;
    private static final MethodHandle
            PacketPlayOutChat$components$setter,
            PacketPlayOutChat$constructor,
            PacketPlayOutPlayerListHeaderFooter$constructor,
            PacketPlayOutPlayerListHeaderFooter$header$setter,
            PacketPlayOutPlayerListHeaderFooter$footer$setter,
            ChatSerializer$parse;

    // PacketPlayOutPlayerListHeaderFooter
    static {
        Field f = null;
        for (Field field : PacketPlayOutChat.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            Class<?> type = field.getType();
            if (type.isArray()) {
                if (type.getComponentType() == BaseComponent.class) {
                    f = field;
                    break;
                }
            }
        }
        if (f == null) {
            throw new AssertionError("TitleAPI Not supported on Bukkit Platform. Please use spigot platform (or platform extends spigot)");
        }
        MethodHandles.Lookup trusted = Root.getTrusted();
        try {
            PacketPlayOutChat$components$setter = trusted.unreflectSetter(f);
            PacketPlayOutPlayerListHeaderFooter$constructor = trusted.findConstructor(PacketPlayOutPlayerListHeaderFooter, MethodType.methodType(void.class));
            {
                Field header, footer;
                try {
                    header = PacketPlayOutPlayerListHeaderFooter.getDeclaredField("header");
                    footer = PacketPlayOutPlayerListHeaderFooter.getDeclaredField("footer");
                } catch (Throwable ignored) {
                    header = PacketPlayOutPlayerListHeaderFooter.getDeclaredField("a");
                    footer = PacketPlayOutPlayerListHeaderFooter.getDeclaredField("b");
                }

                PacketPlayOutPlayerListHeaderFooter$header$setter = trusted.unreflectSetter(header);
                PacketPlayOutPlayerListHeaderFooter$footer$setter = trusted.unreflectSetter(footer);

                // Paper Platform (1.12.2)
                PacketPlayOutPlayerListHeaderFooter$hf_array = header.getType() != IChatBaseComponent;
            }
            // ChatMessageType.GAME_INFO
            MethodHandle PPOC$cons;
            Constructor<?> constructor = Stream.of(PacketPlayOutChat.getDeclaredConstructors())
                    .filter(it -> it.getParameterCount() != 0)
                    .findFirst().orElseThrow(() -> new NoSuchMethodException(PacketPlayOutChat.getName() + ".<init>(...)"));
            MethodHandle base = trusted.unreflectConstructor(constructor);

            try {
                Object gmInf;
                Class<? extends Enum> ChatMessageType = getNmsClass("ChatMessageType").asSubclass((Class) Enum.class);
                try {
                    gmInf = Enum.valueOf(ChatMessageType, "GAME_INFO");
                } catch (Throwable ignore) {
                    gmInf = Reflections.findMethod(ChatMessageType, null, true, ChatMessageType, byte.class)
                            .orElseThrow(() -> new NoSuchMethodException("static " + PacketPlayOutChat.getName() + ".*(byte): " + PacketPlayOutChat.getName()))
                            .invoke(null, (byte) 2);
                }
                MethodType type = base.type();
                if (type.parameterCount() == 3) {
                    PPOC$cons = MethodHandles.insertArguments(base, 0, null, gmInf, new UUID(0, 0));
                } else {
                    PPOC$cons = MethodHandles.insertArguments(base, 0, null, gmInf);
                }
            } catch (Throwable ignore) {
                PPOC$cons = MethodHandles.insertArguments(base, 0, null, (byte) 2);
            }
            PacketPlayOutChat$constructor = PPOC$cons;
            ChatSerializer$parse = trusted.unreflect(Reflections.findMethod(
                    ChatSerializer, null, true, IChatBaseComponent, String.class
            ).orElseThrow(() -> new NoSuchMethodException("static " + ChatSerializer.getName() + ".*(java.lang.String): " + IChatBaseComponent.getName())));
        } catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }
    }

    private static void sendPacket(Player player, Object packet) {
        PlayerHelper.sendPacket(getPlayerConnection(getHandle(player)), packet);
    }

    @Override
    public void sendActionBar(@NotNull Player player, @NotNull BaseComponent... components) {
        try {
            Object packet = PacketPlayOutChat$constructor.invoke();
            PacketPlayOutChat$components$setter.invoke(packet, (Object) components);
            sendPacket(player, packet);
        } catch (Throwable throwable) {
            Root.getUnsafe().throwException(throwable);
        }
    }

    @Override
    public void setTabTitle(@NotNull Player player, @NotNull BaseComponent header, @NotNull BaseComponent footer) {
        try {
            Object packet = PacketPlayOutPlayerListHeaderFooter$constructor.invoke();
            PacketPlayOutPlayerListHeaderFooter$header$setter.invoke(
                    packet,
                    PacketPlayOutPlayerListHeaderFooter$hf_array
                            ? new BaseComponent[]{header}
                            : ChatSerializer$parse.invoke(ComponentSerializer.toString(header))
            );
            PacketPlayOutPlayerListHeaderFooter$footer$setter.invoke(
                    packet,
                    PacketPlayOutPlayerListHeaderFooter$hf_array
                            ? new BaseComponent[]{footer}
                            : ChatSerializer$parse.invoke(ComponentSerializer.toString(footer))
            );
            sendPacket(player, packet);
        } catch (Throwable throwable) {
            Root.getUnsafe().throwException(throwable);
        }
    }
}
