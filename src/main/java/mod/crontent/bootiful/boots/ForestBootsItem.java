package mod.crontent.bootiful.boots;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import mod.crontent.bootiful.Bootiful;
import mod.crontent.bootiful.ModAttributes;
import mod.crontent.bootiful.ModParticles;
import mod.crontent.bootiful.interfaces.IStatusEffectPurgable;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.*;

public class ForestBootsItem extends ArmorItem implements IStatusEffectPurgable {

    /*
     */
    private final int BONUS_INTERVAL = 50;
    private final int BONUS_RADIUS;
    private final double BONUS_CHANCE;
    private final long MAX_CONTRIBUTING_BLOCK_COUNT;

    private final Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> forestSpeedModifier;
    private final double forestSpeedMultiplier;

    private final double LOOT_BONUS_MULTIPLIER;

    private RegistryEntry<Biome> previous_biome = null;


    /**
     * @param material
     * @param durabilityMultiplier
     * @param bonusChance What should be the chance that the effect is triggered when {@link ForestBootsItem#MAX_CONTRIBUTING_BLOCK_COUNT} is reached?
     * @param bonusRadius
     * @param maxContributingBlockCount How many blocks to take into consideration at maximum
     */
    public ForestBootsItem(RegistryEntry<ArmorMaterial> material, int durabilityMultiplier, double bonusChance, int bonusRadius, long maxContributingBlockCount, double forestSpeedMultiplier, double lootBonusMultiplier) {
        super(material, Type.BOOTS, new Settings().maxDamage(Type.BOOTS.getMaxDamage(durabilityMultiplier)));
        this.BONUS_RADIUS = bonusRadius;
        this.BONUS_CHANCE = bonusChance / maxContributingBlockCount;
        this.MAX_CONTRIBUTING_BLOCK_COUNT = maxContributingBlockCount;

        this.LOOT_BONUS_MULTIPLIER = lootBonusMultiplier;

        this.forestSpeedMultiplier = forestSpeedMultiplier;
        this.forestSpeedModifier = HashMultimap.create(1, 1);
        this.forestSpeedModifier.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(Identifier.of(Bootiful.MOD_ID, "forest_boots_speed"), forestSpeedMultiplier, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }


    @Override
    public AttributeModifiersComponent getAttributeModifiers() {
        return super.getAttributeModifiers()
                .with(
                        ModAttributes.NATURE_DROP_CHANCE,
                        new EntityAttributeModifier(
                                Identifier.of(Bootiful.MOD_ID, "forest_boots_nature_drop_chance"),
                                LOOT_BONUS_MULTIPLIER,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        AttributeModifierSlot.FEET);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("item.bootiful.forest_boots_description_1").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("item.bootiful.forest_boots_description_2").formatted(Formatting.AQUA));
        MutableText mutablecomponent = Text.translatable(EntityAttributes.GENERIC_MOVEMENT_SPEED.value().getTranslationKey());
        mutablecomponent = Text.translatable("attribute.modifier.plus.1", (int) (forestSpeedMultiplier * 100), mutablecomponent)
                .append(Text.of(" "))
                .append(Text.translatable("tooltip.bootiful.when_in_forest"))
                .formatted(Formatting.BLUE);
        tooltip.add(mutablecomponent);
    }



    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient()
                && entity instanceof PlayerEntity player) {
            if (player.getEquippedStack(EquipmentSlot.FEET).isOf(this)) {
                handleBonusHealing(world, player);
                handleSpeedIncrease(world, player);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }



    private void handleBonusHealing(World world, PlayerEntity player) {
        if (world.getTime() % BONUS_INTERVAL == 0) {
            Random r = world.getRandom();

                    List<BlockPos> natureBlocks = filterSurroundingBlocks(world, player);
                    //natureBlocks.forEach(pos -> System.out.println(pos.toString()));


                    long amount = Math.min(natureBlocks.size(), MAX_CONTRIBUTING_BLOCK_COUNT);
                    double chance = BONUS_CHANCE * amount;

                    if(r.nextDouble() <= chance){
                        BlockPos block = natureBlocks.get(r.nextInt(natureBlocks.size()));
                        //world.setBlockState(block, Blocks.DIAMOND_BLOCK.getDefaultState());
                        executeBonus(block, player, world);

            }
        }
    }

    private List<BlockPos> filterSurroundingBlocks(World world, PlayerEntity player) {
        List<BlockPos> natureBlocks =
                BlockPos.stream(player.getBoundingBox().expand(BONUS_RADIUS).offset(0,1,1))
                .filter(pos -> isInSphericalRange(pos, player.getBlockPos(), BONUS_RADIUS))
                .filter(pos -> isInTags(world, pos))
                .map(BlockPos::toImmutable)
                .toList();
        //old way
        //.forEach(pos -> natureBlocks.add(pos.toImmutable()));
        return natureBlocks;
    }

    private void executeBonus(BlockPos emanatingBlock, PlayerEntity player, World world) {
        //player.sendMessage(Text.of("executing bonus"));
        Vec3d blockPos = emanatingBlock.toCenterPos();
        Vec3d connection = player.getPos().add(0,1,0).subtract(blockPos);
        //player.sendMessage(Text.of(String.valueOf(connection)));
        if(world instanceof ServerWorld serverWorld){
            for (int i = 0; i < 25; i++) {
                Vec3d current = blockPos.add(connection.multiply((double) i / 25));
                serverWorld.spawnParticles(ModParticles.NATURE_PARTICLE, current.getX(), current.getY(), current.getZ(), 1, .35,-.35,.35, 1.25);
                if(i % 10 == 0) serverWorld.spawnParticles(ParticleTypes.HEART, current.getX(), current.getY(), current.getZ(), 1, .15,-.15,.15, .75);
            }
        }
        StatusEffectInstance regeneration = new StatusEffectInstance(StatusEffects.REGENERATION, BONUS_INTERVAL, 1, false, false, false);
        player.addStatusEffect(regeneration);
    }

    private boolean isInTags(World world, BlockPos pos) {
        BlockState s = world.getBlockState(pos);
        return (s.isIn(BlockTags.LOGS) || s.isIn(BlockTags.LEAVES) || s.isIn(BlockTags.CROPS) || s.isIn(BlockTags.FLOWERS));
    }

    private boolean isInSphericalRange(BlockPos blockPos, BlockPos playerPos, int radius) {
        int dx = blockPos.getX() - playerPos.getX();
        int dy = blockPos.getY() - playerPos.getY();
        int dz = blockPos.getZ() - playerPos.getZ();
        double dSquared = dx * dx + dy * dy + dz * dz;
        return dSquared <= radius * radius;
    }

    private void handleSpeedIncrease(World world, PlayerEntity player) {
        if (!world.isClient()) {
            RegistryEntry<Biome> biome = world.getBiome(player.getBlockPos());
            if (previous_biome == null ||!previous_biome.equals(biome)) {
                previous_biome = biome;
                if (biome.isIn(ConventionalBiomeTags.IS_FOREST)
                        || biome.isIn(ConventionalBiomeTags.IS_JUNGLE)
                        || biome.isIn(ConventionalBiomeTags.IS_VEGETATION_DENSE)) {

                    //player.sendMessage(Text.of("adding effect"));
                    player.getAttributes().addTemporaryModifiers(forestSpeedModifier);
                } else {
                    purgeEffects(player);
                }
            }
        }
    }

    @Override
    public void purgeEffects(LivingEntity entity) {
        entity.getAttributes().removeModifiers(forestSpeedModifier);
        previous_biome = null;
    }
}
