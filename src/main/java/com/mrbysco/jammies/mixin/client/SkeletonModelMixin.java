package com.mrbysco.jammies.mixin.client;

import com.mrbysco.jammies.client.DanceHandler;
import com.mrbysco.jammies.client.JamAnimations;
import com.mrbysco.jammies.dancing.DancingMob;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonModel.class)
public abstract class SkeletonModelMixin<T extends Mob & RangedAttackMob> extends HumanoidModel<T> {
	protected SkeletonModelMixin(ModelPart modelPart) {
		super(modelPart);
	}

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/Mob;FFFFF)V",
			at = @At(value = "HEAD")
	)
	public void jammies$setupAnim(T mob, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (mob instanceof DancingMob dancingMob && !dancingMob.jammies$isDancing()) {
			//Reset pos
			head.resetPose();
			hat.resetPose();
			body.resetPose();
			rightArm.resetPose();
			leftArm.resetPose();
			rightLeg.resetPose();
			leftLeg.resetPose();
		}
	}

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/Mob;FFFFF)V",
			at = @At(value = "TAIL")
	)
	public void jammies$setupAnim2(T monster, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (monster instanceof DancingMob dancingMob && dancingMob.jammies$isDancing()) {
			DanceHandler.animateHumanoidDancing(dancingMob, JamAnimations.SPOOKY, (SkeletonModel) (Object) this, ageInTicks, 1.0F);
		}
	}
}
