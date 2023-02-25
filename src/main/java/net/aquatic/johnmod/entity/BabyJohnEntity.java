package net.aquatic.johnmod.entity;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.gen.StructureAccessor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Iterator;
import java.util.function.Predicate;

import static net.aquatic.johnmod.JohnMod.*;
import static net.minecraft.world.gen.structure.StructureKeys.VILLAGE_DESERT;

public class BabyJohnEntity extends PathAwareEntity implements GeoEntity , Monster {

    private final BreakDoorGoal breakDoorsGoal;
    private boolean canBreakDoors;

    private static final Predicate<Difficulty> DOOR_BREAK_DIFFICULTY_CHECKER;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public BabyJohnEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.breakDoorsGoal = new BreakDoorGoal(this, DOOR_BREAK_DIFFICULTY_CHECKER);
    }

    static {
        DOOR_BREAK_DIFFICULTY_CHECKER = (difficulty) -> {
            return difficulty == Difficulty.HARD;
        };
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
        this.goalSelector.add(6, new MoveThroughVillageGoal(this, 1.0, true, 4, this::getCanBreakDoors));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    public boolean getCanBreakDoors() {
        return this.canBreakDoors;
    }

    public void setCanBreakDoors(boolean canBreakDoors) {
        if (this.shouldBreakDoors() && NavigationConditions.hasMobNavigation(this)) {
            if (this.canBreakDoors != canBreakDoors) {
                this.canBreakDoors = canBreakDoors;
                ((MobNavigation)this.getNavigation()).setCanPathThroughDoors(canBreakDoors);
                if (canBreakDoors) {
                    handSwinging = true;
                    this.goalSelector.add(1, this.breakDoorsGoal);
                } else {
                    handSwinging = false;
                    this.goalSelector.remove(this.breakDoorsGoal);
                }
            }
        } else if (this.canBreakDoors) {
            this.goalSelector.remove(this.breakDoorsGoal);
            this.canBreakDoors = false;
        }
    }

    protected boolean shouldBreakDoors() {
        return true;
    }


    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
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

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("CanBreakDoors", this.getCanBreakDoors());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setCanBreakDoors(nbt.getBoolean("CanBreakDoors"));
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


    boolean dontSpawn;
    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        if(getServer() != null ) {
            StructureAccessor structureAccessor = this.getServer().getOverworld().getStructureAccessor();
            if (spawnReason != null && spawnReason == SpawnReason.NATURAL) {
                int size = 200;
                outer:
                for (int x = -size; x <= size; x++) {
                    for (int z = -size; z <= size; z++) {
                        BlockPos spawnCheck = new BlockPos(this.getX() + x, this.getY() - 1, this.getZ() + z);
                        if (structureAccessor.getStructureContaining(spawnCheck, VILLAGE_DESERT).getStructure() != null) {
                            dontSpawn = true;
                            break outer;
                        }
                    }
                }
            }
        }
        if(dontSpawn){return false;}
        return super.canSpawn(world, spawnReason);
    }


    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.setCanBreakDoors(this.shouldBreakDoors());
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void tick() {
        if( world.getDifficulty() == Difficulty.PEACEFUL){
            remove(RemovalReason.DISCARDED);
        }
        if (hasBenHit) {
            if (JohnHitTime <= JohnMaxReact) {
                JohnHitTime++;
                this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25f);
            } else {
                JohnHitTime = 0;
                hasBenHit = false;
                getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.6f);
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

    @Override
    protected void onKilledBy(@Nullable LivingEntity adversary) {
        if(adversary instanceof ServerPlayerEntity player){
            if(!player.world.isClient) {
                Advancement killMonster = player.server.getAdvancementLoader().get(new Identifier("minecraft:adventure/kill_a_mob"));
                AdvancementProgress progress = player.getAdvancementTracker().getProgress(killMonster);
                Iterator iterator = progress.getUnobtainedCriteria().iterator();
                if (!progress.isDone()) {
                    while(iterator.hasNext()) {
                        String string = (String)iterator.next();
                        player.getAdvancementTracker().grantCriterion(killMonster, string);
                    }
                }
            }
        }
        super.onKilledBy(adversary);
    }
}
