package com.pmod.config.in.action;

import java.util.List;
import java.util.Random;

import com.pmod.Main;
import com.pmod.config.ServerConfig;
import com.pmod.config.in.action.in.BaseDifficultyAction;

import net.minecraft.world.entity.LivingEntity;

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

	@SuppressWarnings("null")
	public final void applyActions(final LivingEntity entity, final float difficulty) {
		if (!isDifficultyMultiplier(difficulty)) {
			return; // difficulty is too low
		}

		if (use_random) {
			float random = new Random().nextFloat();
			for (final BaseDifficultyAction<?> action : actions) {
				if (action.random != null && action.random < random) {
					action.apply(entity, difficulty);
				}
			}
		} else {
			for (final BaseDifficultyAction<?> action : actions) {
				action.apply(entity, difficulty);
			}
		}
	}

	public final boolean isDifficultyMultiplier(final float difficulty) {
		float newDifficulty = ServerConfig.baseDifficulty * difficultyMultiplier;
		Main.Log.info("difficultyMultiplier: {} ({})", newDifficulty, difficulty);
		return difficulty >= newDifficulty;
	}

	@Override
	public String toString() {
		return "DifficultyActionConfig [difficultyMultiplier=" + difficultyMultiplier + ", use_random=" + use_random
				+ ", actions=" + actions + "]";
	}
}