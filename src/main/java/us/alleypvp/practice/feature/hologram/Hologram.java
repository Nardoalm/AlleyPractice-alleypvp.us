package us.alleypvp.practice.feature.hologram;

import us.alleypvp.practice.common.text.CC;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private final Location location;
    private final List<ArmorStand> lines;
    private static final double LINE_SPACING = 0.28;

    public Hologram(Location location) {
        this.location = location;
        this.lines = new ArrayList<>();
    }

    public void update(List<String> textLines) {
        while (lines.size() > textLines.size()) {
            ArmorStand removed = lines.remove(lines.size() - 1);
            removed.remove();
        }

        Location currentLoc = location.clone();
        for (int i = 0; i < textLines.size(); i++) {
            String rawText = textLines.get(i);
            String text = CC.translate(rawText);
            boolean isBlank = rawText == null || rawText.trim().isEmpty();

            if (i < lines.size()) {
                ArmorStand as = lines.get(i);
                as.setCustomName(text);
                as.setCustomNameVisible(!isBlank);
                as.teleport(currentLoc);
            } else {
                ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(currentLoc, EntityType.ARMOR_STAND);
                as.setVisible(false);
                as.setGravity(false);
                as.setCustomName(text);
                as.setCustomNameVisible(!isBlank);
                as.setBasePlate(false);
                as.setSmall(true);
                as.setArms(false);
                lines.add(as);
            }
            currentLoc.subtract(0, LINE_SPACING, 0);
        }
    }

    public void destroy() {
        lines.forEach(ArmorStand::remove);
        lines.clear();
    }

    public Location getLocation() {
        return location;
    }
}