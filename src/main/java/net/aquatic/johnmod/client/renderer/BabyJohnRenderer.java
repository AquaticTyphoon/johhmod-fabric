package net.aquatic.johnmod.client.renderer;

import net.aquatic.johnmod.client.models.BabyJohnModel;
import net.aquatic.johnmod.entity.BabyJohnEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BabyJohnRenderer extends GeoEntityRenderer<BabyJohnEntity> {
    public BabyJohnRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new BabyJohnModel());
    }

}
