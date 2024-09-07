package mod.crontent.bootiful.mixin;

import mod.crontent.bootiful.events.EntityLandingCallback;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityLandingEventMixin {

    @Inject(method = "fall", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/event/GameEvent$Emitter;)V"),
            cancellable = true)
    private void fall(CallbackInfo ci) {
        ActionResult result = EntityLandingCallback.EVENT.invoker().doAction((Entity)(Object)this);

        if (result == ActionResult.FAIL) {
            ci.cancel();
        }
    }
}
