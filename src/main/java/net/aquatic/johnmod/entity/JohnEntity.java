package net.aquatic.johnmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.world.World;

import static net.aquatic.johnmod.JohnMod.*;


public class JohnEntity extends PathAwareEntity implements GeoEntity{

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public JohnEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1F;
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
    }


    public static DefaultAttributeContainer johnAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 50).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6).add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.4f).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5).build();
    }


    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.targetSelector.add(1, new AttackGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, VillagerEntity.class, false));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));

    }

    protected SoundEvent getAmbientSound() {
        return JOHN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return JOHN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return JOHN_DEATH;
    }

    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenPlay("animation.John.walk");
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("animation.John.attack");
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("animation.John.idle");

    @Override
    public double getTick(Object entity) {
        if(playerHitTimer != 0){
            this.speed = 0.15f;
        }else{
            this.speed = 0.5f;
        }
        return GeoEntity.super.getTick(entity);
    }

    public boolean tryAttack(Entity pEntity) {
        if (pEntity instanceof LivingEntity target) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 45, 1));
        }
        return true;
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "livingController", 0, event -> {

            if(lastAttackedTicks > 0) {
                event.getController().setAnimationSpeed(6);
                return event.setAndContinue(ATTACK_ANIM);
            }else{
                if (event.isMoving()) {
                    event.getController().setAnimationSpeed(this.speed + 4);
                    return event.setAndContinue(WALK_ANIM);
                } else {
                    event.getController().setAnimationSpeed(1);
                    return event.setAndContinue(IDLE_ANIM);
                }
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

}
