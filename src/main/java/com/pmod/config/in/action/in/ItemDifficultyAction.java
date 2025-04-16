package com.pmod.config.in.action.in;

import java.util.List;

public final class ItemDifficultyAction extends BaseDifficultyAction<String> {
	public String slot;
	public int count;
	public List<String> enchantments;

	// For GSON deserialization
	public ItemDifficultyAction() {
		super(null, null);
		type = Type.ITEM;
	}

	@Override
	public String toString() {
		return super.toString() + String.format("slot=%s, count=%d, enchantments=%s", slot, count, enchantments);
	}
}
