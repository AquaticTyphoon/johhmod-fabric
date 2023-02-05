package net.aquatic.johnmod.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class ImmortalityBlockItem extends BlockItem {
    public ImmortalityBlockItem(Block block, Settings settings) {
        super(block, settings);
    }
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
