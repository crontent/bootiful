package mod.crontent.bootiful;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Set;

public class ApplyBonusAttributeLootFunction extends ConditionalLootFunction {
    public final RegistryEntry<EntityAttribute> attribute;


    public static final MapCodec<ApplyBonusAttributeLootFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> ApplyBonusAttributeLootFunction.addConditionsField(instance).and(
                    EntityAttribute.CODEC.fieldOf("attribute").forGetter(function -> function.attribute)
                    )
                    .apply(instance, ApplyBonusAttributeLootFunction::new)
    );

    public ApplyBonusAttributeLootFunction(List<LootCondition> conditions, RegistryEntry<EntityAttribute> attribute) {
        super(conditions);
        this.attribute = attribute;
    }


    @Override
    public ItemStack process(ItemStack stack, LootContext context) {
        Entity breakingEntity = context.get(LootContextParameters.THIS_ENTITY);
        double chance = breakingEntity instanceof PlayerEntity playerEntity ? playerEntity.getAttributeValue(ModAttributes.NATURE_DROP_CHANCE) : 0;
        //Bootiful.LOGGER.info("Found Attribute on {} with value {}", breakingEntity.getName(), chance);
        int j = getValue(context.getRandom(), stack.getCount(), chance);
        stack.setCount(j);
        return stack;
    }

    @Override
    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.THIS_ENTITY);
    }

    @Override
    public LootFunctionType<ApplyBonusAttributeLootFunction> getType() {
        return ModLootFunctions.APPLY_BONUS_ATTRIBUTE;
    }

    //originally the oredrop function from ApplyBonusLootFunction
    private int getValue(Random random, int initialCount, double chance) {
        if (chance <= 1.) {
            return random.nextDouble() <= chance ? initialCount : 0;
        } else {
            int level = (int) Math.round((chance - 1) * 4);
            int i = random.nextInt(level + 2) - 1;
            if (i < 0) {
                i = 0;
            }
            return initialCount * (i + 1);
        }
    }

    public static ConditionalLootFunction.Builder<?> builder(RegistryEntry<EntityAttribute> attribute) {
        return builder(conditions -> new ApplyBonusAttributeLootFunction(conditions, attribute));
    }
}
