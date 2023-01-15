package net.aquatic.johnmod;

import net.aquatic.johnmod.enchantment.Blood_Lust;
import net.aquatic.johnmod.entity.BabyJohnEntity;
import net.aquatic.johnmod.entity.JohnEntity;
import net.aquatic.johnmod.items.ImmortalBlade;
import net.aquatic.johnmod.items.ImmortalMaterial;
import net.aquatic.johnmod.items.ImmortalityItem;
import net.aquatic.johnmod.level.EntitySpawns;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.*;
import net.minecraft.item.*;
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

	public static final EntityGroup JOHN_GROUP = new EntityGroup();

	public static final EntityType<JohnEntity> JOHN_ENTITY = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(MODID, "john"), create(SpawnGroup.CREATURE, JohnEntity::new).dimensions(EntityDimensions.fixed(1, 3)).fireImmune().build()
	);
	public static final EntityType<BabyJohnEntity> BABY_JOHN_ENTITY = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier(MODID, "baby_john"), create(SpawnGroup.CREATURE, BabyJohnEntity::new).dimensions(EntityDimensions.fixed(1, 3)).fireImmune().build()
	);
	public static final Item JOHN_EGG = new SpawnEggItem(JOHN_ENTITY, 0x65895d, 0x422c2c, new FabricItemSettings());
	public static final Item BABY_JOHN_EGG = new SpawnEggItem(BABY_JOHN_ENTITY, 0x74a269, 0x422c2c, new FabricItemSettings());

	public static final Item IMMORTALITY_SHARD = new ImmortalityItem(new FabricItemSettings());
	public static final Item IMMORTALITY_GEM = new ImmortalityItem(new FabricItemSettings());

	private static final ToolMaterial ImmortalMaterial = new ImmortalMaterial();
	public static final Item IMMORTAL_BLADE = new ImmortalBlade(ImmortalMaterial,4, 9.7f , new FabricItemSettings());
	private static final ItemGroup JOHN_MOD_ITEMS = FabricItemGroup.builder(new Identifier(MODID, "items"))
			.icon(() -> new ItemStack(JOHN_EGG))
			.build();
	public static final Enchantment BLOOD_LUST = new Blood_Lust(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});

	@Override
	public void onInitialize() {
		GeckoLib.initialize();
		FabricDefaultAttributeRegistry.register(JOHN_ENTITY, JohnEntity.johnAttributes());
		Registry.register(Registries.ITEM, new Identifier(MODID, "john_spawn_egg"), JOHN_EGG);
		ItemGroupEvents.modifyEntriesEvent(JOHN_MOD_ITEMS).register(entries -> entries.add(JOHN_EGG));
		FabricDefaultAttributeRegistry.register(BABY_JOHN_ENTITY, BabyJohnEntity.johnAttributes());
		Registry.register(Registries.ITEM, new Identifier(MODID, "baby_john_spawn_egg"), BABY_JOHN_EGG);
		ItemGroupEvents.modifyEntriesEvent(JOHN_MOD_ITEMS).register(entries -> entries.add(BABY_JOHN_EGG));

		Registry.register(Registries.ITEM, new Identifier(MODID, "immortality_shard"), IMMORTALITY_SHARD);
		Registry.register(Registries.ITEM, new Identifier(MODID, "immortality_gem"), IMMORTALITY_GEM);
		Registry.register(Registries.ITEM, new Identifier(MODID, "immortal_blade"), IMMORTAL_BLADE);
		ItemGroupEvents.modifyEntriesEvent(JOHN_MOD_ITEMS).register(entries -> entries.add(IMMORTALITY_SHARD));
		ItemGroupEvents.modifyEntriesEvent(JOHN_MOD_ITEMS).register(entries -> entries.add(IMMORTALITY_GEM));
		ItemGroupEvents.modifyEntriesEvent(JOHN_MOD_ITEMS).register(entries -> entries.add(IMMORTAL_BLADE));

		Registry.register(Registries.ENCHANTMENT, new Identifier(MODID, "blood_lust"), BLOOD_LUST);
		EntitySpawns.addEntitySpawn();
	}
}
