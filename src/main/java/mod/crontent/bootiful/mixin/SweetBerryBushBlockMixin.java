package mod.crontent.bootiful.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.crontent.bootiful.ModBoots;
import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SweetBerryBushBlock.class)
public class SweetBerryBushBlockMixin {


    //the slowdown Handler probably works differently.
    @WrapOperation(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;slowMovement(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Vec3d;)V"))
    private void slowMovementHandler(Entity instance, BlockState state, Vec3d multiplier, Operation<Void> original){
        if (instance instanceof PlayerEntity player && player.getEquippedStack(EquipmentSlot.FEET).isOf(ModBoots.FOREST_BOOTS)) {
            original.call(instance, state, new Vec3d(1,1,1));
        }
        else{
            original.call(instance, state, multiplier);
        }
    }

    @WrapOperation(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean damageHandler(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
        if (instance instanceof PlayerEntity player && player.getEquippedStack(EquipmentSlot.FEET).isOf(ModBoots.FOREST_BOOTS)) {
            return false;
        } else {
            return original.call(instance, source, amount);
        }
    }
}
