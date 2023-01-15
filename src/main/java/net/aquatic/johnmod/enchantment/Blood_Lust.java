package net.aquatic.johnmod.enchantment;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.aquatic.johnmod.JohnMod.JOHN_GROUP;

public class Blood_Lust extends Enchantment {

    public Blood_Lust(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }
    @Override
    public boolean isCursed() {
        return true;
    }

    @Override
    public float getAttackDamage(int level, EntityGroup group) {
        if (group == JOHN_GROUP) {
            return 2;
        } else {
            return super.getAttackDamage(level, group);
        }
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if(target instanceof LivingEntity livingEntity){
            if(livingEntity.getGroup() == JOHN_GROUP || livingEntity instanceof ServerPlayerEntity){
                if(livingEntity.getHealth() - getAttackDamage(1, livingEntity.getGroup()) >= 0){
                    user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 60, 0));
                }else{
                    user.removeStatusEffect(StatusEffects.POISON);
                    user.heal(livingEntity.getMaxHealth() / 10f);
                }
            }else{
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 120, 1));
            }
        }
    }
}
