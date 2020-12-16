package io.github.karlatemp.mxlib.spigot.impl.cmimp;

import io.github.karlatemp.mxlib.command.annoations.MCommand;
import io.github.karlatemp.mxlib.command.annoations.MCommandHandle;
import io.github.karlatemp.mxlib.command.annoations.MSender;
import io.github.karlatemp.unsafeaccessor.Root;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@MCommand(name = "kt")
public class KTest {
    private Method handle;

    public KTest() {
        try {
            handle = Class.forName("io.github.karlatemp.mxlib.kt.test.TestMain")
                    .getMethod("handle", Player.class);
            Root.openAccess(handle);
        } catch (Throwable ignore) {
        }
    }

    @MCommandHandle
    public void handle(@MSender Player player) throws InvocationTargetException, IllegalAccessException {
        if (handle != null) {
            handle.invoke(null, player);
        }
    }
}
