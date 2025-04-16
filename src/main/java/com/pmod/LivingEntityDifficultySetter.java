package com.pmod;

import com.pmod.config.ServerConfig;
import com.pmod.reflect.RangedAttributeSetter;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class LivingEntityDifficultySetter {
	public static final float MINECRAFT_MAX_VALUE = 1024f;

	public final static void setHealth(final LivingEntity livingEntity, final float difficulty) {
		final float maxHealth = livingEntity.getMaxHealth();
		final float calculated = maxHealth * difficulty;
		if (calculated > maxHealth) {
			setMaxHealth(livingEntity, calculated);
		}

		livingEntity.setHealth(calculated);
	}

	public final static void setMaxHealth(final LivingEntity livingEntity, final float newMaxHealth) {
		if (newMaxHealth > MINECRAFT_MAX_VALUE) {
			RangedAttributeSetter.setField(Attributes.MAX_HEALTH.value(), RangedAttributeSetter.FIELD_MAX_VALUE, Math.clamp(newMaxHealth, 0.0, ServerConfig.generalMaxHealth));
		}

		AttributeInstance attribute = livingEntity.getAttribute(Attributes.MAX_HEALTH);
		if (attribute != null) {
			attribute.setBaseValue(newMaxHealth);
			Main.Log.info("Set max health for mob {} to {} ({}, {})", livingEntity.getType(), newMaxHealth,
					attribute.getValue(), attribute.getBaseValue());
		} else {
			Main.Log.warn("Failed to set max health for mob {}", livingEntity.getType());
		}
	}
}
