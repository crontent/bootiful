package mod.crontent.bootiful.mixin;

import com.google.common.collect.ImmutableList;
import mod.crontent.bootiful.ApplyBonusAttributeLootFunction;
import mod.crontent.bootiful.BonusAttributeLootCondition;
import mod.crontent.bootiful.Bootiful;
import mod.crontent.bootiful.ModAttributes;
import mod.crontent.bootiful.interfaces.PoolConditionsModifyHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.List;

@Mixin(LootPool.Builder.class)
public class LootPoolBuilderAccessor implements PoolConditionsModifyHelper {

    @Mutable
    @Shadow
    @Final
    private ImmutableList.Builder<LootPoolEntry> entries;

    /**
     * Public callable, wraps the currently instanced loot pool conditions with the appropriate Condition
     * @param filterItems
     */
    @Override
    public void bootiful$wrapCondition(ArrayList<String> filterItems) {
        for (var entry : this.entries.build()) {
            bootiful$handleEntry(entry, filterItems);
        }
    }

    /**
     * This can recursively handle Entries containing subentries until it finds an appropriate itemEntry, which it then modifies
     * @param entry
     * @param filterItems
     */
    @Unique
    private void bootiful$handleEntry(LootPoolEntry entry, ArrayList<String> filterItems) {
        if (entry instanceof CombinedEntryAccessor combinedEntry) {
            combinedEntry.getChildren().forEach(child -> bootiful$handleEntry(child, filterItems));
        }
        else if (entry instanceof ItemEntryAccessor itemEntry && filterItems.contains(itemEntry.getItem().getIdAsString())) {

            Bootiful.LOGGER.debug("Working in Item Entry: {}", itemEntry.getItem().getIdAsString());

            if(entry instanceof LeafEntryAccessor leafEntry) {
                leafEntry.getFunctions().forEach(function -> {
                    if(function instanceof ApplyBonusLootFunction applyBonusLootFunction) {
                        Bootiful.LOGGER.debug("Found Function ApplyBonus in {}", leafEntry);
                        addFunctionToLeafEntry(leafEntry, applyBonusLootFunction);
                    }
                });
            }

            if (entry instanceof LootPoolEntryAccessor lootPoolEntry) {
                List<LootCondition> newConditions = bootiful$getModifiedConditions(lootPoolEntry);
                bootiful$updateLootPool(lootPoolEntry, newConditions);
            }
        }
    }

    @Unique
    private static void addFunctionToLeafEntry(LeafEntryAccessor leafEntry, ApplyBonusLootFunction existing) {
        List<LootFunction> newFunctions = new ArrayList<>();
        newFunctions.add(existing);
        newFunctions.add(ApplyBonusAttributeLootFunction.builder(ModAttributes.NATURE_DROP_CHANCE).build());
        leafEntry.setFunctions(newFunctions);
        leafEntry.setCompiledFunctions(LootFunctionTypes.join(newFunctions));
    }


    /**
     * @param lootPoolEntry The Loot Pool whose conditions should be used
     * @return List of Conditions, inappropriate conditions unchanged, appropriate conditions are wrapped in an AnyOf and this contains both the original and the additionsal condition
     * TODO: make this condition agnostic.
     */
    @Unique
    private @NotNull List<LootCondition> bootiful$getModifiedConditions(LootPoolEntryAccessor lootPoolEntry) {
        List<LootCondition> newConditions = new ArrayList<>();
        lootPoolEntry.getConditions().forEach(condition -> {
            if (condition instanceof TableBonusLootCondition tableBonusLootCondition) {
                Bootiful.LOGGER.debug("Found Condition TableBonus in {}", lootPoolEntry);
                newConditions.add(bootiful$getWrappedCondition(tableBonusLootCondition));
            } else if (condition instanceof RandomChanceLootCondition randomChanceLootCondition) {
                Bootiful.LOGGER.debug("Found Condition RandomChance in {}", lootPoolEntry);
                newConditions.add(bootiful$getWrappedCondition(randomChanceLootCondition));
            } else {
                newConditions.add(condition);
            }
        });
        return newConditions;
    }

    /**
     * @param original The Original LootCondition that has already been verified to be of the desired type
     * @return The Original conditions wrapped in an AnyOf and this contains both the original and the additionsal condition
     */
    @Unique
    private LootCondition bootiful$getWrappedCondition(TableBonusLootCondition original) {
        //System.out.println("We are handling a bonus Condition");
        final List<Float> originalChances = original.chances();
        final RegistryEntry<Enchantment> originalEnchantment = original.enchantment();

        //STUPID STUPID STUPID
        float[] chances = new float[originalChances.size()];
        for (int i = 0; i < originalChances.size(); i++) {
            chances[i] = originalChances.get(i);
        }

        return AnyOfLootCondition.builder(
                TableBonusLootCondition.builder(originalEnchantment, chances),
                BonusAttributeLootCondition.builder(ModAttributes.NATURE_DROP_CHANCE, chances[0])
        ).build();
    }

    @Unique
    private LootCondition bootiful$getWrappedCondition(RandomChanceLootCondition original){
        return AnyOfLootCondition.builder(
                RandomChanceLootCondition.builder(original.chance()),
                BonusAttributeLootCondition.builder(ModAttributes.NATURE_DROP_CHANCE, ((ConstantLootNumberProvider)original.chance()).value())
        ).build();
    }

    /**
     * Overwrite Loot Pool conditions with the changed Conditions
     * @param lootPoolEntry
     * @param newConditions
     */
    @Unique
    private static void bootiful$updateLootPool(LootPoolEntryAccessor lootPoolEntry, List<LootCondition> newConditions) {
        lootPoolEntry.setConditions(newConditions);
        lootPoolEntry.setConditionPredicate(Util.allOf(newConditions));
    }
}
