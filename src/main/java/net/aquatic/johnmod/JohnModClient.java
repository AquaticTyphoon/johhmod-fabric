package net.aquatic.johnmod;

import net.aquatic.johnmod.client.renderer.BabyJohnRenderer;
import net.aquatic.johnmod.client.renderer.JohnRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import static net.aquatic.johnmod.JohnMod.BABY_JOHN_ENTITY;
import static net.aquatic.johnmod.JohnMod.JOHN_ENTITY;

public class JohnModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(JOHN_ENTITY, JohnRenderer::new);
        EntityRendererRegistry.register(BABY_JOHN_ENTITY, BabyJohnRenderer::new);
    }
}
