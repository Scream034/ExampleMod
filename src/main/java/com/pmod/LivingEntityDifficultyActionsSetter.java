package com.pmod;

import java.util.ArrayList;
import java.util.List;

import com.pmod.config.in.action.DifficultyActionConfig;

import net.minecraft.world.entity.LivingEntity;

public final class LivingEntityDifficultyActionsSetter {
	public final List<DifficultyActionConfig> configs = new ArrayList<>();

	public LivingEntityDifficultyActionsSetter(List<DifficultyActionConfig> configs) {
		this.configs.addAll(configs);
	}

	public void apply(LivingEntity entity, float difficulty) {
		for (DifficultyActionConfig config : configs) {
			config.applyActions(entity, difficulty);
		}
	}
}
