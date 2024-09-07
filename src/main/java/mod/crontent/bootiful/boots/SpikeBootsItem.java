package mod.crontent.bootiful.boots;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import mod.crontent.bootiful.Bootiful;
import mod.crontent.bootiful.interfaces.IStatusEffectPurgable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class SpikeBootsItem extends ArmorItem implements IStatusEffectPurgable {

    private final Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> noKnockbackModifier;
    private boolean fallEffectPlayed = false;

    public SpikeBootsItem(RegistryEntry<ArmorMaterial> material, int durabilityMultiplier) {
        super(material, Type.BOOTS, new Item.Settings().maxDamage(Type.BOOTS.getMaxDamage(durabilityMultiplier)));

        noKnockbackModifier = HashMultimap.create(1, 1);
        noKnockbackModifier.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(Identifier.of(Bootiful.MOD_ID, "spike_boots_knockback_resistance"), 1f, EntityAttributeModifier.Operation.ADD_VALUE));


    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("item.bootiful.spike_boots_description_1").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("item.bootiful.spike_boots_description_2").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("item.bootiful.spike_boots_action").formatted(Formatting.GOLD));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient()
                && entity instanceof PlayerEntity player){
            if(player.getEquippedStack(EquipmentSlot.FEET).isOf(this)
                    && player.isOnGround()
                    && player.isSneaking()) {
                player.getAttributes().addTemporaryModifiers(noKnockbackModifier);
                if(!fallEffectPlayed){
                    playFallEffect(world, player);
                    fallEffectPlayed = true;
                }
            }else if (fallEffectPlayed){
                purgeEffects(player);
                fallEffectPlayed = false;
            }
        }


        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void playFallEffect(World world, PlayerEntity player) {
        if(!world.isClient &&
                world instanceof ServerWorld serverWorld){
            serverWorld.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK,
                    player.getSteppingBlockState()),
                    player.lastRenderX, player.lastRenderY, player.lastRenderZ,
                    12, 0,0,0,
                    world.random.nextFloat() - 0.92F);
            serverWorld.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_ANVIL_BREAK, SoundCategory.PLAYERS);
            serverWorld.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.PLAYERS);
        }
    }


    public void handleSpikeAttack(PlayerEntity player) {
        World world = player.getEntityWorld();
        float boxHeight = 2;
        List<Entity> e = world.getOtherEntities(player, Box.of(player.getPos(), 2, boxHeight, 2));
        e.forEach((entry) -> {
            if (entry instanceof MobEntity mob) {
                mob.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 3, false, true));
                mob.damage(player.getDamageSources().cactus(), Bootiful.CONFIG.spikeBootsConfig.stompDamage());
            }
        });
    }



    @Override
    public void purgeEffects(LivingEntity entity) {
        entity.getAttributes().removeModifiers(noKnockbackModifier);
    }
}
