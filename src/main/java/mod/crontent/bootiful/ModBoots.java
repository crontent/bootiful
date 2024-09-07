package mod.crontent.bootiful;

import mod.crontent.bootiful.boots.CloudBootsItem;
import mod.crontent.bootiful.boots.ForestBootsItem;
import mod.crontent.bootiful.boots.SpikeBootsItem;
import mod.crontent.bootiful.config.BootifulConfig;
import mod.crontent.bootiful.interfaces.IStatusEffectEquipable;
import mod.crontent.bootiful.interfaces.IStatusEffectPurgable;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBoots {

    private static final BootifulConfig.CloudBootsConfig cc = Bootiful.CONFIG.cloudBootsConfig;
    public static Item CLOUD_BOOTS = register("cloud_boots", new CloudBootsItem(ModArmorMaterials.CLOUD_MATERIAL, 20, 1, cc.doesFallDamageHurt(), cc.doesFallDamageRemoveCondition()));

    //private static final  BootifulConfig.SpikeBootsConfig sc = Bootiful.CONFIG.spikeBootsConfig;
    public static Item SPIKE_BOOTS = register("spike_boots", new SpikeBootsItem(ModArmorMaterials.SPIKE_MATERIAL, 30));

    private static final BootifulConfig.ForestBootsConfig fc = Bootiful.CONFIG.forestBootsConfig;
    public static Item FOREST_BOOTS = register("forest_boots", new ForestBootsItem(ModArmorMaterials.FOREST_MATERIAL,
            17,
            fc.healBonusChance(),
            fc.healBonusRadius(),
            fc.maxContributingBlockCount(),
            fc.forestSpeedMultiplier(),
            fc.bonusLootMultiplier()));

    public static Item register(String id, Item entry){
        return Registry.register(Registries.ITEM,
                Identifier.of(Bootiful.MOD_ID, id),
                entry);
    }



    public static void initialize() {
        Bootiful.LOGGER.info("Registering " + Bootiful.MOD_ID + " Boot items");

        registerCreativeTabs();

        registerStatusEffectEvents();
        registerFallDamageListener();


    }

    private static void registerCreativeTabs() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> {
            itemGroup.add(CLOUD_BOOTS);
            itemGroup.add(SPIKE_BOOTS);
            itemGroup.add(FOREST_BOOTS);
        });
    }

    private static void registerFallDamageListener() {
        //Init fall damage event listener for Cloud boots
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(((entity, source, amount) -> {
            if (entity instanceof PlayerEntity player) {
                if(source.getTypeRegistryEntry().matchesKey(DamageTypes.FALL)){
                    ItemStack item = player.getEquippedStack(EquipmentSlot.FEET);
                    if(item.isOf(CLOUD_BOOTS)){
                        item.damage((int) (amount * ((CloudBootsItem) CLOUD_BOOTS).getFallDurabilityPenalty()), player, EquipmentSlot.FEET);
                    }
                }
            }
            return true;
        }));
    }

    private static void registerStatusEffectEvents() {
        //Purge and Add Status effects automatically
        ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previousStack, currentStack) -> {
            if (livingEntity instanceof PlayerEntity playerEntity) {
                if (previousStack.getItem() instanceof IStatusEffectPurgable purgableItem) {
                    purgableItem.purgeEffects(playerEntity);
                }
                if (currentStack.getItem() instanceof IStatusEffectEquipable equipableItem) {
                    equipableItem.addEffects(playerEntity);
                }
            }
        });
    }
}
