package mod.crontent.bootiful;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModLootFunctions {
    public static final LootFunctionType<ApplyBonusAttributeLootFunction> APPLY_BONUS_ATTRIBUTE =
            register("apply_bonus_attribute", ApplyBonusAttributeLootFunction.CODEC);

    private static <T extends LootFunction> LootFunctionType<T> register(String id, MapCodec<T> codec) {
        return Registry.register(Registries.LOOT_FUNCTION_TYPE, Identifier.of(Bootiful.MOD_ID, id), new LootFunctionType<>(codec));
    }

    public static void initialize() {
        Bootiful.LOGGER.info("Registering " + Bootiful.MOD_ID + " loot functions");
    }

}
