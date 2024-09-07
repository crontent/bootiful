package mod.crontent.bootiful.boots;

import mod.crontent.bootiful.Bootiful;
import mod.crontent.bootiful.interfaces.IAirspeedChange;
import mod.crontent.bootiful.interfaces.IStatusEffectEquipable;
import mod.crontent.bootiful.interfaces.IStatusEffectPurgable;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class CloudBootsItem extends ArmorItem implements IStatusEffectPurgable, IStatusEffectEquipable, IAirspeedChange {
    private final int jumpBoostAmount;
    private final float fallDurabilityPenalty;
    private final boolean doesFallDamageHurt;
    private final boolean doesFallDamageRemoveCondition;

    private float fallDamageMultiplier = -.35f;

    public CloudBootsItem(RegistryEntry<ArmorMaterial> material, int durabilityMultiplier, int jumpBoostAmount, boolean doesFallDamageHurt, boolean doesFallDamageRemoveCondition) {
        super(material, Type.BOOTS, new Item.Settings().maxDamage(Type.BOOTS.getMaxDamage(durabilityMultiplier)));

        this.jumpBoostAmount = jumpBoostAmount;
        this.fallDurabilityPenalty = .75f;

        this.doesFallDamageHurt = doesFallDamageHurt;
        this.doesFallDamageRemoveCondition = doesFallDamageRemoveCondition;

        if (!this.doesFallDamageHurt) {
            this.fallDamageMultiplier = -1f;
        }
    }

    @Override
    public AttributeModifiersComponent getAttributeModifiers() {
        return super.getAttributeModifiers()
                .with(
                        EntityAttributes.GENERIC_MOVEMENT_SPEED,
                        new EntityAttributeModifier(
                                Identifier.of(Bootiful.MOD_ID, "cloud_boots_movement_speed"),
                                .2f,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        AttributeModifierSlot.FEET)
                .with(
                        EntityAttributes.GENERIC_GRAVITY,
                        new EntityAttributeModifier(
                                Identifier.of(Bootiful.MOD_ID, "cloud_boots_gravity"), -.02f,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.FEET)
                .with(
                        EntityAttributes.GENERIC_SAFE_FALL_DISTANCE,
                        new EntityAttributeModifier(
                                Identifier.of(Bootiful.MOD_ID, "cloud_boots_safe_fall"), 2f,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.FEET)
                .with(
                        EntityAttributes.GENERIC_STEP_HEIGHT,
                        new EntityAttributeModifier(
                                Identifier.of(Bootiful.MOD_ID, "cloud_boots_step_height"), 1f,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.FEET)
                .with(
                        EntityAttributes.GENERIC_FALL_DAMAGE_MULTIPLIER,
                        new EntityAttributeModifier(
                                Identifier.of(Bootiful.MOD_ID, "cloud_boots_fall_damage_bonus"), fallDamageMultiplier,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        AttributeModifierSlot.FEET);
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
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("item.bootiful.cloud_boots_description").formatted(Formatting.AQUA));
        MutableText mutablecomponent = Text.translatable(StatusEffects.JUMP_BOOST.value().getTranslationKey());
        mutablecomponent = Text.translatable("potion.withAmplifier", mutablecomponent, Text.translatable("potion.potency." + this.jumpBoostAmount)).formatted(Formatting.BLUE);
        tooltip.add(mutablecomponent);

    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(entity instanceof PlayerEntity player){
            if(player.getEquippedStack(EquipmentSlot.FEET).getItem() == this){

                if(!world.isClient &&
                        world instanceof ServerWorld serverWorld &&
                        !player.isOnGround()
                        && player.fallDistance >= 0.8f){
                    serverWorld.spawnParticles(ParticleTypes.CLOUD, player.lastRenderX, player.lastRenderY, player.lastRenderZ, 2, 0,0,0, world.random.nextFloat() - 0.5F);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
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
