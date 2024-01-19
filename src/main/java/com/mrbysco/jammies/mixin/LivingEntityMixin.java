package com.mrbysco.jammies.mixin;

import com.mrbysco.jammies.capability.DancingData;
import com.mrbysco.jammies.network.message.SyncDancingStatePayload;
import com.mrbysco.jammies.util.DanceUtil;
import com.mrbysco.jammies.util.DetectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
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
		DancingData cap = DanceUtil.getDancingAttachment(livingEntity);
		if (cap != null) {
			cap.setDancing(isPartying);
			DanceUtil.saveDancing(livingEntity, cap);
			//Sync dancing state to client
			PacketDistributor.ALL.noArg().send(new SyncDancingStatePayload(livingEntity.getId(),
					cap.isDancing(), cap.getAccumulatedTime(), cap.getLastTime()));
		}
	}

	@Inject(method = "aiStep()V", at = @At(value = "HEAD"))
	public void jammies$aiStep(CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		DancingData cap = DanceUtil.getDancingAttachment(livingEntity);
		if (cap != null && cap.isDancing() && livingEntity.tickCount % 20 == 0) {
			if (!DetectionUtil.closeToJukebox(livingEntity)) {
				cap.setDancing(false);
				if (cap.isStarted()) cap.stop();
				DanceUtil.saveDancing(livingEntity, cap);
				//Sync dancing state to client
				PacketDistributor.ALL.noArg().send(new SyncDancingStatePayload(livingEntity.getId(),
						cap.isDancing(), cap.getAccumulatedTime(), cap.getLastTime()));
			}
		}
	}
}
