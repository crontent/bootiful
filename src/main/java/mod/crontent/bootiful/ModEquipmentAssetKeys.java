package mod.crontent.bootiful;

import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Map;

public interface ModEquipmentAssetKeys {
    RegistryKey<? extends Registry<EquipmentAsset>> REGISTRY_KEY = RegistryKey.ofRegistry( Identifier.of(Bootiful.MOD_ID, ("equipment_asset")));
    RegistryKey<EquipmentAsset> FOREST = register("forest");
    RegistryKey<EquipmentAsset> CLOUD = register("cloud");
    RegistryKey<EquipmentAsset> SPIKE = register("spike");

    static RegistryKey<EquipmentAsset> register(String name) {
        return RegistryKey.of(REGISTRY_KEY, Identifier.of(Bootiful.MOD_ID, name));
    }
}
