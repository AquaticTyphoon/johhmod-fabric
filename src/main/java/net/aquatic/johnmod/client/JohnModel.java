package net.aquatic.johnmod.client;

import net.aquatic.johnmod.JohnMod;
import net.aquatic.johnmod.entity.JohnEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class JohnModel extends GeoModel<JohnEntity> {
    @Override
    public Identifier getModelResource(JohnEntity object) {
        return new Identifier(JohnMod.MODID, "geo/john.geo.json");
    }

    @Override
    public Identifier getTextureResource(JohnEntity object) {
        return new Identifier(JohnMod.MODID, "textures/entity/john.png");
    }

    @Override
    public Identifier getAnimationResource(JohnEntity animatable) {
        return new Identifier(JohnMod.MODID, "animations/john.animation.json");
    }
}
