package com.mrbysco.jammies.mixin;

import com.mrbysco.jammies.dancing.DancingMob;
import com.mrbysco.jammies.util.DetectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	protected LivingEntityMixin(EntityType<? extends Entity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "defineSynchedData()V",
			at = @At(value = "TAIL"))
	public void jammies$defineSynchedData(CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		if (livingEntity instanceof DancingMob dancingMob) {
			if (dancingMob.jammies$getDancingAccessor() != null)
				this.entityData.define(dancingMob.jammies$getDancingAccessor(), false);
		}
	}

	@Inject(method = "setRecordPlayingNearby(Lnet/minecraft/core/BlockPos;Z)V",
			at = @At(value = "HEAD"))
	public void jammies$setRecordPlayingNearby(BlockPos pos, boolean isPartying, CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		if (livingEntity instanceof DancingMob dancingMob) {
			dancingMob.jammies$setDancing(isPartying);
		}
	}

	@Inject(method = "aiStep()V",
			at = @At(value = "HEAD"))
	public void jammies$aiStep(CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		if (livingEntity instanceof DancingMob dancingMob) {
			if (!DetectionUtil.closeToJukebox(livingEntity) && dancingMob.jammies$isDancing()) {
				dancingMob.jammies$setDancing(false);
				if (dancingMob.jammies$getDanceState().isStarted())
					dancingMob.jammies$getDanceState().stop();
			}
		}

	}

	@Inject(method = "onSyncedDataUpdated(Lnet/minecraft/network/syncher/EntityDataAccessor;)V",
			at = @At(value = "HEAD"))
	public void jammies$onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor, CallbackInfo ci) {
		LivingEntity livingEntity = (LivingEntity) (Object) this;
		if (livingEntity instanceof DancingMob dancingMob) {
			if (dancingMob.jammies$getDancingAccessor().equals(dataAccessor)) {
				if (dancingMob.jammies$isDancing())
					dancingMob.jammies$getDanceState().start(this.tickCount);
				else
					dancingMob.jammies$getDanceState().stop();
			}
		}
	}
}
