package net.aquatic.johnmod.level;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

import static net.aquatic.johnmod.JohnMod.BABY_JOHN_ENTITY;
import static net.aquatic.johnmod.JohnMod.JOHN_ENTITY;

public class EntitySpawns {
    public static void addEntitySpawn(){
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.DESERT, BiomeKeys.SWAMP, BiomeKeys.MANGROVE_SWAMP), SpawnGroup.MONSTER,
                JOHN_ENTITY, 3, 1, 3);

        SpawnRestriction.register(JOHN_ENTITY, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);


        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.DESERT, BiomeKeys.SWAMP, BiomeKeys.MANGROVE_SWAMP), SpawnGroup.MONSTER,
                BABY_JOHN_ENTITY, 5, 3, 8);

        SpawnRestriction.register(BABY_JOHN_ENTITY, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
    }
}
