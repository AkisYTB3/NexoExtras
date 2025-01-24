package org.notionsmp.nexoExtras.listeners;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;
import org.notionsmp.nexoExtras.NexoExtras;
import org.notionsmp.nexoExtras.classes.Mechanics;
import org.notionsmp.nexoExtras.classes.mechanics.PearlThrow;

import java.util.HashMap;
import java.util.UUID;

public class PearlThrowListener implements Listener {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void on(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        String nexoItemId = NexoItems.idFromItem(tool);
        Mechanics mechanics = NexoExtras.getInstance().getMechanics().get(nexoItemId);
        if (mechanics == null || mechanics.getPearlThrow() == null) return;

        PearlThrow pearlThrow = mechanics.getPearlThrow();
        if (!pearlThrow.enabled()) return;

        if (cooldowns.containsKey(player.getUniqueId())) {
            long lastUsed = cooldowns.get(player.getUniqueId());
            if (System.currentTimeMillis() - lastUsed < pearlThrow.cooldown() * 1000L) {
                player.sendMessage("§cYou must wait before using this again!");
                return;
            }
        }

        handleDurabilityOrConsumption(player, tool, pearlThrow);

        throwEnderPearl(player, pearlThrow.multiplier());

        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private void handleDurabilityOrConsumption(Player player, ItemStack tool, PearlThrow pearlThrow) {
        if (tool.getItemMeta() instanceof Damageable damageableMeta) {
            if (pearlThrow.takeDurability()) {
                int currentDamage = damageableMeta.getDamage();
                int maxDamage = damageableMeta.hasMaxDamage() ? damageableMeta.getMaxDamage() : tool.getType().getMaxDurability();

                int newDamage = currentDamage + pearlThrow.durabilityReduction();

                if (newDamage >= maxDamage) {
                    player.getInventory().setItemInMainHand(null);
                    player.playSound(player.getLocation(), "entity.item.break", 1.0f, 1.0f);
                } else {
                    damageableMeta.setDamage(newDamage);
                    tool.setItemMeta(damageableMeta);
                }
            }
        } else if (pearlThrow.consumeItem()) {
            if (tool.getAmount() <= 1) {
                player.getInventory().setItemInMainHand(null);
            } else {
                tool.setAmount(tool.getAmount() - 1);
            }
        }
    }

    private void throwEnderPearl(Player player, double multiplier) {
        Projectile pearl = player.launchProjectile(EnderPearl.class);

        Vector direction = player.getEyeLocation().getDirection();
        pearl.setVelocity(direction.multiply(multiplier));
        player.getWorld().playSound(player.getLocation(), "entity.ender_pearl.throw", 1.0f, 1.0f);
    }
}
