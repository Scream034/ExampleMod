package com.pmod;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class LivingEntityDifficultySetter {
	public static void setHealth(LivingEntity livingEntity, float difficulty) {
		float maxHealth = livingEntity.getMaxHealth();
		float calculated = maxHealth * difficulty;
		if (calculated > maxHealth) {
			setMaxHealth(livingEntity, calculated);
		}
		
		Main.Log.info("Set health for mob {} to {}", livingEntity.getName(), calculated);
		livingEntity.setHealth(calculated);
	}

	public static void setMaxHealth(LivingEntity livingEntity, float newMaxHealth) {
		AttributeInstance attribute = livingEntity.getAttribute(Attributes.MAX_HEALTH);
		if (attribute != null) {
			Main.Log.info("Set max health for mob {} to {}", livingEntity.getName(), newMaxHealth);
			attribute.setBaseValue(newMaxHealth);
		} else {
			Main.Log.warn("Failed to set max health for mob {}", livingEntity.getName());
		}
	}
}
