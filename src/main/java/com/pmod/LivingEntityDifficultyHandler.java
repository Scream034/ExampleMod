package com.pmod;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.LevelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

@Mod(Main.MOD_ID)
public class LivingEntityDifficultyHandler {
	private LevelAccessor globalLevel;
	private LevelData levelData;
	private float difficulty = 100F;

	public LivingEntityDifficultyHandler() {
		// Регистрируем этот класс как обработчик событий на общей шине событий NeoForge
		NeoForge.EVENT_BUS.register(this);
		Main.Log.info("MobDifficultyHandler зарегистрирован.");
	}

	@SubscribeEvent
	public void onWorldLoad(LevelEvent.Load event) {
		this.globalLevel = event.getLevel();
		this.levelData = globalLevel.getLevelData();
	}

	@SubscribeEvent
	public void onWorldUnload(LevelEvent.Unload event) {
		this.globalLevel = null;
		this.levelData = null;
	}

	@SubscribeEvent
	public void onEntityJoinLevel(net.neoforged.neoforge.event.entity.EntityJoinLevelEvent event) {
		if (this.globalLevel == null || event.isCanceled()) {
			return;
		}

		if (event.getEntity() instanceof LivingEntity livingEntity) {
			onLivingEntityJoinLevelInternal(livingEntity);
		}
	}

	public void onLivingEntityJoinLevelInternal(LivingEntity entity) {
		LivingEntityDifficultySetter.setHealth(entity, difficulty);

		Main.Log.info("LivingEntity:\n\tName: {}\n\tAt: {}\n\tTime: {}", entity.getType(), entity.position(), levelData.getGameTime());
	}
}
