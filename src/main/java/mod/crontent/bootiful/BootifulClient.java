package mod.crontent.bootiful;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.GlowParticle;

public class BootifulClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.NATURE_PARTICLE, GlowParticle.ScrapeFactory::new);
    }
}
