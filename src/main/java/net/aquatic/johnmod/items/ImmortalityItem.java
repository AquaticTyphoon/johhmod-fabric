package net.aquatic.johnmod.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ImmortalityItem extends Item {
    public ImmortalityItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
