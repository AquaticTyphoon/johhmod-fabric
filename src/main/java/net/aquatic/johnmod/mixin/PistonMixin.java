package net.aquatic.johnmod.mixin;

import net.aquatic.johnmod.JohnMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBlock.class)
public abstract class PistonMixin extends FacingBlock {
	protected PistonMixin(Settings settings) {
		super(settings);
	}

	@Inject(method = "isMovable", at = @At(value = "HEAD"), cancellable = true)
	private static void isMovable(BlockState state, World world, BlockPos pos, Direction direction, boolean canBreak, Direction pistonDir, CallbackInfoReturnable<Boolean> info) {
		if (state.isOf(JohnMod.IMMORTALITY_BLOCK)) {
			if (!(direction == Direction.DOWN && pos.getY() == world.getBottomY() || direction == Direction.UP && pos.getY() == world.getTopY() - 1)) {
				info.setReturnValue(true);
			}
		}
	}

}
