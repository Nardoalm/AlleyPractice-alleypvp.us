package com.kaosmc.practice.feature.abilities;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.time.TimeUtil;
import com.kaosmc.practice.common.text.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public abstract class Ability implements Listener {
    @Getter
    private static final List<Ability> abilities = Lists.newArrayList();

    private final String ability;
    private Table<String, UUID, Long> cooldown = HashBasedTable.create();

    public Ability(String ability) {
        this.ability = ability;
        abilities.add(this);
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, KaosPractice.getInstance());
    }

    public boolean isAbility(ItemStack itemStack) {
        return (itemStack != null)
                && (itemStack.getType() != Material.AIR)
                && (itemStack.hasItemMeta())
                && (itemStack.getItemMeta().getDisplayName() != null)
                && (itemStack.getItemMeta().getLore() != null)
                && itemStack.getItemMeta().getDisplayName().equals(CC.translate(KaosPractice.getInstance().getService(AbilityService.class).getDisplayName(ability)));
    }

    public String getName() {
        return KaosPractice.getInstance().getService(AbilityService.class).getDisplayName(this.getAbility());
    }

    public boolean hasCooldown(Player player) {
        return this.cooldown.contains(KaosPractice.getInstance().getService(AbilityService.class).getDisplayName(this.getAbility()), player.getUniqueId())
                && this.cooldown.get(KaosPractice.getInstance().getService(AbilityService.class).getDisplayName(this.getAbility()), player.getUniqueId()) > System.currentTimeMillis();
    }

    public void setCooldown(Player player, long time) {
        if (time < 1L) {
            this.cooldown.remove(KaosPractice.getInstance().getService(AbilityService.class).getDisplayName(this.getAbility()), player.getUniqueId());
        }
        else {
            this.cooldown.put(KaosPractice.getInstance().getService(AbilityService.class).getDisplayName(this.getAbility()), player.getUniqueId(), System.currentTimeMillis() + time);
        }
    }
    public String getCooldown(Player player) {
        long cooldownLeft = this.cooldown.get(KaosPractice.getInstance().getService(AbilityService.class).getDisplayName(this.getAbility()), player.getUniqueId()) - System.currentTimeMillis();
        return TimeUtil.formatLongMin(cooldownLeft);
    }
}
