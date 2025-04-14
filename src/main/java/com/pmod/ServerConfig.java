package com.pmod;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ServerConfig {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        private static final ModConfigSpec.DoubleValue BASE_DIFFICULTY = BUILDER
                        .comment("Base difficulty (DONT SET TO 0)")
                        .defineInRange("base_difficulty", 1.0, 0.0, Double.MAX_VALUE);

        private static final ModConfigSpec.BooleanValue USE_HORIZONTAL_DISTANCE = BUILDER
                        .comment("Use horizontal distance to calculate difficulty")
                        .define("use_horizontal_distance", true);

        private static final ModConfigSpec.DoubleValue HORIZONTAL_RADIUS = BUILDER
                        .comment("Horizontal radius for difficulty calculation")
                        .defineInRange("horizontal_radius", 200.0, 0.0, Double.MAX_VALUE);

        private static final ModConfigSpec.DoubleValue HORIZONTAL_MULTIPLIER = BUILDER
                        .comment("Horizontal multiplier for difficulty calculation")
                        .defineInRange("horizontal_multiplier", 0.005, 0.0, Double.MAX_VALUE);

        private static final ModConfigSpec.BooleanValue USE_VERTICAL_DISTANCE = BUILDER
                        .comment("Use vertical distance to calculate difficulty")
                        .define("use_vertical_distance", true);

        private static final ModConfigSpec.DoubleValue VERTICAL_THRESHOLD = BUILDER
                        .comment("Vertical threshold for difficulty calculation")
                        .defineInRange("vertical_threshold", -50.0, Double.MIN_VALUE, Double.MAX_VALUE);

        private static final ModConfigSpec.DoubleValue VERTICAL_MULTIPLIER = BUILDER
                        .comment("Vertical multiplier for difficulty calculation")
                        .defineInRange("vertical_multiplier", 0.01, 0.0, Double.MAX_VALUE);

        private static final ModConfigSpec.BooleanValue INVERT_VERTICAL_DIRECTION = BUILDER
                        .comment("Invert vertical direction for difficulty calculation")
                        .define("invert_vertical_direction", false);

        public static final ModConfigSpec Handler = BUILDER.build();

        public static double base_difficulty;
        public static boolean use_horizontal_distance;
        public static double horizontal_radius;
        public static double horizontal_multiplier;
        public static boolean use_vertical_distance;
        public static double vertical_threshold;
        public static double vertical_multiplier;
        public static boolean invert_vertical_direction;

        @SubscribeEvent
        public static void onLoad(final ModConfigEvent event) {
                base_difficulty = BASE_DIFFICULTY.get();
                use_horizontal_distance = USE_HORIZONTAL_DISTANCE.get();
                horizontal_radius = HORIZONTAL_RADIUS.get();
                horizontal_multiplier = HORIZONTAL_MULTIPLIER.get();
                use_vertical_distance = USE_VERTICAL_DISTANCE.get();
                vertical_threshold = VERTICAL_THRESHOLD.get();
                vertical_multiplier = VERTICAL_MULTIPLIER.get();
                invert_vertical_direction = INVERT_VERTICAL_DIRECTION.get();
        }
}
