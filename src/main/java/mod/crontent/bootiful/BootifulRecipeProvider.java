package mod.crontent.bootiful;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BootifulRecipeProvider extends FabricRecipeProvider {
    public BootifulRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);

                createShapeless(RecipeCategory.COMBAT, ModBoots.FOREST_BOOTS)
                        .input(Items.LEATHER_BOOTS)
                        .input(ModItems.FAIRY_DUST)
                        .criterion(hasItem(Items.LEATHER_BOOTS), conditionsFromItem(Items.LEATHER_BOOTS))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, ModItems.OBSIDIAN_SHARD, 4)
                        .input(Items.OBSIDIAN)
                        .criterion(hasItem(Items.OBSIDIAN), conditionsFromItem(Items.OBSIDIAN))
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, ModBoots.CLOUD_BOOTS, 1)
                        .pattern("l l")
                        .pattern("l l")
                        .input('l', ModItems.GOLDEN_FEATHER)
                        .group("armor")
                        .criterion(hasItem(ModItems.GOLDEN_FEATHER), conditionsFromItem(ModItems.GOLDEN_FEATHER))
                        .offerTo(exporter);

                createShaped(RecipeCategory.COMBAT, ModBoots.SPIKE_BOOTS, 1)
                        .pattern("l l")
                        .pattern("o o")
                        .input('l', ModItems.IRON_PLATING)
                        .input('o', ModItems.OBSIDIAN_SHARD)
                        .criterion(hasItem(ModItems.OBSIDIAN_SHARD), conditionsFromItem(ModItems.OBSIDIAN_SHARD))
                        .group("armor")
                        .offerTo(exporter);

                createShaped(RecipeCategory.MISC, ModItems.GOLDEN_FEATHER, 1)
                        .pattern("lll")
                        .pattern("lol")
                        .pattern("lll")
                        .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                        .input('l', Items.GOLD_INGOT)
                        .input('o', Items.FEATHER)
                        .offerTo(exporter);

                offerBlasting(List.of(Items.IRON_BLOCK), RecipeCategory.MISC, ModItems.IRON_PLATING, 0.3f, 200, "resources");


            }
        };
    }

    @Override
    public String getName() {
        return "BootifulRecipes";
    }
}
