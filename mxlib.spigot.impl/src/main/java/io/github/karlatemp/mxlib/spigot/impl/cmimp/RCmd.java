package io.github.karlatemp.mxlib.spigot.impl.cmimp;

import io.github.karlatemp.mxlib.annotations.injector.AfterInject;
import io.github.karlatemp.mxlib.annotations.injector.Inject;
import io.github.karlatemp.mxlib.command.annoations.MCommand;
import io.github.karlatemp.mxlib.command.annoations.MCommandHandle;
import io.github.karlatemp.mxlib.command.annoations.MSender;
import io.github.karlatemp.mxlib.spigot.TitleApi;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

@MCommand
public class RCmd {
    @Inject
    private TitleApi titleApi;

    @MCommandHandle
    public void handle(@MSender Player sender) {
        sender.sendMessage("FAQ");
        titleApi.sendActionBar(sender, TextComponent.fromLegacyText("Test!! §cMore Test!!"));
        titleApi.setTabTitle(sender,
                new TextComponent("Header"),
                new TextComponent("Footer")
        );
    }

    @AfterInject
    private void post() {
        System.out.println("??");
        System.out.println(titleApi);
        System.out.println("??");
    }
}
