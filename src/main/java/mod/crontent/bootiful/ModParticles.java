package mod.crontent.bootiful;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


public class ModParticles {


    public static final SimpleParticleType NATURE_PARTICLE = register("nature_particle", FabricParticleTypes.simple());

    public static SimpleParticleType register(String id,  SimpleParticleType particle){
        return Registry.register(Registries.PARTICLE_TYPE, Identifier.of(Bootiful.MOD_ID, id), particle);
    }

    public static void initialize() {
        Bootiful.LOGGER.info("Registering " + Bootiful.MOD_ID + " particules");

    }
}
