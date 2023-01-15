package net.aquatic.johnmod.client.models;

import net.aquatic.johnmod.JohnMod;
import net.aquatic.johnmod.entity.BabyJohnEntity;
import net.aquatic.johnmod.entity.JohnEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class BabyJohnModel extends GeoModel<BabyJohnEntity> {
    @Override
    public Identifier getModelResource(BabyJohnEntity object) {
        return new Identifier(JohnMod.MODID, "geo/baby_john.geo.json");
    }

    @Override
    public Identifier getTextureResource(BabyJohnEntity object) {
        return new Identifier(JohnMod.MODID, "textures/entity/baby_john.png");
    }

    @Override
    public Identifier getAnimationResource(BabyJohnEntity animatable) {
        return new Identifier(JohnMod.MODID, "animations/baby_john.animation.json");
    }
}
