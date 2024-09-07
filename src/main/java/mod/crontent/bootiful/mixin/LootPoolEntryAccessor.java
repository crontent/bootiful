package mod.crontent.bootiful.mixin;


import net.minecraft.item.Item;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Predicate;

@Mixin(LootPoolEntry.class)
public interface LootPoolEntryAccessor {

    @Accessor
    List<LootCondition> getConditions();


    @Mutable
    @Accessor
    void setConditions(List<LootCondition> conditions);

    @Accessor
    Predicate<LootContext> getConditionPredicate();


    @Mutable
    @Accessor
    void setConditionPredicate(Predicate<LootContext> conditionPredicate);
}
