package mod.crontent.bootiful;

import mod.crontent.bootiful.boots.ModLootConditions;
import mod.crontent.bootiful.config.BootifulConfig;
import mod.crontent.bootiful.events.ModEventListeners;
import net.fabricmc.api.ModInitializer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Bootiful implements ModInitializer {

	public static final String MOD_ID = "bootiful";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final BootifulConfig CONFIG = BootifulConfig.createAndLoad();


	@Override
	public void onInitialize() {
		ModAttributes.initialize();
		ModArmorMaterials.initialize();
		ModItems.initialize();
		ModBoots.initialize();
		ModEventListeners.initialize();
		ModLootFunctions.initialize();
		ModLootConditions.initialize();
		ModParticles.initialize();





	}
}