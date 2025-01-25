package org.notionsmp.nexoExtras;

import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.notionsmp.nexoExtras.classes.Mechanics;
import org.notionsmp.nexoExtras.listeners.NexoItemsLoadedListener;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public final class NexoExtras extends JavaPlugin {

    @Getter
    public static NexoExtras instance;
    public Map<String, Mechanics> mechanics = new HashMap<>();
    public Set<File> nexoFiles = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;

        registerListeners();
    }

    @Override
    public void onDisable() {
    }

    private void registerListeners() {
        registerListener(new NexoItemsLoadedListener());
    }

    private void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
}
