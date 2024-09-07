package mod.crontent.bootiful.mixin;


import net.minecraft.item.Item;
import net.minecraft.loot.entry.CombinedEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(CombinedEntry.class)
public interface CombinedEntryAccessor {

    @Accessor
    List<LootPoolEntry> getChildren();

    @Mutable
    @Accessor
    void setChildren(List<LootPoolEntry> children);


}
