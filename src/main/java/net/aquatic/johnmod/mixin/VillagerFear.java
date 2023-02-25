package net.aquatic.johnmod.mixin;

import com.google.common.collect.ImmutableMap;
import net.aquatic.johnmod.entity.BabyJohnEntity;
import net.aquatic.johnmod.entity.JohnEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.NearestVisibleLivingEntitySensor;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public abstract class VillagerFear extends NearestVisibleLivingEntitySensor {

    @Inject(method = "matches", at = @At(value = "HEAD"), cancellable = true)
    private void matches(LivingEntity entity, LivingEntity target, CallbackInfoReturnable<Boolean> bool) {
        if(target instanceof JohnEntity || target instanceof BabyJohnEntity &&  this.isCloseEnoughForDanger(entity, target)){
            bool.setReturnValue(true);
        }
    }

    private boolean isCloseEnoughForDanger(LivingEntity villager, LivingEntity target) {
        return target.squaredDistanceTo(villager) <= 95;
    }

}
