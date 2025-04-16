package com.pmod.config.in.action.in;

public final class ModifierDifficultyAction extends BaseDifficultyAction<Float> {
	public String attribute;
	public String operation;

	// For GSON deserialization
	public ModifierDifficultyAction() {
		super(null, null);
		type = Type.MODIFIER;
	}

	@Override
	public String toString() {
		return super.toString() + String.format("attribute=%s, operation=%s", attribute, operation);
	}
}