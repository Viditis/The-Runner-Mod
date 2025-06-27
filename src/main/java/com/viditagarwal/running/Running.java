package com.viditagarwal.running;

import com.viditagarwal.running.network.ChronoModePacket;
import com.viditagarwal.running.network.PhaseTogglePacket;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Running implements ModInitializer {
	public static final String MOD_ID = "running";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Running mod initializing...");
		ModItems.registerItems();
		TickHandler.register();
		TickFreezeHandler.register();
		ChronoModePacket.register();
		PhaseTogglePacket.register();
		ModItemGroup.register();
	}
}
