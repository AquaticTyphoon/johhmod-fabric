package net.aquatic.johnmod.items;

import net.aquatic.johnmod.JohnMod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class ImmortalBlade extends SwordItem {
    public ImmortalBlade(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        if(stack.getEnchantments().isEmpty()){
            stack.addEnchantment(JohnMod.BLOOD_LUST, 1);
        }
        return super.hasGlint(stack);
    }

}
