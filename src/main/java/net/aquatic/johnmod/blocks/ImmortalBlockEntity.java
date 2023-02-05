package net.aquatic.johnmod.blocks;

import net.aquatic.johnmod.JohnMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ImmortalBlockEntity extends BlockEntity {
    public ImmortalBlockEntity(BlockPos pos, BlockState state) {
        super(JohnMod.IMMORTALITY_BLOCK_ENTITY, pos, state);
    }



}
