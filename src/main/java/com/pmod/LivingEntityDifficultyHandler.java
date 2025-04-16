package com.pmod;

import com.pmod.config.ServerConfig;
import com.pmod.distance.DistanceHandler;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.LevelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

@Mod(Main.MOD_ID)
public final class LivingEntityDifficultyHandler {
	public float Difficulty = 1.0f;

	private transient LevelAccessor globalLevel;
	private transient LevelData levelData;
	private transient DistanceHandler distanceHandler;

	public LivingEntityDifficultyHandler() {
		// Регистрируем этот класс как обработчик событий на общей шине событий NeoForge
		NeoForge.EVENT_BUS.register(this);
		Main.Log.info("MobDifficultyHandler зарегистрирован.");
	}


	@SubscribeEvent
	public void onWorldLoad(final LevelEvent.Load event) {
		Difficulty = (float) ServerConfig.baseDifficulty;
		this.globalLevel = event.getLevel();
		this.levelData = globalLevel.getLevelData();
		this.distanceHandler = new DistanceHandler(levelData.getSpawnPos());
	}

	@SubscribeEvent
	public void onWorldUnload(LevelEvent.Unload event) {
		this.globalLevel = null;
		this.levelData = null;
		this.distanceHandler = null;
	}

	@SubscribeEvent
	public void onEntityJoinLevel(final net.neoforged.neoforge.event.entity.EntityJoinLevelEvent event) {
		if (this.globalLevel == null || event.isCanceled()) {
			return;
		}

		if (event.getEntity() instanceof LivingEntity livingEntity) {
			onLivingEntityJoinLevelInternal(livingEntity);
		}
	}

	public void onLivingEntityJoinLevelInternal(final LivingEntity entity) {
		final float difficulty = calculateDifficulty(entity);

		applyDifficulty(entity, difficulty);

		Main.Log.info(
				"LivingEntity:\n\tName: {}\n\tAt: {}\n\tTime: {}\n\tDifficulty: {}",
				entity.getType(), entity.position(), levelData.getGameTime(), difficulty);
	}

	public final void applyDifficulty(final LivingEntity entity, final float difficulty) {
		LivingEntityDifficultySetter.setHealth(entity, difficulty);
	}

	public final float calculateDifficulty(final LivingEntity entity) {
		return Difficulty + distanceHandler.calculateDifficulty(entity);
	}
}
