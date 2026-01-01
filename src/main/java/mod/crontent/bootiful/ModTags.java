package mod.crontent.bootiful;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static final TagKey<Item> REPAIRS_FOREST_BOOTS = TagKey.of(Registries.ITEM.getKey(), Identifier.of(Bootiful.MOD_ID, "repairs_forest_boots"));
    public static final TagKey<Item> REPAIRS_CLOUD_BOOTS = TagKey.of(Registries.ITEM.getKey(), Identifier.of(Bootiful.MOD_ID, "repairs_cloud_boots"));
    public static final TagKey<Item> REPAIRS_SPIKE_BOOTS = TagKey.of(Registries.ITEM.getKey(), Identifier.of(Bootiful.MOD_ID, "repairs_spike_boots"));

    public static void initialize() {
        Bootiful.LOGGER.info("Registering " + Bootiful.MOD_ID + " tags");
    }
}
