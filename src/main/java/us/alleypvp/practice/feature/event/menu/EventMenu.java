package us.alleypvp.practice.feature.event.menu;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.common.item.ItemBuilder;
import us.alleypvp.practice.common.text.CC;
import us.alleypvp.practice.feature.event.EventDefinition;
import us.alleypvp.practice.feature.event.EventPhase;
import us.alleypvp.practice.feature.event.EventService;
import us.alleypvp.practice.feature.event.model.ActiveEvent;
import us.alleypvp.practice.library.menu.Button;
import us.alleypvp.practice.library.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EventMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&b&lEventos";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new LinkedHashMap<>();
        EventService eventService = AlleyPractice.getInstance().getService(EventService.class);

        int slot = 11;
        for (EventDefinition definition : eventService.getEventDefinitions()) {
            if (!definition.isEnabled()) {
                continue;
            }

            buttons.put(slot, new EventButton(definition));
            slot = this.skipIfSlotCrossingBorder(slot + 1);
        }

        if (buttons.isEmpty()) {
            buttons.put(13, Button.placeholder(Material.BARRIER, (byte) 0, CC.translate("&cNenhum evento disponível")));
        }

        this.addGlass(buttons, 15);
        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    private static final class EventButton extends Button {
        private final EventDefinition definition;

        private EventButton(EventDefinition definition) {
            this.definition = definition;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            EventService eventService = AlleyPractice.getInstance().getService(EventService.class);
            ActiveEvent activeEvent = eventService.getActiveEvent();

            List<String> lore = new ArrayList<>(this.definition.getDescription());
            lore.add("");

            if (activeEvent == null) {
                if (player.hasPermission("kaos.command.donator.host")) {
                    lore.add("&aClique para hospedar este evento.");
                } else {
                    lore.add("&7Nenhum evento deste tipo está ativo.");
                    lore.add("&cApenas jogadores com permissão podem hospedar.");
                }
            } else if (activeEvent.getDefinition().getKey().equalsIgnoreCase(this.definition.getKey())) {
                lore.add("&fStatus: " + (activeEvent.getPhase() == EventPhase.RUNNING ? "&cEm andamento" : "&aAguardando"));
                lore.add("&fJogadores: &b" + activeEvent.getParticipants().size() + "/" + activeEvent.getDefinition().getMaximumPlayers());

                if (activeEvent.isParticipant(player.getUniqueId())) {
                    lore.add("&cClique para sair do evento.");
                } else if (activeEvent.getPhase() == EventPhase.RUNNING) {
                    lore.add("&cEsse evento já começou.");
                } else {
                    lore.add("&aClique para entrar no evento.");
                }
            } else {
                lore.add("&cJá existe outro evento ativo: " + activeEvent.getDefinition().getDisplayName());
            }

            return new ItemBuilder(this.definition.getMaterial())
                    .name(this.definition.getDisplayName())
                    .durability(this.definition.getDurability())
                    .lore(lore)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) {
                return;
            }

            EventService eventService = AlleyPractice.getInstance().getService(EventService.class);
            ActiveEvent activeEvent = eventService.getActiveEvent();

            if (activeEvent == null) {
                if (!player.hasPermission("kaos.command.donator.host")) {
                    player.sendMessage(CC.translate("&cNão há nenhum evento ativo no momento."));
                    this.playFail(player);
                    return;
                }

                if (eventService.hostEvent(player, this.definition.getKey())) {
                    this.playSuccess(player);
                } else {
                    this.playFail(player);
                }
                return;
            }

            if (!activeEvent.getDefinition().getKey().equalsIgnoreCase(this.definition.getKey())) {
                player.sendMessage(CC.translate("&cJá existe outro evento ativo no momento."));
                this.playFail(player);
                return;
            }

            if (activeEvent.isParticipant(player.getUniqueId())) {
                if (eventService.leaveActiveEvent(player, false)) {
                    this.playSuccess(player);
                } else {
                    this.playFail(player);
                }
                return;
            }

            if (eventService.joinActiveEvent(player)) {
                this.playSuccess(player);
            } else {
                this.playFail(player);
            }
        }
    }
}
