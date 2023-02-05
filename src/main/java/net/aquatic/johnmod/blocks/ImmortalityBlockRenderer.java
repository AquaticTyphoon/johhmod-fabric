package net.aquatic.johnmod.blocks;

import net.aquatic.johnmod.JohnMod;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

import static net.aquatic.johnmod.blocks.ImmortalityBlock.RENDERER;
import static net.minecraft.client.render.RenderLayer.*;

public class ImmortalityBlockRenderer implements BlockEntityRenderer<ImmortalBlockEntity> {
    BlockEntityRendererFactory.Context contextUsed;

    public ImmortalityBlockRenderer(BlockEntityRendererFactory.Context context) {
        contextUsed = context;
    }
    public static VertexConsumer getBlockGlint(VertexConsumerProvider provider) {
        return VertexConsumers.union(provider.getBuffer(getGlint()), provider.getBuffer(RenderLayers.getBlockLayer(JohnMod.IMMORTALITY_BLOCK.getDefaultState().with(RENDERER, true))));
    }

    public void render(ImmortalBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexProvider, int light, int overlay) {
        MatrixStack glowStack = matrices;
        contextUsed.getRenderManager().renderBlock(JohnMod.IMMORTALITY_BLOCK.getDefaultState().with(RENDERER, true), blockEntity.getPos(), blockEntity.getWorld(), glowStack, getBlockGlint(vertexProvider), false, blockEntity.getWorld().random);
    }
}
