package com.pmod;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.pmod.config.ServerConfig;
import com.pmod.distance.DistanceHandler;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.LevelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IPlayerExtension;
import net.neoforged.neoforge.event.level.LevelEvent;

@Mod(Main.MOD_ID)
public final class LivingEntityDifficultyHandler {
	private static final Supplier<AttachmentType<Boolean>> IS_MOB_APPLIED = Main.ATTACHMENT_TYPES.register(
			"is_mob_applied", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

	private transient LevelAccessor globalLevel;
	private transient LevelData levelData;
	private transient DistanceHandler distanceHandler;
	private transient LivingEntityDifficultyActionsSetter actionsSetter;

	public float Difficulty = 0.5f;

	public LivingEntityDifficultyHandler() {
		// Регистрируем этот класс как обработчик событий на общей шине событий NeoForge
		NeoForge.EVENT_BUS.register(this);
		Main.Log.info("MobDifficultyHandler зарегистрирован.");
	}

	@SubscribeEvent
	public void onWorldLoad(final LevelEvent.Load event) {
		if (this.globalLevel != null) {
			Main.Log.warn("World already loaded");
			return; // cannot reload
		}

		Difficulty = (float) ServerConfig.baseDifficulty;
		this.globalLevel = event.getLevel();
		this.levelData = globalLevel.getLevelData();
		this.distanceHandler = new DistanceHandler(levelData.getSpawnPos());
		this.actionsSetter = new LivingEntityDifficultyActionsSetter(ServerConfig.getDifficultyActionConfigs());
	}

	@SubscribeEvent
	public void onWorldUnload(LevelEvent.Unload event) {
		this.globalLevel = null;
		this.levelData = null;
		this.distanceHandler = null;
		this.actionsSetter = null;
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
		if (entity instanceof IPlayerExtension) {
			Main.Log.warn("WTF is this player doing here?)");
			return;
		}
		else if (entity.hasData(IS_MOB_APPLIED) && entity.getData(IS_MOB_APPLIED)) {
			return;
		}

		final float difficulty = calculateDifficulty(entity);

		applyDifficulty(entity, difficulty);

		entity.setData(IS_MOB_APPLIED, true);

		Main.Log.info(
				"LivingEntity:\n\tName: {}\n\tAt: {}\n\tTime: {}\n\tDifficulty: {}",
				entity.getType(), entity.position(), levelData.getGameTime(), difficulty);
	}

	public final void applyDifficulty(final LivingEntity entity, final float difficulty) {
		LivingEntityDifficultySetter.setHealth(entity, difficulty);
		actionsSetter.apply(entity, difficulty);
	}

	public final float calculateDifficulty(final LivingEntity entity) {
		return Difficulty + distanceHandler.calculateDifficulty(entity);
	}
}
