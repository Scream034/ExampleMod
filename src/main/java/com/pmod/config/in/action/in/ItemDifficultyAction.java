package com.pmod.config.in.action.in;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import com.pmod.Main;

public final class ItemDifficultyAction extends BaseDifficultyAction<String> {
	public String slot;
	public int count = 1;
	public Integer customDamage;

	// For GSON deserialization
	public ItemDifficultyAction() {
		super(null, null);
		type = Type.ITEM;
	}

	@Override
	public boolean apply(final LivingEntity entity, final float difficulty) {
		ResourceLocation itemLocation = ResourceLocation.parse(this.value);
		var xItem = BuiltInRegistries.ITEM.get(itemLocation);

		if (xItem.isEmpty()) {
			Main.Log.warn("Failed to get item from registry by name: {}", this.value);
			return false;
		}

		ItemStack itemStack = new ItemStack(xItem.get(), this.count);

		try {
			EquipmentSlot equipmentSlot = EquipmentSlot.valueOf(this.slot.toUpperCase());
			entity.setItemSlot(equipmentSlot, itemStack);
		} catch (IllegalArgumentException e) {
			Main.Log.warn("Failed to get EquipmentSlot from string: {}", this.slot);
			return false;
		}

		return false;
	}

	@Override
	public String toString() {
		return super.toString() + String.format("slot=%s, count=%d", slot, count);
	}
}