package com.pmod.distance;

import com.pmod.Main;
import com.pmod.config.ServerConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

public final class DistanceHandler {
	private final transient BlockPos origin;

	public DistanceHandler(BlockPos origin) {
		this.origin = origin;
	}

	public float calculateDifficulty(final LivingEntity entity) {
		final float hdm = calculateHorizontalDistanceMultiplier(entity);
		final float vdm = calculateVerticalDistanceMultiplier(entity);
		
		Main.Log.info("HDM, VDM: ({}, {})", hdm, vdm);
		return hdm + vdm;
	}

	public final float calculateHorizontalDistanceMultiplier(final LivingEntity entity) {
		return (float) Math.sqrt(Math.pow(entity.getX() - origin.getX(), 2) + Math.pow(entity.getZ() - origin.getZ(), 2)) * ServerConfig.horizontalMultiplier;
	}

	public float calculateVerticalDistanceMultiplier(final LivingEntity entity) {
		final int spawnY = origin.getY();
		final double entityY = entity.getY();
		final double distance = entityY - spawnY;

		if (ServerConfig.invertVerticalDirection) {
			if (distance > ServerConfig.verticalThreshold) {
				return (float) (distance - ServerConfig.verticalThreshold) * ServerConfig.verticalMultiplier;
			}
		} else {
			if (distance < ServerConfig.verticalThreshold) {
				return (float) (ServerConfig.verticalThreshold - distance) * ServerConfig.verticalMultiplier;
			}
		}
		return 0;
	}
}
