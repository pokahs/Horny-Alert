package io.github.pokahs.hornyalert;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HornyAlert implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("horny-alert");

	@Override
	public void onInitialize() {

		LOGGER.info("Horny Alert Initialized!");
	}
}