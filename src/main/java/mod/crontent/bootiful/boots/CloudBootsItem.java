package mod.crontent.bootiful.boots;

import mod.crontent.bootiful.Bootiful;
import mod.crontent.bootiful.ModArmorMaterials;
import mod.crontent.bootiful.interfaces.IAirspeedChange;
import mod.crontent.bootiful.interfaces.IStatusEffectEquipable;
import mod.crontent.bootiful.interfaces.IStatusEffectPurgable;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class CloudBootsItem extends Item implements IStatusEffectPurgable, IStatusEffectEquipable, IAirspeedChange {
    private final int jumpBoostAmount;
    private final float fallDurabilityPenalty;
    private final boolean doesFallDamageRemoveCondition;

    private static float fallDamageMultiplier = -.35f;

    public CloudBootsItem(Item.Settings settings) {
        super(settings
                .armor(ModArmorMaterials.CLOUD_MATERIAL, EquipmentType.BOOTS)
                .rarity(Rarity.EPIC)
                .attributeModifiers(getAttributeModifiers())
        );

        this.jumpBoostAmount = 1;
        this.fallDurabilityPenalty = .75f;

        this.doesFallDamageRemoveCondition = Bootiful.CONFIG.cloudBootsConfig.doesFallDamageRemoveCondition();

        if (!Bootiful.CONFIG.cloudBootsConfig.doesFallDamageHurt()) {
            fallDamageMultiplier = -1f;
        }
    }

    public static AttributeModifiersComponent getAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(
                        EntityAttributes.MOVEMENT_SPEED,
                        new EntityAttributeModifier(
                                Identifier.of(Bootiful.MOD_ID, "cloud_boots_movement_speed"),
                                .2f,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        AttributeModifierSlot.FEET)
                .add(
                        EntityAttributes.GRAVITY,
                        new EntityAttributeModifier(
                                Identifier.of(Bootiful.MOD_ID, "cloud_boots_gravity"), -.02f,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.FEET)
                .add(
                        EntityAttributes.SAFE_FALL_DISTANCE,
                        new EntityAttributeModifier(
                                Identifier.of(Bootiful.MOD_ID, "cloud_boots_safe_fall"), 2f,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.FEET)
                .add(
                        EntityAttributes.STEP_HEIGHT,
                        new EntityAttributeModifier(
                                Identifier.of(Bootiful.MOD_ID, "cloud_boots_step_height"), 1f,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.FEET)
                .add(
                        EntityAttributes.FALL_DAMAGE_MULTIPLIER,
                        new EntityAttributeModifier(
                                Identifier.of(Bootiful.MOD_ID, "cloud_boots_fall_damage_bonus"), fallDamageMultiplier,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        AttributeModifierSlot.FEET)
                .build();
    }

    public int getJumpBoostLevel()
    {
        return this.jumpBoostAmount;
    }


    public float getFallDurabilityPenalty() {
        if(doesFallDamageRemoveCondition) return fallDurabilityPenalty;
        else return 0f;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        textConsumer.accept(Text.translatable("item.bootiful.cloud_boots_description").formatted(Formatting.AQUA));
        MutableText mutablecomponent = Text.translatable(StatusEffects.JUMP_BOOST.value().getTranslationKey());
        mutablecomponent = Text.translatable("potion.withAmplifier", mutablecomponent, Text.translatable("potion.potency." + this.jumpBoostAmount)).formatted(Formatting.BLUE);
        textConsumer.accept(mutablecomponent);
    }


    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if(entity instanceof PlayerEntity player){
            if(player.getEquippedStack(EquipmentSlot.FEET).getItem() == this){

                if(world instanceof ServerWorld serverWorld &&
                        !player.isOnGround()
                        && player.fallDistance >= 0.8f){
                    serverWorld.spawnParticles(ParticleTypes.CLOUD, player.lastRenderX, player.lastRenderY, player.lastRenderZ, 2, 0,0,0, world.random.nextFloat() - 0.5F);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot);
    }

    @Override
    public void purgeEffects(LivingEntity entity) {
        entity.removeStatusEffect(StatusEffects.JUMP_BOOST);
    }

    @Override
    public void addEffects(LivingEntity entity) {
        StatusEffectInstance jumpBoost = new StatusEffectInstance(StatusEffects.JUMP_BOOST, -1, this.jumpBoostAmount, false, false, false);
        entity.addStatusEffect(jumpBoost);
    }

    @Override
    public float getAirspeedModifier() {
        return this.jumpBoostAmount * 1.25f;
    }

}
