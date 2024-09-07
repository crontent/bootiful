package mod.crontent.bootiful;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static Item GOLDEN_FEATHER = register("golden_feather", new Item(new Item.Settings()));
    public static Item OBSIDIAN_SHARD = register("obsidian_shard", new Item(new Item.Settings()));
    public static Item IRON_PLATING = register("iron_plating", new Item(new Item.Settings()));
    public static Item FAIRY_DUST = register("fairy_dust", new Item(new Item.Settings()));


    public static Item register(String id, Item entry){
        return Registry.register(Registries.ITEM,
                Identifier.of(Bootiful.MOD_ID, id),
                entry);
    }

    public static void initialize(){
        Bootiful.LOGGER.info("Registering " + Bootiful.MOD_ID + " generic items");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((itemGroup) -> {
            itemGroup.add(GOLDEN_FEATHER);
            itemGroup.add(OBSIDIAN_SHARD);
            itemGroup.add(IRON_PLATING);
            itemGroup.add(FAIRY_DUST);
        });
    }
}
