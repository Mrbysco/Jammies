package com.mrbysco.jammies;

import com.mojang.logging.LogUtils;
import com.mrbysco.jammies.network.JammiesNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(JammiesMod.MOD_ID)
public class JammiesMod {
	public static final String MOD_ID = "jammies";
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final ResourceLocation DANCING_CAP = new ResourceLocation(MOD_ID, "capability.jammies_dancing");

	public JammiesMod() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::setup);

		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
	}

	private void setup(final FMLCommonSetupEvent event) {
		JammiesNetworking.init();
	}
}
