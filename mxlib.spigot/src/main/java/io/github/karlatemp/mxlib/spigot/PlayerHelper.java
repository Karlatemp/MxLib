package io.github.karlatemp.mxlib.spigot;

import io.github.karlatemp.unsafeaccessor.Root;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import static io.github.karlatemp.mxlib.spigot.NmsHelper.getNmsClass;
import static io.github.karlatemp.mxlib.spigot.NmsHelper.throwx;

public class PlayerHelper {

    private static final Class<?> EntityPlayer = getNmsClass("EntityPlayer");
    private static final Class<?> PlayerConnection = getNmsClass("PlayerConnection");
    private static final Class<?> Packet = getNmsClass("Packet");
    private static final MethodHandle EntityPlayer$playerConnection;
    private static final MethodHandle PlayerConnection$sendPacket;

    static {
        try {
            MethodHandles.Lookup trusted = Root.getTrusted();
            EntityPlayer$playerConnection = trusted.unreflectGetter(
                    EntityPlayer.getDeclaredField("playerConnection")
            );
            PlayerConnection$sendPacket = trusted.unreflect(
                    PlayerConnection.getDeclaredMethod("sendPacket", Packet)
            );
        } catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }
    }

    public static Object getPlayerConnection(Object entityPlayer) {
        try {
            return EntityPlayer$playerConnection.invoke(entityPlayer);
        } catch (Throwable throwable) {
            return throwx(throwable);
        }
    }

    public static void sendPacket(Object connection, Object packet) {
        try {
            PlayerConnection$sendPacket.invoke(connection, packet);
        } catch (Throwable throwable) {
            throwx(throwable);
        }
    }

}
