package com.mrbysco.jammies;

import com.mojang.logging.LogUtils;
import com.mrbysco.jammies.network.JammiesNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(JammiesMod.MOD_ID)
public class JammiesMod {
	public static final String MOD_ID = "jammies";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final TagKey<EntityType<?>> CAN_DANCE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MOD_ID, "can_dance"));

	public JammiesMod(IEventBus eventBus) {
		eventBus.addListener(JammiesNetworking::setupPackets);
		AttachmentHandler.ATTACHMENT_TYPES.register(eventBus);
		NeoForge.EVENT_BUS.register(new AttachmentHandler());
	}
}
