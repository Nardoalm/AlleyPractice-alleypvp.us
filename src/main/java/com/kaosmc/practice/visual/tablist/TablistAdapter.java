package com.kaosmc.practice.visual.tablist;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Kaos
 * @date 07/09/2024 - 15:17
 */
public interface TablistAdapter {

    List<String> getHeader(Player player);

    List<String> getFooter(Player player);

    void update(Player player);
}