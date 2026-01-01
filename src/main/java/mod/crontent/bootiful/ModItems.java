package mod.crontent.bootiful;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {
    public static Item GOLDEN_FEATHER = register("golden_feather", Item::new, new Item.Settings());
    public static Item OBSIDIAN_SHARD = register("obsidian_shard", Item::new, new Item.Settings());
    public static Item IRON_PLATING = register("iron_plating", Item::new, new Item.Settings());
    public static Item FAIRY_DUST = register("fairy_dust", Item::new, new Item.Settings());


    public static Item oldregister(String id, Item entry){
        return Registry.register(Registries.ITEM,
                Identifier.of(Bootiful.MOD_ID, id),
                entry);
    }

    public static Item register(String id, Function<Item.Settings, Item> itemFunction, Item.Settings settings){
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Bootiful.MOD_ID, id));

        Item item = itemFunction.apply(settings.registryKey(itemKey));

        Registry.register(Registries.ITEM, itemKey, item);

        return item;
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
