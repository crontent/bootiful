package mod.crontent.bootiful.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mod.crontent.bootiful.ModBoots;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyExpressionValue(method = "travel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
    private float getSlipperinessOrDefault(float original){
        if(((Object)this) instanceof PlayerEntity player){
            return player.getEquippedStack(EquipmentSlot.FEET).isOf(ModBoots.SPIKE_BOOTS) ? 0.6f : original;
        }
        return original;
    }
}
