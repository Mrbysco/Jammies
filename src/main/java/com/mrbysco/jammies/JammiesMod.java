package com.mrbysco.jammies;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(JammiesMod.MOD_ID)
public class JammiesMod {
	public static final String MOD_ID = "jammies";
	private static final Logger LOGGER = LogUtils.getLogger();

	public JammiesMod() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
	}
}
