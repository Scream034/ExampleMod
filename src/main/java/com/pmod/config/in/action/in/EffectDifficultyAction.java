package com.pmod.config.in.action.in;

public final class EffectDifficultyAction extends BaseDifficultyAction<String> {
	public int amplifier;
	public int duration;

	// For GSON deserialization
	public EffectDifficultyAction() {
		super(null, null);
		type = Type.EFFECT;
	}

	@Override
	public String toString() {
		return super.toString() + String.format("amplifier=%s, duration=%s", this.amplifier, this.duration);
	}
}