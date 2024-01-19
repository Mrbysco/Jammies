package com.mrbysco.jammies.mixin.client;

import com.mrbysco.jammies.capability.DancingData;
import com.mrbysco.jammies.client.DanceHandler;
import com.mrbysco.jammies.client.JamAnimations;
import com.mrbysco.jammies.util.DanceUtil;
import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractZombieModel.class)
public abstract class AbstractZombieModelMixin<T extends Monster> extends HumanoidModel<T> {
	protected AbstractZombieModelMixin(ModelPart modelPart) {
		super(modelPart);
	}

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/monster/Monster;FFFFF)V",
			at = @At(value = "HEAD")
	)
	public void jammies$setupAnim(T mob, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		DancingData cap = DanceUtil.getDancingAttachment(mob);
		if (cap != null && cap.isDancing()) {
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

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/monster/Monster;FFFFF)V",
			at = @At(value = "TAIL")
	)
	public void jammies$setupAnim2(T mob, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		DanceHandler.doALittleDance(mob, JamAnimations.THRILLER, (AbstractZombieModel) (Object) this, ageInTicks, 1.0F);
	}
}
