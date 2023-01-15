package net.aquatic.johnmod.client.renderer;

import net.aquatic.johnmod.client.models.JohnModel;
import net.aquatic.johnmod.entity.JohnEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class JohnRenderer extends GeoEntityRenderer<JohnEntity> {
    public JohnRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new JohnModel());
    }

}
