package com.pmod.config.in.action.in;

import com.pmod.Main;
import com.pmod.list.RandomValueFromList;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public final class EffectDifficultyAction extends BaseDifficultyAction<String> {
	public RandomValueFromList<Integer> amplifier = new RandomValueFromList<>(1, 0.8f, 3, 0.4f, 8, 0.25f);
	public RandomValueFromList<Integer> duration = new RandomValueFromList<>(1200, 0.25f, 3600, 0.4f, 8192, 0.25f); // 1 minute

	// For GSON deserialization
	public EffectDifficultyAction() {
		super(null, null);
		type = Type.EFFECT;
	}

	@Override
	public boolean apply(final LivingEntity entity, final float difficulty) {
		ResourceLocation effectLocation = ResourceLocation.parse(this.value);
		var effect = BuiltInRegistries.MOB_EFFECT.get(effectLocation);

		if (effect.isPresent()) {
			MobEffectInstance effectInstance = new MobEffectInstance(effect.get(), this.duration.value(),
					this.amplifier.value());
			entity.addEffect(effectInstance);
			return true;
		} else {
			Main.Log.warn("Failed find effect with ID: {}", this.value);
			return false;
		}
	}

	@Override
	public String toString() {
		return super.toString() + String.format("amplifier=%s, duration=%s", this.amplifier, this.duration);
	}
}