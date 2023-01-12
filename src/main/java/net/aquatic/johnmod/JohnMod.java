package net.aquatic.johnmod;

import net.aquatic.johnmod.entity.JohnEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

import static net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder.*;

public class JohnMod implements ModInitializer {
	public static final String MODID = "johnmod";
	public static final Logger LOGGER = LoggerFactory.getLogger("MODID");
	public static final SoundEvent JOHN_AMBIENT = SoundEvent.of(new Identifier(MODID, "john_ambient"));
	public static final SoundEvent JOHN_DEATH = SoundEvent.of(new Identifier(MODID, "john_death"));
	public static final SoundEvent JOHN_HURT = SoundEvent.of(new Identifier(MODID, "john_hurt"));
	public static final SoundEvent BABY_JOHN_AMBIENT = SoundEvent.of(new Identifier(MODID, "baby_john_ambient"));
	public static final SoundEvent BABY_JOHN_DEATH = SoundEvent.of(new Identifier(MODID, "baby_john_death"));
	public static final SoundEvent BABY_JOHN_HURT = SoundEvent.of(new Identifier(MODID, "baby_john_hurt"));

	public static final EntityType<JohnEntity> JOHN_ENTITY = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(MODID, "john_entity"), create(SpawnGroup.CREATURE, JohnEntity::new).dimensions(EntityDimensions.fixed(1, 3)).build()
	);

	@Override
	public void onInitialize() {
		GeckoLib.initialize();
		FabricDefaultAttributeRegistry.register(JOHN_ENTITY, JohnEntity.johnAttributes());
	}
}
