package org.notionsmp.nexoExtras.listeners;

import com.nexomc.nexo.api.events.NexoItemsLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.notionsmp.nexoExtras.NexoExtras;
import org.notionsmp.nexoExtras.utils.ItemConfigUtil;

public class NexoItemsLoadedListener implements Listener {
    @EventHandler
    public void on(NexoItemsLoadedEvent event) {
        NexoExtras.getInstance().getNexoFiles().clear();
        NexoExtras.getInstance().getNexoFiles().addAll(ItemConfigUtil.getItemFiles());

        ItemConfigUtil.loadMechanics();
    }
}
