package mod.crontent.bootiful;

import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModArmorMaterials {


    public static final RegistryEntry<ArmorMaterial> FOREST_MATERIAL = register("forest_material",
            Map.of(
                    ArmorItem.Type.HELMET, 0,
                    ArmorItem.Type.CHESTPLATE, 0,
                    ArmorItem.Type.LEGGINGS, 0,
                    ArmorItem.Type.BOOTS, 1
            ),
            18,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            0F,
            0.0F,
            () -> Ingredient.ofItems(Items.LEATHER));

    public static final RegistryEntry<ArmorMaterial> CLOUD_MATERIAL = register("cloud_material",
            Map.of(
                    ArmorItem.Type.HELMET, 0,
                    ArmorItem.Type.CHESTPLATE, 0,
                    ArmorItem.Type.LEGGINGS, 0,
                    ArmorItem.Type.BOOTS, 2
            ),
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            1F,
            0.0F,
            () -> Ingredient.ofItems(ModItems.GOLDEN_FEATHER));

    public static final RegistryEntry<ArmorMaterial> SPIKE_MATERIAL = register("spike_material",
            Map.of(
                    ArmorItem.Type.HELMET, 0,
                    ArmorItem.Type.CHESTPLATE, 0,
                    ArmorItem.Type.LEGGINGS, 0,
                    ArmorItem.Type.BOOTS, 3
            ),
            8,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            1.5F,
            0.0F,
            () -> Ingredient.ofItems(Items.DIAMOND));



    private static RegistryEntry<ArmorMaterial> register(
            String id,
            Map<ArmorItem.Type, Integer> defense,
            int enchantability,
            RegistryEntry<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient
    ) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(Identifier.of(Bootiful.MOD_ID, id)));
        return register(id, defense, enchantability, equipSound, toughness, knockbackResistance, repairIngredient, list);
    }

    /**
     * Register Function for optional dyability, using leather
     * @param id
     * @param defense
     * @param enchantability
     * @param equipSound
     * @param toughness
     * @param knockbackResistance
     * @param repairIngredient
     * @param layers
     * @return
     */
    private static RegistryEntry<ArmorMaterial> register(
            String id,
            Map<ArmorItem.Type, Integer> defense,
            int enchantability,
            RegistryEntry<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient,
            List<ArmorMaterial.Layer> layers
    ) {
        EnumMap<ArmorItem.Type, Integer> enumMap = new EnumMap<>(ArmorItem.Type.class);

        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            enumMap.put(type, defense.get(type));
        }

        return Registry.registerReference(
                Registries.ARMOR_MATERIAL,
                Identifier.of(Bootiful.MOD_ID, id),
                new ArmorMaterial(enumMap, enchantability, equipSound, repairIngredient, layers, toughness, knockbackResistance)
        );
    }

    public static void initialize() {
        Bootiful.LOGGER.info("Registering " + Bootiful.MOD_ID + " Armor Materials");
    }
}
