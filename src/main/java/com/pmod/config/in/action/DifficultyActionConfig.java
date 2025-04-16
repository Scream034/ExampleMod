package com.pmod.config.in.action;

import java.util.List;

import com.pmod.config.in.action.in.BaseDifficultyAction;

public final class DifficultyActionConfig {
	public final float difficultyMultiplier;
	public final boolean use_random;
	public final List<BaseDifficultyAction<?>> actions;

	public DifficultyActionConfig(final float difficultyMultiplier, final boolean use_random,
			final List<BaseDifficultyAction<?>> actions) {
		this.difficultyMultiplier = difficultyMultiplier;
		this.use_random = use_random;
		this.actions = actions;
	}

	@Override
	public String toString() {
		return "DifficultyActionConfig [difficultyMultiplier=" + difficultyMultiplier + ", use_random=" + use_random
				+ ", actions=" + actions + "]";
	}
}