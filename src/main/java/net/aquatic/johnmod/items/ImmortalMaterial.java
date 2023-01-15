package net.aquatic.johnmod.items;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class ImmortalMaterial implements ToolMaterial {
    @Override
    public int getDurability() {
        return 2143;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 9.7f;
    }

    @Override
    public float getAttackDamage() {
        return 4;
    }

    @Override
    public int getMiningLevel() {
        return 5;
    }

    @Override
    public int getEnchantability() {
        return 10;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.getRepairIngredient();
    }
}
