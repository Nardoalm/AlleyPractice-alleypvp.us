package com.kaosmc.practice.feature.command.impl.main;

import com.kaosmc.practice.common.text.CC;
import com.kaosmc.practice.common.text.ClickableUtil;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.library.command.annotation.CompleterData;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @date 19/04/2024 - 17:39
 */
public class KaosCommand extends BaseCommand {
    @CompleterData(name = "kaos")
    public List<String> kaosCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();
        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            completion.addAll(Arrays.asList(
                    "reload", "debug", "core"
            ));
        }

        return completion;
    }

    @CommandData(
            name = "kaos",
            aliases = {"kaospractice", "practice", "prac"},
            inGameOnly = false,
            usage = "kaos",
            description = "Exibe informações sobre o plugin."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        Arrays.asList(
                "",
                "     &6&lKaosPractice",
                "      &6&l│ &fAutor: &6ysubz",
                "      &6&l│ &fWebsite: &6kaosmc.com",
                "      &6&l│ &fEquipe: &6" + this.plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", "").replace(",", "&7,&6"),
                "",
                "      &6&l│ &fLicense: &6CC BY-NC-SA 4.0",
                "      &6&l│ &fVersion: &6" + this.plugin.getDescription().getVersion(),
                ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));

        if (sender instanceof Player) {
            TextComponent clickable = this.createLinkComponent();
            command.getPlayer().spigot().sendMessage(clickable);
            sender.sendMessage("");
        }
    }

    private @NotNull TextComponent createLinkComponent() {
        TextComponent websiteComponent = ClickableUtil.createLinkComponent("&b&l[WEBSITE]", "https://kaosmc.com", "&aClique para abrir o website.");
        String spacing = "  ";

        TextComponent clickable = new TextComponent("     ");
        clickable.addExtra(websiteComponent);
        clickable.addExtra(spacing);
        return clickable;
    }
}
