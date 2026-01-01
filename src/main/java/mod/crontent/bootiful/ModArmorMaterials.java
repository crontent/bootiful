package mod.crontent.bootiful;

import net.minecraft.item.*;
import net.minecraft.item.equipment.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModArmorMaterials {


    public static final RegistryKey<EquipmentAsset> FOREST_MATERIAL_KEY = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(Bootiful.MOD_ID, "forest"));
    public static final RegistryKey<EquipmentAsset> CLOUD_MATERIAL_KEY = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(Bootiful.MOD_ID, "cloud"));
    public static final RegistryKey<EquipmentAsset> SPIKE_MATERIAL_KEY = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(Bootiful.MOD_ID, "spike"));

    public static final ArmorMaterial FOREST_MATERIAL = new ArmorMaterial(
            17, //durab
            Map.of( //defenses
                EquipmentType.HELMET, 0,
                EquipmentType.CHESTPLATE, 0,
                EquipmentType.LEGGINGS, 0,
                EquipmentType.BOOTS, 1,
                EquipmentType.BODY, 1),
            18, //enchantment value
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, //equip sound
            0.0F,//toughness
            0.0F,//kb resistance
            ModTags.REPAIRS_FOREST_BOOTS, //repair ingredient TagKey<Item>
            FOREST_MATERIAL_KEY // RegistryKey<EquipmentAsset>
    );


    public static final ArmorMaterial CLOUD_MATERIAL = new ArmorMaterial(
            20, //durab
            Map.of( //defenses
                    EquipmentType.HELMET, 0,
                    EquipmentType.CHESTPLATE, 0,
                    EquipmentType.LEGGINGS, 0,
                    EquipmentType.BOOTS, 2,
                    EquipmentType.BODY, 2),
            10, //enchantment value
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, //equip sound
            1.0F,//toughness
            0.0F,//kb resistance
            ModTags.REPAIRS_CLOUD_BOOTS, //repair ingredient TagKey<Item>
            CLOUD_MATERIAL_KEY // RegistryKey<EquipmentAsset>
    );

    public static final ArmorMaterial SPIKE_MATERIAL = new ArmorMaterial(
            30, //durab
            Map.of( //defenses
                    EquipmentType.HELMET, 0,
                    EquipmentType.CHESTPLATE, 0,
                    EquipmentType.LEGGINGS, 0,
                    EquipmentType.BOOTS, 3,
                    EquipmentType.BODY, 3),
            8, //enchantment value
            SoundEvents.ITEM_ARMOR_EQUIP_IRON, //equip sound
            1.5F,//toughness
            0.0F,//kb resistance
            ModTags.REPAIRS_SPIKE_BOOTS, //repair ingredient TagKey<Item>
            SPIKE_MATERIAL_KEY // RegistryKey<EquipmentAsset>
    );


    public static void initialize() {
        Bootiful.LOGGER.info("Registering " + Bootiful.MOD_ID + " Armor Mats");
    }

}
