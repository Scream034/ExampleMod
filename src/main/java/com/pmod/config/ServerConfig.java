package com.pmod.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pmod.Main;
import com.pmod.config.in.action.DifficultyActionConfig;
import com.pmod.config.in.action.in.BaseDifficultyAction;
import com.pmod.config.in.action.in.BaseDifficultyActionTypeAdapter;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.loading.FMLPaths;

@EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ServerConfig {
	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	private static final ModConfigSpec.DoubleValue BASE_DIFFICULTY = BUILDER
			.defineInRange("base_difficulty", 0.5, 0.0, Float.MAX_VALUE);

	private static final ModConfigSpec.BooleanValue USE_HORIZONTAL_DISTANCE = BUILDER
			.define("use_horizontal_distance", true);

	private static final ModConfigSpec.DoubleValue HORIZONTAL_MULTIPLIER = BUILDER
			.defineInRange("horizontal_multiplier", 0.02, 0.0, Float.MAX_VALUE);

	private static final ModConfigSpec.BooleanValue USE_VERTICAL_DISTANCE = BUILDER
			.define("use_vertical_distance", true);

	private static final ModConfigSpec.DoubleValue VERTICAL_THRESHOLD = BUILDER
			.defineInRange("vertical_threshold", -48.0, Double.MIN_VALUE, Float.MAX_VALUE);

	private static final ModConfigSpec.DoubleValue VERTICAL_MULTIPLIER = BUILDER
			.defineInRange("vertical_multiplier", 0.01, 0.0, Float.MAX_VALUE);

	private static final ModConfigSpec.BooleanValue INVERT_VERTICAL_DIRECTION = BUILDER
			.define("invert_vertical_direction", false);

	private static final ModConfigSpec.DoubleValue GENERAL_MAX_HEALTH = BUILDER
			.defineInRange("max_health", 100_000F, 10.0, Float.MAX_VALUE);

	public static final ModConfigSpec Handler = BUILDER.build();

	public static float baseDifficulty;
	public static boolean useHorizontalDistance;
	public static float horizontalMultiplier;
	public static boolean useVerticalDistance;
	public static float verticalThreshold;
	public static float verticalMultiplier;
	public static boolean invertVerticalDirection;
	public static float generalMaxHealth;

	@SubscribeEvent
	public final static void onLoad(final ModConfigEvent event) {
		if (!Handler.isLoaded()) {
			Main.Log.warn("ServerConfig not loaded!");
			return;
		}

		baseDifficulty = BASE_DIFFICULTY.get().floatValue();
		useHorizontalDistance = USE_HORIZONTAL_DISTANCE.get();
		horizontalMultiplier = HORIZONTAL_MULTIPLIER.get().floatValue();
		useVerticalDistance = USE_VERTICAL_DISTANCE.get();
		verticalThreshold = VERTICAL_THRESHOLD.get().floatValue();
		verticalMultiplier = VERTICAL_MULTIPLIER.get().floatValue();
		invertVerticalDirection = INVERT_VERTICAL_DIRECTION.get();
		generalMaxHealth = GENERAL_MAX_HEALTH.get().floatValue();

		Main.Log.info("ServerConfig loaded.");
	}

	/**
	 * Loads all difficulty action configs from the difficulty_actions directory
	 * Use when the mod is loaded
	 */
	public final static @Nullable List<DifficultyActionConfig> getDifficultyActionConfigs() {
		Main.Log.info("Loading difficulty action configs...");
		// Create GSON with custom type adapter
		final Gson gsonWithAdapter = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(BaseDifficultyAction.class, new BaseDifficultyActionTypeAdapter())
				.create();

		// Gson без адаптера для DifficultyActionConfig
		final Gson defaultGson = new GsonBuilder().setPrettyPrinting().create();

		Path configDir = FMLPaths.CONFIGDIR.get().resolve(Main.MOD_ID);
		File difficultyActionsDir = configDir.resolve("difficulty_actions").toFile();

		Main.Log.info("Checking for difficulty_actions config directory...");
		if (!difficultyActionsDir.exists()) {
			if (difficultyActionsDir.mkdirs()) {
				Main.Log.info("Created difficulty_actions config directory.");
			} else {
				Main.Log.error("Failed to create difficulty_actions config directory.");
				return null;
			}
		}

		final List<DifficultyActionConfig> difficultyActionConfigs = new ArrayList<>();

		Main.Log.info("Loading difficulty action configs from: {}", difficultyActionsDir.getAbsolutePath());
		File[] files = difficultyActionsDir.listFiles((dir, name) -> name.endsWith(".json"));
		if (files != null) {
			for (File file : files) {
				try (FileReader reader = new FileReader(file)) {
					JsonObject root = defaultGson.fromJson(reader, JsonObject.class);
					float difficultyMultiplier = root.get("difficultyMultiplier").getAsFloat();
					boolean use_random = root.get("use_random").getAsBoolean();
					JsonArray actionsArray = root.getAsJsonArray("actions");

					List<BaseDifficultyAction<?>> actionsList = new ArrayList<>();
					for (JsonElement actionElement : actionsArray) {
						BaseDifficultyAction<?> action = gsonWithAdapter.fromJson(actionElement, BaseDifficultyAction.class);
						if (action != null) {
							actionsList.add(action);
						}
					}

					DifficultyActionConfig config = new DifficultyActionConfig(difficultyMultiplier, use_random, actionsList);
					difficultyActionConfigs.add(config);
					Main.Log.info("Loaded difficulty action config from: {}\n{}", file.getName(), config);

				} catch (IOException e) {
					Main.Log.error("Error loading difficulty action config from {}: {}", file.getName(), e.getMessage());
				}
			}
		}

		return difficultyActionConfigs;
	}
}