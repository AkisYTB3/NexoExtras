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
            });
        }
    }
}