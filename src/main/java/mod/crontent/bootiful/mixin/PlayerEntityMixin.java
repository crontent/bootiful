package mod.crontent.bootiful.mixin;

import mod.crontent.bootiful.ModAttributes;
import mod.crontent.bootiful.interfaces.IAirspeedChange;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow @Final private PlayerAbilities abilities;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    /**
     * Modify Air speed for all Boots with interface IAirspeedChange
     */
    @Inject(method = "getOffGroundSpeed", at = @At(value = "RETURN"), cancellable = true)
    private void getOffGroundSpeed(CallbackInfoReturnable<Float> cir) {
        Item boots = this.getEquippedStack(EquipmentSlot.FEET).getItem();

        if(boots instanceof IAirspeedChange b){
            if(!(this.abilities.flying && !this.hasVehicle())){
                cir.setReturnValue( b.getAirspeedModifier() * (this.isSprinting() ? 0.025999999F : 0.02F));
            }
        }
    }

    /**
     * Generate Mod Attributes on Player
     */
    @Inject(method= "createPlayerAttributes", require = 1, allow = 1, at = @At(value = "RETURN"))
    private static void addAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir){
        cir.getReturnValue().add(ModAttributes.NATURE_DROP_CHANCE);
    }
}
