package org.notionsmp.nexoExtras.classes.mechanics;

public record PearlThrow(
        boolean enabled,
        int cooldown,
        boolean consumeItem,
        boolean takeDurability,
        int durabilityReduction,
        double multiplier
) {
}
