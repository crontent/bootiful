package mod.crontent.bootiful.config;

import io.wispforest.owo.config.annotation.*;

import java.util.*;

@Modmenu(modId = "bootiful")
@Config(name = "bootiful-config", wrapperName = "BootifulConfig")
@SuppressWarnings("unused")
public class ConfigModel {

    //CLOUD BOOTS
    @Nest
    @Expanded
    public CloudBootsConfig cloudBootsConfig = new CloudBootsConfig();

    public static class CloudBootsConfig {
        //public boolean useLegacyRecipe = false;
        @RestartRequired
        public boolean doesFallDamageHurt = true;
        @RestartRequired
        public boolean doesFallDamageRemoveCondition = true;
    }


    //SPIKE BOOTS
    @Nest
    @Expanded
    public SpikeBootsConfig spikeBootsConfig = new SpikeBootsConfig();

    public static class SpikeBootsConfig {
        @RangeConstraint(min = 0f, max = 100f)
        public float stompDamage = 5f;
    }


    //FOREST BOOTS
    @Nest
    @Expanded
    public ForestBootsConfig forestBootsConfig = new ForestBootsConfig();

    public static class ForestBootsConfig{
        @RestartRequired
        public float fairyDustDropChance = 0.005f;

        @RestartRequired
        public double bonusLootMultiplier = 1.5f;

        @RestartRequired
        @RangeConstraint(min = 0f, max = 1f)
        public double healBonusChance = .25f;
        @RestartRequired
        @RangeConstraint(min = 0, max = 5)
        public int healBonusRadius = 4;
        @RestartRequired
        public long maxContributingBlockCount = 13L;
        @RestartRequired
        public double forestSpeedMultiplier = .25f;

        @RestartRequired
        public List<String> bonusChanceItems = new ArrayList<>(Arrays.asList(
                "minecraft:oak_sapling",
                "minecraft:spruce_sapling",
                "minecraft:birch_sapling",
                "minecraft:jungle_sapling",
                "minecraft:acacia_sapling",
                "minecraft:cherry_sapling",
                "minecraft:dark_oak_sapling",
                "minecraft:mangrove_propagule",
                "minecraft:dandelion",
                "minecraft:poppy",
                "minecraft:blue_orchid",
                "minecraft:allium",
                "minecraft:azure_bluet",
                "minecraft:red_tulip",
                "minecraft:orange_tulip",
                "minecraft:white_tulip",
                "minecraft:pink_tulip",
                "minecraft:oxeye_daisy",
                "minecraft:cornflower",
                "minecraft:lily_of_the_valley",
                "minecraft:wither_rose",
                "minecraft:torchflower",
                "minecraft:brown_mushroom",
                "minecraft:red_mushroom",
                "minecraft:crimson_fungus",
                "minecraft:warped_fungus",
                "minecraft:crimson_roots",
                "minecraft:warped_roots",
                "minecraft:nether_sprouts",
                "minecraft:weeping_vines",
                "minecraft:twisting_vines",
                "minecraft:moss_carpet",
                "minecraft:pink_petals",
                "minecraft:moss_block",
                "minecraft:hanging_roots",
                "minecraft:big_dripleaf",
                "minecraft:small_dripleaf",
                "minecraft:bamboo",
                "minecraft:mycelium",
                "minecraft:lilac",
                "minecraft:rose_bush",
                "minecraft:peony",
                "minecraft:tall_grass",
                "minecraft:large_fern",
                "minecraft:hay_block",
                "minecraft:wheat_seeds",
                "minecraft:wheat",
                "minecraft:cocoa_beans",
                "minecraft:carrot",
                "minecraft:potato",
                "minecraft:beetroot_seeds",
                "minecraft:beetroot",
                "minecraft:pumpkin",
                "minecraft:carved_pumpkin",
                "minecraft:melon",
                "minecraft:melon_seeds",
                "minecraft:apple"
                //MIGHT INSERT STUFF LIKE TWILIGHT FOREST HERE
        ));
    }

}
