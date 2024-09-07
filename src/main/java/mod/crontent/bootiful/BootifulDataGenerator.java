package mod.crontent.bootiful;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.AnyOfLootCondition;
import net.minecraft.loot.condition.TableBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class BootifulDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(LootTableTest::new);
    }

    private static class LootTableTest extends FabricBlockLootTableProvider {
        public LootTableTest(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture){
            super(dataOutput, registriesFuture);
        }


        @Override
        public void generate() {
            RegistryWrapper.Impl<Enchantment> impl = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);

//            addDrop(Blocks.GRASS_BLOCK, drops(Items.DIAMOND)
//                    .apply(ApplyBonusAttributeLootFunction.builder(ModAttributes.NATURE_DROP_CHANCE));

            addDrop(Blocks.GRAVEL,
                    block -> this.dropsWithSilkTouch(
                            block,
                            this.addSurvivesExplosionCondition(
                                    block,
                                    ItemEntry.builder(Items.FLINT)
                                            .conditionally(AnyOfLootCondition.builder(
                                                    TableBonusLootCondition.builder(impl.getOrThrow(Enchantments.FORTUNE), 0.1F, 0.14285715F, 0.25F, 1.0F),
                                                    BonusAttributeLootCondition.builder(ModAttributes.NATURE_DROP_CHANCE, 0.1F)
                                            ))
                                            .alternatively(ItemEntry.builder(block))
                            )
                    )
            );
        }
    }

}

