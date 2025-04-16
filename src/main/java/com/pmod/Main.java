package com.pmod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmod.config.ServerConfig;

@Mod(Main.MOD_ID)
public final class Main {
    public static final transient String MOD_ID = "pmod";
    public static final transient Logger Log = LoggerFactory.getLogger(Main.class);

    public Main(final IEventBus modEventBus, final ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.Handler);
        Log.info("{} full mod loaded. Congratulations!", MOD_ID);
    }
}