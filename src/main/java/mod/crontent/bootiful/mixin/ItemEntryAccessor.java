package mod.crontent.bootiful.mixin;


import net.minecraft.item.Item;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.event.listener.GameEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ItemEntry.class)
public interface ItemEntryAccessor {

    @Accessor
    RegistryEntry<Item> getItem();

    @Mutable
    @Accessor
    void setItem(RegistryEntry<Item> item);



}
