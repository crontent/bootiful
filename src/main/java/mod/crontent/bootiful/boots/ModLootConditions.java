package mod.crontent.bootiful.boots;

import com.mojang.serialization.MapCodec;
import mod.crontent.bootiful.Bootiful;
import mod.crontent.bootiful.BonusAttributeLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModLootConditions {
    public static final LootConditionType TABLE_BONUS_ATTRIBUTE = register("table_bonus_attribute", BonusAttributeLootCondition.CODEC);

    private static LootConditionType register(String id, MapCodec<? extends LootCondition> codec) {
        return Registry.register(Registries.LOOT_CONDITION_TYPE, Identifier.of(Bootiful.MOD_ID, id), new LootConditionType(codec));
    }

    public static void initialize() {
        Bootiful.LOGGER.info("Registering " + Bootiful.MOD_ID + " loot conditions");
    }
}
