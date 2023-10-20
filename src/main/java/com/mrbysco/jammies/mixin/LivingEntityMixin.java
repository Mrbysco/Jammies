package com.mrbysco.jammies.mixin;

import com.mrbysco.jammies.CapabilityHandler;
import com.mrbysco.jammies.capability.DancingCapability;
import com.mrbysco.jammies.capability.IDancingMob;
import com.mrbysco.jammies.network.JammiesNetworking;
import com.mrbysco.jammies.network.message.SyncDancingStateMessage;
import com.mrbysco.jammies.util.DetectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	protected LivingEntityMixin(EntityType<? extends Entity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "setRecordPlayingNearby(Lnet/minecraft/core/BlockPos;Z)V", at = @At(value = "HEAD"))
	public void jammies$setRecordPlayingNearby(BlockPos pos, boolean isPartying, CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		IDancingMob cap = livingEntity.getCapability(CapabilityHandler.DANCING_CAPABILITY).orElse(null);
		if (cap != null) {
			cap.setDancing(isPartying);
			//Sync dancing state to client
			JammiesNetworking.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncDancingStateMessage(getId(), DancingCapability.writeNBT(cap)));
		}
	}

	@Inject(method = "aiStep()V", at = @At(value = "HEAD"))
	public void jammies$aiStep(CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		IDancingMob cap = livingEntity.getCapability(CapabilityHandler.DANCING_CAPABILITY).orElse(null);
		if (cap != null) {
			if (!DetectionUtil.closeToJukebox(livingEntity) && cap.isDancing()) {
				cap.setDancing(false);
				if (cap.isStarted()) cap.stop();
				//Sync dancing state to client
				JammiesNetworking.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncDancingStateMessage(getId(), DancingCapability.writeNBT(cap)));
			}
		}
	}
}
