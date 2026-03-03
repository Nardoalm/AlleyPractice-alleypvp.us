package com.kaosmc.practice.feature.command.impl.main.impl;

import com.kaosmc.practice.adapter.core.Core;
import com.kaosmc.practice.adapter.core.CoreAdapter;
import com.kaosmc.practice.adapter.core.CoreType;
import com.kaosmc.practice.library.command.BaseCommand;
import com.kaosmc.practice.library.command.CommandArgs;
import com.kaosmc.practice.library.command.annotation.CommandData;
import com.kaosmc.practice.common.text.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Kaos
 * @since 26/04/2025
 */
public class KaosCoreCommand extends BaseCommand {
    @CommandData(
            name = "kaos.core",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kaos core",
            description = "Exibe informacoes sobre a integracao do core."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        Core core = this.plugin.getService(CoreAdapter.class).getCore();

        Arrays.asList(
                "",
                "&6&lInformacoes do Core Hook",
                " &6&l│ &rPlugin: &6" + core.getType().getPluginName(),
                //" &6&l│ &rVersion: &6" + core.getType().getPluginVersion(),
                " &6&l│ &rAutores: &6" + core.getType().getPluginAuthor(),
                ""
        ).forEach(line -> sender.sendMessage(CC.translate(line)));

        if (core.getType() == CoreType.DEFAULT) {
            sender.sendMessage(CC.translate("&7Aviso: esta e a implementacao padrao do servidor, pois nenhum core compativel foi encontrado."));
        }
    }
}
