package net.aquatic.johnmod.entity;

import net.minecraft.block.*;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.*;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.structure.StructureKeys;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import static net.aquatic.johnmod.JohnMod.*;
import static net.minecraft.world.gen.structure.StructureKeys.VILLAGE_DESERT;

public class JohnEntity extends PathAwareEntity implements GeoEntity , Monster {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public JohnEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
    }

    public static DefaultAttributeContainer johnAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 50).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6).add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.4f).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5).build();
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

    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    protected SoundEvent getAmbientSound() {
        return JOHN_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return JOHN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return  JOHN_DEATH;
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
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        StructureAccessor structureAccessor = this.getServer().getOverworld().getStructureAccessor();
        int size = 50;
        for(int x = -size; x<= size; x++){
            for(int z = -size; z<= size; z++){
                BlockPos spawnPos = new BlockPos(x + this.getBlockPos().getX(), this.getY(), z + this.getBlockPos().getZ());
                if(!structureAccessor.getStructureContaining(spawnPos, VILLAGE_DESERT).isNeverReferenced()) {
                    return false;
                }
                break;
            }
            break;
        }

        return super.canSpawn(world, spawnReason);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if(spawnReason == SpawnReason.NATURAL){
            if(world.getServer() != null) {
                StructureAccessor structureAccessor = this.getServer().getOverworld().getStructureAccessor();
                if(structureAccessor.getStructureContaining(getBlockPos(), VILLAGE_DESERT).getStructure() != null) {
                    int size = 50;
                    for (int x = -size; x <= size; x++) {
                        for (int z = -size; z <= size; z++) {
                            BlockPos spawnPos = new BlockPos(x + this.getBlockPos().getX(), this.getY(), z + this.getBlockPos().getZ());
                            if(structureAccessor.getStructureContaining(spawnPos, VILLAGE_DESERT).getStructure() != null) {
                                this.remove(RemovalReason.CHANGED_DIMENSION);
                            }
                        }
                    }
                }
            }
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
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
                this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15f);
            }else{
                JohnHitTime = 0;
                hasBenHit = false;
                this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5f);
            }
        }
        super.tick();
    }

    @Override
    public boolean tryAttack(Entity target) {
        if(target instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 45, 1));
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
