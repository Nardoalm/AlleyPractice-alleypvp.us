package us.alleypvp.practice.feature.kit.command.helper.impl;

import us.alleypvp.practice.common.reflect.utility.ReflectionUtility;
import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 14/07/2025
 */
public class UnbreakableCommand extends BaseCommand {
    @CommandData(
            name = "unbreakable",
            description = "Define o estado inquebrável do item na sua mão",
            usage = "unbreakable <true|false>",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_HOLD_ITEM));
            return;
        }

        boolean unbreakable = Boolean.parseBoolean(args[0]);

        ItemStack unbreakAbleItem = ReflectionUtility.setUnbreakable(item, unbreakable);
        player.setItemInHand(unbreakAbleItem);

        player.sendMessage(CC.translate("&aDefiniu o estado inquebrável do item para &b" + unbreakable + "&a."));
    }
}
