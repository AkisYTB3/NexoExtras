package org.notionsmp.nexoExtras.classes;

import lombok.Getter;
import org.notionsmp.nexoExtras.classes.mechanics.PearlThrow;

@Getter
public class Mechanics {

    private final String id;
    private PearlThrow pearlThrow;

    public Mechanics(String id) {
        this.id = id;
    }

    public void setPearlThrow(boolean enabled, int cooldown, boolean consumeItem, boolean takeDurability, int durabilityReduction, double multiplier) {
        this.pearlThrow = new PearlThrow(enabled, cooldown, consumeItem, takeDurability, durabilityReduction, multiplier);
    }
}
