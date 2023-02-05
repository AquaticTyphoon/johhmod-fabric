package net.aquatic.johnmod.blocks;

import net.aquatic.johnmod.JohnMod;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

import static net.minecraft.client.render.RenderLayer.getDirectEntityGlint;
import static net.minecraft.client.render.RenderLayer.getGlint;

public class ImmortalityBlockRenderer implements BlockEntityRenderer<ImmortalBlockEntity> {
    BlockEntityRendererFactory.Context contextUsed;
    public ImmortalityBlockRenderer(BlockEntityRendererFactory.Context context) {
        contextUsed = context;
    }
    public static VertexConsumer getDirectItemGlintConsumer(VertexConsumerProvider provider) {
        return VertexConsumers.union(provider.getBuffer(getGlint()));
    }
    @Override
    public void render(ImmortalBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexProvider, int light, int overlay) {
       MatrixStack glowStack = matrices;
       glowStack.scale(1.1f , 1.1f ,1.1f);
       glowStack.push();
       glowStack.pop();
       contextUsed.getRenderManager().renderBlock(blockEntity.getWorld().getBlockState(blockEntity.getPos()), blockEntity.getPos(), blockEntity.getWorld(), glowStack, getDirectItemGlintConsumer(vertexProvider), false, blockEntity.getWorld().random);
    }
}
