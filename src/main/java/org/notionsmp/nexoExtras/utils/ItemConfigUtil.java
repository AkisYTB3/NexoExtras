package org.notionsmp.nexoExtras.utils;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.notionsmp.nexoExtras.NexoExtras;
import org.notionsmp.nexoExtras.classes.Mechanics;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ItemConfigUtil {

    private static final Set<File> itemFiles = new HashSet<>();

    public static Set<File> getItemFiles() {
        itemFiles.clear();
        itemFiles.addAll(NexoItems.itemMap().keySet());
        return itemFiles;
    }

    public static void loadMechanics() {
        NexoExtras.getInstance().getMechanics().clear();

        for (File itemFile : getItemFiles()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(itemFile);
            config.getKeys(false).forEach(itemId -> {

                ConfigurationSection itemSection = config.getConfigurationSection(itemId);
                if (itemSection == null || !itemSection.contains("Mechanics")) {
                    return;
                }

                Mechanics mechanic = NexoExtras.getInstance().getMechanics()
                        .computeIfAbsent(itemId, Mechanics::new);

                loadPearlThrow(itemSection, mechanic);
            });
        }
    }

    private static void loadPearlThrow(ConfigurationSection section, Mechanics mechanic) {
        if (section.contains("Mechanics.pearl_throw")) {
            boolean enabled = section.getBoolean("Mechanics.pearl_throw.enabled", false);
            int cooldown = section.getInt("Mechanics.pearl_throw.cooldown", 0);
            boolean consumeItem = section.getBoolean("Mechanics.pearl_throw.consume_item", false);
            boolean takeDurability = section.getBoolean("Mechanics.pearl_throw.take_durability", false);
            int durabilityReduction = section.getInt("Mechanics.pearl_throw.durability_reduction", 1);
            double multiplier = section.getDouble("Mechanics.pearl_throw.multiplier", 1.5);

            if (consumeItem && takeDurability) {
                throw new IllegalArgumentException("pearl_throw cannot have both consume_item and take_durability enabled.");
            }

            mechanic.setPearlThrow(enabled, cooldown, consumeItem, takeDurability, durabilityReduction, multiplier);
        }
    }


}