package mod.crontent.bootiful.events;

import mod.crontent.bootiful.ModBoots;
import mod.crontent.bootiful.ModItems;
import mod.crontent.bootiful.boots.SpikeBootsItem;
import mod.crontent.bootiful.interfaces.PoolConditionsModifyHelper;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.ActionResult;
import java.util.ArrayList;

import static mod.crontent.bootiful.Bootiful.CONFIG;
import static mod.crontent.bootiful.Bootiful.LOGGER;


public class ModEventListeners {

    public static void initialize(){

        EntityLandingCallback.EVENT.register((entity ->
        {
            if (entity instanceof PlayerEntity player && !entity.getWorld().isClient()) {
                if (player.getEquippedStack(EquipmentSlot.FEET).isOf(ModBoots.SPIKE_BOOTS) && player.isSneaking()){
                    ((SpikeBootsItem) player.getEquippedStack(EquipmentSlot.FEET).getItem()).handleSpikeAttack(player);
                }
            }
            return ActionResult.PASS;
        }));



        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin()) {
                if (key.getValue().getPath().startsWith("blocks") || key.getValue().getPath().startsWith("entities")) {
                    LOGGER.debug(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
                    LOGGER.debug("Processing Loot Table: {}", key.getValue());
                    LOGGER.debug("___________________________________________________________");
                    tableBuilder.modifyPools(original -> {
                        PoolConditionsModifyHelper.wrapCondition(original, (ArrayList<String>) CONFIG.forestBootsConfig.bonusChanceItems());
                    });
                }
                if(Blocks.DARK_OAK_LEAVES.getLootTableKey().equals(key)){
                    LootPool.Builder poolBuilder = LootPool.builder().with(ItemEntry.builder(ModItems.FAIRY_DUST)).conditionally(RandomChanceLootCondition.builder(CONFIG.forestBootsConfig.fairyDustDropChance()));
                    tableBuilder.pool(poolBuilder);
                }
            }
        });
    }
}
