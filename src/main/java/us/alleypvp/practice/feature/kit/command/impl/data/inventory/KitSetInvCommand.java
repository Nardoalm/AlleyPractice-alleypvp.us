package us.alleypvp.practice.feature.kit.command.impl.data.inventory;

import us.alleypvp.practice.common.InventoryUtil;
import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.feature.kit.KitService;
import us.alleypvp.practice.library.command.BaseCommand;
import us.alleypvp.practice.library.command.CommandArgs;
import us.alleypvp.practice.library.command.annotation.CommandData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 22:23
 */
public class KitSetInvCommand extends BaseCommand {
    @CommandData(
            name = "kit.setinventory",
            aliases = "kit.setinv",
            isAdminOnly = true,
            usage = "kit setinventory <kitName>",
            description = "Define o inventário e a armadura de um kit para seu inventário e armadura atuais."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        if (kitService == null) {
            player.sendMessage(CC.translate("&cServico de kit indisponivel no momento."));
            return;
        }
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        ItemStack[] inventory = InventoryUtil.cloneItemStackArray(player.getInventory().getContents());
        ItemStack[] armor = InventoryUtil.cloneItemStackArray(player.getInventory().getArmorContents());
        if (inventory == null) {
            inventory = new ItemStack[player.getInventory().getSize()];
        }
        if (armor == null) {
            armor = new ItemStack[4];
        }

        kit.setItems(inventory);
        kit.setArmor(armor);
        kitService.saveKit(kit);

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        if (profileService != null) {
            profileService.resetLayoutForKit(kit);
        }

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_INVENTORY_SET).replace("{kit-name}", kit.getName())));
    }
}
