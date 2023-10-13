package com.mrbysco.jammies.mixin;

import com.mrbysco.jammies.dancing.DancingMob;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("WrongEntityDataParameterClass")
@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster implements DancingMob {
	@Unique
	private static final EntityDataAccessor<Boolean> JAMMIES_DANCING = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);

	@Unique
	public final AnimationState jammies$thrillerState = new AnimationState();

	protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public boolean jammies$isDancing() {
		return this.entityData.get(JAMMIES_DANCING);
	}

	@Override
	public void jammies$setDancing(boolean dancing) {
		this.entityData.set(JAMMIES_DANCING, dancing);
	}

	@Override
	public EntityDataAccessor<Boolean> jammies$getDancingAccessor() {
		return JAMMIES_DANCING;
	}

	@Override
	public AnimationState jammies$getDanceState() {
		return jammies$thrillerState;
	}
}
