package net.aquatic.johnmod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
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
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import static net.aquatic.johnmod.JohnMod.*;

public class BabyJohnEntity extends PathAwareEntity implements GeoEntity , Monster {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public BabyJohnEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
    }

    public static DefaultAttributeContainer johnAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3).add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.4f).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5).build();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, VillagerEntity.class, false));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));

    }

    protected SoundEvent getAmbientSound() {
        return BABY_JOHN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return  BABY_JOHN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return  BABY_JOHN_DEATH;
    }

    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenPlay("animation.John.walk");
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("animation.John.attack");
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("animation.John.idle");

    int JohnMaxReact = 80;
    int JohnHitTime;
    boolean hasBenHit;

    @Override
    public boolean damage(DamageSource source, float amount) {
        hasBenHit = true;
        return super.damage(source, amount);
    }

    @Override
    public void tick() {
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            if(!world.isClient) {
                this.remove(RemovalReason.CHANGED_DIMENSION);
            }
        }


        if(hasBenHit){
            if(JohnHitTime <= JohnMaxReact){
                JohnHitTime ++;
                this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25f);
            }else{
                JohnHitTime = 0;
                hasBenHit = false;
                this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.6f);
            }
        }
        super.tick();
    }

    @Override
    public boolean tryAttack(Entity target) {
        if(target instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 22, 0));
        }
        return super.tryAttack(target);
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "livingController", 0, event -> {
            if(handSwinging){
                event.getController().setAnimationSpeed(6);
                event.setAnimation(ATTACK_ANIM);
                if(event.getController().getAnimationState().equals(AnimationController.State.STOPPED)){
                    handSwinging = false;
                }
            }else {
                if (event.isMoving()) {
                    event.getController().setAnimationSpeed(this.speed + 4);
                    event.setAndContinue(WALK_ANIM);
                } else {
                    event.getController().setAnimationSpeed(1);
                    event.setAndContinue(IDLE_ANIM);

                }
            }
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public EntityGroup getGroup() {
        return JOHN_GROUP;
    }
}
