package com.pmod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID = "pmod";
    public static final Logger Log = LoggerFactory.getLogger(Main.class);

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        new LivingEntityDifficultyHandler();
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.Handler);
        Log.info("{} full mod loaded. Congratulations!", MOD_ID);
    }
}