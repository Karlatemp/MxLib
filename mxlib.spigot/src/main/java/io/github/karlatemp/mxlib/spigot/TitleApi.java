package io.github.karlatemp.mxlib.spigot;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface TitleApi {
    public void sendActionBar(
            @NotNull Player player,
            @NotNull BaseComponent... components
    );

    public void setTabTitle(
            @NotNull Player player,
            @NotNull BaseComponent header,
            @NotNull BaseComponent footer
    );
}
