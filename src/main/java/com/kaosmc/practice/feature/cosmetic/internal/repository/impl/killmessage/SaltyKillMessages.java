package com.kaosmc.practice.feature.cosmetic.internal.repository.impl.killmessage;

import com.kaosmc.practice.feature.cosmetic.model.CosmeticType;
import com.kaosmc.practice.feature.cosmetic.annotation.CosmeticData;
import org.bukkit.Material;

/**
 * @author Remi
 * @project kaos-practice
 * @date 27/06/2025
 */
@CosmeticData(
        type = CosmeticType.KILL_MESSAGE,
        name = "Salty Messages",
        description = "A pack of messages to taunt your opponents.",
        icon = Material.BOOK_AND_QUILL,
        slot = 11,
        price = 750
)
public class SaltyKillMessages extends KillMessagePack {
    @Override
    protected String getResourceFileName() {
        return "salty_messages.yml";
    }
}