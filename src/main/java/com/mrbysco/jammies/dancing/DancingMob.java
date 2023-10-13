package com.mrbysco.jammies.dancing;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.AnimationState;

public interface DancingMob {
	boolean jammies$isDancing();

	void jammies$setDancing(boolean dancing);

	EntityDataAccessor<Boolean> jammies$getDancingAccessor();

	AnimationState jammies$getDanceState();
}
