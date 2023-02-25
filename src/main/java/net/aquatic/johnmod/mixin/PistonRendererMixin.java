package net.aquatic.johnmod.mixin;

import net.aquatic.johnmod.JohnMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.PistonBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.aquatic.johnmod.blocks.ImmortalityBlock.RENDERER;
import static net.minecraft.client.render.RenderLayer.getGlint;


@Mixin(PistonBlockEntityRenderer.class)
public abstract class PistonRendererMixin implements BlockEntityRenderer<PistonBlockEntity> {
	@Shadow(aliases = "manager")
	BlockRenderManager manager;
	public  VertexConsumer getBlockGlint(VertexConsumerProvider provider) {
		return VertexConsumers.union(provider.getBuffer(getGlint()), provider.getBuffer(RenderLayers.getBlockLayer(JohnMod.IMMORTALITY_BLOCK.getDefaultState().with(RENDERER, true))));
	}
	@Inject(method = "renderModel", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/block/BlockRenderManager;getModelRenderer()Lnet/minecraft/client/render/block/BlockModelRenderer;"), cancellable = true)
	public void render(BlockPos pos, BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, boolean cull, int overlay, CallbackInfo ci) {
		if(state.isOf(JohnMod.IMMORTALITY_BLOCK)) {
			BlockState glint = JohnMod.IMMORTALITY_BLOCK.getDefaultState().with(RENDERER, true);
			manager.renderBlock(glint, pos, world, matrices, getBlockGlint(vertexConsumers), false, world.random);
		}
	}
}
