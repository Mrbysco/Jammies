package com.mrbysco.jammies;

import com.mojang.logging.LogUtils;
import com.mrbysco.jammies.network.JammiesNetworking;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(JammiesMod.MOD_ID)
public class JammiesMod {
	public static final String MOD_ID = "jammies";
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final ResourceLocation DANCING_CAP = new ResourceLocation(MOD_ID, "capability.jammies_dancing");

	public JammiesMod() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::setup);

		NeoForge.EVENT_BUS.register(new CapabilityHandler());
	}

	private void setup(final FMLCommonSetupEvent event) {
		JammiesNetworking.init();
	}
}
