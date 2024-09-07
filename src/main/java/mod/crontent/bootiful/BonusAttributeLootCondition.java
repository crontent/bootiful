package mod.crontent.bootiful;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.crontent.bootiful.boots.ModLootConditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.Set;

public record BonusAttributeLootCondition(RegistryEntry<EntityAttribute> attribute, float baseChance) implements LootCondition {
    public static final MapCodec<BonusAttributeLootCondition> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            EntityAttribute.CODEC.fieldOf("attribute").forGetter(BonusAttributeLootCondition::attribute),
                            Codec.FLOAT.fieldOf("base_chance").forGetter(BonusAttributeLootCondition::baseChance)

                    )
                    .apply(instance, BonusAttributeLootCondition::new)
    );


    @Override
    public LootConditionType getType() {
        return ModLootConditions.TABLE_BONUS_ATTRIBUTE;
    }

    @Override
    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext context) {
        Entity breakingEntity = context.get(LootContextParameters.THIS_ENTITY);
        double attributeOverOneValue = breakingEntity instanceof PlayerEntity player ? player.getAttributeValue(ModAttributes.NATURE_DROP_CHANCE) - 1f : 0f;

        if (attributeOverOneValue <= 0.001f) return false;

        double chance = this.baseChance + this.baseChance * attributeOverOneValue;
        boolean result = context.getRandom().nextFloat() <= chance;
        breakingEntity.sendMessage(Text.of("Chance of Drop" + chance + ", Will I drop bcuz of Atribute? " + result));
        return result;
    }

    public static LootCondition.Builder builder(RegistryEntry<EntityAttribute> attribute, float baseChance) {
        return () -> new BonusAttributeLootCondition(attribute, baseChance);
    }
}
