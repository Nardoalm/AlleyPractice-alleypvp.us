package us.alleypvp.practice.feature.party.menu.event;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.item.ItemBuilder;
import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.feature.party.Party;
import us.alleypvp.practice.feature.party.menu.event.impl.PartyEventFFAMenu;
import us.alleypvp.practice.feature.party.menu.event.impl.PartyEventSplitMenu;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.library.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 18:29
 */
@AllArgsConstructor
public class PartyEventMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&b&lEscolha o tipo de evento da party";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new PartyEventButton(
                Material.DIAMOND_SWORD, 0,
                "&b&lDivisão de Times",
                Arrays.asList(
                        CC.MENU_BAR,
                        "&7Divide a party em",
                        "&72 times para lutar",
                        "&7um contra o outro.",
                        "",
                        "&aClique para selecionar um kit.",
                        CC.MENU_BAR
                )
        ));

        buttons.put(13, new PartyEventButton(
                Material.GOLD_AXE, 0,
                "&b&lFree for All",
                Arrays.asList(
                        CC.MENU_BAR,
                        "&7Todos os jogadores lutam",
                        "&7entre si.",
                        "",
                        "&aClique para selecionar um kit.",
                        CC.MENU_BAR
                )
        ));

        buttons.put(15, new PartyEventButton(
                Material.REDSTONE, 0,
                "&cMelhor de 3 Sumo",
                Arrays.asList(
                        CC.MENU_BAR,
                        "&7Este evento ainda",
                        "&7não foi implementado.",
                        "",
                        "&c&mClique para iniciar o evento.",
                        CC.MENU_BAR
                )
        ));

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @AllArgsConstructor
    private static class PartyEventButton extends Button {
        private Material material;
        private int durability;
        private String name;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(material)
                    .name(name)
                    .lore(lore)
                    .durability(durability)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            ProfileService profileService = AlleyPractice.getInstance().getService(ProfileService.class);
            if (profileService == null || profileService.getProfile(player.getUniqueId()) == null) {
                player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_IN_PARTY));
                return;
            }

            Party party = profileService.getProfile(player.getUniqueId()).getParty();
            if (party == null) {
                player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_IN_PARTY));
                return;
            }

            if (!party.isLeader(player)) {
                player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_PARTY_LEADER));
                return;
            }

            if (party.getMembers().size() < 2) {
                player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_PARTY_NEED_TWO_PLAYERS));
                return;
            }

            switch (material) {
                case DIAMOND_SWORD:
                    new PartyEventSplitMenu().openMenu(player);
                    break;
                case GOLD_AXE:
                    new PartyEventFFAMenu().openMenu(player);
                    break;
                case REDSTONE:
                    // Start best of 3 sumo event
                    break;
            }
        }
    }
}
