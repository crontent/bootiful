package mod.crontent.bootiful;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModAttributes {

    public static final RegistryEntry<EntityAttribute> NATURE_DROP_CHANCE = register("nature_drop_chance", 1.0, 0.0, 4.0);

    private static RegistryEntry<EntityAttribute> register(String id, final double base, final double min, final double max) {
        final String name = "attribute.name.generic." + Bootiful.MOD_ID + '.' + id;
        EntityAttribute a = new ClampedEntityAttribute(name, base, min, max).setTracked(true);
        return Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(Bootiful.MOD_ID, id), a);
    }

    public static void initialize() {
        Bootiful.LOGGER.info("Registering " + Bootiful.MOD_ID + " Attributes");
    }
}
