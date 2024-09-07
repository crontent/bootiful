package mod.crontent.bootiful.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.LootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.BiFunction;

@Mixin(LeafEntry.class)
public interface LeafEntryAccessor {

    @Accessor
    List<LootFunction> getFunctions();

    @Mutable
    @Accessor
    void setFunctions(List<LootFunction> functions);

    @Accessor
    BiFunction<ItemStack, LootContext, ItemStack> getCompiledFunctions();

    @Mutable
    @Accessor
    void setCompiledFunctions(BiFunction<ItemStack, LootContext, ItemStack> compiledFunctions);


}
